import { MongoClient } from 'mongodb';
import fs from 'fs';
import path from 'path';

const url = 'mongodb://localhost:27017';
const dbName = 'buvette';
const client = new MongoClient(url);

async function loadJson(fileName) {
  // Resolve path relative to project root
  const filePath = path.resolve('scripts', 'data', fileName);
  const data = JSON.parse(fs.readFileSync(filePath, 'utf8'));
  return data;
}

async function syncCollection(db, collectionName, data, uniqueKey) {
  const collection = db.collection(collectionName);

  for (const item of data) {
    // Upsert: insert if not exist, update if exists
    await collection.updateOne(
      { [uniqueKey]: item[uniqueKey] },
      { $set: item },
      { upsert: true }
    );
  }
}

async function init() {
  try {
    await client.connect();
    const db = client.db(dbName);

    // Load menu items JSON
    const menuData = await loadJson('menu_items.json');

    // Sync menu collection
    await syncCollection(db, 'menu_items', menuData, 'name');

    console.log('MenuItems collection synced successfully!');
  } catch (err) {
    console.error(err);
  } finally {
    await client.close();
  }
}

init();
