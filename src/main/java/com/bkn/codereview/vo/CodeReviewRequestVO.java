package com.bkn.codereview.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class CodeReviewRequestVO {
    String filaName;
    String patch;
    String status;
    String originCode;
    String changedCode;
}
