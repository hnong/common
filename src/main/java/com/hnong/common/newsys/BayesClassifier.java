package com.hnong.common.newsys;

import java.util.*;


/**
 * 朴素贝叶斯分类器
 */

public class BayesClassifier {
    private static float zoomFactor = 10.00f;

    /**
     * 默认的构造器，初始化训练集
     */
    public BayesClassifier() {
    }

    /**
     * 计算给定的文本属性向量X在给定的分类Cj中的类条件概率
     * <p/>
     * <code>ClassConditionalProbability</code>连乘值
     *
     * @param X  给定的文本属性向量
     * @param Cj 给定的类别
     * @return 分类条件概率连乘值，即<br>
     */
    private float calcProd(Set<String> X, String Cj) {
        float ret = 1F;
        // 类条件概率连乘
        for (String Xi : X) {
            //因为结果过小，因此在连乘之前放大10倍，这对最终结果并无影响，因为我们只是比较概率大小而已
            ret *= ClazzConditionProbability.calProxc(Cj, Xi) * zoomFactor;
            System.out.println(Xi + ":" + ret);
        }

        // 再乘以先验概率
        ret *= PriorProbability.calPro(Cj);
        return ret;
    }

    /**
     * 对给定的文本进行分类
     *
     * @param text 给定的文本
     * @return 分类结果
     */
    public String classify(String text) {
        Set<String> terms = ChineseAnalyzer.split2Set(text);//中文分词处理(分词后结果可能还包含有停用词）

        List<String> clazz = CorpusManager.getInstance().getClazz();//分类
        List<ClassifyModel> crs = new LinkedList<ClassifyModel>();//分类结果

        float priority = 0F;
        ClassifyModel cr = null;

        for (String ci : clazz) {
            priority = calcProd(terms, ci);//计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
            //保存分类结果
            cr = new ClassifyModel();
            cr.setClazz(ci);
            cr.setProbability(priority);
            crs.add(cr);

            System.out.println(ci + "：" + priority);
        }

        //返回概率最大的分类
        return crs.get(0).getClazz();
    }


    public static void main(String[] args) {

        String text = "　新浪财经评论员老艾[微博]认为，今日大盘走的让大家颇为纠结，上午的一波直线拉升一度让大家亢奋，但午后新增量能未能跟上，再次缓缓滑落。上涨原因是央行逆回购170亿结束5周零操作，缓解市场资金压力。央妈终于不再像上次那样袖手旁观，7月份很快也要过去了，钱荒问题会得以缓解。创业板在早盘大幅杀跌，但很快引来资金抄底，收了一根长下影线，近期从机构及资金动向来看，对创业板不是很有利，毕竟累计涨幅太高，资金有套现冲动也很正常。但目前来看，权重及周期股仍难有作为，市场热点仍得由中小盘概念股发起，相信在经过回调之后，一些长期有发展潜力的个股仍会再次受到资金追捧，目前趁着回调大浪淘沙，正是精选个股的好机会，为下一波行情做好准备。。";

        BayesClassifier classifier = new BayesClassifier();//构造Bayes分类器
        String result = classifier.classify(text);//进行分类
        System.out.println("此项属于[" + result + "]");

    }

    /**
     C000007	汽车
     C000008	财经
     C000010	IT
     C000013	健康
     C000014	体育
     C000016	旅游
     C000020	教育
     C000022	招聘
     C000023	文化
     C000024	军事
     */

}