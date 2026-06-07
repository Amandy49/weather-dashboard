# Weather Dashboard
访问链接：8.163.28.204

一个基于 **Spring Boot + Vue 3** 的现代天气仪表盘应用，提供实时天气数据、城市搜索和天气预报功能。

## 🌟 项目特性

- ✅ **实时天气查询** - 支持全球城市天气查询
- ✅ **地理定位** - 获取当前位置的天气信息
- ✅ **城市搜索** - 快速搜索和对比不同城市天气
- ✅ **天气预报** - 获取城市的3小时粒度天气预报
- ✅ **查询历史** - 保存最近查询的城市天气
- ✅ **响应式设计** - 完美适配桌面和移动设备
- ✅ **美观UI** - 使用 Element Plus + SCSS 打造现代界面

## 🛠️ 技术栈

### 后端 (Backend)
- **Java 17** - 编程语言
- **Spring Boot 3.1.5** - 应用框架
- **Spring Data JPA** - ORM 框架
- **MySQL 8.0** - 数据库
- **OpenWeatherMap API** - 天气数据源

### 前端 (Frontend)
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Element Plus** - UI 组件库
- **Axios** - HTTP 客户端

## 📋 项目结构

```
weather-dashboard/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/weather/
│   │   ├── WeatherDashboardApplication.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   └── config/
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── frontend/                         # Vue 3 前端
│   ├── src/
│   │   ├── main.js
│   │   ├── App.vue
│   │   ├── views/
│   │   ├── router/
│   │   ├── services/
│   │   └── stores/
│   ├── index.html
│   └── package.json
│
└── README.md
```

## 🚀 快速开始

### 前提条件
- Java 17+
- Node.js 16+
- MySQL 8.0+
- OpenWeatherMap API Key

### 1. 获取 API Key

访问 [OpenWeatherMap](https://openweathermap.org/api) 获取免费 API Key

### 2. 配置数据库

```sql
CREATE DATABASE weather_db CHARACTER SET utf8mb4;
```

### 3. 后端配置

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/weather_db
    username: root
    password: your_password

weather:
  api:
    key: YOUR_API_KEY
```

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5173`

## 📡 API 端点

| 方法 | 路由 | 说明 |
|------|------|------|
| GET | `/api/weather/city?name=Beijing` | 根据城市名称获取天气 |
| GET | `/api/weather/coordinates?lat=39.9&lon=116.4` | 根据坐标获取天气 |
| GET | `/api/weather/forecast?city=Beijing` | 获取城市预报 |
| GET | `/api/weather/recent` | 获取最近查询 |
| GET | `/api/weather/search?keyword=Bei` | 搜索城市 |
| GET | `/api/weather/health` | 健康检查 |

## 🎨 功能页面

### Dashboard (首页)
- 实时天气显示
- 城市搜索
- 当前位置定位
- 详细天气参数
- 最近查询记录

### Search (搜索页)
- 城市搜索
- 搜索结果展示
- 城市对比
- 快速查看

## 📦 Docker 部署

```bash
# 一键启动所有服务
docker-compose up -d

# 停止服务
docker-compose down
```

## 📚 详细文档

- [DEVELOPMENT.md](DEVELOPMENT.md) - 开发指南
- [DEPLOYMENT.md](DEPLOYMENT.md) - 部署指南

## 🔧 环境变量

```bash
WEATHER_API_KEY=your_api_key
DB_URL=jdbc:mysql://localhost:3306/weather_db
DB_USERNAME=root
DB_PASSWORD=password
```

## 📝 许可证

MIT License

## 👨‍💻 作者

Amandy49

---

**提示**：这是一个学习项目，欢迎 Fork 和改进！
