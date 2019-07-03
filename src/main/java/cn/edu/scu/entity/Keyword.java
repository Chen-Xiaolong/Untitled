package cn.edu.scu.entity;

public class Keyword {
    private int keywordId;

    private String keywordName;

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keyWordId) {
        this.keywordId = keyWordId;
    }

    public String getKeyWordName() {
        return keywordName;
    }

    public void setKeywordName(String keyWordName) {
        this.keywordName = keyWordName;
    }

    @Override
    public String toString() {
        return "KeyWord{" +
                "keyWordId=" + keywordId +
                ", keyWordName='" + keywordName + '\'' +
                '}';
    }
}
