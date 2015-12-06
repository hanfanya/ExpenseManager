package com.example.samuel.expensemanager.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by gongxiaolong on 15/12/3.
 */
public class MyUser extends BmobUser {
    private String nickname;

    private String userimageurl;

    private String userimageFileName;

    public String getUserimageFileName() {
        return userimageFileName;
    }

    public void setUserimageFileName(String userimageFileName) {
        this.userimageFileName = userimageFileName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserimageurl() {
        return userimageurl;
    }

    public void setUserimageurl(String userimageurl) {
        this.userimageurl = userimageurl;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
