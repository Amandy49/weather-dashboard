# 开发指南

## 项目架构

这是一个完全的前后端分离项目：

```
客户端请求 → Vue 前端 → Axios HTTP → Spring Boot API → MySQL 数据库 → OpenWeatherMap API
```

## 工作流程

### 1. 用户搜索城市
```
Dashboard.vue → searchWeather() → weatherStore.fetchWeatherByCity() → api.js → WeatherController → WeatherService → WeatherApiService → OpenWeatherMap → MySQL → 返回前端
```

### 2. 获取当前位置
```
Dashboard.vue → getCurrentLocation() → 浏览器地理定位 → weatherStore.fetchWeatherByCoordinates() → WeatherController → 获取天气
```

## 开发步骤

### 1. 克隆项目

```bash
git clone https://github.com/Amandy49/weather-dashboard.git
cd weather-dashboard
```

### 2. 创建数据库

```sql
mysql -u root -p
CREATE DATABASE weather_db CHARACTER SET utf8mb4;
```

### 3. 配置后端

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/weather_db
    username: root
    password: your_password

weather:
  api:
    key: YOUR_OPENWEATHERMAP_API_KEY
```

获取 API Key：访问 https://openweathermap.org/api

### 4. 启动后端

```bash
cd backend
mvn clean compile
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 `http://localhost:5173`

## API 调用示例

### 获取北京天气

```bash
curl "http://localhost:8080/api/weather/city?name=Beijing"
```

### 获取坐标天气

```bash
curl "http://localhost:8080/api/weather/coordinates?lat=39.9&lon=116.4"
```

### 搜索城市

```bash
curl "http://localhost:8080/api/weather/search?keyword=Bei"
```

## 响应示例

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "city": "Beijing",
    "country": "CN",
    "latitude": 39.9,
    "longitude": 116.4,
    "temperature": 20.5,
    "feelsLike": 19.8,
    "humidity": 65,
    "pressure": 1013,
    "windSpeed": 3.2,
    "description": "partly cloudy",
    "icon": "02d",
    "cloudiness": 30,
    "visibility": 10000,
    "uvIndex": 5,
    "createdAt": "2024-01-15 10:30:45",
    "updatedAt": "2024-01-15 10:30:45"
  }
}
```

## 常见问题

### API Key 无效

检查 OpenWeatherMap 账户，确保 Key 已激活

### 前后端无法通信

检查防火墙、CORS 设置、确保端口正确

### 数据库连接失败

检查 MySQL 运行状态，验证用户名密码，确认数据库存在

## 提交代码

```bash
# 创建新分支
git checkout -b feature/your-feature

# 提交代码
git commit -m "feat: add new feature"

# 推送
git push origin feature/your-feature

# 创建 Pull Request
```
