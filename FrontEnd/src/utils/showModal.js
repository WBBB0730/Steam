import { createApp } from 'vue'
import Modal from '@/components/Modal.vue'

export function showModal({ title = '', content = '', showCancel = true, cancelText = '取消', confirmText = '确定' }) {
    return new Promise((resolve, reject) => {
        const app = document.getElementById('app')
        const modal = createApp(Modal, { title, content, showCancel, cancelText, confirmText })
        const modalDiv = document.createElement('div')
        modal.mount(modalDiv)
        modalDiv.addEventListener('success', () => {
            resolve()
        })
        modalDiv.addEventListener('fail', () => {
            reject()
        })
        modalDiv.addEventListener('complete', () => {
            modal.unmount()
            app.removeChild(modalDiv)
        })
        app.appendChild(modalDiv)
    })
}
