const express = require('express');
const db = require('../db');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();
router.use(requireAuth);

router.get('/', (req, res) => {
  const notes = db
    .prepare('SELECT id, title, body, created_at, updated_at FROM notes WHERE user_id = ? ORDER BY updated_at DESC')
    .all(req.userId);
  res.json({ notes });
});

router.post('/', (req, res) => {
  const { title, body } = req.body || {};
  if (typeof title !== 'string' || title.trim() === '') {
    return res.status(400).json({ error: 'Title is required' });
  }
  const result = db
    .prepare('INSERT INTO notes (user_id, title, body) VALUES (?, ?, ?)')
    .run(req.userId, title, body || '');
  const note = db.prepare('SELECT id, title, body, created_at, updated_at FROM notes WHERE id = ?').get(result.lastInsertRowid);
  res.status(201).json({ note });
});

function findOwnedNote(id, userId) {
  return db.prepare('SELECT * FROM notes WHERE id = ? AND user_id = ?').get(id, userId);
}

router.put('/:id', (req, res) => {
  const note = findOwnedNote(req.params.id, req.userId);
  if (!note) {
    return res.status(404).json({ error: 'Note not found' });
  }
  const { title, body } = req.body || {};
  const nextTitle = typeof title === 'string' && title.trim() !== '' ? title : note.title;
  const nextBody = typeof body === 'string' ? body : note.body;
  db.prepare("UPDATE notes SET title = ?, body = ?, updated_at = datetime('now') WHERE id = ?").run(
    nextTitle,
    nextBody,
    note.id
  );
  const updated = db.prepare('SELECT id, title, body, created_at, updated_at FROM notes WHERE id = ?').get(note.id);
  res.json({ note: updated });
});

router.delete('/:id', (req, res) => {
  const note = findOwnedNote(req.params.id, req.userId);
  if (!note) {
    return res.status(404).json({ error: 'Note not found' });
  }
  db.prepare('DELETE FROM notes WHERE id = ?').run(note.id);
  res.status(204).end();
});

module.exports = router;
