package com.geoway.appks.Model;


public class RoomModel {
    private int roomid;
    private String roomname;
    private int roomstate;
    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }
    public int getRoomid() {
        return roomid;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }
    public String getRoomname() {
        return roomname;
    }

    public void setRoomstate(int roomstate) {
        this.roomstate = roomstate;
    }
    public int getRoomstate() {
        return roomstate;
    }
}
