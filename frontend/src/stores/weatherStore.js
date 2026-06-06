import { defineStore } from 'pinia'
import { ref } from 'vue'
import { weatherAPI } from '../services/api'

export const useWeatherStore = defineStore('weather', () => {
  const currentWeather = ref(null)
  const recentWeathers = ref([])
  const forecast = ref(null)
  const loading = ref(false)
  const error = ref(null)
  const searchResults = ref([])

  const fetchWeatherByCity = async (city) => {
    loading.value = true
    error.value = null
    try {
      const response = await weatherAPI.getWeatherByCity(city)
      if (response.code === 200) {
        currentWeather.value = response.data
        return response.data
      } else {
        error.value = response.message
        return null
      }
    } catch (err) {
      error.value = err.message || '获取天气失败'
      console.error('Fetch weather error:', err)
      return null
    } finally {
      loading.value = false
    }
  }

  const fetchWeatherByCoordinates = async (lat, lon) => {
    loading.value = true
    error.value = null
    try {
      const response = await weatherAPI.getWeatherByCoordinates(lat, lon)
      if (response.code === 200) {
        currentWeather.value = response.data
        return response.data
      } else {
        error.value = response.message
        return null
      }
    } catch (err) {
      error.value = err.message || '获取天气失败'
      console.error('Fetch weather error:', err)
      return null
    } finally {
      loading.value = false
    }
  }

  const fetchForecast = async (city) => {
    try {
      const response = await weatherAPI.getForecast(city)
      if (response.code === 200) {
        forecast.value = response.data
        return response.data
      } else {
        error.value = response.message
        return null
      }
    } catch (err) {
      console.error('Fetch forecast error:', err)
      return null
    }
  }

  const fetchRecentWeathers = async () => {
    try {
      const response = await weatherAPI.getRecentWeathers()
      if (response.code === 200) {
        recentWeathers.value = response.data
        return response.data
      } else {
        error.value = response.message
        return []
      }
    } catch (err) {
      console.error('Fetch recent weathers error:', err)
      return []
    }
  }

  const searchCities = async (keyword) => {
    if (!keyword.trim()) {
      searchResults.value = []
      return
    }
    try {
      const response = await weatherAPI.searchCity(keyword)
      if (response.code === 200) {
        searchResults.value = response.data || []
      } else {
        searchResults.value = []
      }
    } catch (err) {
      console.error('Search error:', err)
      searchResults.value = []
    }
  }

  const clearSearchResults = () => {
    searchResults.value = []
  }

  const clearError = () => {
    error.value = null
  }

  return {
    currentWeather,
    recentWeathers,
    forecast,
    loading,
    error,
    searchResults,
    fetchWeatherByCity,
    fetchWeatherByCoordinates,
    fetchForecast,
    fetchRecentWeathers,
    searchCities,
    clearSearchResults,
    clearError
  }
})
