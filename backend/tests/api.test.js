const test = require('node:test');
const assert = require('node:assert');
const fs = require('fs');
const path = require('path');
const http = require('http');

process.env.DB_PATH = path.join(__dirname, 'test.sqlite');
process.env.JWT_SECRET = 'test-secret';

if (fs.existsSync(process.env.DB_PATH)) fs.unlinkSync(process.env.DB_PATH);

const { createApp } = require('../server');

let server;
let baseUrl;

test.before(async () => {
  server = createApp().listen(0);
  await new Promise((resolve) => server.once('listening', resolve));
  baseUrl = `http://127.0.0.1:${server.address().port}`;
});

test.after(async () => {
  await new Promise((resolve) => server.close(resolve));
  if (fs.existsSync(process.env.DB_PATH)) fs.unlinkSync(process.env.DB_PATH);
  const wal = process.env.DB_PATH + '-wal';
  const shm = process.env.DB_PATH + '-shm';
  if (fs.existsSync(wal)) fs.unlinkSync(wal);
  if (fs.existsSync(shm)) fs.unlinkSync(shm);
});

async function request(method, urlPath, { token, body } = {}) {
  const res = await fetch(baseUrl + urlPath, {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  const text = await res.text();
  const json = text ? JSON.parse(text) : null;
  return { status: res.status, json };
}

test('health check', async () => {
  const res = await request('GET', '/api/health');
  assert.strictEqual(res.status, 200);
  assert.strictEqual(res.json.status, 'ok');
});

test('rejects registration with short password', async () => {
  const res = await request('POST', '/api/auth/register', {
    body: { email: 'short@example.com', password: '123' },
  });
  assert.strictEqual(res.status, 400);
});

test('register, login, and manage notes end to end', async () => {
  const email = 'user@example.com';
  const password = 'correct-horse-battery-staple';

  const registerRes = await request('POST', '/api/auth/register', { body: { email, password } });
  assert.strictEqual(registerRes.status, 201);
  assert.ok(registerRes.json.token);

  const dupRes = await request('POST', '/api/auth/register', { body: { email, password } });
  assert.strictEqual(dupRes.status, 409);

  const loginRes = await request('POST', '/api/auth/login', { body: { email, password } });
  assert.strictEqual(loginRes.status, 200);
  const token = loginRes.json.token;

  const badLoginRes = await request('POST', '/api/auth/login', { body: { email, password: 'wrong' } });
  assert.strictEqual(badLoginRes.status, 401);

  const noAuthRes = await request('GET', '/api/notes');
  assert.strictEqual(noAuthRes.status, 401);

  const emptyListRes = await request('GET', '/api/notes', { token });
  assert.strictEqual(emptyListRes.status, 200);
  assert.deepStrictEqual(emptyListRes.json.notes, []);

  const createRes = await request('POST', '/api/notes', {
    token,
    body: { title: 'First note', body: 'Hello cloud' },
  });
  assert.strictEqual(createRes.status, 201);
  const noteId = createRes.json.note.id;

  const updateRes = await request('PUT', `/api/notes/${noteId}`, {
    token,
    body: { title: 'Updated note' },
  });
  assert.strictEqual(updateRes.status, 200);
  assert.strictEqual(updateRes.json.note.title, 'Updated note');
  assert.strictEqual(updateRes.json.note.body, 'Hello cloud');

  const listRes = await request('GET', '/api/notes', { token });
  assert.strictEqual(listRes.json.notes.length, 1);

  const deleteRes = await request('DELETE', `/api/notes/${noteId}`, { token });
  assert.strictEqual(deleteRes.status, 204);

  const finalListRes = await request('GET', '/api/notes', { token });
  assert.strictEqual(finalListRes.json.notes.length, 0);
});

test('a second user cannot see or modify the first user\'s notes', async () => {
  const userA = await request('POST', '/api/auth/register', {
    body: { email: 'a@example.com', password: 'password-a-1234' },
  });
  const userB = await request('POST', '/api/auth/register', {
    body: { email: 'b@example.com', password: 'password-b-1234' },
  });

  const created = await request('POST', '/api/notes', {
    token: userA.json.token,
    body: { title: 'Private to A' },
  });
  const noteId = created.json.note.id;

  const bList = await request('GET', '/api/notes', { token: userB.json.token });
  assert.deepStrictEqual(bList.json.notes, []);

  const bUpdate = await request('PUT', `/api/notes/${noteId}`, {
    token: userB.json.token,
    body: { title: 'Hijacked' },
  });
  assert.strictEqual(bUpdate.status, 404);

  const bDelete = await request('DELETE', `/api/notes/${noteId}`, { token: userB.json.token });
  assert.strictEqual(bDelete.status, 404);
});
