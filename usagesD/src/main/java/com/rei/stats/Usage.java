package com.rei.stats;


public class Usage {
    private String category;
    private String key;
    private long timestamp;

    public Usage() {
    }
    
    public Usage(String category, String key, long timestamp) {
        this.category = category;
        this.key = key;
        this.timestamp = timestamp;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}