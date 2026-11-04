package com.alatka.rule.example.entity;

public class SysParam {

    private String key;

    private String val;

    private String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "{" + "key='" + key + '\'' + ", val='" + val + '\'' + ", desc='" + desc + '\'' + '}';
    }
}
