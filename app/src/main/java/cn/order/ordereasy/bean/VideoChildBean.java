package cn.order.ordereasy.bean;

public class VideoChildBean extends BaseEntity {
    private String subtitle;
    private String desc;
    private String youku_src;
    private String url;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getYouku_src() {
        return youku_src;
    }

    public void setYouku_src(String youku_src) {
        this.youku_src = youku_src;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}