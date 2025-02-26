package com.bkn.codereview.github;

import com.bkn.codereview.vo.ContentVO;
import com.bkn.codereview.vo.PullResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class GithubService {
    private final GithubClient githubClient;

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
}
