import axios from 'axios'
import store from '@/store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'

const service = axios.create({
  // url = base url + request url
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 5000
})

service.interceptors.request.use(config => {
  NProgress.start()
  if (store.state.token) {
    config.headers.Authorization = store.state.token
  } else if (window.sessionStorage.getItem('token')) {
    config.headers.Authorization = window.sessionStorage.getItem('token')
  }
  return config
}, error => {
  return Promise.reject(error)
})

service.interceptors.response.use(config => {
  if (!config.data.success) {
    Message({
      message: config.data.message || '服务器端异常！',
      type: 'error',
      duration: 3 * 1000
    })
  }
  NProgress.done()
  return config
}, error => {
  if (error.response) {
    switch (error.response.status) {
      case 401:
        this.$store.commit('delToken');
        router.replace({
          path: '/login',
          query: {
            redirect: router.currentRoute.fullPath
          }
        })
    }
  }
  return Promise.reject(error.response.data)
})

const music = axios.create({
  baseURL: 'https://autumnfish.cn',
  timeout: 5000
})

music.interceptors.request.use(config => {
  NProgress.start()
  return config
})

music.interceptors.response.use(config => {
  NProgress.done()
  return config
})

export {
  service,
  music
}