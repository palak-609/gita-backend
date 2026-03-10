package com.gita.gitabackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Verse {
    public int id;

    @JsonProperty("chapter_number")
    public int chapter;

    @JsonProperty("verse_number")
    public int verse;

    public String text;
    public String transliteration;

    @JsonProperty("word_meanings")
    public String wordMeanings;

    public String translation;
}