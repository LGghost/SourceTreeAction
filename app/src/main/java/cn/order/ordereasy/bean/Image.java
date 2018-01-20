package cn.order.ordereasy.bean;

/**
 * Created by Mr.Pan on 2017/9/11.
 */

public class Image extends BaseEntity{
    private String title="";
    private String url="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
