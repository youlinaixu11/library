import router from './router'
import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import '@/assets/global.css'

Vue.config.productionTip = false
Vue.use(ElementUI,{size: 'small'});

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
