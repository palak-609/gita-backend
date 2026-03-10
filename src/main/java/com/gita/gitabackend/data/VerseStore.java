package com.gita.gitabackend.data;

import com.gita.gitabackend.model.Verse;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class VerseStore {

    private final List<Verse> verses;

    public VerseStore() {
        this.verses = loadVerses();
    }

    public List<Verse> getVerses() {
        return verses;
    }

    private List<Verse> loadVerses() {
        List<Verse> list = new ArrayList<>();
        try {
            // Looks for verse.json in the project root (GITSNLP/gitabackend/)
            // Copy your data/verse.json into gitabackend/src/main/resources/verse.json
            InputStream is = getClass().getClassLoader().getResourceAsStream("verse.json");
            if (is == null) {
                System.err.println("❌ verse.json not found in resources! Copy it to src/main/resources/");
                return list;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            Verse v = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.contains("\"chapter_number\"")) {
                    v = new Verse();
                    v.chapter = Integer.parseInt(line.split(":")[1].replace(",", "").trim());
                }
                if (v != null && line.contains("\"verse_number\"")) {
                    v.verse = Integer.parseInt(line.split(":")[1].replace(",", "").trim());
                }
                if (v != null && line.contains("\"text\"")) {
                    v.text = extractValue(line);
                }
                if (v != null && line.contains("\"transliteration\"")) {
                    v.transliteration = extractValue(line);
                }
                if (v != null && line.contains("\"word_meanings\"")) {
                    v.wordMeanings = extractValue(line);
                    list.add(v);
                    v = null;
                }
            }
            reader.close();
            System.out.println("✅ Loaded " + list.size() + " verses from verse.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String extractValue(String line) {
        int start = line.indexOf(":") + 1;
        String val = line.substring(start).trim();
        if (val.startsWith("\"")) val = val.substring(1);
        if (val.endsWith(","))   val = val.substring(0, val.length() - 1);
        if (val.endsWith("\""))  val = val.substring(0, val.length() - 1);
        return val.trim();
    }
}