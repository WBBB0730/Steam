import request from '@/utils/request'

export function getRecommendationsApi(num) {
    return request({
        url: '/app/recommendations',
        method: 'GET',
        params: { num }
    })
}

export function getSpecialsApi() {
    return request({
        url: '/app/specials',
        method: 'GET'
    })
}
