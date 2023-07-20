import { createApp, provide } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'
import loadingDirective from '@/directives/loading'
import truncDirective from '@/directives/trunc'

const app = createApp(App)

app.use(router)
app.use(store)
app.directive('loading', loadingDirective)
app.directive('trunc', truncDirective)

app.mount('#app')
