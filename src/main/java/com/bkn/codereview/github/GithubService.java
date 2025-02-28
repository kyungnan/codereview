package com.bkn.codereview.github;

import com.bkn.codereview.vo.CodeReviewRequestVO;
import com.bkn.codereview.vo.ContentVO;
import com.bkn.codereview.vo.PullResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class GithubService {
    private final GithubClient githubClient;
    private String repo;
    private int pullNumber;

    /**
     * 테스트용
     * @param prUrl
     * @param repo
     */
    public void testService(String prUrl, String repo) {
        githubClient.testUser(prUrl, repo);
    }

    /**
     * PR 요청 정보 중 .java 확장자 파일만 가져오기
     * @param repo
     * @param pullNumber
     * @return
     */
    public List<PullResponseVO> getChangedJavaFiles(String repo, int pullNumber) {
        List<PullResponseVO> javaFiles = githubClient.getChangedJavaFiles(repo, pullNumber);
        log.debug("javaFiles : {}", javaFiles.stream().toString());
        return javaFiles;
    }

    /**
     * origin & changed Code 가져오기
     * @param repo
     * @param fileName
     * @param branchName
     * @return
     */
    public String getContentCode(String repo, String fileName, String branchName) {
        ContentVO contentVO = githubClient.getOriginFile(repo, fileName, branchName);
        // Base64 Decoding
        return new String(Base64.getDecoder().decode(contentVO.getContent().replaceAll("\\s", "")), StandardCharsets.UTF_8);
    }

    /**
     * 브랜치명 가져오기
     * @param prUrl
     * @return
     */
    public String getBranchName(String prUrl) {
        Map map = githubClient.getBranchName(prUrl);
        Map head = (Map) map.get("head");
        return head.get("ref").toString();
    }

    /**
     * 코드리뷰에 필요한 Request 정보 추출
     * @param payload
     * @return
     */
    public List<CodeReviewRequestVO> extractCodeReviewData(Map payload) {
        // payload에서 필요한 정보 추출
        String prUrl = (String) ((Map<?, ?>) payload.get("pull_request")).get("url");
        repo = (String) ((Map<?, ?>) payload.get("repository")).get("full_name");
        pullNumber = Integer.parseInt(payload.get("number").toString());

        // 브랜치명 가져오기
        String branchName = this.getBranchName(prUrl);
        // PR 코드 가져오기 (oring & change)
        List<CodeReviewRequestVO> codeReviewRequestVOList = new ArrayList<>();
        List<PullResponseVO> changedJavaFiles = this.getChangedJavaFiles(repo, pullNumber);
        for (PullResponseVO changedFile : changedJavaFiles) {
            String changedCode = this.getContentCode(repo, changedFile.getFilename(), branchName);
            String originCode = this.getContentCode(repo, changedFile.getFilename(), "master");
            codeReviewRequestVOList.add(CodeReviewRequestVO.builder()
                    .filaName(changedFile.filename)
                    .patch(changedFile.patch)
                    .status(changedFile.status)
                    .originCode(originCode)
                    .changedCode(changedCode)
                    .build());
        }
        return codeReviewRequestVOList;
    }

    /**
     * 코드리뷰 결과를 PR에 코멘트 남기기
     * @param codeReviewMap
     */
    public void postPrComment(Map codeReviewMap) {
        codeReviewMap.forEach((filename, review) -> {
            String codeReview = String.format("### 파일명: `%s`\n\n%s", filename, review);
            githubClient.postPrComment(repo, pullNumber, codeReview);
        });
    }
}
