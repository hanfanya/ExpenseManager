package com.example.samuel.expensemanager.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "TYPE_INFO".
 */
public class TypeInfo {

    private Long id;
    private String typeObjectId;
    private Integer typeColor;
    private String typeName;
    private Integer typeFlag;
    private Integer frequency;
    private Integer uploadFlag;

    public TypeInfo() {
    }

    public TypeInfo(Long id) {
        this.id = id;
    }

    public TypeInfo(Long id, String typeObjectId, Integer typeColor, String typeName, Integer typeFlag, Integer frequency, Integer uploadFlag) {
        this.id = id;
        this.typeObjectId = typeObjectId;
        this.typeColor = typeColor;
        this.typeName = typeName;
        this.typeFlag = typeFlag;
        this.frequency = frequency;
        this.uploadFlag = uploadFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeObjectId() {
        return typeObjectId;
    }

    public void setTypeObjectId(String typeObjectId) {
        this.typeObjectId = typeObjectId;
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

    public Integer getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(Integer typeFlag) {
        this.typeFlag = typeFlag;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(Integer uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

}
