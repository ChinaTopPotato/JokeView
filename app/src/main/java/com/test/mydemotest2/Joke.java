package com.test.mydemotest2;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Joke类，进行笑话相关内容的存储，同时使用Serializable接口实现可以intent传输Joke类
 */

public class Joke extends DataSupport implements Serializable {
    private String title;
    private String content;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
