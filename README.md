# Camunda 8 Demo

基于 Spring Boot 3 和 Camunda 8 的流程编排示例项目，演示如何使用 Zeebe 引擎实现订单处理自动化流程。

## 技术栈

| 组件 | 版本 |
|------|------|
| Spring Boot | 3.2.5 |
| Camunda SDK | 8.5.18 |
| Java | 17+ |
| Zeebe | 8.6.2 |
| Operate | 8.6.2 |
| Elasticsearch | 8.12.2 |

## 业务流程

订单处理流程 (`order-process.bpmn`) 包含以下步骤：

```
Start → Validate Order → Process Payment → Ship Order → End
```

- **validate-order**：验证订单信息
- **process-payment**：处理支付交易
- **ship-order**：生成物流单号

## API 接口

### 启动流程实例

```
POST /api/process/start
Content-Type: application/json
```

**请求体：**
```json
{
  "orderId": "ORD-001",
  "customerName": "John Doe",
  "amount": 99.99
}
```

**响应：**
```json
{
  "processInstanceKey": 2251799813685272,
  "bpmnProcessId": "order-process",
  "version": 1
}
```

## Quick Start

### 前置条件

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose

### 1. 启动基础设施

```bash
docker compose up -d
```

启动以下服务：
- **Zeebe**：流程引擎 (`localhost:26500`)
- **Operate**：流程监控 UI (`http://localhost:8081`)
- **Elasticsearch**：数据存储 (`http://localhost:9200`)

> **数据持久化**：容器数据已挂载到本地 `data/` 目录，重启后数据不会丢失。该目录已被 `.gitignore` 忽略，仅保留在本地。

### 2. 启动应用

```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动，并自动部署 BPMN 流程。

### 3. 测试流程

```bash
curl -X POST http://localhost:8080/api/process/start \
  -H "Content-Type: application/json" \
  -d '{"orderId":"ORD-001","customerName":"John Doe","amount":99.99}'
```

### 4. 查看流程

打开浏览器访问 Operate UI：http://localhost:8081

搜索返回的 `processInstanceKey` 即可查看流程执行详情和变量。

## 项目结构

```
├── docker-compose.yml
├── pom.xml
└── src/main/
    ├── java/com/example/camunda8demo/
    │   ├── Camunda8DemoApplication.java
    │   ├── ProcessDeployment.java          # 自动部署 BPMN
    │   ├── controller/
    │   │   └── ProcessController.java      # REST API
    │   └── worker/
    │       └── OrderProcessingWorker.java  # Job Workers
    └── resources/
        ├── application.yml
        └── processes/
            └── order-process.bpmn          # 流程定义
```
