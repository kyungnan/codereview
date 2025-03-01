package com.bkn.codereview.analysis;

import com.bkn.codereview.vo.CodeReviewRequestVO;
import com.bkn.codereview.vo.CodeReviewResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CodeAnalysisService {
    private final WebClient webClient;
    @Value("${openai.api-key}")
    private String AI_TOKEN;
    public CodeAnalysisService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + AI_TOKEN)
                .build();
    }
    public Map requestCodeReview(List<CodeReviewRequestVO> CodeReviewRequestVOList) {
        Map map = new HashMap();
        for (CodeReviewRequestVO codeReviewRequestVO : CodeReviewRequestVOList) {
            String prompt = this.buildPrompt(codeReviewRequestVO);

            String response = this.webClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue("""
                                {
                                  "model": "gpt-4",
                                  "messages": [
                                    {"role": "system", "content": "You are a code reviewer. Provide feedback on the following code changes."},
                                    {"role": "user", "content": "%s"}
                                  ]
                                }
                            """.formatted(prompt))
                    .retrieve()
                    .bodyToMono(CodeReviewResponseVO.class)  // VO로 변환
                    .map(CodeReviewResponseVO::getReviewContent) // 코드 리뷰 결과 content만 추출
                    .block();

            map.put(codeReviewRequestVO.getFilaName(), response);
        }

        return map;
    }

    private String buildPrompt(CodeReviewRequestVO codeReviewRequestVO) {
        return """
            Code Review 요청입니다.
            파일명: %s
            상태: %s
            변경 전 코드:
            %s
            변경 후 코드:
            %s
            변경 사항 (Patch):
            %s
            
            이 변경 사항이 적절한지 코드 리뷰를 해주세요.
            - 버그 가능성
            - 코드 스타일 및 유지보수성
            - 성능 개선 가능성
        """.formatted(codeReviewRequestVO.getFilaName(), codeReviewRequestVO.getStatus(),
                codeReviewRequestVO.getOriginCode(), codeReviewRequestVO.getChangedCode(), codeReviewRequestVO.getPatch());
    }
}
