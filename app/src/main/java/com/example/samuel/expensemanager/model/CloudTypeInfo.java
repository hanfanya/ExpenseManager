package com.example.samuel.expensemanager.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Samuel on 15/11/12 10:44.
 * Email:samuel40@126.com
 */
public class CloudTypeInfo extends BmobObject {
    private String username;
    private Integer typeColor;
    private String typeName;
    private String typeFlag;

    public CloudTypeInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(Integer typeColor) {
        this.typeColor = typeColor;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag = typeFlag;
    }
}