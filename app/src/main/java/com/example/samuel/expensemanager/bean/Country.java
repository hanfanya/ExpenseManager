package com.example.samuel.expensemanager.bean;

import java.io.Serializable;

/**
 * 货币数据
 * Created by Allen_C on 2015/12/2.
 */
public class Country implements Serializable {
    private int ImageId;//国旗图标
    private String Currency;//货币缩写
    private String Unit;//货币单位
    private boolean isShown;//是否在ListView显示
    private boolean isSelected;//是否被用户选择作为原始货币
    private boolean isReplaced;//是否要被替换
    private double amount;

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }


    public boolean isShown() {
        return isShown;
    }

    public void setIsShown(boolean isShown) {
        this.isShown = isShown;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isReplaced() {
        return isReplaced;
    }

    public void setIsReplaced(boolean isReplaced) {
        this.isReplaced = isReplaced;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
