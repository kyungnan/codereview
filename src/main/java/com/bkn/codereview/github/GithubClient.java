package com.bkn.codereview.github;

import com.bkn.codereview.vo.ContentVO;
import com.bkn.codereview.vo.PullResponseVO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("githubClient")
public class GithubClient {
    private static GithubClient instance = null;
    private WebClient webClient;
    private String baseUrl = "https://api.github.com";
    private String userUrl = "/user";
    @Value("${github.token}")
    private String GITHUB_TOKEN;

    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + GITHUB_TOKEN)
                .build();
    }

    /**
     * Get singleton Instance
     * @return
     */
    public static GithubClient getClient() {
        if (instance == null)
            instance = new GithubClient();
        return instance;
    }

    /**
     * 테스트용
     */
    public void testUser(String prUrl, String repo) {
        try {
            String response = this.webClient
                    .get()
                    .uri(userUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.debug("response: {}", response);
        } catch (Exception e) {
            log.error("[githubClient.testUser] API Error. cause: {}, msg: {}, {}", e.getClass(), e.getMessage(), e);
        }
    }

    /**
     * 변경된 파일목록 조회
     * @param repo
     * @param pullNumber
     * @return
     */
    private Mono<List<PullResponseVO>> getChangedFiles(String repo, int pullNumber) {
        return webClient.get()
                .uri(String.format("/repos/%s/pulls/%d/files", repo, pullNumber))
                .header("Authorization", "Bearer " + GITHUB_TOKEN)
                .retrieve()
                .bodyToFlux(PullResponseVO.class)
                .collectList();
    }

    /**
     * .java 파일만 추출
     * @param repo
     * @param pullNumber
     * @return
     */
    public List<PullResponseVO> getChangedJavaFiles(String repo, int pullNumber) {
        return getChangedFiles(repo, pullNumber)
                .block()
                .stream()
                .filter(file -> file.getFilename().endsWith(".java"))
                .toList();
    }

    /**
     * 파일명 & 브랜치명으로 실제 코드 조회
     * @param repo
     * @param fileName
     * @param branchName
     * @return
     */
    public ContentVO getOriginFile(String repo, String fileName, String branchName) {
        // /repos/{owner}/{repo}/contents/{path}
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("/repos/%s/contents/%s", repo, fileName))
                        .queryParam("ref", branchName)
                        .build())
                .retrieve()
                .bodyToMono(ContentVO.class)
                .block();
    }

    /**
     * 브랜치명 가져오기 위해 prUrl 조회
     * @param prUrl
     * @return
     */
    public Map getBranchName(String prUrl) {
        return this.webClient.get()
                .uri(prUrl.replace(baseUrl, ""))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    /**
     * 코드리뷰 결과를 PR에 코멘트 남기기
     * @param repo
     * @param pullNumber
     * @param codeReview
     */
    public void postPrComment(String repo, int pullNumber, String codeReview) {
        // /repos/{owner}/{repo}/issues/{pull_number}/comments
        this.webClient.post()
                .uri(String.format("/repos/%s/issues/%d/comments", repo, pullNumber))
                .bodyValue(Map.of("body", codeReview)) // PR 코멘트 내용
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(response -> log.info("[GithubClient.postPrComment] PR comment success."));
    }
}
