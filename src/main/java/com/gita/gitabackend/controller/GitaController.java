package com.gita.gitabackend.controller;

import com.gita.gitabackend.model.Verse;
import com.gita.gitabackend.service.GitaService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GitaController {

    private final GitaService gitaService;

    public GitaController(GitaService gitaService) {
        this.gitaService = gitaService;
    }

    // GET /api/verses?emotion=anger
    @GetMapping("/verses")
    public List<Map<String, Object>> getVerses(
            @RequestParam(defaultValue = "sadness") String emotion) {

        List<Verse> verses = gitaService.getVersesByEmotion(emotion);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Verse v : verses) {
            Map<String, Object> verseMap = new LinkedHashMap<>();
            verseMap.put("chapter",         v.chapter);
            verseMap.put("verse",           v.verse);
            verseMap.put("sanskrit",        v.text);
            verseMap.put("transliteration", v.transliteration);
            verseMap.put("wordMeanings",    v.wordMeanings);
            verseMap.put("translation",     v.translation);
            verseMap.put("purport", v.purport);
            result.add(verseMap);
        }
        return result;
    }

    // GET /api/health
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("status", "ok");
        map.put("server", "gita-spring-boot");
        return map;
    }
}