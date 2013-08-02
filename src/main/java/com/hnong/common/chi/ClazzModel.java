package com.hnong.common.chi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 分类对象
 * User: zhong.huang
 * Date: 13-7-31
 */
public class ClazzModel {
    private String name;
    private String path;
    private List<DocModel> docModels;
    private Set<String> keywords;

    private List<String> docsPath;

    public ClazzModel() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DocModel> getDocModels() {
        return docModels;
    }

    public void setDocModels(List<DocModel> docModels) {
        this.docModels = docModels;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public void addKeywords(Set<String> keywords) {
        if (this.keywords == null) {
            this.keywords = new HashSet<String>();
        }
        this.keywords.addAll(keywords);
    }

    public List<String> getDocsPath() {
        if (docsPath == null) {
            docsPath = new ArrayList<String>();
            for (DocModel docModel : docModels) {
                docsPath.add(docModel.getPath());
            }
        }
        return docsPath;
    }
}
