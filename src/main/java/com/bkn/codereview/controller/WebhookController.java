package com.bkn.codereview.controller;

import com.bkn.codereview.analysis.CodeAnalysisService;
import com.bkn.codereview.github.GithubService;
import com.bkn.codereview.vo.CodeReviewRequestVO;
import com.bkn.codereview.vo.PullResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {
    private final GithubService githubService;
    private final CodeAnalysisService codeAnalysisService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody Map payload) {
        String action = (String) payload.get("action");
        if ("opened".equals(action) || "synchronize".equals(action)) {
            // payload에서 코드리뷰에 필요한 데이터 추출
            List<CodeReviewRequestVO> codeReviewRequestVOList = githubService.extractCodeReviewData(payload);
            // Open AI 코드리뷰 요청
            Map<String, String> aiReview = codeAnalysisService.requestCodeReview(codeReviewRequestVOList);

            for (Map.Entry<String, String> entry : aiReview.entrySet()) {
                System.out.println(entry.getKey() + " ::: " + entry.getValue());
            }
        }
        return ResponseEntity.ok("Webhook received");
    }
}
