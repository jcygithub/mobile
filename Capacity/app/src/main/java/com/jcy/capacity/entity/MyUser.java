package com.jcy.capacity.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/8/26.
 * 用户属性
 */

public class MyUser extends BmobUser{
    private int age;
    private boolean sex;
    //简介
    private String desc;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
