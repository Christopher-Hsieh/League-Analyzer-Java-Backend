package com.DataRepresentations;

public class Summoner {
    private final long id;
    private final String content;

    public Summoner(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
