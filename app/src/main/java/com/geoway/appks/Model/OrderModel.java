package com.geoway.appks.Model;

/**
 * 订单实体
 */

public class OrderModel {

    private Integer orderid;
    private String staffid;
    private Integer ordermoney;
    private String roomid;
    private String createdate;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public Integer getOrdermoney() {
        return ordermoney;
    }

    public void setOrdermoney(Integer ordermoney) {
        this.ordermoney = ordermoney;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
