package com.jn.winremote.ui.pairing

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.jn.winremote.util.rotateLumaPlane
import java.nio.ByteBuffer

/**
 * CameraX [ImageAnalysis.Analyzer] that decodes QR codes from the live
 * preview using ZXing's core decoder directly on the Y (luminance) plane —
 * no extra "embedded" scanning library/Activity needed.
 *
 * Not unit-testable (needs a real camera frame / Android ImageProxy); the
 * pure rotation math it depends on ([rotateLumaPlane]) is tested instead.
 */
class QrAnalyzer(private val onDecoded: (String) -> Unit) : ImageAnalysis.Analyzer {

    private val reader = QRCodeReader()
    private val hints = mapOf(DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE))

    override fun analyze(image: ImageProxy) {
        try {
            val plane = image.planes.getOrNull(0) ?: return
            val luma = extractLumaPlane(plane.buffer, plane.rowStride, plane.pixelStride, image.width, image.height)
            val (rotated, w, h) = rotateLumaPlane(luma, image.width, image.height, image.imageInfo.rotationDegrees)
            val source = PlanarYUVLuminanceSource(rotated, w, h, 0, 0, w, h, false)
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            val result: Result = reader.decode(bitmap, hints)
            onDecoded(result.text)
        } catch (_: NotFoundException) {
            // No QR code in this frame — expected on most frames while aiming the camera.
        } catch (_: Exception) {
            // Malformed/short frame; just skip it and wait for the next one.
        } finally {
            reader.reset()
            image.close()
        }
    }

    private fun extractLumaPlane(buffer: ByteBuffer, rowStride: Int, pixelStride: Int, width: Int, height: Int): ByteArray {
        val data = ByteArray(width * height)
        if (pixelStride == 1 && rowStride == width) {
            buffer.duplicate().get(data)
            return data
        }
        val dup = buffer.duplicate()
        val rowBuffer = ByteArray(rowStride)
        for (row in 0 until height) {
            val rowStart = row * rowStride
            if (rowStart >= dup.capacity()) break
            dup.position(rowStart)
            val length = minOf(rowStride, dup.remaining())
            dup.get(rowBuffer, 0, length)
            for (col in 0 until width) {
                val idx = col * pixelStride
                data[row * width + col] = if (idx < length) rowBuffer[idx] else 0
            }
        }
        return data
    }
}
