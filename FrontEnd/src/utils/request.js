import axios from 'axios'
import store from '@/store'

const service = axios.create({
    // baseURL: 'http://localhost:8080',
    baseURL: 'https://mock.apifox.cn/m1/2987398-0-default',
    timeout: 5000
})

service.interceptors.request.use((config) => {
    config.headers.token = store.getters['user/token'] || null
    return config
}, (error) => {
    return Promise.reject(error)
})

service.interceptors.response.use((response) => {
    const res = response.data
    if (res.code !== 200) {
        console.error(response.config.url, res)
        return Promise.reject(res)
    }
    return res
}, (error) => {
    console.error(error)
    return Promise.reject(error)
})

export default service
