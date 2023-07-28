<template>
  <div class="app">
    <div class="app-header">
      <div class="app-header-avatar">
        <img :src="avatar" alt="">
      </div>
      {{ nickname }}
    </div>

    <div class="app-content">
      <div class="nav">
        <div class="nav-title">好友</div>
        <RouterLink class="nav-item" :class="{ current: currentRoute === 0 }" to="/friends">
          <img class="nav-item-icon" src="@/assets/iconsheet_friends_list.png" alt="">
          您的好友
          <div v-if="friendNum" class="nav-item-num">{{ friendNum }}</div>
        </RouterLink>
        <RouterLink class="nav-item" :class="{ current: currentRoute === 1 }" to="/friends/add">
          <img class="nav-item-icon" src="@/assets/iconsheet_friends_add.png" alt="">
          添加好友
        </RouterLink>
        <RouterLink class="nav-item" :class="{ current: currentRoute === 2 }" to="/friends/pending">
          <img class="nav-item-icon" src="@/assets/iconsheet_friends_pending.png" alt="">
          待处理邀请
          <div v-if="pendingNum" class="nav-item-num">{{ pendingNum }}</div>
        </RouterLink>
      </div>
      <div class="router-view-area">
        <RouterView/>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, provide, ref } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { getFriendNumApi, getPendingNumApi } from '@/api/friend'

const friendNum = ref(0)
const pendingNum = ref(0)
provide('friendNum', friendNum)
provide('pendingNum', pendingNum)

const store = useStore()
const nickname = computed(() => store.getters['user/nickname'])
const avatar = computed(() => store.getters['user/avatar'])

const route = useRoute()
const currentRoute = computed(() => {
  switch (route.name) {
    case 'friendList':
      return 0
    case 'friendAdd':
      return 1
    case 'friendPending':
      return 2
    default:
      return -1
  }
})

onMounted(() => {
  getFriendNum()
  getPendingNum()
})

function getFriendNum() {
  getFriendNumApi().then(({ data }) => {
    friendNum.value = data
  })
}

function getPendingNum() {
  getPendingNumApi().then(({ data }) => {
    pendingNum.value = data
  })
}

</script>

<style scoped lang="scss">
.app {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: calc(100vh - 104px);
  background: #1b2838 url("@/assets/colored_body_top2.png") center -104px no-repeat;
}

.app-header {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 940px;
  padding: 25px 0;
  color: #ffffff;
  //background: radial-gradient(circle at center bottom, rgba(32, 70, 102, 1) 60%, rgba(32, 70, 102, 0));
  background: url("@/assets/bg_highlight.png") center;
  font-size: 26px;
}

.app-header-avatar {
  width: 68px;
  height: 68px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.app-content {
  display: flex;
  width: 940px;
  gap: 20px;
}

.nav {
  display: flex;
  flex-direction: column;
}

.nav-title {
  padding: 12px 12px 0;
}

.nav-title, .nav-item-num {
  color: #7092a5;
  font-size: 11px;
}

.nav-item {
  box-sizing: border-box;
  display: flex;
  align-items: center;
  gap: 20px;
  width: 230px;
  height: 40px;
  padding: 10px;
  color: #ebebeb;
  font-size: 14px;
  text-decoration: none;

  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }

  &.current {
    background-color: rgba(255, 255, 255, 0.1);
  }
}

.nav-item-icon {
  width: 16px;
  height: 16px;
}

.nav-item-num {
  margin-left: auto;
}

.router-view-area {
  width: 100%;
  padding-top: 24px;

  router-view {
    width: 690px;
  }
}

</style>
