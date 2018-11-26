package com.frame.framedesignproject.model;

import com.base.model.BaseData;

/**
 * Author:GuosongBai
 * Date:2018/11/7 16:12
 * Description:
 */
public class UserInfo extends BaseData {
    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
