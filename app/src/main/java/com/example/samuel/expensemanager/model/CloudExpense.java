package com.example.samuel.expensemanager.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Samuel on 15/11/12 10:42.
 * Email:samuel40@126.com
 */
public class CloudExpense extends BmobObject {
    private String username;
    private String date;
    private Double figure;
    private String typeName;
    private Integer typeColor;
    private Integer typeFlag;

    public CloudExpense() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getFigure() {
        return figure;
    }

    public void setFigure(Double figure) {
        this.figure = figure;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(Integer typeColor) {
        this.typeColor = typeColor;
    }

    public Integer getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(Integer typeFlag) {
        this.typeFlag = typeFlag;
    }
}
