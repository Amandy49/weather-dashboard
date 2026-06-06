FROM openjdk:17-jdk-slim

WORKDIR /app

# 复制 pom.xml 和依赖
COPY pom.xml .
RUN apt-get update && apt-get install -y maven && mvn dependency:resolve

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 运行应用
EXPOSE 8080
CMD ["java", "-jar", "target/weather-dashboard-1.0.0.jar"]
