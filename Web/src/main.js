import Vue from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import './plugins/element.js';
import vuetify from './plugins/vuetify';
import VueFullPage from 'vue-fullpage.js';

import io from 'socket.io-client';
const socket = io('https://j4f001.p.ssafy.io:8002');
// const socket = io('http://localhost:9002');

Vue.prototype.$socket = socket;

Vue.use(VueFullPage);

Vue.config.productionTip = false;

new Vue({
  store,
  router,
  vuetify,
  render: (h) => h(App),
}).$mount('#app');
