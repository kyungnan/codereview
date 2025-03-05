package com.bkn.codereview.controller;

import com.bkn.codereview.analysis.CodeAnalysisService;
import com.bkn.codereview.vo.CodeReviewRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CodeReviewController {
    private final CodeAnalysisService codeAnalysisService;

    @PostMapping("/review")
    public Map requestReview(@RequestBody CodeReviewRequestVO codeReviewRequestVO) {
        return codeAnalysisService.requestCodeReview(Arrays.asList(codeReviewRequestVO));
    }
}
