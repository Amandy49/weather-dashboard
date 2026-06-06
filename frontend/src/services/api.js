import axios from 'axios'

const API_BASE_URL = '/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export const weatherAPI = {
  getWeatherByCity: (city) => {
    return api.get('/weather/city', { params: { name: city } })
  },

  getWeatherByCoordinates: (lat, lon) => {
    return api.get('/weather/coordinates', { params: { lat, lon } })
  },

  getForecast: (city) => {
    return api.get('/weather/forecast', { params: { city } })
  },

  getRecentWeathers: () => {
    return api.get('/weather/recent')
  },

  searchCity: (keyword) => {
    return api.get('/weather/search', { params: { keyword } })
  },

  health: () => {
    return api.get('/weather/health')
  }
}

export default api
