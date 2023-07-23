<template>
  <div class="global-header">
    <div class="content">
      <div class="logo">
        <img src="@/assets/logo_steam.svg" alt="steam" />
      </div>
      <div class="nav">
        <RouterLink class="nav-item" :class="{ current: current === 0 }" to="/" @click="storeMenuLocked = true" @mouseenter="storeMenuLocked = false">
          商店
          <div v-show="!storeMenuLocked" class="nav-menu">
            <RouterLink class="nav-menu-item" to="/" @click="storeMenuLocked = true">主页</RouterLink>
            <RouterLink class="nav-menu-item" to="/wishlist" @click="storeMenuLocked = true">愿望单</RouterLink>
          </div>
        </RouterLink>
        <RouterLink class="nav-item" :class="{ current: current === 1 }" to="/community">社区</RouterLink>
        <RouterLink v-if="token" class="nav-item nickname" :class="{ current: current === 2 }" :to="`/profile/${userId}`">
          {{ nickname }}</RouterLink>
        <RouterLink v-else class="nav-item" :class="{ current: current === 3 }" to="/about">关于</RouterLink>
        <RouterLink v-if="token" class="nav-item" :class="{ current: current === 4 }" to="/chat">聊天</RouterLink>
        <RouterLink class="nav-item" :class="{ current: current === 5 }" to="">客服</RouterLink>
      </div>
      <div class="actions">
        <div class="action-menu">
          <RouterLink class="install" :class="{ light: token === null }" to="/about">
            <img src="@/assets/btn_header_installsteam_download.png" alt="安装Steam">
            安装 Steam
          </RouterLink>
          <div v-if="token" class="account-pulldown" @mouseenter="actionMenuLocked = false">
            {{ nickname }}
            <img src="@/assets/btn_arrow_down_padded.png" alt="">
            <div v-show="!actionMenuLocked" class="account-pulldown-menu">
              <RouterLink class="account-pulldown-menu-item" :to="`/profile/${userId}`" @click="actionMenuLocked = true">查看个人资料</RouterLink>
              <div class="account-pulldown-menu-item" @click="logout(); actionMenuLocked = true">注销：<span>{{ username }}</span></div>
            </div>
          </div>
        </div>

        <RouterLink v-if="token" class="user-avatar" :to="`/profile/${userId}`">
          <img :src="avatar || '/src/assets/blank.png'" alt="">
        </RouterLink>
        <RouterLink v-else class="login" to="/login">登录</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, inject, ref } from 'vue'
import { useStore } from 'vuex'

const storeMenuLocked = ref(false)
const actionMenuLocked = ref(false)

const store = useStore()
const current = computed(() => store.getters['current'])
const token = computed(() => store.getters['user/token'])
const userId = computed(() => store.getters['user/userId'])
const username = computed(() => store.getters['user/username'])
const nickname = computed(() => store.getters['user/nickname'])
const avatar = computed(() => store.getters['user/avatar'])

function logout() {
  store.dispatch('user/logout')
}

</script>

<style scoped lang="scss">
.global-header {
  position: relative;
  z-index: 1000;
  display: flex;
  justify-content: center;
  width: 100%;
  min-width: 940px;
  background-color: $global-header-bg-color;
  font-family: "Motiva Sans", sans-serif;;
}
.content {
  position: relative;
  display: flex;
  align-items: center;
  width: 940px;
  height: 104px;
}
.logo {
  width: 176px;
  height: 44px;
  margin-right: 24px;
  img {
    width: 100%;
    height: 100%;
  }
}
.nav {
  display: flex;
}
.nav-item {
  position: relative;
  padding: 7px;
  color: #dcdedf;
  text-decoration: none;
  &:hover {
    color: #ffffff;
  }
  &.nickname {
    font-weight: 500;
  }
  &.current {
    color: #1a9fff;
  }
  &:hover>.nav-menu {
    opacity: 1;
    pointer-events: auto;
  }
}
.nav-menu {
  position: absolute;
  left: 0;
  top: 100%;
  background-color: #3D4450;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
  &:hover {
    opacity: 0;
    pointer-events: none;
  }
}
.nav-menu-item {
  display: inline-block;
  width: 48px;
  padding: 6px 15px;
  color: #dcdedf;
  font-size: 12px;
  text-decoration: none;
  &:hover {
    color: #171a21;
    background-color: #dcdedf;
  }
}
.actions {
  position: absolute;
  right: 0;
  top: 6px;
  display: flex;
}
.action-menu {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-right: 8px;
}
.install {
  display: flex;
  align-items: center;
  padding: 0 9px;
  color: #e5e4dc;
  background-color: rgba(103, 112, 123, 0.2);
  font-family: "Motiva Sans", sans-serif;
  font-size: 11px;
  line-height: 24px;
  text-decoration: none;
  &:hover {
    color: #ffffff;
  }
  &.light {
    background-color: #5c7e10;
  }
  img {
    flex-shrink: 0;
    margin-right: 9px;
  }
}
.message {
  background-color: #5c7e10;

  &:hover {
    background-color: #7ea64b;
  }
}
.account-pulldown {
  position: relative;
  display: flex;
  align-items: center;
  height: 24px;
  color: #b8b6b4;
  font-size: 11px;
  cursor: pointer;
  &:hover {
    color: #ffffff;
    &>.account-pulldown-menu {
      opacity: 1;
      pointer-events: auto;
    }
  }
}
.account-pulldown-menu {
  position: absolute;
  right: 0;
  top: 100%;
  border: 1px solid #3D4450;
  box-shadow: 0 0 12px #000000;
  background-color: #3d4450;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
}
.account-pulldown-menu-item {
  display: inline-block;
  width: 148px;
  padding: 5px 12px;
  color: #b8b6b4;
  font-size: 12px;
  font-family: Motiva Sans, Arial, Helvectica, Verdana, sans-serif;
  text-decoration: none;
  &:hover {
    color: #171d25;
    background-color: #dcdedf;
  }
  span {
    color: #57cbde;
  }
}
.user-avatar {
  display: inline-block;
  width: 32px;
  height: 32px;
  border: 2px solid #474747;
  img {
    width: 32px;
    height: 32px;
    background: linear-gradient( to bottom, #41778f 5%, #3d697b 95%);
  }
}
.login {
  color: #b8b6b4;
  font-size: 11px;
  line-height: 24px;
  text-decoration: none;
  &:hover {
    color: #ffffff;
  }
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.fade-enter-active,
.fade-leave-active {
  transition: 0.2s;
}
</style>
