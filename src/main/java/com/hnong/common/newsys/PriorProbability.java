package com.hnong.common.newsys;

/**
 * 先验概率计算
 * <p/>
 * <h3>先验概率计算</h3>
 * <p/>
 * P(c<sub>j</sub>)=N(C=c<sub>j</sub>)<b>/</b>N <br>
 * <p/>
 * 其中，N(C=c<sub>j</sub>)表示类别c<sub>j</sub>中的训练文本数量；
 * <p/>
 * N表示训练文本集总数量。
 */

public class PriorProbability {
    /**
     * 先验概率,给定的分类的概率
     * @param clazz 给定的分类
     * @return 给定条件下的先验概率
     */
    public static float calPro(String clazz) {
        float ret = 0f;
        float Nc = CorpusManager.getInstance().getDocsCount(clazz);
        float N = CorpusManager.getInstance().getCorpusDocsCount();
        ret = Nc / N;
        System.out.println("calPro: "+ clazz + "-" + ret);
        return ret;
    }
}