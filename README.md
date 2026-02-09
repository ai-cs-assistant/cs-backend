# ai-cs-chat 後端
智能客服聊天

串接 OpenAI GPT 模型
模擬系統讓客服人員與AI進行對話，並詢問各種問題

# 後端
## 一、使用 WebSocket 接收 前端 的訊息，並串接 OpenAI API gpt-4o 模型
## 二、Redis 黑名單機制-阻擋失效 Token 攻擊
Side Project 中，後端實作了 JWT 黑名單機制，透過 Redis 儲存已登出的失效 Token，有效阻擋過期或遭竄改的 Token 遭惡意使用，避免偽造用戶身分發出非法請求。透過 redis-cli MONITOR 指令即時監控 Redis 命令執行情形，Demo 流程中先展示登入與登出操作後，擷取已失效的 Token，並驗證該 Token 在再次發送請求時 遭後端拒絕的處理流程。


## 專案簡介

* 使用 JWT 做為 基礎的身分驗證
* Spring Security 的授權流程與 FilterChain 設計
* 使用 Redis 實作 JWT 黑名單，補足 logout 與撤銷能力
* WebSocket 即時通訊的安全設計，Handshake 階段的身份驗證


---

## 使用技術

* 認證與授權：Spring Security + JWT
* Token 撤銷機制：Redis JWT Blacklist（搭配 TTL）
* 即時通訊：Spring WebSocket

---

### 授權規則設計

```text
/auth/**        → permitAll（登入、註冊等）
/auth/logout    → authenticated
/api/**         → authenticated
/ws/**          → permitAll（交由 WebSocket 握手階段驗證）
anyRequest      → authenticated
```

### JWT Authentication Filter

自訂 JWT Filter 的職責：

1. 解析 `Authorization: Bearer <token>`
2. 驗證 JWT 簽章與有效期限
3. 檢查 token 是否存在於 Redis 黑名單
4. 驗證成功後，建立 Authentication 並放入 `SecurityContext`

---

## Redis JWT 黑名單設計


### Logout 流程

1. Client 呼叫 `/auth/logout`
2. Server 取得當前 JWT
3. 將 JWT 存入 Redis blacklist
4. 設定 TTL 為該 token 剩餘有效時間

### 後續請求處理

* 每次請求在 JWT Filter 中先檢查 Redis blacklist
* 若命中，直接回傳 401 Unauthorized

---

## WebSocket 安全設計

* Client 在 Handshake 時傳遞 JWT（Query Parameter）
* Server 在 Handshake Interceptor 中驗證 JWT
* 驗證通過才允許建立 WebSocket 連線

計畫 後續階段補強 token 傳遞方式的安全性、WebSocket token 過期與主動斷線機制
---


