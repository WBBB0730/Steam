import { createStore } from 'vuex'
import user from '@/store/modules/user'

const state = {
    current: -1
}

const getters = {
    current: state => state.current
}

const mutations = {
    setCurrent(state, current) {
        state.current = current
    }
}

export default createStore({
    modules: {
        user
    },
    state,
    getters,
    mutations
})
