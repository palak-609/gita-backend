package com.gita.gitabackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gita.gitabackend.model.Verse;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GitaService {

    private List<Verse> allVerses = new ArrayList<>();

    private static final Map<String, List<Integer>> EMOTION_MAP = new HashMap<>() {{
    put("anger",               List.of(109, 110, 156)); // 2:62, 2:63, 3:37
    put("sadness",             List.of(61,  67,  689)); // 2:14, 2:20, 18:66
    put("fear",                List.of(172, 87,  653)); // 4:10, 2:40, 18:30
    put("joy",                 List.of(225, 102, 253)); // 5:21, 2:55, 6:20
    put("confusion",           List.of(54,  121, 684)); // 2:7,  3:2,  18:61
    put("lust",                List.of(162, 156, 226)); // 3:43, 3:37, 5:22
    put("greed",               List.of(592, 131, 620)); // 16:21,3:12, 17:25
    put("attachment/delusion", List.of(109, 689, 138)); // 2:62, 18:66,3:19
    put("grief",               List.of(67,  74,  58));  // 2:20, 2:27, 2:11
    put("anxiety",             List.of(268, 259, 484)); // 6:35, 6:26, 12:15
    put("disgust",             List.of(574, 572, 670)); // 16:3, 16:1, 18:47
    put("surprise",            List.of(426, 76,  197)); // 11:12,2:29, 4:35
    put("contempt",            List.of(573, 482, 222)); // 16:2, 12:13,5:18
    put("shame/guilt",         List.of(689, 198, 368)); // 18:66,4:36, 9:30
}};

    @PostConstruct
    public void loadVerses() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/verse_translated.json");
            allVerses = mapper.readValue(is, new TypeReference<List<Verse>>() {});
            System.out.println("✅ Loaded " + allVerses.size() + " verses.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load verse_translated.json", e);
        }
    }

    public List<Verse> getVersesByEmotion(String emotion) {
        List<Integer> ids = EMOTION_MAP.getOrDefault(
            emotion.toLowerCase(), EMOTION_MAP.get("confusion")
        );
        Map<Integer, Verse> byId = allVerses.stream()
            .collect(Collectors.toMap(v -> v.id, v -> v));
        return ids.stream()
            .map(byId::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}