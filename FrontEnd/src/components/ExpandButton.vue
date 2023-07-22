<template>
  <div v-if="status !== 2" class="expand-button" :class="{ show }" @click.stop.prevent="openMenu()">
    <div class="expand-button-icon"/>
    <div class="expand-menu" :class="{ show: showMenu }" @mouseleave="closeMenu()">
      <div v-if="status === 0" class="expand-menu-item" @click="addToWishlist()">添加至您的愿望单</div>
      <div v-else class="expand-menu-item" @click="removeFromWishlist()">移除出您的愿望单</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { addToWishlistApi, removeFromWishlistApi } from '@/api/wishlist'

const props = defineProps({
  show: {
    type: Boolean,
    default: true
  },
  appId: {
    type: Number,
    required: true
  },
  status: {
    type: Number,
    required: true
  }
})
const emits = defineEmits(['update:status'])
const showMenu = ref(false)

function openMenu() {
  showMenu.value = true
}

function closeMenu() {
  showMenu.value = false
}

function addToWishlist() {
  addToWishlistApi(props.appId).then(() => {
    closeMenu()
    setTimeout(() => {
      emits('update:status', 1)
    }, 100)
  })
}

function removeFromWishlist() {
  removeFromWishlistApi(props.appId).then(() => {
    closeMenu()
    setTimeout(() => {
      emits('update:status', 0)
    }, 100)
  })
}
</script>

<style scoped lang="scss">
.expand-button {
  position: absolute;
  z-index: 100;
  right: 0;
  top: 0;
  padding: 5px 5px 0 0;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;

  &.show {
    opacity: 1;
    pointer-events: auto;
  }

  & > .expand-button-icon {
    position: relative;
    width: 15px;
    height: 15px;
    border-radius: 3px;
    background: #e5e5e5 url("@/assets/icon_expand_dark.png") center center no-repeat;
    box-shadow: 0 0 3px #000000;
  }

  &:hover > .expand-button-icon {
    background: #67c1f5 url("@/assets/icon_expand_white.png") center center no-repeat;
  }
}

.expand-menu {
  position: absolute;
  right: 0;
  top: 0;
  padding: 8px;
  border-radius: 3px;
  box-shadow: 0 0 3px #000000;
  background-color: #e5e5e5;
  opacity: 0;
  cursor: default;
  pointer-events: none;
  transition: opacity 0.1s;

  &.show {
    opacity: 1;
    pointer-events: auto;
  }

  .expand-menu-item {
    padding: 4px 8px;
    border-radius: 2px;
    color: #407898;
    background-color: rgba(0, 0, 0, 0.1);
    font-size: 11px;
    white-space: nowrap;
    cursor: pointer;

    &:hover {
      color: #ffffff;
      background-color: #67c1f5;
    }
  }
}
</style>
