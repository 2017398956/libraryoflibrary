package com.nfl.libraryoflibrary.beans;

/**
 * 索引类的标志位的实体基类
 */

public class BaseIndexTagBean {
    protected String baseIndexTag;//所属的分类（城市的汉语拼音首字母）

    public String getBaseIndexTag() {
        return baseIndexTag;
    }

    public void setBaseIndexTag(String baseIndexTag) {
        this.baseIndexTag = baseIndexTag;
    }
}
