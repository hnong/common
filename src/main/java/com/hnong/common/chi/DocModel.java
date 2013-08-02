package com.hnong.common.chi;

import java.util.Set;

/**
 * 语料文档
 * User: zhong.huang
 * Date: 13-7-31
 */
public class DocModel {
    private String name;
    private String path;
    private Set<String> keywords;

    public DocModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

}
