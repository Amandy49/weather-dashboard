<template>
  <div id="app" class="app-container">
    <nav class="navbar">
      <div class="nav-brand">
        <h1>🌦️ 天气仪表盘</h1>
      </div>
      <div class="nav-links">
        <router-link to="/" class="nav-link">首页</router-link>
        <router-link to="/search" class="nav-link">搜索</router-link>
      </div>
    </nav>

    <main class="main-content">
      <router-view />
    </main>

    <footer class="footer">
      <p>&copy; 2024 Weather Dashboard | Powered by Spring Boot + Vue 3</p>
    </footer>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useWeatherStore } from './stores/weatherStore'

const weatherStore = useWeatherStore()

onMounted(() => {
  weatherStore.fetchRecentWeathers()
})
</script>

<style scoped lang="scss">
#app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.navbar {
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);

  .nav-brand h1 {
    font-size: 1.5rem;
    margin: 0;
  }

  .nav-links {
    display: flex;
    gap: 2rem;

    .nav-link {
      color: white;
      text-decoration: none;
      font-size: 1rem;
      transition: color 0.3s ease;

      &:hover {
        color: #667eea;
      }

      &.router-link-active {
        color: #667eea;
        border-bottom: 2px solid #667eea;
        padding-bottom: 0.25rem;
      }
    }
  }
}

.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.footer {
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  text-align: center;
  padding: 1rem;
  margin-top: auto;

  p {
    margin: 0;
    font-size: 0.9rem;
  }
}

@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    gap: 1rem;

    .nav-links {
      gap: 1rem;
    }
  }

  .main-content {
    padding: 1rem;
  }
}
</style>
