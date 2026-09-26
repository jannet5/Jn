import { useEffect, useState } from "react";
import {
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { fetchEtymology } from "./src/api";
import { addToHistory, loadHistory } from "./src/history";

export default function App() {
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);
  const [history, setHistory] = useState([]);

  useEffect(() => {
    loadHistory().then(setHistory);
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
        <Text style={styles.title}>Kelime Kökeni</Text>
        <Text style={styles.subtitle}>
          Bir kelime ya da isim yaz, kökenini öğren.
        </Text>

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
    </KeyboardAvoidingView>
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
  title: { fontSize: 28, fontWeight: "700", color: "#20201d" },
  subtitle: { fontSize: 14, color: "#6b6a63", marginTop: 4, marginBottom: 20 },
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
});
