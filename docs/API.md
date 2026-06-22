# 代码片段管理系统 - API 接口文档

## 基础信息

- **基础路径**：`http://localhost:8080/api`
- **认证方式**：JWT Bearer Token（请求头 `Authorization: Bearer <token>`）
- **统一返回格式**：
```json
{
  "code": 200,    // 200=成功 401=未登录 500=异常
  "msg": "操作成功",
  "data": {}
}
```

---

## 一、用户模块

### 1.1 用户注册

| 项目 | 内容 |
|------|------|
| 路径 | `POST /api/user/register` |
| 认证 | 否 |

**请求体**：
```json
{
  "username": "admin123",
  "password": "123456",
  "confirmPassword": "123456",
  "nickName": "管理员"
}
```

**校验规则**：
- `username`：必填，6-20位
- `password`：必填，6-16位
- `confirmPassword`：必填，需与 password 一致

**成功响应**：
```json
{ "code": 200, "msg": "注册成功", "data": null }
```

---

### 1.2 用户登录

| 项目 | 内容 |
|------|------|
| 路径 | `POST /api/user/login` |
| 认证 | 否 |

**请求体**：
```json
{
  "username": "admin123",
  "password": "123456",
  "rememberMe": false
}
```

**成功响应**：
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "admin123",
    "nickName": "管理员"
  }
}
```

> `rememberMe=true` 时，令牌有效期 7 天；否则 2 小时。

---

### 1.3 获取当前用户信息

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/user/info` |
| 认证 | 是 |

**成功响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "username": "admin123",
    "nickName": "管理员",
    "createTime": "2024-01-01T12:00:00"
  }
}
```

---

### 1.4 修改昵称

| 项目 | 内容 |
|------|------|
| 路径 | `PUT /api/user/nickname` |
| 认证 | 是 |

**请求体**：
```json
{ "nickName": "新昵称" }
```

---

### 1.5 修改密码

| 项目 | 内容 |
|------|------|
| 路径 | `PUT /api/user/password` |
| 认证 | 是 |

**请求体**：
```json
{
  "oldPassword": "123456",
  "newPassword": "654321",
  "confirmPassword": "654321"
}
```

---

## 二、代码分类模块

### 2.1 新增分类

| 项目 | 内容 |
|------|------|
| 路径 | `POST /api/category` |
| 认证 | 是 |

**请求体**：
```json
{
  "categoryName": "Java工具类",
  "sort": 1
}
```

---

### 2.2 编辑分类

| 项目 | 内容 |
|------|------|
| 路径 | `PUT /api/category` |
| 认证 | 是 |

**请求体**：
```json
{
  "id": 1,
  "categoryName": "Java工具类v2",
  "sort": 2
}
```

---

### 2.3 删除分类

| 项目 | 内容 |
|------|------|
| 路径 | `DELETE /api/category/{id}` |
| 认证 | 是 |

> 分类下存在代码片段时不可删除，返回错误提示。

---

### 2.4 分页查询分类

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/category/page?current=1&size=10` |
| 认证 | 是 |

**成功响应**：
```json
{
  "code": 200,
  "data": {
    "total": 5,
    "records": [...],
    "current": 1,
    "size": 10
  }
}
```

---

### 2.5 查询所有分类（下拉框用）

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/category/all` |
| 认证 | 是 |

---

## 三、代码标签模块

### 3.1 新增标签

| 项目 | 内容 |
|------|------|
| 路径 | `POST /api/tag` |
| 认证 | 是 |

**请求体**：
```json
{ "tagName": "数据库" }
```

---

### 3.2 编辑标签

| 项目 | 内容 |
|------|------|
| 路径 | `PUT /api/tag` |
| 认证 | 是 |

**请求体**：
```json
{ "id": 1, "tagName": "后端" }
```

---

### 3.3 删除标签

| 项目 | 内容 |
|------|------|
| 路径 | `DELETE /api/tag/{id}` |
| 认证 | 是 |

---

### 3.4 分页查询标签

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/tag/page?current=1&size=10` |
| 认证 | 是 |

---

### 3.5 查询所有标签

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/tag/all` |
| 认证 | 是 |

---

## 四、代码片段模块

### 4.1 新增代码片段

| 项目 | 内容 |
|------|------|
| 路径 | `POST /api/snippet` |
| 认证 | 是 |

**请求体**：
```json
{
  "title": "快速排序算法",
  "language": "python",
  "categoryId": 1,
  "tagIds": [1, 2],
  "codeContent": "def quick_sort(arr):\n    ...",
  "remark": "Python实现快速排序"
}
```

---

### 4.2 编辑代码片段

| 项目 | 内容 |
|------|------|
| 路径 | `PUT /api/snippet` |
| 认证 | 是 |

**请求体**：同新增，多传 `id` 字段

---

### 4.3 删除代码片段

| 项目 | 内容 |
|------|------|
| 路径 | `DELETE /api/snippet/{id}` |
| 认证 | 是 |

---

### 4.4 分页查询代码片段

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/snippet/page` |
| 认证 | 是 |

**请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 否 | 标题模糊搜索 |
| language | String | 否 | 语言筛选 |
| categoryId | Long | 否 | 分类ID筛选 |
| tagId | Long | 否 | 标签ID筛选 |
| isCollect | Boolean | 否 | 仅看收藏 |
| current | Integer | 否 | 页码（默认1） |
| size | Integer | 否 | 每页条数（默认10） |

**成功响应**：
```json
{
  "code": 200,
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "title": "快速排序",
        "language": "python",
        "categoryName": "算法",
        "tags": [{ "id": 1, "tagName": "排序" }],
        "isCollect": 0,
        "createTime": "2024-01-01T12:00:00"
      }
    ],
    "current": 1,
    "size": 10
  }
}
```

---

### 4.5 获取代码详情

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/snippet/{id}` |
| 认证 | 是 |

---

### 4.6 切换收藏

| 项目 | 内容 |
|------|------|
| 路径 | `PUT /api/snippet/{id}/collect` |
| 认证 | 是 |

---

### 4.7 获取代码内容

| 项目 | 内容 |
|------|------|
| 路径 | `GET /api/snippet/{id}/content` |
| 认证 | 是 |

---

## 五、AI 辅助模块

### 5.1 AI 代码处理

| 项目 | 内容 |
|------|------|
| 路径 | `POST /api/ai/process` |
| 认证 | 是 |

**请求体**：
```json
{
  "codeContent": "def add(a,b):\nreturn a+b",
  "language": "python",
  "actionType": "comment"
}
```

**actionType 说明**：

| 值 | 功能 |
|------|------|
| `comment` | 添加详细注释 |
| `debug` | BUG检测并修复 |
| `refactor` | 精简重构优化 |

**成功响应**：
```json
{
  "code": 200,
  "msg": "处理完成",
  "data": "# 这是处理后的代码...\ndef add(a, b):\n  ..."
}
```
