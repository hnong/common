package com.hnong.common.newsys;

/**
 * 分类结果
 */

public class ClassifyModel implements Comparable<ClassifyModel> {
    private float probability = 0f;//分类的概率
    private String clazz;//分类

    public ClassifyModel() {
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public int compareTo(ClassifyModel o) {
        double ret = this.probability - o.getProbability();
        if (ret < 0) {
            return 1;
        } else {
            return -1;
        }
    }
}