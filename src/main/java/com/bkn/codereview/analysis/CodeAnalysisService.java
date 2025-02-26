package com.bkn.codereview.analysis;

import com.bkn.codereview.vo.CodeReviewRequestVO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CodeAnalysisService {
    private final WebClient webClient;

    public CodeAnalysisService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com").build();
    }
    public String requestCodeReview(CodeReviewRequestVO codeReviewRequest) {
        return "";
    }
}
