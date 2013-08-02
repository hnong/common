package com.hnong.common.chi;

import java.io.*;
import java.util.*;

/**
 * User: zhong.huang
 * Date: 13-7-31
 */
public class CorpusManager {
    private static CorpusManager INSTANCE = null;
    private static String defaultPath = "E:\\temp\\Sample";//"E:\\temp\\Reduced";
    private static int Total_Docs = 0;

    private Map<String, Float> keywords_chi = null;//训练语料分类集合
    private Map<String, ClazzModel> clazzModels = null;
    private File dir = null;//训练语料存放目录

    private CorpusManager() {
        keywords_chi = new HashMap<String, Float>();
        clazzModels = new HashMap<String, ClazzModel>();
        this.init();
    }

    private void init() {
        this.dir = new File(defaultPath);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("训练语料库搜索失败！ [" + defaultPath + "]");
        }

        File clazzPath = null;
        List<DocModel> docModels = null;
        ClazzModel clazzModel = null;
        DocModel docModel = null;
        Set<String> keywords = null;
        for (String cla : dir.list()) {
            clazzModel = new ClazzModel();
            clazzModel.setName(cla);
            clazzModel.setPath(dir.getPath() + File.separator + cla);
            clazzPath = new File(clazzModel.getPath());
            docModels = new ArrayList<DocModel>();

            for (String docName : clazzPath.list()) {
                Total_Docs++;
                docModel = new DocModel();
                docModel.setName(docName);
                docModel.setPath(dir.getPath() + File.separator + cla + File.separator + docName);
                keywords = ChineseAnalyzer.split2Set(getText(docModel.getPath()));
                docModel.setKeywords(keywords);
                docModels.add(docModel);
                clazzModel.addKeywords(keywords);
                for (String keyword : keywords) {
                    keywords_chi.put(keyword, 0f);
                }
            }
            clazzModel.setDocModels(docModels);
            clazzModels.put(cla, clazzModel);
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
    public Set<String> getClazz() {
        return this.clazzModels.keySet();
    }

    /**
     * 根据训练文本类别返回这个类别下的所有训练文本路径（full path）
     *
     * @param clazz 给定的分类
     * @return 给定分类下所有文件的路径（full path）
     */
    public List<String> getDocsPath(String clazz) {
        return clazzModels.get(clazz).getDocsPath();
    }

    /**
     * 返回训练文本集中在给定分类下的训练文本数目
     *
     * @param clazz 给定的分类
     * @return 训练文本集中在给定分类下的训练文本数目
     */
    public int getDocsCount(String clazz) {
        return clazzModels.get(clazz).getDocModels().size();
    }

    /**
     * 返回给定路径的文本文件内容
     *
     * @param filePath 给定的文本文件路径
     * @return 文本内容
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    private static String getText(String filePath) {
        InputStreamReader isReader = null;
        try {
            isReader = new InputStreamReader(new FileInputStream(filePath), "GBK");

            BufferedReader reader = new BufferedReader(isReader);
            String aline;
            StringBuilder sb = new StringBuilder();
            while ((aline = reader.readLine()) != null) {
                sb.append(aline).append("\n");
            }

            isReader.close();
            reader.close();
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 返回训练文本集中所有的文本数目
     *
     * @return 训练文本集中所有的文本数目
     */
    public int getCorpusDocsCount() {
        return this.Total_Docs;
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
        for (DocModel docModel : clazzModels.get(clazz).getDocModels()) {
            if (docModel.getKeywords().contains(key)) {
                ret++;
            }
        }
        return ret;
    }

    public Set<String> getKeywords() {
        return keywords_chi.keySet();
    }

    public Map<String, ClazzModel> getClazzModels() {
        return this.clazzModels;
    }

    public static void main(String[] args) {
       CorpusManager.getInstance().getKeywords();
    }
}
