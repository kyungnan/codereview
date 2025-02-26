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
        System.out.println(payload.toString());
        String action = (String) payload.get("action");
        if ("opened".equals(action) || "synchronize".equals(action)) {
            System.out.println("action: " + action);
            String prUrl = (String) ((Map<?, ?>) payload.get("pull_request")).get("url");
            String repo = (String) ((Map<?, ?>) payload.get("repository")).get("full_name");
            int pullNumber = Integer.parseInt(payload.get("number").toString());

            // 브랜치명 가져오기
            String branchName = githubService.getBranchName(prUrl);

            // PR 코드 가져오기 (oring & change)
            List<PullResponseVO> changedJavaFiles = githubService.getChangedJavaFiles(repo, pullNumber);
            for (PullResponseVO changedFile : changedJavaFiles) {
                String changedCode = githubService.getContentCode(repo, changedFile.getFilename(), branchName);
                String originCode = githubService.getContentCode(repo, changedFile.getFilename(), "master");

                // Open AI 코드리뷰 요청
                String aiReview = codeAnalysisService.requestCodeReview(CodeReviewRequestVO.builder()
                        .filaName(changedFile.filename)
                        .patch(changedFile.patch)
                        .status(changedFile.status)
                        .originCode(originCode)
                        .changedCode(changedCode)
                        .build());
            }
        }
        return ResponseEntity.ok("Webhook received");
    }
}
