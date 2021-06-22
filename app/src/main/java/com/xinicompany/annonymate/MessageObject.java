package com.xinicompany.annonymate;

public class MessageObject {
    public String text , time , sender , msgid;
    public boolean flag;
    MessageObject(String text , String time , String sender , Boolean flag , String msgid){
        this.flag = flag;
        this.sender = sender;
        this.text = text;
        this.time = time;
        this.msgid = msgid;
    }
}
