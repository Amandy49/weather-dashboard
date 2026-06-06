<template>
  <div class="search-page">
    <div class="search-header">
      <h2>搜索城市</h2>
      <p>查找并比较不同城市的天气</p>
    </div>

    <div class="search-box">
      <input
        v-model="searchKeyword"
        type="text"
        placeholder="输入城市名称搜索..."
        @input="onSearchInput"
        class="search-input"
      />
      <button @click="clearSearch" class="clear-btn">清除</button>
    </div>

    <div v-if="weatherStore.searchResults.length > 0" class="results">
      <h3>搜索结果 ({{ weatherStore.searchResults.length }})</h3>
      <div class="results-list">
        <div
          v-for="weather in weatherStore.searchResults"
          :key="weather.id"
          class="result-item"
          @click="selectWeather(weather.city)"
        >
          <div class="result-header">
            <h4>{{ weather.city }}, {{ weather.country }}</h4>
            <span class="result-temp">{{ Math.round(weather.temperature) }}°C</span>
          </div>
          <p class="result-desc">{{ weather.description }}</p>
          <div class="result-info">
            <span>湿度: {{ weather.humidity }}%</span>
            <span>风速: {{ weather.windSpeed }} m/s</span>
            <span>气压: {{ Math.round(weather.pressure) }} hPa</span>
          </div>
          <p class="result-time">{{ formatTime(weather.updatedAt) }}</p>
        </div>
      </div>
    </div>

    <div v-else-if="searchKeyword && !weatherStore.loading" class="empty-state">
      <p>未找到相关城市</p>
      <p class="empty-tip">试试搜索"北京"、"上海"等主要城市</p>
    </div>

    <div v-if="!searchKeyword" class="tips">
      <h3>搜索提示</h3>
      <ul>
        <li>输入城市英文名称，如 Beijing, Shanghai, London 等</li>
        <li>支持模糊搜索，输入"Bei"可以找到北京</li>
        <li>点击搜索结果可以查看详细天气信息</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useWeatherStore } from '../stores/weatherStore'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const weatherStore = useWeatherStore()
const router = useRouter()
const searchKeyword = ref('')

const onSearchInput = async () => {
  if (searchKeyword.value.trim()) {
    await weatherStore.searchCities(searchKeyword.value)
  } else {
    weatherStore.clearSearchResults()
  }
}

const clearSearch = () => {
  searchKeyword.value = ''
  weatherStore.clearSearchResults()
}

const selectWeather = async (city) => {
  await weatherStore.fetchWeatherByCity(city)
  router.push('/')
  ElMessage.success(`已切换到 ${city}`)
}

const formatTime = (timeString) => {
  return timeString || ''
}
</script>

<style scoped lang="scss">
.search-page {
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

.search-header {
  color: white;
  margin-bottom: 2rem;
  text-align: center;

  h2 {
    font-size: 2rem;
    margin: 0 0 0.5rem 0;
  }

  p {
    font-size: 1.1rem;
    margin: 0;
    opacity: 0.9;
  }
}

.search-box {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  background: rgba(255, 255, 255, 0.95);
  padding: 1rem;
  border-radius: 0.75rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  .search-input {
    flex: 1;
    padding: 0.75rem 1rem;
    border: none;
    border-radius: 0.5rem;
    font-size: 1rem;
    background: rgba(102, 126, 234, 0.05);

    &:focus {
      outline: none;
      background: rgba(102, 126, 234, 0.1);
    }
  }

  .clear-btn {
    padding: 0.75rem 1.5rem;
    border: none;
    border-radius: 0.5rem;
    background: #ff6b6b;
    color: white;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;

    &:hover {
      background: #ff5252;
      transform: translateY(-2px);
    }
  }
}

.results {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 0.75rem;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  h3 {
    color: #333;
    margin-bottom: 1.5rem;
    font-size: 1.3rem;
  }

  .results-list {
    display: flex;
    flex-direction: column;
    gap: 1rem;

    .result-item {
      border: 1px solid #e0e0e0;
      border-radius: 0.5rem;
      padding: 1.5rem;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        border-color: #667eea;
        background: rgba(102, 126, 234, 0.05);
        transform: translateX(5px);
        box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
      }

      .result-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 0.75rem;

        h4 {
          margin: 0;
          color: #333;
          font-size: 1.1rem;
        }

        .result-temp {
          font-size: 1.5rem;
          color: #667eea;
          font-weight: bold;
        }
      }

      .result-desc {
        color: #666;
        margin: 0.5rem 0;
        text-transform: capitalize;
      }

      .result-info {
        display: flex;
        gap: 1.5rem;
        margin: 0.75rem 0;
        flex-wrap: wrap;

        span {
          color: #999;
          font-size: 0.9rem;
        }
      }

      .result-time {
        color: #bbb;
        font-size: 0.85rem;
        margin: 0;
      }
    }
  }
}

.empty-state {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 0.75rem;
  padding: 3rem 2rem;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  color: #666;

  p {
    font-size: 1.1rem;
    margin: 0.5rem 0;
  }

  .empty-tip {
    color: #999;
    font-size: 0.95rem;
  }
}

.tips {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 0.75rem;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  color: #333;

  h3 {
    margin-top: 0;
    color: #667eea;
  }

  ul {
    list-style: none;
    padding: 0;
    margin: 0;

    li {
      padding: 0.75rem 0;
      border-bottom: 1px solid #e0e0e0;
      color: #666;

      &:last-child {
        border-bottom: none;
      }

      &:before {
        content: '✓ ';
        color: #667eea;
        font-weight: bold;
        margin-right: 0.5rem;
      }
    }
  }
}

@media (max-width: 768px) {
  .search-box {
    flex-direction: column;

    .search-input,
    .clear-btn {
      width: 100%;
    }
  }

  .results .results-list .result-item .result-info {
    gap: 0.75rem;
  }

  .search-header h2 {
    font-size: 1.5rem;
  }
}
</style>
