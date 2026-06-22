# 代码片段管理系统 - 部署文档

---

## 一、环境要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | 后端运行环境 |
| Maven | 3.8+ | 后端构建工具 |
| MySQL | 8.0+ | 数据库 |
| Node.js | 18+ | 前端运行环境 |
| npm | 9+ | 前端包管理 |

---

## 二、本地开发启动

### 2.1 数据库初始化

1. 安装并启动 MySQL 8.0
2. 执行建表脚本：

```bash
mysql -u root -p < backend/sql/init.sql
```

或在 MySQL 客户端中直接执行 `backend/sql/init.sql` 的内容。

### 2.2 修改后端配置

编辑 `backend/src/main/resources/application.yml`，修改数据库连接：

```yaml
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/code_snippet_db?...
      username: root        # 改为你的MySQL用户名
      password: root        # 改为你的MySQL密码
```

如需使用 AI 功能，登录系统后进入「系统设置」页面，填写 AI 接口信息（接口地址、密钥、模型名称），保存后即可使用。

### 2.3 启动后端

```bash
cd backend

# 方法1：使用 Maven 命令
mvn spring-boot:run

# 方法2：使用 IDEA
# 打开 backend 目录，运行 Gcsj2Application.main()
```

启动成功后，后端运行在 `http://localhost:8080`。

### 2.4 启动前端

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

启动成功后，前端运行在 `http://localhost:3000`，已配置代理到后端 8080 端口。

---

## 三、生产环境部署

### 3.1 后端打包

```bash
cd backend
mvn clean package -DskipTests
```

打包产物位于 `backend/target/code-snippet-server-1.0.0.jar`。

运行 jar 包：

```bash
java -jar code-snippet-server-1.0.0.jar
```

### 3.2 前端打包

```bash
cd frontend
npm run build
```

打包产物位于 `frontend/dist/` 目录，可部署到 Nginx。

### 3.3 Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态资源
    location / {
        root /path/to/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 代理
    location /api/ {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

---

## 四、Docker 部署

### 4.1 一键启动

确保已安装 Docker 和 Docker Compose，然后在项目根目录运行：

```bash
docker-compose up -d
```

这将自动启动：
- MySQL 8 数据库（端口 3306）
- SpringBoot 后端（端口 8080）
- Nginx 前端（端口 80）

### 4.2 自定义配置

编辑 `docker-compose.yml` 和环境变量文件修改数据库密码、JWT 密钥等。

---

## 五、常见问题

### Q1：前端启动报错 `Cannot find module 'xxx'`
```bash
cd frontend && rm -rf node_modules && npm install
```

### Q2：后端连接数据库失败
- 确认 MySQL 服务已启动
- 确认 `application.yml` 中数据库连接信息正确
- 确认已执行 `init.sql` 创建数据库和表

### Q3：跨域问题
- 开发环境：Vite 已配置代理，不会出现跨域
- 生产环境：使用 Nginx 反向代理，不会出现跨域
- 后端也已配置 CorsConfig 作为兜底

### Q4：JWT 令牌失效
- 默认有效期 2 小时，勾选"记住我"为 7 天
- 可在 `application.yml` 的 `jwt.expiration` 中调整
