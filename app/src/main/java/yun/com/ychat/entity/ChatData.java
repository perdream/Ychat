package yun.com.ychat.entity;

/**
 * Created by LPX on 2018/3/18.
 */

public class ChatData {
    //信息类型（get/send）
    private int type;
    //文本
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
