package com.jnu.lzk_android.Dataprocess;

import java.io.Serializable;

public class GIft implements Serializable {
    //文本记录，金额和对象
    private String textName;
    //时间
    private String time;
    //原因记录
    private String textReason;
    //收礼为1，随礼为0
    private int type;

    public GIft(String textName, String time, String textReason,int type) {
        this.textName = textName;
        this.time = time;
        this.textReason=textReason;
        this.type = type;
    }

    public void  setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }
    public int getType() {
        return this.type;
    }
    public String getTextName() {
        return textName;
    }
    public String getTextReason() {
        return textReason;
    }

    public void setTextName(String Record_text) {
        this.textName = Record_text;
    }
    public void setTextReason(String Record_text) {
        this.textReason = Record_text;
    }
    public void setType(int Record_type) {
        this.type = Record_type;
    }

    public boolean EqualsGift(GIft gIft){
        if(this.type == gIft.type && this.textName.equals(gIft.textName)
                && this.time.equals(gIft.time)&&this.textReason.equals(gIft.textReason)){
            return true;
        }
        else
            return false;
    }
}
