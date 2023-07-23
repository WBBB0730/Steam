<template>
  <div class="app" @mousemove="handleDrag" @mouseup="handleDragEnd">
    <div class="header">
      <div class="header-avatar">
        <img :src="avatar" alt="">
      </div>
      {{ nickname }} 的愿望单
    </div>

    <div class="filter">
      <input v-model="keyword" class="search" placeholder="按名称搜索">
      <div class="sort" :class="{ focus: sortMenuShown }" tabindex="0"
           @click="openSortMenu()" @mouseleave="closeSortMenu()">
        排序依据：
        <div class="sort-type">{{ ['您的排序', '名称', '价格', '折扣', '添加日期', '发行日期'].at(sort) }}</div>
        <img src="@/assets/btn_arrow_down_padded_white.png" alt="">
        <div class="sort-menu">
          <div class="sort-menu-item" @click.stop="changeSortType(0)">您的排序</div>
          <div class="sort-menu-item" @click.stop="changeSortType(1)">名称</div>
          <div class="sort-menu-item" @click.stop="changeSortType(2)">价格</div>
          <div class="sort-menu-item" @click.stop="changeSortType(3)">折扣</div>
          <div class="sort-menu-item" @click.stop="changeSortType(4)">添加日期</div>
          <div class="sort-menu-item" @click.stop="changeSortType(5)">发行日期</div>
        </div>
      </div>
    </div>

    <div ref="wishlistRef" class="list" :style="{ height: wishlist.length * 180 + 'px' }">
      <div v-for="(item, index) in wishlist" :key="index"
           class="list-item" :class="{ dragging: draggingIndex === index }"
           :style="{ top: draggingIndex === index ? draggingTop + 'px' : (12 + 180 * index) + 'px' }">
        <div v-if="sort === 0 && !keyword" class="handle" @mousedown="handleDragStart($event, index)"/>
        <RouterLink class="list-item-image" :to="`/app/${item.appId}`">
          <img :src="item.header" alt="">
        </RouterLink>
        <div class="list-item-info">
          <RouterLink class="name" :to="`/app/${item.appId}`">{{ item.name }}</RouterLink>
          <div class="middle">
            <div class="details">
              <div class="release-label">发行日期：</div>
              <div class="release">{{ getDateStr(item.releaseTime, 'YYYY 年 M 月 D 日', 'M 月 D 日') }}</div>
              <div class="developer-label">开发商：</div>
              <div class="developer">
                <RouterLink :to="''">{{ item.developer }}</RouterLink>
              </div>
              <div class="publisher-label">发行商：</div>
              <div class="publisher">
                <RouterLink :to="''">{{ item.publisher }}</RouterLink>
              </div>
            </div>
            <div class="price-area">
              <div v-if="item.discount" class="discount">{{ getDiscountStr(item.discount) }}</div>
              <div class="price">
                <div v-if="item.discount" class="origin-price">{{ getPriceStr(item.price) }}</div>
                <div class="final-price">{{ getPriceStr(item.finalPrice) }}</div>
              </div>
            </div>
          </div>
          <div class="bottom">
            <div class="platforms">
              <div v-if="item.win" class="platform" title="Windows">
                <img src="@/assets/icon_platform_win.png" alt="">
              </div>
              <div v-if="item.mac" class="platform" title="MacOS">
                <img src="@/assets/icon_platform_mac.png" alt="">
              </div>
              <div v-if="item.linux" class="platform" title="Linux">
                <img src="@/assets/icon_platform_linux.png" alt="">
              </div>
            </div>
            <div class="add-time-area">
              添加日期：{{ getDateStr(item.addTime, 'YYYY/M/D') }} （<span class="remove"
                                                                         @click="removeFromWishlist(item.appId)">移除</span>）
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useStore } from 'vuex'
import { computed, onMounted, ref, watch } from 'vue'
import { quickSort } from '@/utils/sort'
import { getDateStr, getDiscountStr, getPriceStr } from '@/utils/format'
import { getWishlistApi, removeFromWishlistApi, sortWishlistApi } from '@/api/wishlist'

const wishlistRef = ref(null)
const draggingIndex = ref(-1)
const draggingTop = ref(0)

const keyword = ref('')
const sort = ref(parseInt(localStorage.getItem('steamWishlistSort') || '1'))
const sortMenuShown = ref(false)
const originList = ref([])
const wishlist = ref([])

const store = useStore()
const avatar = computed(() => store.getters['user/avatar'])
const nickname = computed(() => store.getters['user/nickname'])

watch([keyword, sort, originList], computeWishlist)

onMounted(() => {
  getWishlist()
})

function getWishlist() {
  getWishlistApi().then(({ data }) => {
    originList.value = data
  })
}

function computeWishlist() {
  const words = keyword.value.trim().split(/\s+/)
  let list = originList.value.filter(item => {
    for (let word of words) {
      word = word.toLowerCase()
      if (!item.name.toLowerCase().includes(word) &&
          !item.developer.toLowerCase().includes(word) && !item.publisher.toLowerCase().includes(word))
        return false
    }
    return true
  })
  switch (sort.value) {
    case 0:
      quickSort(list, (a, b) => {
        if (a.sort === b.sort)
          return a.name <= b.name
        if (a.sort === 0)
          return false
        if (b.sort === 0)
          return true
        return a.sort <= b.sort
      })
      break
    case 1:
      quickSort(list, (a, b) => (a.name <= b.name))
      break
    case 2:
      quickSort(list, (a, b) => (a.finalPrice === b.finalPrice ? a.name <= b.name : a.finalPrice <= b.finalPrice))
      break
    case 3:
      quickSort(list, (a, b) => (a.discount === b.discount ? (a.finalPrice === b.finalPrice ? a.name <= b.name : a.finalPrice <= b.finalPrice) : a.discount >= b.discount))
      break
    case 4:
      quickSort(list, (a, b) => (a.addTime === b.addTime ? a.name <= b.name : a.addTime >= b.addTime))
      break
    case 5:
      quickSort(list, (a, b) => (a.releaseTime === b.releaseTime ? a.name <= b.name : a.releaseTime >= b.releaseTime))
      break
  }
  wishlist.value = [...list]
}

function openSortMenu() {
  if (!sortMenuShown.value)
    sortMenuShown.value = true
}

function closeSortMenu() {
  if (sortMenuShown.value)
    sortMenuShown.value = false
}

function changeSortType(type) {
  closeSortMenu()
  sort.value = type
  localStorage.setItem('steamWishlistSort', type)
}

function removeFromWishlist(appId) {
  removeFromWishlistApi(appId).then(() => {
    originList.value = originList.value.filter(item => item.appId !== appId)
  })
}

function handleDragStart(event, index) {
  draggingIndex.value = index
  const wishlistRect = wishlistRef.value.getBoundingClientRect()
  draggingTop.value = event.y - wishlistRect.y - 84
}

function handleDrag(event) {
  if (draggingIndex.value === -1)
    return
  const wishlistRect = wishlistRef.value.getBoundingClientRect()
  draggingTop.value = event.y - wishlistRect.y - 84
  const newIndex = getIndexByTop(draggingTop.value)
  if (newIndex !== draggingIndex.value) {
    const temp = wishlist.value.splice(draggingIndex.value, 1)[0]
    wishlist.value.splice(newIndex, 0, temp)
    draggingIndex.value = newIndex
  }
}

function handleDragEnd() {
  if (draggingIndex.value === -1)
    return
  draggingIndex.value = -1
  const map = new Map()
  originList.value.forEach((item) => {
    map.set(item.appId, item)
  })
  wishlist.value.forEach((item, index) => {
    item.sort = index + 1
    map.get(item.appId).sort = index + 1
  })
  sortWishlistApi(originList.value.map(item => {
    return {
      appId: item.appId,
      sort: item.sort
    }
  }))
}

function getIndexByTop(top) {
  let index = Math.floor((top - 12) / 180)
  if (index < draggingIndex.value)
    index++
  return Math.max(Math.min(index, originList.value.length - 1), 0)
}

</script>

<style scoped lang="scss">
.app {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #202326;
}

.header {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 940px;
  padding: 25px 0;
  color: #ffffff;
  font-size: 26px;
}

.header-avatar {
  width: 68px;
  height: 68px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.filter {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 940px;
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.search {
  flex-grow: 1;
  box-sizing: border-box;
  height: 38px;
  padding: 0 12px;
  border: 1px solid #000;
  border-radius: 4px;
  color: #ffffff;
  background-color: rgba(0, 0, 0, 0.1);
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.5) inset, 1px 1px 0 0 rgba(255, 255, 255, 0.2);
  outline: none;

  &:hover, &:focus {
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 0 3px rgba(0, 0, 0, 0.5) inset, 1px 1px 0 0 rgba(255, 255, 255, 0);
  }
}

.sort {
  position: relative;
  z-index: 100;
  display: flex;
  align-items: center;
  height: 38px;
  padding: 0 12px;
  margin-right: 12px;
  color: #9099a1;
  font-size: 13px;
  letter-spacing: 1px;
  cursor: pointer;
  transition: color 0.2s, background-color 0.2s;

  &:hover {
    color: #ffffff;
    background-color: rgba(255, 255, 255, 0.1);
  }

  &.focus {
    color: #ffffff;
    background-color: #808a9c;
  }
}

.sort-type {
  color: #ffffff;
}

.sort-menu {
  position: absolute;
  left: 0;
  right: 0;
  top: 100%;
  padding: 4px 0 12px 0;
  background-color: #808a9c;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;

  .sort.focus > & {
    opacity: 1;
    pointer-events: auto;
  }
}

.sort-menu-item {
  display: flex;
  align-items: center;
  height: 32px;
  padding: 0 12px;
  color: #ffffff;
  font-size: 12px;
  transition: background-color 0.1s;

  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
}

.list {
  position: relative;
  width: 940px;
  padding: 12px 0 80px 0;
}

.list-item {
  position: absolute;
  left: 0;
  display: flex;
  width: 940px;
  background-color: rgba(64, 81, 99, 0.9);

  &:not(.dragging) {
    transition: top 0.3s;
  }

  &.dragging {
    z-index: 10;
    background-color: #3d4b5a;
  }
}

.handle {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
  width: 40px;
  border-right: 1px solid rgba(0, 0, 0, 0.2);
  background: url("@/assets/handle.png") center center no-repeat;
  cursor: move;
  user-select: none;
}

.list-item-image {
  width: 292px;
  height: 136px;
  margin: 16px;
  flex-shrink: 0;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.list-item-info {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex-grow: 1;
  padding: 16px 16px 16px 0;
  overflow: hidden;
}

.name {
  color: #ffffff;
  font-size: 22px;
  text-decoration: none;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.middle {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  width: 100%;
}

.details {
  display: grid;
  grid-template-columns: 140px auto;
  grid-row-gap: 2px;
}

.release-label, .release, .developer-label, .publisher-label, .add-time-area {
  color: #b2b8bd;
  font-size: 11px;
}

.developer, .publisher {
  display: flex;

  * {
    color: #66c0f4;
    font-size: 11px;
    text-decoration: none;
  }

  &:hover {
    color: #ffffff;
  }
}

.price-area {
  display: flex;
  height: 32px;
  padding: 2px;
  background-color: rgba(0, 0, 0, 0.2);
}

.discount {
  padding: 0 6px;
  color: #beee11;
  background-color: rgba(76, 107, 34, 0.5);
  font-size: 24px;
  font-weight: bold;
  line-height: 32px;
}

.price {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-end;
  padding: 0 10px;
}

.origin-price {
  position: relative;
  color: #707e89;
  font-size: 11px;

  &::after {
    content: "";
    position: absolute;
    left: 2px;
    right: 0;
    top: 50%;
    height: 1px;
    background-color: #738895;
    box-shadow: 0 0 2px #000000;
    transform: rotate(-8deg);
  }
}

.final-price {
  color: #ffffff;
  font-size: 13px;
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  width: 100%;
}

.platforms {
  display: flex;
}

.platform {
  width: 20px;
  height: 20px;

  img {
    width: 100%;
    height: 100%;
  }
}

.remove {
  border-bottom: 1px dotted rgba(178, 184, 189, 0.6);
  cursor: pointer;

  &:hover {
    color: #d8dde2;
  }
}

</style>