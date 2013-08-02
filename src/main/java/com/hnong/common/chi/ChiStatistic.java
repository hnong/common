package com.hnong.common.chi;

import java.util.*;

/**
 * User: zhong.huang
 * Date: 13-8-2
 */
public class ChiStatistic {
    private static final double MIN_X2 = 0.5d;

    private Map<String, Double> keywords_chi = null;//训练语料分类集合
    private Map<String, Double> keywords_idf = null;//训练语料分类集合
    private Map<String, List<KeyChi>> clazz_keywords_chi = null;

    public ChiStatistic() {
        keywords_idf = new HashMap<String, Double>();
    }

    //1. 计算每个分类下的key的x2值，并选取top500个词作为特征词
    //2. 取得特征词之后，技术每个词的idf,作为语料库的特征词
    public void x2() {
        int a = 0; // 属于某个分类且出现在这个分类所有jd中的次数
        int b = 0; // 不属于这个分类但出现在其它分类所有jd中的次数
        int c = 0; // 属于这个分类但没有出现在这个分类所有jd中的次数
        int d = 0; // 不属于这个分类也没有出现在其它分类所有jd中的次数

        double P_key = 0d;
        double E_key = 0d;//包含Key的jd数量
        double x2 = 0d;
        Set<String> others = null;
        int n = 0;

        List<KeyChi> keyChis = null;
        KeyChi keyChi = null;
        for (Map.Entry<String, ClazzModel> entry : CorpusManager.getInstance().getClazzModels().entrySet()) {
            System.out.println(entry.getKey());
            for (String keyWord : entry.getValue().getKeywords()) {
                //属于这个分类
                a = CorpusManager.getInstance().getDocCountContainKey(entry.getKey(), keyWord);
                c = CorpusManager.getInstance().getDocsCount(entry.getKey()) - a;

                //不属于这个分类 ,注意这里要删除当前的分类
                others = new HashSet<String>();
                others.addAll(CorpusManager.getInstance().getClazz());
                others.add(entry.getKey());

                for (String clazz : others) {
                    n = CorpusManager.getInstance().getDocCountContainKey(clazz, keyWord);
                    b += n;
                    c += CorpusManager.getInstance().getDocsCount(clazz) - n;
                }
                //P_key = (a + b) / (a + b + c + d);
                //E_key = (a + c) * P_key;//

                x2 = Math.pow(a * d - b * c, 2) / ((a + b) * (c + d));
                if (x2 < 10000d) {//todo
                    continue;
                }
                System.out.println(keyWord + "--" + x2);
                // 暂时不保留x2值，直接保留idf
//                keywords_chi.put(keyWord, x2);
//                keyChi = new KeyChi();
//                keyChi.setKey(keyWord);
//                keyChi.setChi(x2);
//                keyChis.add(keyChi);
                keywords_idf.put(keyWord, idf(CorpusManager.getInstance().getCorpusDocsCount() / (a + b)));
            }
            //clazz_keywords_chi.put(entry.getKey(), keyChis);
        }
    }

    private Double idf(double n) {
        return Math.log(n + 0.01);
    }

    private double tf_idf(int tf, double idf) {
        return tf * idf;
    }

    class KeyChi {
        private String key;
        private double chi;

        KeyChi() {
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public double getChi() {
            return chi;
        }

        public void setChi(double chi) {
            this.chi = chi;
        }
    }

    public static void main(String[] args) {
        ChiStatistic chi = new ChiStatistic();
        chi.x2();
    }
}
