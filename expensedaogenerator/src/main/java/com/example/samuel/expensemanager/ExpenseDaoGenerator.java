package com.example.samuel.expensemanager;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExpenseDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.example.samuel.expensemanager.model");
//        schema.enableKeepSectionsByDefault();
//        schema.enableActiveEntitiesByDefault();

        Entity expense = schema.addEntity("Expense");

        expense.addIdProperty();//本地数据库表的 id
        expense.addStringProperty("expenseObjectId");//云端数据库表的 id
        expense.addStringProperty("date");//日期
        expense.addDoubleProperty("figure");//金额
        expense.addStringProperty("typeName");//类别名称
        expense.addIntProperty("typeColor");//类别颜色
        expense.addIntProperty("typeFlag");//类别属性
        expense.addIntProperty("isUploaded");//是否上传
        expense.addIntProperty("isModified");//是否删除
        expense.addIntProperty("isDeleted");//是否修改

        Entity typeInfo = schema.addEntity("TypeInfo");
        typeInfo.addIdProperty();
        typeInfo.addStringProperty("typeObjectId");
        typeInfo.addIntProperty("typeColor");
        typeInfo.addStringProperty("typeName");
        typeInfo.addStringProperty("typeFlag");
        typeInfo.addIntProperty("isUploaded");
        typeInfo.addIntProperty("isModified");
        typeInfo.addIntProperty("isDeleted");


        new DaoGenerator().generateAll(schema, "/Users/Samuel/AndroidStudioProjects/ExpenseManager/app/src/main/java");
    }
}
