<template>
  <div>
    <div class="invitations">
      <div class="title">收到的邀请</div>
      <RouterLink v-for="(item, index) in pendingInvitations" :key="index" class="user" :to="`/profile/${item.userId}`">
        <div class="user-avatar">
          <img :src="item.avatar" alt="">
        </div>
        <div class="user-nickname">{{ item.nickname }}</div>
        <div v-loading="item.acceptLoading" class="user-accept-button" :class="{ disabled: item.refuseLoading }"
             @click.stop.prevent="acceptInvitation(item)">接受</div>
        <div v-loading="item.refuseLoading" class="user-refuse-button" :class="{ disabled: item.acceptLoading }"
             @click.stop.prevent="refuseInvitation(item)">拒绝</div>
      </RouterLink>
      <div v-if="!pendingLoading && pendingInvitations.length === 0" class="empty">没有待处理的好友邀请可显示。</div>
    </div>
    <div class="invitations">
      <div class="title">发出的邀请</div>
      <RouterLink v-for="(item, index) in sentInvitations" :key="index" class="user" :to="`/profile/${item.userId}`">
        <div class="user-avatar">
          <img :src="item.avatar" alt="">
        </div>
        <div class="user-nickname">{{ item.nickname }}</div>
        <div v-loading="item.loading" class="user-cancel-button" @click.stop.prevent="cancelInvitation(item)">取消邀请</div>
      </RouterLink>
      <div v-if="!sentLoading && sentInvitations.length === 0" class="empty">没有已发送的好友邀请可显示。</div>
    </div>
  </div>
</template>

<script setup>
import { inject, onMounted, ref } from 'vue'
import {
  acceptInvitationApi,
  cancelInvitationApi,
  getPendingInvitationsApi,
  getSentInvitationsApi,
  refuseInvitationApi
} from '@/api/friend'

const pendingInvitations = ref([])
const sentInvitations = ref([])
const pendingLoading = ref(false)
const sentLoading = ref(false)
const pendingNum = inject('pendingNum')
const friendNum = inject('friendNum')

onMounted(() => {
  getPendingInvitations()
  getSentInvitations()
})

function getPendingInvitations() {
  pendingLoading.value = true
  getPendingInvitationsApi().then(({ data }) => {
    pendingInvitations.value = [...data]
    pendingNum.value = data.length
  }).finally(() => {
    pendingLoading.value = false
  })
}

function getSentInvitations() {
  sentLoading.value = true
  getSentInvitationsApi().then(({ data }) => {
    sentInvitations.value = [...data]
  }).finally(() => {
    sentLoading.value = false
  })
}

function acceptInvitation(invitation) {
  if (invitation.refuseLoading)
    return
  invitation.acceptLoading = true
  acceptInvitationApi(invitation.userId).then(() => {
    pendingInvitations.value = pendingInvitations.value.filter(item => item !== invitation)
    pendingNum.value--
    friendNum.value++
  }).finally(() => {
    invitation.acceptLoading = false
  })
}

function refuseInvitation(invitation) {
  if (invitation.acceptLoading)
    return
  invitation.refuseLoading = true
  refuseInvitationApi(invitation.userId).then(() => {
    pendingInvitations.value = pendingInvitations.value.filter(item => item !== invitation)
    pendingNum.value--
  }).finally(() => {
    invitation.refuseLoading = false
  })
}

function cancelInvitation(invitation) {
  invitation.loading = true
  cancelInvitationApi(invitation.userId).then(() => {
    sentInvitations.value = sentInvitations.value.filter(item => item !== invitation)
  }).finally(() => {
    invitation.loading = false
  })
}

</script>

<style scoped lang="scss">

.invitations {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 125px;
  margin-bottom: 24px;
}

.title {
  box-sizing: border-box;
  width: 100%;
  padding: 12px;
  margin-bottom: 12px;
  color: #ffffff;
  background-color: #015e80;
  font-size: 14px;
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

.user-accept-button, .user-refuse-button, .user-cancel-button {
  flex-shrink: 0;
  padding: 0 20px;
  border-radius: 2px;
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

.user-accept-button, .user-cancel-button {
  margin-left: auto;
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
