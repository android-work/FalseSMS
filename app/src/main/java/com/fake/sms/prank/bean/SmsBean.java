package com.fake.sms.prank.bean;

/**
 * @author Mr.Liu
 */
public class SmsBean {

    private int isLeft;
    private String smsContent;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public int isLeft() {
        return isLeft;
    }

    public void setLeft(int left) {
        isLeft = left;
    }


}
