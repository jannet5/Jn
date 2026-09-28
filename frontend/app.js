const API_BASE = window.CLOUD_NOTES_API_BASE || 'http://localhost:3000/api';
const TOKEN_KEY = 'cloud-notes-token';

const authView = document.getElementById('authView');
const notesView = document.getElementById('notesView');
const authForm = document.getElementById('authForm');
const authTitle = document.getElementById('authTitle');
const authError = document.getElementById('authError');
const toggleAuthMode = document.getElementById('toggleAuthMode');
const logoutBtn = document.getElementById('logoutBtn');
const noteForm = document.getElementById('noteForm');
const notesList = document.getElementById('notesList');
const syncStatus = document.getElementById('syncStatus');

let mode = 'login';

function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

function setToken(token) {
  if (token) localStorage.setItem(TOKEN_KEY, token);
  else localStorage.removeItem(TOKEN_KEY);
}

async function api(path, options = {}) {
  const token = getToken();
  const res = await fetch(API_BASE + path, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    },
  });
  if (res.status === 401) {
    setToken(null);
    showAuthView();
    throw new Error('Oturum süresi doldu, tekrar giriş yapın');
  }
  const text = await res.text();
  const json = text ? JSON.parse(text) : null;
  if (!res.ok) {
    throw new Error(json && json.error ? json.error : 'Bilinmeyen hata');
  }
  return json;
}

function showAuthView() {
  authView.hidden = false;
  notesView.hidden = true;
  logoutBtn.hidden = true;
}

function showNotesView() {
  authView.hidden = true;
  notesView.hidden = false;
  logoutBtn.hidden = false;
  loadNotes();
}

toggleAuthMode.addEventListener('click', () => {
  mode = mode === 'login' ? 'register' : 'login';
  authTitle.textContent = mode === 'login' ? 'Giriş yap' : 'Kayıt ol';
  authForm.querySelector('.primary-btn').textContent = mode === 'login' ? 'Giriş yap' : 'Kayıt ol';
  toggleAuthMode.textContent = mode === 'login' ? 'Hesabın yok mu? Kayıt ol' : 'Zaten hesabın var mı? Giriş yap';
  authError.textContent = '';
});

authForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  authError.textContent = '';
  const email = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value;
  try {
    const data = await api(`/auth/${mode === 'login' ? 'login' : 'register'}`, {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
    setToken(data.token);
    authForm.reset();
    showNotesView();
  } catch (err) {
    authError.textContent = err.message;
  }
});

logoutBtn.addEventListener('click', () => {
  setToken(null);
  showAuthView();
});

noteForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const title = document.getElementById('noteTitle').value.trim();
  const body = document.getElementById('noteBody').value;
  if (!title) return;
  try {
    syncStatus.textContent = 'Kaydediliyor...';
    await api('/notes', { method: 'POST', body: JSON.stringify({ title, body }) });
    noteForm.reset();
    await loadNotes();
  } catch (err) {
    syncStatus.textContent = `Hata: ${err.message}`;
  }
});

async function loadNotes() {
  try {
    syncStatus.textContent = 'Senkronize ediliyor...';
    const data = await api('/notes');
    renderNotes(data.notes);
    syncStatus.textContent = `Buluta bağlı · ${data.notes.length} not`;
  } catch (err) {
    syncStatus.textContent = `Çevrimdışı veya hata: ${err.message}`;
  }
}

function renderNotes(notes) {
  notesList.innerHTML = '';
  for (const note of notes) {
    const li = document.createElement('li');
    li.className = 'note-item';
    li.innerHTML = `
      <h3></h3>
      <p></p>
      <div class="note-actions">
        <button data-action="edit">Düzenle</button>
        <button data-action="delete" class="delete">Sil</button>
      </div>
    `;
    li.querySelector('h3').textContent = note.title;
    li.querySelector('p').textContent = note.body;

    li.querySelector('[data-action="delete"]').addEventListener('click', async () => {
      try {
        await api(`/notes/${note.id}`, { method: 'DELETE' });
        await loadNotes();
      } catch (err) {
        syncStatus.textContent = `Hata: ${err.message}`;
      }
    });

    li.querySelector('[data-action="edit"]').addEventListener('click', async () => {
      const newTitle = prompt('Yeni başlık', note.title);
      if (newTitle === null) return;
      const newBody = prompt('Yeni içerik', note.body);
      if (newBody === null) return;
      try {
        await api(`/notes/${note.id}`, {
          method: 'PUT',
          body: JSON.stringify({ title: newTitle, body: newBody }),
        });
        await loadNotes();
      } catch (err) {
        syncStatus.textContent = `Hata: ${err.message}`;
      }
    });

    notesList.appendChild(li);
  }
}

if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('sw.js').catch(() => {
      /* offline shell is a progressive enhancement; app still works without it */
    });
  });
}

if (getToken()) {
  showNotesView();
} else {
  showAuthView();
}
