package com.hnong.common.chi;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 中文分词器
 */

public class ChineseAnalyzer {
    private static IKSegmentation ikSeg = new IKSegmentation(new StringReader(""), true);

    public ChineseAnalyzer() {
    }

    /**
     * 对给定的文本进行中文分词
     *
     * @param content 给定的文本
     * @return 分词完毕的文本
     */
    public static Set<String> split2Set(String content) {
        Set<String> result = new HashSet<String>();
        ikSeg.reset(new StringReader(content));
        try {
            Lexeme lexeme = null;
            String txt = null;
            while ((lexeme = ikSeg.next()) != null) {
                txt = lexeme.getLexemeText();
                result.add(txt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对给定的文本进行中文分词
     *
     * @param content 给定的文本
     * @return 分词完毕的文本
     */
    public static List<String> split2List(String content) {
        List<String> result = new ArrayList<String>();
        ikSeg.reset(new StringReader(content));
        try {
            Lexeme lexeme = null;
            String txt = null;
            while ((lexeme = ikSeg.next()) != null) {
                txt = lexeme.getLexemeText();

                result.add(txt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> wds = ChineseAnalyzer.split2List("我是中国人，你是哪里人呢？不会也是中国的吧！");
        for (String wd : wds) {
            System.out.println(wd);
        }
    }
}