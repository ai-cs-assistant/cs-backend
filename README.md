# ai-cs-chat
智能客服聊天

串接 OpenAI GPT 模型
模擬系統讓客服人員與AI進行對話，並詢問各種問題

# 後端
## 一、使用 WebSocket 接收 前端 的訊息，並串接 OpenAI API gpt-4o 模型
## 二、Redis 黑名單機制-阻擋失效 Token 攻擊
Side Project 中，後端實作了 JWT 黑名單機制，透過 Redis 儲存已登出的失效 Token，有效阻擋過期或遭竄改的 Token 遭惡意使用，避免偽造用戶身分發出非法請求。透過 redis-cli MONITOR 指令即時監控 Redis 命令執行情形，Demo 流程中先展示登入與登出操作後，擷取已失效的 Token，並驗證該 Token 在再次發送請求時 遭後端拒絕的處理流程。
