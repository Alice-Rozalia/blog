import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/utils/format.js'
// import './plugins/element.js'
import './assets/css/global.css'
import VueQuillEditor from 'vue-quill-editor'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import vueAplayer from 'vue-aplayer'
import waterfall from 'vue-waterfall2'

Vue.use(VueQuillEditor)
Vue.use(vueAplayer)
Vue.use(mavonEditor)
Vue.use(waterfall)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')