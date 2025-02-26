package com.bkn.codereview.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContentVO {
    public String name;
    public String path;
    public String sha;
    public int size;
    public String url;

    public String htmlUrl;
    public String gitUrl;
    public String downloadUrl;
    public String type;
    public String content;
    public String encoding;
    public Link link;

    public class Link {
        public String self;
        public String git;
        public String html;
    }
}