package com.hnong.common.newsys;

import java.io.*;
import java.util.*;

/**
 * User: zhong.huang
 * Date: 13-7-31
 */
public class CorpusManager {
    private static CorpusManager INSTANCE = null;
    private static String defaultPath = "E:\\temp\\Reduced";
    private static int N = 0;

    private Map<String, Integer> clazz_n = new HashMap<String, Integer>();//分类下的文档数
    private Map<String, List<String>> clazz_docs_path = new HashMap<String, List<String>>();//分类下的文档路径
    private List<String> clazz;//训练语料分类集合
    private File dir;//训练语料存放目录

    private CorpusManager() {
        this.init();
    }

    private void init() {
        this.dir = new File(defaultPath);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("训练语料库搜索失败！ [" + defaultPath + "]");
        }
        this.clazz = Arrays.asList(dir.list());

        // 计算各分类下的文档数
        int n = 0;
        File classDir = null;
        String[] docs = null;
        List<String> docs_path = null;
        for (String cla : clazz) {
            classDir = new File(dir.getPath() + File.separator + cla);
            docs = classDir.list();
            n = docs.length;
            clazz_n.put(cla, n);

            docs_path = new ArrayList<String>();
            clazz_docs_path.put(cla, docs_path);
            for (String name : docs) {
                docs_path.add(dir.getPath() + File.separator + cla + File.separator + name);
            }
            N += n;
        }
    }

    public static CorpusManager getInstance() {
        if (null == INSTANCE) {
            synchronized (CorpusManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new CorpusManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 返回训练文本类别，这个类别就是目录名
     *
     * @return 训练文本类别
     */
    public List<String> getClazz() {
        return this.clazz;
    }

    /**
     * 根据训练文本类别返回这个类别下的所有训练文本路径（full path）
     *
     * @param clazz 给定的分类
     * @return 给定分类下所有文件的路径（full path）
     */
    public List<String> getDocsPath(String clazz) {
        return clazz_docs_path.get(clazz);
    }

    /**
     * 返回训练文本集中在给定分类下的训练文本数目
     *
     * @param clazz 给定的分类
     * @return 训练文本集中在给定分类下的训练文本数目
     */
    public int getDocsCount(String clazz) {
        return clazz_n.get(clazz);
    }

    /**
     * 返回给定路径的文本文件内容
     *
     * @param filePath 给定的文本文件路径
     * @return 文本内容
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static String getText(String filePath) throws IOException {
        InputStreamReader isReader = new InputStreamReader(new FileInputStream(filePath), "GBK");
        BufferedReader reader = new BufferedReader(isReader);
        String aline;
        StringBuilder sb = new StringBuilder();
        while ((aline = reader.readLine()) != null) {
            sb.append(aline).append("\n");
        }

        isReader.close();
        reader.close();
        return sb.toString();
    }

    /**
     * 返回训练文本集中所有的文本数目
     *
     * @return 训练文本集中所有的文本数目
     */
    public int getCorpusDocsCount() {
        return N;
    }

    /**
     * 返回给定分类中包含关键字／词的训练文本的数目
     *
     * @param clazz 给定的分类
     * @param key   给定的关键字／词
     * @return 给定分类中包含关键字／词的训练文本的数目
     */
    public int getDocCountContainKey(String clazz, String key) {
        int ret = 0;
        try {
            String text = null;
            for (String doc : clazz_docs_path.get(clazz)) {
                text = getText(doc);
                if (text.contains(key)) {
                    ret++;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
