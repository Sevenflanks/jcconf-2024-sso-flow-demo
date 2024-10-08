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