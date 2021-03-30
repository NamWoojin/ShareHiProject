import Vue from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'
import './plugins/element.js'
import vuetify from './plugins/vuetify';
import VueFullPage from 'vue-fullpage.js'

import io from 'socket.io-client'
const socket = io('http://192.168.0.2:9002')

Vue.prototype.$socket = socket;

Vue.use(VueFullPage);

Vue.config.productionTip = false

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
