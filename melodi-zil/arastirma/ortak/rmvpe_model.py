"""RMVPE (Robust Model for Vocal Pitch Estimation) - torch ve onnxruntime yolu.

Model kodu RVC projesindeki rmvpe.py'nin sade hali (E2E = DeepUnet + BiGRU).
Agirliklar: /home/user/models/rmvpe/rmvpe.pt  ve  rmvpe.onnx
Girdi: 16 kHz mono float32 ses. Cikti: (f0_hz, zaman_sn) ; 10 ms adim, f0=0 -> sessiz.

Kullanim:
    from rmvpe_model import RMVPE
    m = RMVPE("/home/user/models/rmvpe/rmvpe.pt")          # torch
    f0 = m.infer_from_audio(audio16k, thred=0.03)          # Hz, 0 = sessiz
    m = RMVPE("/home/user/models/rmvpe/rmvpe.onnx")        # onnxruntime
"""
import numpy as np
import torch
import torch.nn as nn
import torch.nn.functional as F
import librosa

N_MELS = 128
SR = 16000
HOP = 160


class ConvBlockRes(nn.Module):
    def __init__(self, in_channels, out_channels, momentum=0.01):
        super().__init__()
        self.conv = nn.Sequential(
            nn.Conv2d(in_channels, out_channels, (3, 3), (1, 1), (1, 1), bias=False),
            nn.BatchNorm2d(out_channels, momentum=momentum),
            nn.ReLU(),
            nn.Conv2d(out_channels, out_channels, (3, 3), (1, 1), (1, 1), bias=False),
            nn.BatchNorm2d(out_channels, momentum=momentum),
            nn.ReLU(),
        )
        if in_channels != out_channels:
            self.shortcut = nn.Conv2d(in_channels, out_channels, (1, 1))
            self.is_shortcut = True
        else:
            self.is_shortcut = False

    def forward(self, x):
        if self.is_shortcut:
            return self.conv(x) + self.shortcut(x)
        return self.conv(x) + x


class ResEncoderBlock(nn.Module):
    def __init__(self, in_channels, out_channels, kernel_size, n_blocks=1, momentum=0.01):
        super().__init__()
        self.n_blocks = n_blocks
        self.conv = nn.ModuleList()
        self.conv.append(ConvBlockRes(in_channels, out_channels, momentum))
        for _ in range(n_blocks - 1):
            self.conv.append(ConvBlockRes(out_channels, out_channels, momentum))
        self.kernel_size = kernel_size
        if self.kernel_size is not None:
            self.pool = nn.AvgPool2d(kernel_size=kernel_size)

    def forward(self, x):
        for i in range(self.n_blocks):
            x = self.conv[i](x)
        if self.kernel_size is not None:
            return x, self.pool(x)
        return x


class Encoder(nn.Module):
    def __init__(self, in_channels, in_size, n_encoders, kernel_size, n_blocks, out_channels=16, momentum=0.01):
        super().__init__()
        self.n_encoders = n_encoders
        self.bn = nn.BatchNorm2d(in_channels, momentum=momentum)
        self.layers = nn.ModuleList()
        self.latent_channels = []
        for _ in range(self.n_encoders):
            self.layers.append(ResEncoderBlock(in_channels, out_channels, kernel_size, n_blocks, momentum=momentum))
            self.latent_channels.append([out_channels, in_size])
            in_channels = out_channels
            out_channels *= 2
            in_size //= 2
        self.out_size = in_size
        self.out_channel = out_channels

    def forward(self, x):
        concat_tensors = []
        x = self.bn(x)
        for i in range(self.n_encoders):
            t, x = self.layers[i](x)
            concat_tensors.append(t)
        return x, concat_tensors


class Intermediate(nn.Module):
    def __init__(self, in_channels, out_channels, n_inters, n_blocks, momentum=0.01):
        super().__init__()
        self.n_inters = n_inters
        self.layers = nn.ModuleList()
        self.layers.append(ResEncoderBlock(in_channels, out_channels, None, n_blocks, momentum))
        for _ in range(self.n_inters - 1):
            self.layers.append(ResEncoderBlock(out_channels, out_channels, None, n_blocks, momentum))

    def forward(self, x):
        for i in range(self.n_inters):
            x = self.layers[i](x)
        return x


class ResDecoderBlock(nn.Module):
    def __init__(self, in_channels, out_channels, stride, n_blocks=1, momentum=0.01):
        super().__init__()
        out_padding = (0, 1) if stride == (1, 2) else (1, 1)
        self.n_blocks = n_blocks
        self.conv1 = nn.Sequential(
            nn.ConvTranspose2d(in_channels, out_channels, (3, 3), stride, (1, 1),
                               output_padding=out_padding, bias=False),
            nn.BatchNorm2d(out_channels, momentum=momentum),
            nn.ReLU(),
        )
        self.conv2 = nn.ModuleList()
        self.conv2.append(ConvBlockRes(out_channels * 2, out_channels, momentum))
        for _ in range(n_blocks - 1):
            self.conv2.append(ConvBlockRes(out_channels, out_channels, momentum))

    def forward(self, x, concat_tensor):
        x = self.conv1(x)
        x = torch.cat((x, concat_tensor), dim=1)
        for i in range(self.n_blocks):
            x = self.conv2[i](x)
        return x


class Decoder(nn.Module):
    def __init__(self, in_channels, n_decoders, stride, n_blocks, momentum=0.01):
        super().__init__()
        self.layers = nn.ModuleList()
        self.n_decoders = n_decoders
        for _ in range(self.n_decoders):
            out_channels = in_channels // 2
            self.layers.append(ResDecoderBlock(in_channels, out_channels, stride, n_blocks, momentum))
            in_channels = out_channels

    def forward(self, x, concat_tensors):
        for i in range(self.n_decoders):
            x = self.layers[i](x, concat_tensors[-1 - i])
        return x


class DeepUnet(nn.Module):
    def __init__(self, kernel_size, n_blocks, en_de_layers=5, inter_layers=4, in_channels=1, en_out_channels=16):
        super().__init__()
        self.encoder = Encoder(in_channels, 128, en_de_layers, kernel_size, n_blocks, en_out_channels)
        self.intermediate = Intermediate(self.encoder.out_channel // 2, self.encoder.out_channel,
                                         inter_layers, n_blocks)
        self.decoder = Decoder(self.encoder.out_channel, en_de_layers, kernel_size, n_blocks)

    def forward(self, x):
        x, concat_tensors = self.encoder(x)
        x = self.intermediate(x)
        return self.decoder(x, concat_tensors)


class BiGRU(nn.Module):
    def __init__(self, input_features, hidden_features, num_layers):
        super().__init__()
        self.gru = nn.GRU(input_features, hidden_features, num_layers=num_layers,
                          batch_first=True, bidirectional=True)

    def forward(self, x):
        return self.gru(x)[0]


class E2E(nn.Module):
    def __init__(self, n_blocks, n_gru, kernel_size, en_de_layers=5, inter_layers=4, in_channels=1,
                 en_out_channels=16):
        super().__init__()
        self.unet = DeepUnet(kernel_size, n_blocks, en_de_layers, inter_layers, in_channels, en_out_channels)
        self.cnn = nn.Conv2d(en_out_channels, 3, (3, 3), padding=(1, 1))
        self.fc = nn.Sequential(
            BiGRU(3 * N_MELS, 256, n_gru),
            nn.Linear(512, 360),
            nn.Dropout(0.25),
            nn.Sigmoid(),
        )

    def forward(self, mel):
        mel = mel.transpose(-1, -2).unsqueeze(1)
        x = self.cnn(self.unet(mel)).transpose(1, 2).flatten(-2)
        return self.fc(x)


class MelSpectrogram(nn.Module):
    """RMVPE log-mel: n_fft=1024, hop=160, 128 mel, 30-8000 Hz, htk."""

    def __init__(self, n_mel=N_MELS, sr=SR, win_length=1024, hop_length=HOP, fmin=30, fmax=8000, clamp=1e-5):
        super().__init__()
        self.n_fft = win_length
        self.win_length = win_length
        self.hop_length = hop_length
        self.clamp = clamp
        basis = librosa.filters.mel(sr=sr, n_fft=win_length, n_mels=n_mel, fmin=fmin, fmax=fmax, htk=True)
        self.register_buffer("mel_basis", torch.from_numpy(basis).float())
        self.register_buffer("window", torch.hann_window(win_length))

    def forward(self, audio):
        fft = torch.stft(audio, n_fft=self.n_fft, hop_length=self.hop_length, win_length=self.win_length,
                         window=self.window, center=True, return_complex=True)
        mag = torch.sqrt(fft.real.pow(2) + fft.imag.pow(2))
        mel = torch.matmul(self.mel_basis, mag)
        return torch.log(torch.clamp(mel, min=self.clamp))


class RMVPE:
    def __init__(self, model_path="/home/user/models/rmvpe/rmvpe.pt", device="cpu"):
        self.device = device
        self.mel_extractor = MelSpectrogram().to(device)
        self.onnx = str(model_path).endswith(".onnx")
        if self.onnx:
            import onnxruntime as ort
            so = ort.SessionOptions()
            so.intra_op_num_threads = 2
            self.sess = ort.InferenceSession(str(model_path), so, providers=["CPUExecutionProvider"])
            self.onnx_in = [i.name for i in self.sess.get_inputs()]
            self.onnx_out = [o.name for o in self.sess.get_outputs()]
        else:
            model = E2E(4, 1, (2, 2))
            ckpt = torch.load(str(model_path), map_location="cpu")
            model.load_state_dict(ckpt)
            self.model = model.eval().to(device)
        cents = 20 * np.arange(360) + 1997.3794084376191
        self.cents_mapping = np.pad(cents, (4, 4))

    @torch.no_grad()
    def mel2hidden(self, mel):
        n_frames = mel.shape[-1]
        pad = 32 * ((n_frames - 1) // 32 + 1) - n_frames
        mel = F.pad(mel, (0, pad), mode="reflect")
        if self.onnx:
            feeds = {self.onnx_in[0]: mel.cpu().numpy().astype(np.float32)}
            if len(self.onnx_in) > 1:
                feeds[self.onnx_in[1]] = np.array([0.03], dtype=np.float32)
            out = self.sess.run(None, feeds)[0]
            return torch.from_numpy(out)[:, :n_frames]
        return self.model(mel)[:, :n_frames]

    def to_local_average_cents(self, salience, thred=0.05):
        center = np.argmax(salience, axis=1)
        salience = np.pad(salience, ((0, 0), (4, 4)))
        center = center + 4
        starts, ends = center - 4, center + 5
        idx = np.arange(salience.shape[0])[:, None] * 0 + np.arange(9)[None, :] + starts[:, None]
        sal = np.take_along_axis(salience, idx, axis=1)
        cm = self.cents_mapping[idx]
        out = (sal * cm).sum(1) / np.maximum(sal.sum(1), 1e-9)
        out[salience.max(1) <= thred] = 0
        return out

    def salience(self, audio16k):
        """(T,360) salience. T = ceil(len/160)+1 kare, 10 ms."""
        audio = torch.from_numpy(np.asarray(audio16k, dtype=np.float32)).unsqueeze(0).to(self.device)
        mel = self.mel_extractor(audio)
        hidden = self.mel2hidden(mel)
        return hidden.squeeze(0).cpu().numpy()

    def infer_from_audio(self, audio16k, thred=0.03):
        """16 kHz mono ses -> f0 (Hz) dizisi, 10 ms adim. 0 = sessiz."""
        sal = self.salience(audio16k)
        if self.onnx and sal.ndim == 1:  # onnx dogrudan f0 donduruyorsa
            return sal
        cents = self.to_local_average_cents(sal, thred=thred)
        f0 = 10 * (2 ** (cents / 1200))
        f0[f0 == 10] = 0
        return f0

    def infer_with_times(self, audio16k, thred=0.03):
        f0 = self.infer_from_audio(audio16k, thred)
        return f0, np.arange(len(f0)) * HOP / SR
