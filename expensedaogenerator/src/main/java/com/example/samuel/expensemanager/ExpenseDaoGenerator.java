package com.example.samuel.expensemanager;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExpenseDaoGenerator {
    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");

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
        expense.addIntProperty("typeFlag");//类别属性，1 表示支出，0表示收入
        expense.addIntProperty("uploadFlag");//数据状态
        expense.addStringProperty("time");//增加记录或修改记录的具体时间

        Entity typeInfo = schema.addEntity("TypeInfo");
        typeInfo.addIdProperty();
        typeInfo.addStringProperty("typeObjectId");
        typeInfo.addIntProperty("typeColor");
        typeInfo.addStringProperty("typeName");
        typeInfo.addIntProperty("typeFlag");
        typeInfo.addIntProperty("frequency");
        typeInfo.addIntProperty("uploadFlag");


//        new DaoGenerator().generateAll(schema, "/Users/Samuel/AndroidStudioProjects/ExpenseManager/app/src/main/java");
        new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
    }
}
