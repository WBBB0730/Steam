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

export function getSearchSuggestionsApi(keyword) {
    return request({
        url: '/app/search/suggestions',
        method: 'GET',
        params: { keyword }
    })
}
