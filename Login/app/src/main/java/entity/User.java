package entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/23.
 */

public class User {
    private String icon;

    private String name;

    private String pwd;

    private Date birthday;

    public User() {
    }

    public User(String name, String pwd, Date birthday) {
        super();
        this.name = name;
        this.pwd = pwd;
        this.birthday = birthday;
    }

    public User(String icon, String name, String pwd, Date birthday) {
        super();
        this.icon = icon;
        this.name = name;
        this.pwd = pwd;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


}
