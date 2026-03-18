# 部署方式
本專案使用 Docker Compose 進行部署，包含：
- Spring Boot 應用服務
- MariaDB 資料庫
## 啟動方式：bash
1. 先執行jar檔打包 -> mvn clean install -DskipTests
2. 執行 -> docker compose up --build 
## 啟動後網址瀏覽
http://<server-ip>:8080



# REST API
## 取得指定幣別最近 7 筆匯率資料
GET /api/rates/{currency}
### Path Parameter
currency : 幣別 (範例：USD)
### 呼叫範例
curl http://localhost:8080/api/rates/USD
### 回傳JSON範例
[
    {
        "baseCurrency": "TWD",
        "targetCurrency": "USD",
        "cashBuy": 31.515,
        "cashSell": 32.185,
        "spotBuy": 31.84,
        "spotSell": 31.99,
        "rateDate": "2026-03-15"
    }
]


## 手動更新某幣別匯率
POST /api/update
### 呼叫範例
curl -X POST http://localhost:8080/api/update \
-H "Content-Type: application/json" \
-d '{
"currency": "USD",
"rateDate": "2026-03-17",
"cashBuy": 31.2,
"cashSell": 31.8,
"spotBuy": 31.4,
"spotSell": 31.6
}'
### 回傳JSON範例
{
"id": 41,
"baseCurrency": "TWD",
"targetCurrency": "USD",
"cashBuy": 32.2,
"cashSell": 31.8,
"spotBuy": 31.4,
"spotSell": 31.6,
"rateDate": "2026-03-17",
"createdAt": "2026-03-17T12:02:01.812574",
"updatedAt": "2026-03-17T13:57:43.367899"
}