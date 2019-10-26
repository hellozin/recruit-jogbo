// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import EventBus from './EventBus'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)
Vue.config.productionTip = false
const instance = axios.create({
  baseURL: 'http://localhost:8080/api/'
})

instance.defaults.headers['Content-Type'] = 'application/json'

instance.interceptors.request.use(
  function (config) {
    const apiToken = localStorage.getItem('apiToken')
    if (apiToken) {
      config.headers['api_key'] = 'Bearer ' + apiToken
    }
    return config
  }, function (error) {
    Promise.reject(error)
  }
)

instance.interceptors.response.use(
  function (response) {
    return response
  }, function (error) {
    if (error.response) {
      let response = error.response
      if (response.data) {
        response = response.data.response
        if (response.status === 'UNAUTHORIZED') {
          localStorage.removeItem('apiToken')
          localStorage.removeItem('username')
          window.location.href = '/login'
        }
      } else {
        console.log(response)
      }
    }
    return Promise.reject(error)
  }
)

Vue.prototype.$axios = instance
Vue.prototype.$bus = EventBus

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
