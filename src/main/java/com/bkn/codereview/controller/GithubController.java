package com.bkn.codereview.controller;

import com.bkn.codereview.github.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class GithubController {
    private final GithubService githubService;

    @PostMapping("/pr/comment")
    public void postPrComment(@RequestBody Map codeReviewMap) {
        githubService.postPrComment(codeReviewMap);
    }
}
