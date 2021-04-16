package com.example.daiqu.bishe.data;

public class msgDataWithNameAndType extends msgData {
    private String type;
    private String fromUserName;
    private String toUserName;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getFromUserName() {
        return fromUserName;
    }
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
    public String getToUserName() {
        return toUserName;
    }
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
}
