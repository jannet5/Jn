import { useEffect, useState } from "react";
import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Linking,
  Modal,
  Platform,
  Pressable,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { getApiKey, setApiKey } from "./src/apiKey";
import { fetchEtymology } from "./src/etymology";
import { addToHistory, loadHistory } from "./src/history";

export default function App() {
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);
  const [history, setHistory] = useState([]);
  const [settingsVisible, setSettingsVisible] = useState(false);
  const [hasApiKey, setHasApiKey] = useState(false);

  useEffect(() => {
    loadHistory().then(setHistory);
    getApiKey().then((key) => setHasApiKey(Boolean(key)));
  }, []);

  async function search(word) {
    const trimmed = word.trim();
    if (!trimmed) return;

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const data = await fetchEtymology(trimmed);
      setResult(data);
      setHistory(await addToHistory(trimmed));
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <KeyboardAvoidingView
      style={styles.flex}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
    >
      <StatusBar barStyle="dark-content" />
      <ScrollView
        style={styles.flex}
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
      >
        <View style={styles.headerRow}>
          <View>
            <Text style={styles.title}>Kelime Kökeni</Text>
            <Text style={styles.subtitle}>
              Bir kelime ya da isim yaz, kökenini öğren.
            </Text>
          </View>
          <Pressable style={styles.gearButton} onPress={() => setSettingsVisible(true)}>
            <Text style={styles.gearText}>⚙︎</Text>
          </Pressable>
        </View>

        {!hasApiKey && (
          <Pressable style={styles.hint} onPress={() => setSettingsVisible(true)}>
            <Text style={styles.hintText}>
              Özel isimler ve nadir kelimeler için Ayarlar'dan bir Anthropic API key
              ekleyebilirsin. Dokun ve ekle →
            </Text>
          </Pressable>
        )}

        <View style={styles.searchRow}>
          <TextInput
            style={styles.input}
            placeholder="örn. Ahmet, kalem, merhaba..."
            value={query}
            onChangeText={setQuery}
            onSubmitEditing={() => search(query)}
            autoCapitalize="none"
            autoCorrect={false}
            returnKeyType="search"
          />
          <Pressable
            style={styles.button}
            onPress={() => search(query)}
            disabled={loading}
          >
            <Text style={styles.buttonText}>Ara</Text>
          </Pressable>
        </View>

        {history.length > 0 && (
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={false}
            style={styles.historyRow}
          >
            {history.map((word) => (
              <Pressable
                key={word}
                style={styles.chip}
                onPress={() => {
                  setQuery(word);
                  search(word);
                }}
              >
                <Text style={styles.chipText}>{word}</Text>
              </Pressable>
            ))}
          </ScrollView>
        )}

        {loading && <ActivityIndicator style={styles.loading} size="large" />}

        {error && (
          <View style={styles.errorBox}>
            <Text style={styles.errorText}>{error}</Text>
          </View>
        )}

        {result && !loading && <ResultCard result={result} />}
      </ScrollView>

      <SettingsModal
        visible={settingsVisible}
        onClose={() => setSettingsVisible(false)}
        onSaved={(saved) => setHasApiKey(saved)}
      />
    </KeyboardAvoidingView>
  );
}

function SettingsModal({ visible, onClose, onSaved }) {
  const [value, setValue] = useState("");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (visible) {
      getApiKey().then((key) => setValue(key || ""));
    }
  }, [visible]);

  async function handleSave() {
    setSaving(true);
    try {
      await setApiKey(value);
      onSaved(Boolean(value.trim()));
      onClose();
    } finally {
      setSaving(false);
    }
  }

  return (
    <Modal visible={visible} animationType="slide" transparent onRequestClose={onClose}>
      <View style={styles.modalBackdrop}>
        <View style={styles.modalCard}>
          <Text style={styles.modalTitle}>Anthropic API Key</Text>
          <Text style={styles.modalBody}>
            Nişanyan Sözlük'te bulunamayan kelimeler/isimler için (örn. "Ahmet") ve ham
            veriyi okunaklı hale getirmek için yapay zeka kullanılıyor. Bu key telefonunda
            güvenli bir şekilde saklanır, hiçbir sunucuya gönderilmez — doğrudan
            Anthropic'e bağlanılır.
          </Text>
          <Pressable
            onPress={() => Linking.openURL("https://console.anthropic.com/settings/keys")}
          >
            <Text style={styles.modalLink}>console.anthropic.com'dan key oluştur →</Text>
          </Pressable>

          <TextInput
            style={styles.modalInput}
            placeholder="sk-ant-..."
            value={value}
            onChangeText={setValue}
            autoCapitalize="none"
            autoCorrect={false}
            secureTextEntry
          />

          <View style={styles.modalActions}>
            <Pressable style={styles.modalSecondaryButton} onPress={onClose}>
              <Text style={styles.modalSecondaryText}>Vazgeç</Text>
            </Pressable>
            <Pressable
              style={styles.modalPrimaryButton}
              onPress={handleSave}
              disabled={saving}
            >
              <Text style={styles.buttonText}>{saving ? "Kaydediliyor..." : "Kaydet"}</Text>
            </Pressable>
          </View>
        </View>
      </View>
    </Modal>
  );
}

function ResultCard({ result }) {
  return (
    <View style={styles.card}>
      <View style={styles.cardHeader}>
        <Text style={styles.word}>{result.word}</Text>
        <View
          style={[
            styles.badge,
            result.source === "nisanyan" ? styles.badgeVerified : styles.badgeAi,
          ]}
        >
          <Text style={styles.badgeText}>
            {result.source === "nisanyan" ? "Nişanyan Sözlük" : "Yapay zeka tahmini"}
          </Text>
        </View>
      </View>

      <Field label="Kökeni" value={result.originLanguage} />
      <Field label="Açıklama" value={result.summary} />
      <Field label="Köken zinciri" value={result.derivationChain} />
      <Field label="Anlamı" value={result.meaning} />
      {result.funFact && <Field label="Ek bilgi" value={result.funFact} />}

      {result.source === "ai" && (
        <Text style={styles.disclaimer}>
          Bu bilgi doğrulanmış bir sözlükten değil, yapay zekanın kendi
          bilgisinden geliyor. Kesin bilgi için ayrıca araştırmanı öneririz.
        </Text>
      )}
    </View>
  );
}

function Field({ label, value }) {
  return (
    <View style={styles.field}>
      <Text style={styles.fieldLabel}>{label}</Text>
      <Text style={styles.fieldValue}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1, backgroundColor: "#fbfaf7" },
  content: { padding: 20, paddingTop: 60, paddingBottom: 40 },
  headerRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-start",
  },
  title: { fontSize: 28, fontWeight: "700", color: "#20201d" },
  subtitle: { fontSize: 14, color: "#6b6a63", marginTop: 4, marginBottom: 20 },
  gearButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: "#efece3",
    alignItems: "center",
    justifyContent: "center",
  },
  gearText: { fontSize: 18 },
  hint: {
    backgroundColor: "#eef2fb",
    borderRadius: 12,
    padding: 12,
    marginBottom: 16,
  },
  hintText: { fontSize: 12, color: "#3b5a9a" },
  searchRow: { flexDirection: "row", gap: 10 },
  input: {
    flex: 1,
    backgroundColor: "#fff",
    borderRadius: 12,
    paddingHorizontal: 16,
    paddingVertical: 12,
    fontSize: 16,
    borderWidth: 1,
    borderColor: "#e4e2da",
  },
  button: {
    backgroundColor: "#20201d",
    borderRadius: 12,
    paddingHorizontal: 20,
    justifyContent: "center",
  },
  buttonText: { color: "#fff", fontWeight: "600", fontSize: 16 },
  historyRow: { marginTop: 14, maxHeight: 40 },
  chip: {
    backgroundColor: "#efece3",
    borderRadius: 20,
    paddingHorizontal: 14,
    paddingVertical: 8,
    marginRight: 8,
  },
  chipText: { color: "#4a4941", fontSize: 13 },
  loading: { marginTop: 30 },
  errorBox: {
    marginTop: 24,
    backgroundColor: "#fdecec",
    borderRadius: 12,
    padding: 16,
  },
  errorText: { color: "#a83232" },
  card: {
    marginTop: 24,
    backgroundColor: "#fff",
    borderRadius: 16,
    padding: 20,
    borderWidth: 1,
    borderColor: "#eeece4",
  },
  cardHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 16,
  },
  word: { fontSize: 24, fontWeight: "700", color: "#20201d", textTransform: "capitalize" },
  badge: { borderRadius: 20, paddingHorizontal: 10, paddingVertical: 4 },
  badgeVerified: { backgroundColor: "#e3f2e6" },
  badgeAi: { backgroundColor: "#fdf3e0" },
  badgeText: { fontSize: 11, fontWeight: "600", color: "#4a4941" },
  field: { marginBottom: 14 },
  fieldLabel: {
    fontSize: 12,
    fontWeight: "700",
    color: "#9a988e",
    textTransform: "uppercase",
    marginBottom: 4,
    letterSpacing: 0.5,
  },
  fieldValue: { fontSize: 15, color: "#33322d", lineHeight: 21 },
  disclaimer: {
    marginTop: 8,
    fontSize: 12,
    color: "#a07a2b",
    fontStyle: "italic",
  },
  modalBackdrop: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.4)",
    justifyContent: "flex-end",
  },
  modalCard: {
    backgroundColor: "#fff",
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    padding: 24,
    paddingBottom: 36,
  },
  modalTitle: { fontSize: 18, fontWeight: "700", color: "#20201d", marginBottom: 10 },
  modalBody: { fontSize: 13, color: "#5c5b53", lineHeight: 19, marginBottom: 10 },
  modalLink: { fontSize: 13, color: "#3b5a9a", marginBottom: 18, fontWeight: "600" },
  modalInput: {
    backgroundColor: "#f5f4ef",
    borderRadius: 12,
    paddingHorizontal: 16,
    paddingVertical: 12,
    fontSize: 15,
    borderWidth: 1,
    borderColor: "#e4e2da",
    marginBottom: 20,
  },
  modalActions: { flexDirection: "row", gap: 10, justifyContent: "flex-end" },
  modalSecondaryButton: { paddingHorizontal: 18, paddingVertical: 12, justifyContent: "center" },
  modalSecondaryText: { color: "#6b6a63", fontWeight: "600" },
  modalPrimaryButton: {
    backgroundColor: "#20201d",
    borderRadius: 12,
    paddingHorizontal: 20,
    paddingVertical: 12,
    justifyContent: "center",
  },
});
