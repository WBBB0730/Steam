import { createRouter, createWebHistory } from 'vue-router'
import Shop from '@/views/shop/Shop.vue'
import store from '@/store'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'shop',
            component: Shop
        },
        {
            path: '/app/:appId',
            name: 'app',
            component: () => import('@/views/shop/App.vue')
        },
        {
            path: '/wishlist',
            name: 'wishlist',
            component: () => import('@/views/Shop/WishList.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: '/community',
            name: 'community',
            component: () => import('@/views/community/Community.vue')
        },
        {
            path: '/profile/:userId',
            name: 'profile',
            component: () => import('@/views/community/Profile.vue')
        },
        {
            path: '/about',
            name: 'about',
            component: () => import('@/views/about/About.vue')
        },
        {
            path: '/chat',
            name: 'chat',
            component: () => import('@/views/chat/Chat.vue')
        },
        {
            path: '/join',
            name: 'join',
            component: () => import('@/views/Join.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('@/views/Login.vue'),
            beforeEnter: (to, from) => {
                const token = store.getters['user/token']
                // 如果已经登录，跳转到重定向页面或首页
                if (token) {
                    return to.query.redir || '/'
                }
                if (!to.query.redir) {
                    return {
                        name: 'login',
                        query: { redir: from.path }
                    }
                }
            }
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: '/'
        }
    ]
})

router.beforeEach(async (to, from) => {
    const token = store.getters['user/token']
    // 获取个人资料
    if (token) {
        const { nickname, avatar } = store.getters['user/userInfo']
        if (nickname && avatar) {
            store.dispatch('user/getUserInfo').then()
        } else {
            await store.dispatch('user/getUserInfo').catch((reason) => {
                console.log(reason)
                if (reason.code === 401 || reason.code === 404) {
                    store.dispatch('user/logout')
                }
            })
        }
    }

    // 监听路由变化
    if (['shop', 'wishlist'].includes(to.name)) {
        store.commit('setCurrent', 0)
    } else if (['community', 'profile'].includes(to.name)) {
        console.log(store.getters['user/userId'], to.params)
        if (['profile'].includes(to.name) && token && store.getters['user/userId'].toString() === to.params.userId) {
            store.commit('setCurrent', 2)
        } else {
            store.commit('setCurrent', 1)
        }
    } else if (['about'].includes(to.name)) {
        store.commit('setCurrent', 3)
    } else if (['chat'].includes(to.name)) {
        store.commit('setCurrent', 4)
    }
})

export default router
