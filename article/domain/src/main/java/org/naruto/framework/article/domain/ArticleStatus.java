package org.naruto.framework.article.domain;

public enum ArticleStatus {

    PUBLISH("publish"),DRAFT("draft")
    ;

    private String value;
    private ArticleStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}