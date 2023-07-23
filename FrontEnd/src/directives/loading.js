import { createApp } from 'vue'
import Loading from '@/components/Loading.vue'

/**
 * 处理v-loading指令，为true时在元素上显示加载动画
 */
export default {
    mounted(el, binding) {
        const style = getComputedStyle(el)
        if (style.position === 'static') {
            el.style.position = 'relative'
        }
        const loading = createApp(Loading, {
            background: style.background
        })
        const div =  document.createElement('div')
        div.style.display = binding.value ? 'block' : 'none'
        div.className = 'v-loading'
        el.appendChild(div)
        loading.mount(div)
    },
    updated(el, binding) {
        const div = el.getElementsByClassName('v-loading')[0]
        div.style.display = binding.value ? 'block' : 'none'
    }
}
