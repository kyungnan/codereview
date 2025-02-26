package com.bkn.codereview.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PullResponseVO {
    public String sha;
    public String filename;
    public String status;
    public int additions;
    public int deletions;
    public int changes;
    public String blobUrl;
    public String rawUrl;
    public String contentsUrl;
    public String patch;
}
