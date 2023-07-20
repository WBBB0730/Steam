import request from '@/utils/request'

export function addToWishlistApi(appId) {
    return request({
        url: '/wishlist',
        method: 'POST',
        params: { appId }
    })
}
export function removeFromWishlistApi(appId) {
    return request({
        url: '/wishlist',
        method: 'DELETE',
        params: { appId }
    })
}

