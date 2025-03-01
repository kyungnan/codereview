package com.bkn.codereview.notification;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SlackNotificationService {
    private final WebClient webClient;

    public SlackNotificationService(WebClient.Builder webClientBuilder) {
        //https://hooks.slack.com/services/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
        this.webClient = webClientBuilder
                .baseUrl("https://hooks.slack.com/services/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")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void notifyToSlack(Map aiReview) {
        aiReview.forEach((filename, review) -> {
            notiryToSlack(filename.toString(), review.toString());
        });
    }

    /**
     * Slack에 알림
     * @param filename
     * @param review
     */
    private void notiryToSlack(String filename, String review) {
        this.webClient.post()
                .bodyValue(Map.of("text", String.format("%s \n %s", filename, review))) // PR 코멘트 내용
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
