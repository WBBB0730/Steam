<template>
  <div>
    <input v-model="keyword" class="search" placeholder="搜索用户" @input="handleKeywordInputDebounce()">
    <div v-if="users.length" class="users">
      <RouterLink v-for="(item, index) in users" :key="index" class="user" :to="`/profile/${item.userId}`">
        <div class="user-avatar">
          <img v-lazy="item.avatar" alt="">
        </div>
        <div class="user-nickname">{{ item.nickname }}</div>
        <div v-if="item.status !== 1" v-loading="item.loading" class="user-button" :class="{ disabled: item.status !== 0 }"
             @click.stop.prevent="sendInvitation(item)">添加好友</div>
        <div v-else v-loading="item.loading" class="user-button" @click.stop.prevent="cancelInvitation(item)">取消邀请</div>
      </RouterLink>
      <Pagination v-model:current="pageIndex" :size="10" :total="total" @change="searchUsers"/>
    </div>
    <div v-if="isEmpty" class="empty">没有符合您搜索的用户</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { searchUsersApi } from '@/api/user'
import { debounce } from '@/utils/debounce'
import Pagination from '@/components/Pagination.vue'
import { cancelInvitationApi, sendInvitationApi } from '@/api/friend'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()

const keyword = ref(route.query.keyword || '')
const pageIndex = ref(route.query.page ? parseInt(route.query.page) : 1)
const total = ref(0)
const users = ref([])
const isEmpty = ref(false)

const handleKeywordInputDebounce = debounce(handleKeywordInput, 1000)

onMounted(() => {
  searchUsers()
})

function handleKeywordInput() {
  pageIndex.value = 1
  router.replace(`/friends/add?keyword=${keyword.value}&page=${pageIndex.value}`)
  searchUsers()
}

function searchUsers() {
  if (keyword.value.length === 0) {
    users.value.length = 0
    return
  }
  isEmpty.value = false
  searchUsersApi(keyword.value, pageIndex.value, 10).then(({ data }) => {
    total.value = data.total
    users.value = [...data.list]
    if (data.length === 0)
      isEmpty.value = true
  })
}

function sendInvitation(user) {
  if (user.status !== 0)
    return
  const { userId } = user
  user.loading = true
  sendInvitationApi(userId).then(() => {
    user.status = 1
  }).finally(() => {
    user.loading = false
  })
}

function cancelInvitation(user) {
  if (user.status !== 1)
    return
  const { userId } = user
  user.loading = true
  cancelInvitationApi(userId).then(() => {
    user.status = 0
  }).finally(() => {
    user.loading = false
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
  box-shadow: 1px 1px 0 rgba(255, 255, 255, 0.2);
  outline: none;
  color: #909090;
  background-color: rgba(0, 0, 0, 0.2);
  font-size: 14px;

  &::placeholder {
    color: #7092a5;
    font-style: italic;
  }
}

.users {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 12px;
}

.user {
  display: flex;
  align-items: center;
  gap: 16px;
  box-sizing: border-box;
  width: 690px;
  padding: 16px;
  border-radius: 4px;
  margin-bottom: 12px;
  color: #f5f5f5;
  background-color: #2c3842;
  text-decoration: none;
}

.user-avatar {
  flex-shrink: 0;
  width: 80px;
  height: 80px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.user-button {
  flex-shrink: 0;
  padding: 0 20px;
  border-radius: 2px;
  margin-left: auto;
  color: #f5f5f5;
  background: linear-gradient(to right, #47bfff 0%, #1a44c2 60%) 20%;
  background-size: 400% 100%;
  font-size: 14px;
  line-height: 32px;
  cursor: pointer;
  transition: background-position 0.2s;

  &:not(.disabled):hover {
    color: #ffffff;
    box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.3);
    background-position: 0;
  }

  &.disabled {
    cursor: default;
    color: #464d58;
    background: rgba(61, 67, 77, 0.35);
  }
}

pagination {
  margin-left: auto;
}

.empty {
  width: 690px;
  padding: 24px 0;
  text-align: center;
  color: #969696;
  font-weight: lighter;
}

</style>
