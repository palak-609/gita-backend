package com.gita.gitabackend.service;

import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    /**
     * Converts word_meanings like:
     * "kāmaḥ—desire; krodhaḥ—wrath; eṣhaḥ—this; rajaḥ-guṇa—the mode of passion"
     * into a readable English paragraph.
     */
    public String getTranslation(int chapter, int verse, String transliteration, String wordMeanings) {
        if (wordMeanings == null || wordMeanings.trim().isEmpty()) {
            return "This verse from the Bhagavad Gita, Chapter " + chapter + ", Verse " + verse
                    + ", contains profound spiritual wisdom from Lord Krishna.";
        }

        try {
            return buildReadableTranslation(chapter, verse, wordMeanings);
        } catch (Exception e) {
            return cleanFallback(wordMeanings);
        }
    }

    private String buildReadableTranslation(int chapter, int verse, String wordMeanings) {
        // Clean the raw word_meanings string
        String cleaned = wordMeanings
                .replace("\\n", " ")
                .replace("\n", " ")
                .replace("\\t", " ")
                .trim();

        // Split into individual word—meaning pairs
        String[] pairs = cleaned.split(";");
        StringBuilder meanings = new StringBuilder();

        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) continue;

            // Each pair is like "kāmaḥ—desire" or "śhri-bhagavān uvācha—the Supreme Lord said"
            int dashIdx = pair.indexOf("—");
            if (dashIdx == -1) dashIdx = pair.indexOf("-");  // fallback
            if (dashIdx == -1) continue;

            String meaning = pair.substring(dashIdx + 1).trim();
            // Remove trailing punctuation
            meaning = meaning.replaceAll("[,;]+$", "").trim();

            if (!meaning.isEmpty() && meaning.length() > 1) {
                if (meanings.length() > 0) meanings.append("; ");
                meanings.append(meaning);
            }
        }

        if (meanings.length() == 0) return cleanFallback(wordMeanings);

        // Build a clean readable string
        String raw = meanings.toString();

        // Capitalize first letter
        if (!raw.isEmpty()) {
            raw = raw.substring(0, 1).toUpperCase() + raw.substring(1);
        }

        // If it ends without a period, add one
        if (!raw.endsWith(".") && !raw.endsWith("!") && !raw.endsWith("?")) {
            raw = raw + ".";
        }

        return raw;
    }

    private String cleanFallback(String wordMeanings) {
        return wordMeanings
                .replace("\\n", " ")
                .replace("\n", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}