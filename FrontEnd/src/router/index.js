import { createRouter, createWebHistory } from 'vue-router'
import Store from '@/views/store/Store.vue'
import store from '@/store'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'store',
            component: Store
        },
        {
            path: '/app/:appId',
            name: 'app',
            component: () => import('@/views/store/App.vue')
        },
        {
            path: '/wishlist',
            name: 'wishlist',
            component: () => import('@/views/store/WishList.vue'),
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
            path: '/friends',
            name: 'friends',
            component: () => import('@/views/mine/Friends.vue'),
            children: [
                {
                    path: '',
                    name: 'friendList',
                    component: () => import('@/views/mine/friends/List.vue')
                },
                {
                    path: 'add',
                    name: 'friendAdd',
                    component: () => import('@/views/mine/friends/Add.vue')
                },
                {
                    path: 'pending',
                    name: 'friendPending',
                    component: () => import('@/views/mine/friends/Pending.vue')
                }
            ]
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
    } else {
        if (['wishlist', 'friendList', 'friendAdd', 'friendPending'].includes(to.name)) {
            return {
                name: 'login',
                query: { redir: to.path }
            }
        }
    }
})

export default router
