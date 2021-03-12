package com.example.daiqu.bishe.data;

import java.io.File;

public class TaskData {
    private Integer id;
    private String taskCode;
    private String publisherPhone;
    private String accepterPhone;
    private String type;
    private String title;
    private String getPlace;
    private String postPlace;
    private String needTime;
    private String money;
    private String infomation;
    private String state;
    private String pic;
    private File file;
    private String time;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public TaskData(){

    }
    public TaskData(String title,String state,String type,String getPlace,String postPlace,String time){
        this.title = title;
        this.state = state;
        this.type = type;
        this.getPlace = getPlace;
        this.postPlace = postPlace;
        this.time = time;
    }



    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getPublisherPhone() {
        return publisherPhone;
    }

    public void setPublisherPhone(String publisherPhone) {
        this.publisherPhone = publisherPhone;
    }

    public String getAccepterPhone() {
        return accepterPhone;
    }

    public void setAccepterPhone(String accepterPhone) {
        this.accepterPhone = accepterPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGetPlace() {
        return getPlace;
    }

    public void setGetPlace(String getPlace) {
        this.getPlace = getPlace;
    }

    public String getPostPlace() {
        return postPlace;
    }

    public void setPostPlace(String postPlace) {
        this.postPlace = postPlace;
    }

    public String getNeedTime() {
        return needTime;
    }

    public void setNeedTime(String needTime) {
        this.needTime = needTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
