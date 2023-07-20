import Md5 from 'crypto-js/md5'
import { loginApi, getUserInfoApi } from '@/api/user'

const state = {
    token: JSON.parse(localStorage.getItem('steamToken')) || JSON.parse(sessionStorage.getItem('steamToken')) || null,
    userId: null,
    username: null,
    nickname: null,
    avatar: null
}

const getters = {
    token: state => state.token,
    userId: state => state.userId,
    username: state => state.username,
    nickname: state => state.nickname,
    avatar: state => state.avatar,
    userInfo: ({ userId, username, nickname, avatar }) => {
        return { userId, username, nickname, avatar }
    }
}

const mutations = {
    setToken(state, token) {
        state.token = token
    },
    setUserId(state, userId) {
        state.userId = userId
    },
    setUsername(state, username) {
        state.username = username
    },
    setAvatar(state, avatar) {
        state.avatar = avatar
    },
    setUserInfo(state, { userId, username, nickname, avatar }) {
        state.userId = userId
        state.username = username
        state.nickname = nickname
        state.avatar = avatar
    }
}

const actions = {
    /**
     * 注销
     */
    logout({ commit }) {
        commit('setToken', null)
        commit('setUserInfo', { userId: null, username: null, nickname: null, avatar: null })
        localStorage.removeItem('steamToken')
        sessionStorage.removeItem('steamToken')
    },
    /**
     * 登录
     */
    login({ commit }, { username, password, rememberMe }) {
        return new Promise((resolve, reject) => {
            password = Md5(password).toString()
            loginApi({ username, password }).then(({ data }) => {
                const { token } = data
                commit('setToken', token)
                if (rememberMe) {
                    localStorage.setItem('steamToken', JSON.stringify(token))
                } else {
                    sessionStorage.setItem('steamToken', JSON.stringify(token))
                }
                resolve()
            }).catch((reason) => {
                reject(reason)
            })
        })
    },
    /**
     * 获取个人资料
     */
    getUserInfo({ commit, state }) {
        return new Promise((resolve, reject) => {
            if (!state.token) {
                reject(new Error('未登录'))
            }
            getUserInfoApi(state.userId).then(({ data }) => {
                commit('setUserInfo', data)
                resolve()
            }).catch((reason) => {
                reject(reason)
            })
        })
    }
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}
