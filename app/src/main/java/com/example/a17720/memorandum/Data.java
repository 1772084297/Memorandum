package com.example.a17720.memorandum;

import org.litepal.crud.DataSupport;

/**
 * Created by 17720 on 2017/12/8.
 */

public class Data extends DataSupport {
    private int id;
    private String title;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}
