# JCConf 2024 如何不要那麼狼狽的串接各種甲方的SSO - Demo

## 架構

- [/ui](ui): 前端專案，使用 Vue 撰寫
  - required: Nodejs 18+ & pnpm
  - setup: `pnpm install`
  - run: `pnpm dev`
  - port: 3000
- [/ap](ap): 後端專案，使用 Spring Boot 3 撰寫
  - required: Java 23, Maven 3.9+(or Maven Wrapper)
  - run: `mvn spring-boot:run` or `.\mvnw spring-boot:run`
  - port: 8080
- [/protal](protal): 入口，使用 Oauth2 Proxy
  - required: Docker 27+
  - run: `docker compose up -d`
  - port: 4180
  > Docker Desktop 必須要進到設定，勾起 General > Add the *.docker.internal...，
  > 且允許 com.docker.backend 通過防火牆
- [/sso](sso): SSO Server，使用 Keycloak + LDAP
  - required: Docker 27+
  - run: `docker compose up -d`
  - port: 8081 (keycloak), 5001 (ldap)
  > 以 `admin/admin` 登入，初次啟動後需要進入 User federation > ldap 重新設定 Bind credentials 為 admin
  > ，儲存後右上角Action選單選擇Sync All Users

## 使用說明

1. 啟動 SSO
2. 啟動 UI 與 AP
3. 啟動 Protal
4. 確認上面設定有沒有問題(尤其是SSO的Bind credentials跑掉的問題)
5. 進入 http://host.docker.internal:4180 (登入帳密 user01/password1 user02/password2)
6. 點擊 JCConf 2024
7. 操作 Hello / Check Twjug / Check Admin / Sign Out

# JCConf 2025(暫定) 如何有點狼狽的串接甲方自定義的SSO - Demo

## 情境

> 甲方有既存的SSO機制, 方式是先在甲方的SSO平台登入後, 透過甲方自己的Portal頁面進入到本系統  
> 進入本系統時, 會帶著一個Token(放在header或body), 本系統會需要拿token去向SSO平台要回User資訊(一次性)

## 實作方式

[sso-custom\tw.com.softleader.demo.oauth_server.SecurityConfig](./sso-custom/src/main/java/tw/com/softleader/demo/oauth_server/SecurityConfig.java)

1. 甲方SSO Token -> Chain1: 將 token 預先放到 session 備用, 並透過303將轉導為 GET -> 進入 Chain2

> 由於 oauth2-proxy 在 POST 時不會把 oauth2 流程所需資訊放到 body(form-data)  
> 但又 Spring Oauth2 在 POST 時只會從 body(form-data) 取資訊, 因此需要轉 GET

2. 自 oauth2-proxy -> Chain2: 正規 oauth2 流程, 但初次進入時由於本 auth server 尚未有登入資訊, 因此驗證失敗

> 已登入完成的情況下(有session), 於本步驟會驗證成功並 302 轉導至 oauth2-proxy callback url

3. 若 Chain2 驗證失敗, 透過302轉導 -> Chain3: 甲方SSO Token解析流程, 目的是將 token 驗證並解析為 User 資訊後放入 security context, 且需要存入 session(重要)

> 需要放入 session 的主因是 Chain2 驗證是否已登入的方式就是透過取得 session

4. Chain3 驗證成功, 透過302轉導 -> Chain2: 驗證成功 302 轉導至 oauth2-proxy callback url -> 登入完畢
