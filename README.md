🚀 AI 기반 GitHub PR 코드 리뷰 자동화 시스템

📌 프로젝트 소개

이 프로젝트는 OpenAI 기반 코드 리뷰 자동화 시스템입니다. GitHub PR이 생성되면, 변경된 코드를 분석하여 AI가 코드 리뷰를 자동으로 생성하고, PR에 댓글을 남깁니다. 또한, Slack을 통해 리뷰 결과를 알림으로 받을 수도 있습니다.

🎯 주요 기능

✅ GitHub PR 코드 리뷰 자동화

PR이 생성되면 GitHub Webhook을 통해 변경된 파일을 분석

변경된 코드와 기존 코드를 비교하여 OpenAI에 코드 리뷰 요청

AI가 생성한 코드 리뷰를 PR에 자동으로 댓글로 작성

✅ Slack 알림 연동

PR에 대한 코드 리뷰가 완료되면 Slack 채널에 자동 알림

리뷰 피드백을 실시간으로 확인 가능

✅ 코드 품질 향상 지원

AI를 활용하여 일관성 있고 객관적인 코드 리뷰 제공

코드 스타일, 성능, 보안 취약점 등의 개선 방향 제시

🛠 기술 스택

Backend: Java 17, Spring Boot 3.4.1

AI: OpenAI GPT-4 API

Notifications: Slack Webhook

🚀 설치 및 실행 방법

🔗 GitHub Webhook 설정 방법

GitHub 저장소 > Settings > Webhooks 로 이동

Add webhook 클릭

Payload URL: http://your-server.com/webhook

Content type: application/json

Events: Pull requests

Add webhook 클릭

📌 API 명세

1️⃣ Webhook 엔드포인트

POST /webhook

설명: GitHub PR 이벤트를 수신하고 AI 코드 리뷰 요청을 수행합니다.

2️⃣ OpenAI 코드 리뷰 요청

POST /review

설명: 변경된 코드와 기존 코드를 OpenAI에 전송하여 코드 리뷰를 요청합니다.

3️⃣ PR에 코드 리뷰 댓글 작성

POST /pr/comment

설명: OpenAI가 생성한 리뷰 내용을 GitHub PR에 댓글로 등록합니다.

🎯 향후 개선 방향

🚀 AI 리뷰의 정확도 향상 (리뷰 레벨 조정, 코드 스타일 가이드 반영)

🚀 코드 자동 수정 제안 기능 추가

🚀 GitHub Actions 연동으로 PR 병합 자동화
