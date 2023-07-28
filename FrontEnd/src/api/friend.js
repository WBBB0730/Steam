import request from '@/utils/request'

export function getFriendNumApi() {
    return request({
        url: '/friend/num',
        method: 'GET'
    })
}

export function getFriendListApi() {
    return request({
        url: '/friend/list',
        method: 'GET'
    })
}

export function sendInvitationApi(userId) {
    return request({
        url: '/friend/invite',
        method: 'POST',
        params: { userId }
    })
}

export function cancelInvitationApi(userId) {
    return request({
        url: '/friend/cancel',
        method: 'POST',
        params: { userId }
    })
}

export function acceptInvitationApi(userId) {
    return request({
        url: '/friend/accept',
        method: 'POST',
        params: { userId }
    })
}

export function refuseInvitationApi(userId) {
    return request({
        url: '/friend/refuse',
        method: 'POST',
        params: { userId }
    })
}

export function getPendingInvitationsApi() {
    return request({
        url: '/friend/pending',
        method: 'GET'
    })
}

export function getSentInvitationsApi() {
    return request({
        url: '/friend/invite',
        method: 'GET'
    })
}

export function getPendingNumApi() {
    return request({
        url: '/friend/pending/num',
        method: 'GET'
    })
}
