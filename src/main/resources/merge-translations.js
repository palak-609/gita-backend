// merge-translations.js
// Run: node merge-translations.js
// Requires Node 18+ (built-in fetch) or install node-fetch: npm install node-fetch

const fs = require("fs");
const path = require("path");

// ── CONFIG ────────────────────────────────────────────────────────────────────
const INPUT_FILE  = "./verse.json";          // your original file
const OUTPUT_FILE = "./verse_translated.json"; // output

// Pick one translator key:
//   siva     → Swami Sivananda       (literal, classical)
//   purohit  → Shri Purohit Swami    (poetic, flowing)      ← recommended
//   san      → Dr. S. Sankaranarayan (scholarly)
//   adi      → Swami Adidevananda    (devotional)
//   gambir   → Swami Gambirananda    (academic)
//   prabhu   → A.C. Bhaktivedanta   (ISKCON)
const TRANSLATOR = "prabhu";

// Delay between requests in ms (be kind to the free API)
const DELAY_MS = 150;
// ─────────────────────────────────────────────────────────────────────────────

const sleep = ms => new Promise(r => setTimeout(r, ms));

async function fetchTranslation(chapter, verse) {
  const url = `https://vedicscriptures.github.io/slok/${chapter}/${verse}`;
  const res = await fetch(url);
  if (!res.ok) throw new Error(`HTTP ${res.status} for ${chapter}:${verse}`);
  const data = await res.json();
  return data[TRANSLATOR]?.et || "";
}

async function main() {
  if (!fs.existsSync(INPUT_FILE)) {
    console.error(`❌  ${INPUT_FILE} not found. Put this script in the same folder as your verse.json.`);
    process.exit(1);
  }

  const raw = fs.readFileSync(INPUT_FILE, "utf8");
  const verses = JSON.parse(raw);
  const total = verses.length;
  console.log(`📖  Loaded ${total} verses from ${INPUT_FILE}`);
  console.log(`🌐  Fetching translations (${TRANSLATOR}) from vedicscriptures.github.io...\n`);

  const result = [];
  let success = 0;
  let failed  = 0;

  for (let i = 0; i < verses.length; i++) {
    const v = verses[i];
    const ch = v.chapter_number;
    const vn = v.verse_number;

    try {
      const translation = await fetchTranslation(ch, vn);
      result.push({ ...v, translation });
      success++;

      // Progress bar
      const pct = Math.round(((i + 1) / total) * 100);
      const bar = "█".repeat(Math.floor(pct / 5)) + "░".repeat(20 - Math.floor(pct / 5));
      process.stdout.write(`\r  [${bar}] ${pct}%  (${i + 1}/${total})  Ch${ch}:${vn}  `);

    } catch (err) {
      result.push({ ...v, translation: "" });
      failed++;
      process.stdout.write(`\r  ⚠  Skipped Ch${ch}:${vn} — ${err.message}\n`);
    }

    await sleep(DELAY_MS);
  }

  console.log(`\n\n✅  Done! ${success} translated, ${failed} skipped.`);
  fs.writeFileSync(OUTPUT_FILE, JSON.stringify(result, null, 2), "utf8");
  console.log(`💾  Saved to ${OUTPUT_FILE}`);
}

main().catch(err => {
  console.error("\n❌  Fatal error:", err.message);
  process.exit(1);
});
