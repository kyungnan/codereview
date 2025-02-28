package com.bkn.codereview.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeReviewResponseVO {
    /**
     * {
     *   "id": "chatcmpl-abc123",
     *   "object": "chat.completion",
     *   "created": 1700000000,
     *   "model": "gpt-4",
     *   "choices": [
     *     {
     *       "message": {
     *         "role": "assistant",
     *         "content": "### 코드 리뷰\n\n `@RequestMapping(\"/\")` 주석 처리\n해당 변경으로 인해 애플리케이션의 기본 요청 매핑이 사라졌습니다. 만약 `/` 경로로의 요청이 필요하다면, 적절한 대체 매핑을 추가해야 합니다.\n\n **개선 제안**\n- `@RequestMapping(\"/\")`을 제거하는 이유가 명확해야 합니다.\n- `/` 경로가 더 이상 필요 없다면, 관련된 API 문서도 업데이트해야 합니다.\n"
     *       },
     *       "finish_reason": "stop",
     *       "index": 0
     *     }
     *   ],
     *   "usage": {
     *     "prompt_tokens": 500,
     *     "completion_tokens": 100,
     *     "total_tokens": 600
     *   }
     * }
     */
    @JsonProperty("choices")
    private List<Choice> choices;

    public String getReviewContent() {
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).getMessage().getContent();
        }
        return "";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        @JsonProperty("message")
        private Message message;

        public Message getMessage() {
            return message;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        @JsonProperty("content")
        private String content;

        public String getContent() {
            return content;
        }
    }
}
