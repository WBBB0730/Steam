/**
 * 处理v-trunc指令，为true时自动截断多行字符串
 */
export default {
    mounted(el) {
        if (el.scrollHeight <= el.clientHeight) {
            return
        }
        let s = el.innerText
        while (el.scrollHeight > el.clientHeight) {
            s = s.slice(0, -1)
            el.innerText = s + '...'
        }
    },
    updated(el) {
        while (el.scrollHeight > el.clientHeight) {
            el.innerText = el.innerText.slice(0, -1)
        }
    }
}
