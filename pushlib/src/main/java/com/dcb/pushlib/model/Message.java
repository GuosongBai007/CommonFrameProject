package com.dcb.pushlib.model;

/**
 * 消息模型
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public final class Message {
    private int notifyID;  //这个字段用于通知的消息类型，在透传中都是默认0
    private String messageID;
    private String title;
    private String message;
    private String extra;
    private Target target;
    private int msgType = -1;
    private String userId = "";

    public int getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(int notifyID) {
        this.notifyID = notifyID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "notifyID=" + notifyID +
                ", messageID='" + messageID + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", extra='" + extra + '\'' +
                ", target=" + target +
                '}';
    }


}
