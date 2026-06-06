# 部署指南

## Docker Compose 部署（推荐）

### 1. 克隆项目

```bash
git clone https://github.com/Amandy49/weather-dashboard.git
cd weather-dashboard
```

### 2. 创建 .env 文件

```bash
cat > .env << EOF
WEATHER_API_KEY=your_openweathermap_api_key
MYSQL_ROOT_PASSWORD=strong_root_password
MYSQL_PASSWORD=strong_user_password
EOF
```

### 3. 启动所有服务

```bash
docker-compose up -d
```

### 4. 查看日志

```bash
docker-compose logs -f
```

### 5. 访问应用

- 前端: http://localhost:5173
- 后端 API: http://localhost:8080/api

## 手动部署

### 后端部署

```bash
cd backend
mvn clean package

# 上传到服务器
scp target/weather-dashboard-1.0.0.jar user@server:/app/

# SSH 登录并运行
ssh user@server
cd /app
java -jar weather-dashboard-1.0.0.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/weather_db \
  --spring.datasource.username=weather_user \
  --spring.datasource.password=password \
  --weather.api.key=YOUR_API_KEY
```

### 前端部署

```bash
cd frontend
npm install
npm run build

# 上传到 Nginx
scp -r dist/* user@server:/var/www/weather-dashboard/
```

### Nginx 配置

```nginx
server {
    listen 80;
    server_name weather.example.com;

    location / {
        root /var/www/weather-dashboard;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## HTTPS 配置

```bash
# 使用 Let's Encrypt
sudo apt install certbot python3-certbot-nginx
sudo certbot certonly --nginx -d weather.example.com
```

## 故障排除

### 常见问题

```bash
# 1. 检查内存
free -h

# 2. 检查磁盘
df -h

# 3. 检查数据库连接
mysql -h localhost -u weather_user -p weather_db -e "SELECT 1;"

# 4. 查看应用日志
docker-compose logs weather-backend
```
