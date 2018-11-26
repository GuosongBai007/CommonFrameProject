package com.dcb.pushlib.model;

/**
 * Token Target存储类
 * Created by: GuosngBai
 * 2018/4/27.
 */
public class TokenModel {

    private String mToken;
    private Target mTarget;

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public Target getTarget() {
        return mTarget;
    }

    public void setTarget(Target mTarget) {
        this.mTarget = mTarget;
    }
}
