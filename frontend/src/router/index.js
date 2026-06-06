import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import Search from '../views/Search.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/search',
    name: 'Search',
    component: Search
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
