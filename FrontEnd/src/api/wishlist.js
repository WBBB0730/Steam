import request from '@/utils/request'

export function getWishlistSizeApi() {
    return request({
        'url': '/wishlist/size',
        method: 'GET'
    })
}

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

export function getWishlistApi() {
    return request({
        url: '/wishlist',
        method: 'GET'
    })
}

export function sortWishlistApi(data) {
    return request({
        url: '/wishlist/sort',
        method: 'PUT',
        data
    })
}
