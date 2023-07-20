import request from '@/utils/request'

export function joinApi(data) {
    return request({
        url: '/user/join',
        method: 'POST',
        data
    })
}

export function loginApi(data) {
    return request({
        url: '/user/login',
        method: 'POST',
        data
    })
}

export function getUserInfoApi(userId) {
    return request({
        url: '/user/info',
        method: 'GET',
        params: { userId }
    })
}

export function checkUsernameAvailableApi(username) {
    return request({
        url: '/user/available',
        method: 'GET',
        params: { username }
    })
}
