<template>
  <div class="dashboard">
    <div class="search-section">
      <div class="search-container">
        <input
          v-model="searchInput"
          type="text"
          placeholder="输入城市名称..."
          @keyup.enter="searchWeather"
          class="search-input"
        />
        <button @click="searchWeather" class="search-btn">搜索</button>
        <button @click="getCurrentLocation" class="location-btn">📍 当前位置</button>
      </div>
    </div>

    <el-loading v-if="weatherStore.loading" fullscreen />

    <el-alert
      v-if="weatherStore.error"
      :title="weatherStore.error"
      type="error"
      closable
      @close="weatherStore.clearError"
      style="margin-bottom: 1rem"
    />

    <div v-if="weatherStore.currentWeather" class="weather-card">
      <div class="weather-main">
        <div class="city-info">
          <h2>{{ weatherStore.currentWeather.city }}, {{ weatherStore.currentWeather.country }}</h2>
          <p class="coordinates">
            📍 {{ weatherStore.currentWeather.latitude.toFixed(2) }},
            {{ weatherStore.currentWeather.longitude.toFixed(2) }}
          </p>
        </div>

        <div class="temperature-section">
          <div class="temp-display">
            <span class="temperature">{{ Math.round(weatherStore.currentWeather.temperature) }}°C</span>
            <span class="weather-icon">{{ getWeatherIcon(weatherStore.currentWeather.icon) }}</span>
          </div>
          <p class="description">{{ weatherStore.currentWeather.description }}</p>
          <p class="feels-like">体感温度: {{ Math.round(weatherStore.currentWeather.feelsLike) }}°C</p>
        </div>
      </div>

      <div class="details-grid">
        <div class="detail-item">
          <span class="detail-label">湿度</span>
          <span class="detail-value">{{ weatherStore.currentWeather.humidity }}%</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">气压</span>
          <span class="detail-value">{{ Math.round(weatherStore.currentWeather.pressure) }} hPa</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">风速</span>
          <span class="detail-value">{{ weatherStore.currentWeather.windSpeed }} m/s</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">能见度</span>
          <span class="detail-value">{{ (weatherStore.currentWeather.visibility / 1000).toFixed(1) }} km</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">云量</span>
          <span class="detail-value">{{ weatherStore.currentWeather.cloudiness }}%</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">更新时间</span>
          <span class="detail-value">{{ formatTime(weatherStore.currentWeather.updatedAt) }}</span>
        </div>
      </div>
    </div>

    <div v-if="weatherStore.recentWeathers.length > 0" class="recent-section">
      <h3>最近查询</h3>
      <div class="recent-grid">
        <div
          v-for="weather in weatherStore.recentWeathers.slice(0, 6)"
          :key="weather.id"
          class="recent-card"
          @click="selectRecentWeather(weather.city)"
        >
          <p class="recent-city">{{ weather.city }}</p>
          <p class="recent-temp">{{ Math.round(weather.temperature) }}°C</p>
          <p class="recent-desc">{{ weather.description }}</p>
        </div>
      </div>
    </div>

    <div v-if="!weatherStore.currentWeather && !weatherStore.loading" class="welcome">
      <div class="welcome-content">
        <h2>👋 欢迎来到天气仪表盘</h2>
        <p>搜索城市或使用当前位置查看天气信息</p>
        <div class="welcome-features">
          <div class="feature">
            <span>🌍</span>
            <p>全球城市天气查询</p>
          </div>
          <div class="feature">
            <span>📊</span>
            <p>详细天气数据展示</p>
          </div>
          <div class="feature">
            <span>⏰</span>
            <p>查询历史记录</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useWeatherStore } from '../stores/weatherStore'
import { ElMessage } from 'element-plus'

const weatherStore = useWeatherStore()
const searchInput = ref('')

const searchWeather = async () => {
  if (!searchInput.value.trim()) {
    ElMessage.warning('请输入城市名称')
    return
  }
  await weatherStore.fetchWeatherByCity(searchInput.value)
}

const getCurrentLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const { latitude, longitude } = position.coords
        await weatherStore.fetchWeatherByCoordinates(latitude, longitude)
      },
      (error) => {
        console.error('Geolocation error:', error)
        ElMessage.error('无法获取当前位置，请检查浏览器权限设置')
      }
    )
  } else {
    ElMessage.error('浏览器不支持地理定位')
  }
}

const selectRecentWeather = async (city) => {
  searchInput.value = city
  await weatherStore.fetchWeatherByCity(city)
}

const getWeatherIcon = (icon) => {
  const iconMap = {
    '01d': '☀️', '01n': '🌙',
    '02d': '⛅', '02n': '☁️',
    '03d': '☁️', '03n': '☁️',
    '04d': '☁️', '04n': '☁️',
    '09d': '🌧️', '09n': '🌧️',
    '10d': '🌦️', '10n': '🌧️',
    '11d': '⛈️', '11n': '⛈️',
    '13d': '❄️', '13n': '❄️',
    '50d': '🌫️', '50n': '🌫️'
  }
  return iconMap[icon] || '🌡️'
}

const formatTime = (timeString) => {
  return timeString.split(' ')[1] || ''
}

onMounted(() => {
  weatherStore.fetchRecentWeathers()
})
</script>

<style scoped lang="scss">
.dashboard {
  animation: fadeIn 0.5s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.search-section {
  margin-bottom: 2rem;

  .search-container {
    display: flex;
    gap: 1rem;
    flex-wrap: wrap;

    .search-input {
      flex: 1;
      min-width: 200px;
      padding: 0.75rem 1rem;
      border: none;
      border-radius: 0.5rem;
      font-size: 1rem;
      background: rgba(255, 255, 255, 0.95);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

      &:focus {
        outline: none;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
      }
    }

    .search-btn,
    .location-btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 0.5rem;
      font-size: 1rem;
      cursor: pointer;
      transition: all 0.3s ease;
      background: rgba(255, 255, 255, 0.95);
      color: #667eea;
      font-weight: 600;

      &:hover {
        background: white;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
      }

      &:active {
        transform: translateY(0);
      }
    }
  }
}

.weather-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 1rem;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  animation: slideUp 0.5s ease-out;

  .weather-main {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 2rem;
    flex-wrap: wrap;
    gap: 2rem;

    .city-info {
      h2 {
        margin: 0;
        font-size: 1.8rem;
        color: #333;
      }

      .coordinates {
        color: #666;
        font-size: 0.9rem;
        margin: 0.5rem 0 0 0;
      }
    }

    .temperature-section {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 0.5rem;

      .temp-display {
        display: flex;
        align-items: center;
        gap: 1rem;

        .temperature {
          font-size: 3.5rem;
          font-weight: bold;
          color: #667eea;
        }

        .weather-icon {
          font-size: 3rem;
        }
      }

      .description {
        font-size: 1.2rem;
        color: #666;
        margin: 0;
        text-transform: capitalize;
      }

      .feels-like {
        color: #999;
        font-size: 0.9rem;
        margin: 0;
      }
    }
  }

  .details-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 1rem;

    .detail-item {
      background: rgba(102, 126, 234, 0.1);
      padding: 1rem;
      border-radius: 0.5rem;
      display: flex;
      flex-direction: column;
      gap: 0.5rem;

      .detail-label {
        color: #666;
        font-size: 0.85rem;
        font-weight: 600;
        text-transform: uppercase;
      }

      .detail-value {
        font-size: 1.3rem;
        font-weight: bold;
        color: #667eea;
      }
    }
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.recent-section {
  margin-bottom: 2rem;

  h3 {
    color: white;
    font-size: 1.3rem;
    margin-bottom: 1rem;
  }

  .recent-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 1rem;

    .recent-card {
      background: rgba(255, 255, 255, 0.9);
      border-radius: 0.75rem;
      padding: 1rem;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        background: white;
      }

      .recent-city {
        font-weight: bold;
        color: #333;
        margin: 0 0 0.5rem 0;
        font-size: 1rem;
      }

      .recent-temp {
        font-size: 1.5rem;
        color: #667eea;
        margin: 0 0 0.5rem 0;
        font-weight: bold;
      }

      .recent-desc {
        color: #666;
        font-size: 0.85rem;
        margin: 0;
      }
    }
  }
}

.welcome {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;

  .welcome-content {
    text-align: center;
    color: white;

    h2 {
      font-size: 2.5rem;
      margin-bottom: 1rem;
    }

    p {
      font-size: 1.2rem;
      margin-bottom: 2rem;
      opacity: 0.9;
    }

    .welcome-features {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
      gap: 2rem;
      margin-top: 2rem;

      .feature {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 1rem;

        span {
          font-size: 3rem;
        }

        p {
          margin: 0;
          font-size: 1rem;
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .weather-card {
    padding: 1rem;

    .weather-main {
      flex-direction: column;
      align-items: flex-start;
    }
  }

  .search-section .search-container {
    flex-direction: column;

    .search-input,
    .search-btn,
    .location-btn {
      width: 100%;
    }
  }

  .welcome h2 {
    font-size: 1.8rem;
  }
}
</style>
