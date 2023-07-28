<template>
  <div>
    <input v-model="keyword" class="search" placeholder="搜索好友">
    <div class="friends-area">
      <RouterLink v-for="(item, index) in friends" :key="index" class="friend" :to="`/profile/${item.userId}`">
        <div class="friend-avatar">
          <img v-lazy="item.avatar" alt="">
        </div>
        <div class="friend-name">{{ item.nickname }}</div>
      </RouterLink>
    </div>
    <div v-if="!loading && friends.length === 0" class="empty">没有好友可显示。</div>
  </div>
</template>

<script setup>
import { computed, inject, onMounted, ref } from 'vue'
import { getFriendListApi } from '@/api/friend'

const keyword = ref('')
const originList = ref([])
const friends = computed(() => {
  if (keyword.value.trim().length === 0)
    return originList.value
  return originList.value.filter(item => item.nickname.includes(keyword.value.trim()) || item.userId.toString() === keyword.value.trim())
})
const loading = ref(false)

const friendNum = inject('friendNum')

onMounted(() => {
  getFriendList()
})

function getFriendList() {
  loading.value = true
  getFriendListApi().then(({ data }) => {
    originList.value = [...data]
    friendNum.value = data.length
  }).finally(() => {
    loading.value = false
  })
}

</script>

<style scoped lang="scss">
.search {
  box-sizing: border-box;
  width: 690px;
  height: 30px;
  padding: 0 10px;
  margin-bottom: 20px;
  border: 1px solid #000000;
  border-radius: 3px;
  box-shadow: 1px 1px 0 rgba( 255, 255, 255, 0.2);
  outline: none;
  color: #909090;
  background-color: rgba(0, 0, 0, 0.2);
  font-size: 14px;

  &::placeholder {
    color: #7092a5;
    font-style: italic;
  }
}

.friends-area {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 12px;
  width: 100%;
}

.friend {
  display: flex;
  align-items: center;
  box-sizing: border-box;
  width: 222px;
  border: 1px solid rgba(29, 31, 36, 0.8);
  border-radius: 2px;
  color: #898989;
  background-color: rgba(29, 31, 36, 0.8);
  font-size: 14px;
  text-decoration: none;

  &:hover {
    box-shadow: 2px 2px 5px rgba( 0, 0, 0, 0.2);
  }
}

.friend-avatar {
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  border-radius: 2px;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.friend-name {
  padding: 0 12px;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.empty {
  width: 690px;
  padding: 24px 0;
  text-align: center;
  color: #969696;
  font-weight: lighter;
}

</style>
