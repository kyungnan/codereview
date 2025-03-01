package com.bkn.codereview.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SlackNotificationService {
    private final WebClient webClient;
    @Value("${slack.webhook-url}")
    private String SLACK_WEBHOOK_URL;

    public SlackNotificationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(SLACK_WEBHOOK_URL)
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
