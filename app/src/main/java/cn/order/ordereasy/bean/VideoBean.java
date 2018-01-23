package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

public class VideoBean extends BaseEntity {
    private String title;
    private List<VideoChildBean> content = new ArrayList<>();
    private int isSelcet = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VideoChildBean> getContent() {
        return content;
    }

    public void setContent(List<VideoChildBean> content) {
        this.content = content;
    }

    public int getIsSelcet() {
        return isSelcet;
    }

    public void setIsSelcet(int isSelcet) {
        this.isSelcet = isSelcet;
    }
}