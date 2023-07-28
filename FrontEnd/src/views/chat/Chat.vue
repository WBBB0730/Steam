<template>
  <div class="app">
    <div class="friends-area">
      <div class="friends-header">
        <div class="friends-header-avatar">
          <img :src="avatar" alt="">
        </div>
        <div class="friends-header-nickname">{{ nickname }}</div>
      </div>
      <div class="friends-title">
        好友 {{ onlineFriends.length }} / {{ friendMap.size }}
      </div>
      <div class="friends">
        <!-- 在线好友 -->
        <div class="online-friends-area">
          <div class="online-friends-title">
            在线
            <div class="online-friends-num">({{ onlineFriends.length }})</div>
          </div>
          <div class="online-friends">
            <div v-for="(item, index) in onlineFriends" :key="index" class="online-friend"
                 :class="{ current: current != null && item.userId === current.userId }"
                 @click="changeCurrent(item)">
              <div class="online-friend-avatar">
                <img v-lazy="item.avatar" alt="">
              </div>
              <div class="online-friend-nickname">{{ item.nickname }}</div>
              <div v-if="item.unreadNum" class="online-friend-unread">{{ item.unreadNum }}</div>
            </div>
          </div>
        </div>
        <!-- 离线好友 -->
        <div class="offline-friends-area">
          <div class="offline-friends-title">
            离线
            <div class="offline-friends-num">({{ offlineFriends.length }})</div>
          </div>
          <div class="offline-friends">
            <div v-for="(item, index) in offlineFriends" :key="index" class="offline-friend"
                 :class="{ current: current != null && item.userId === current.userId }"
                 @click="changeCurrent(item)">
              <div class="offline-friend-avatar">
                <img v-lazy="item.avatar" alt="">
              </div>
              <div class="offline-friend-nickname">{{ item.nickname }}</div>
              <div v-if="item.unreadNum" class="offline-friend-unread">{{ item.unreadNum }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="gap"/>

    <div class="chat-area">
      <template v-if="current">
        <div class="chat-title">{{ current.nickname }}</div>

        <div ref="chatContentRef" class="chat-content" @scroll="handleScroll">
          <div class="chat-messages">
            <div v-if="!loading && current.firstMessageId && messages.length > 0 && messages[0].id > current.firstMessageId" class="get-more-messages" @click="getMessages(current.userId, messages[0] ? messages[0].id : null)">查看更多消息</div>
            <template v-for="(item, index) in messages" :key="item.id">
              <div v-if="index === 0 || item.createTime > messages[index - 1].createTime + 120000" class="chat-message-time">
                {{ getTimeStr(item.createTime, 'YYYY-MM-DD hh:mm', 'MM-DD hh:mm', 'hh:mm') }}
              </div>
              <div v-if="item.fromId === userId" class="chat-message_self">
                <div class="chat-message-content_self">{{ item.content }}</div>
                <div class="chat-message-user_self">
                  <img :src="avatar" alt="">
                </div>
              </div>
              <div v-else class="chat-message_friend">
                <div class="chat-message-user_friend">
                  <img :src="current.avatar" alt="">
                </div>
                <div class="chat-message-content_friend">{{ item.content }}</div>
              </div>
            </template>
          </div>
        </div>

        <div class="chat-bottom">
          <div class="chat-input-area">
            <textarea v-model="inputMessage" class="chat-input" @keydown.ctrl.enter="sendMessage(current.userId, inputMessage)" />
            <button class="chat-send" title="发送 (Ctrl+Enter)" :disabled="inputMessage.length === 0"
                    @click="sendMessage(current.userId, inputMessage)">
              <svg xmlns="http://www.w3.org/2000/svg" class="SVGIcon_Button SVGIcon_Submit" x="0px" y="0px" viewBox="0 0 100 100">
                <g transform="translate(0,-952.36218)">
                  <path d="m 92.115057,974.14842 a 2.0001999,2.0001999 0 0 0 -1.96764,2.02965 l 0.0376,31.19553 -77.475501,0 16.161909,-15.73013 a 2.0002746,2.0002746 0 1 0 -2.790355,-2.8667 L 6.3913393,1007.9405 a 2.0001999,2.0001999 0 0 0 -0.0011,2.8646 l 19.6896957,19.2036 a 2.0002671,2.0002671 0 1 0 2.792551,-2.8646 l -16.170767,-15.771 79.153048,0 a 2.0001999,2.0001999 0 0 0 1.72959,-0.5437 2.0001999,2.0001999 0 0 0 0.0598,-0.058 2.0001999,2.0001999 0 0 0 0.54259,-1.7218 l -0.0388,-32.87638 a 2.0001999,2.0001999 0 0 0 -2.03297,-2.02522 z" fill-opacity="1" fill-rule="evenodd" stroke="none" visibility="visible" display="inline"></path>
                </g>
              </svg>
            </button>
          </div>
        </div>
      </template>
      <div class="scroll-to-bottom-area">
        <div v-if="!isAtBottom && hasNewMessage" class="scroll-to-bottom" @click="scrollToBottom()">收到新消息</div>
        <div v-else-if="!isAtBottom" class="scroll-to-bottom" @click="scrollToBottom()">滚动到底部</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useStore } from 'vuex'
import { computed, nextTick, onUnmounted, ref } from 'vue'
import { binaryInsert, quickSort } from '@/utils/sort'
import { useRouter } from 'vue-router'
import { showModal } from '@/utils/showModal'
import { getTimeStr } from '@/utils/format'

const store = useStore()
const token = computed(() => store.getters['user/token'])
const userId = computed(() => store.getters['user/userId'])
const nickname = computed(() => store.getters['user/nickname'])
const avatar = computed(() => store.getters['user/avatar'])
const socket = new WebSocket(`ws://localhost:8080/chat?userId=${ userId.value }`)
const router = useRouter()

const friendMap = ref(new Map())
const onlineFriends = computed(() => {
  const onlineFriends = Array.from(friendMap.value.values()).filter(item => item.online)
  quickSort(onlineFriends, (a, b) => {
    if (a.lastMessageTime === b.lastMessageTime)
      return a.nickname <= b.nickname
    if (!a.lastMessageTime)
      return false
    if (!b.lastMessageTime)
      return true
    return a.lastMessageTime >= b.lastMessageTime
  })
  return onlineFriends
})
const offlineFriends = computed(() => {
  const offlineFriends = Array.from(friendMap.value.values()).filter(item => !item.online)
  quickSort(offlineFriends, (a, b) => {
    if (a.lastMessageTime === b.lastMessageTime)
      return a.nickname <= b.nickname
    if (!a.lastMessageTime)
      return false
    if (!b.lastMessageTime)
      return true
    return a.lastMessageTime >= b.lastMessageTime
  })
  return offlineFriends
})
const current = ref(null)

const messagesMap = ref(new Map())
const messages = computed(() => {
  if (current.value === null)
    return []
  return messagesMap.value.get(current.value.userId)
})
const inputMessage = ref('')
const loading = ref(false)

const chatContentRef = ref(null)
const isAtBottom = ref(true)
const hasNewMessage = ref(false)

onUnmounted(() => {
  socket.onclose = null
  socket.close()
})

socket.onopen = () => {
  connect()
  getFriends()
}

socket.onmessage = ({ data }) => {
  data = JSON.parse(data)
  switch (data.type) {
    case 'online':
      handleFriendOnline(data.data)
      break
    case 'offline':
      handleFriendOffline(data.data)
      break
    case 'friends':
      handleFriendsData(data.data)
      break
    case 'message':
      handleMessageData(data.data)
      break
  }
}

socket.onerror = () => {
  showModal({
    title: '连接失败',
    content: '未能连接到Steam，请重新连接',
    showCancel: false
  }).then(() => {
    router.go(0)
  })
}

socket.onclose = () => {
  showModal({
    title: '连接已断开',
    content: '您与Steam的连接已断开，请重新连接',
    showCancel: false
  }).then(() => {
    router.go(0)
  })
}

/** 发送身份验证 */
function connect() {
  socket.send(JSON.stringify({
    type: 'connect',
    content: token.value
  }))
}

/** 获取好友列表 */
function getFriends() {
  socket.send(JSON.stringify({
    type: 'friends'
  }))
}

/** 获取聊天记录 */
function getMessages(userId, messageId) {
  loading.value = true
  socket.send(JSON.stringify({
    type: 'fetch',
    content: userId.toString(),
    extra: messageId ? messageId.toString() : null
  }))
}

/** 处理收到的好友列表 */
function handleFriendsData(data) {
  friendMap.value.clear()
  for (const friend of data)
    friendMap.value.set(friend.userId, friend)
}

/** 处理收到的聊天消息 */
function handleMessageData(data) {
  loading.value = false
  if (!isAtBottom.value && data.toId === userId.value && !data.isRead)
    hasNewMessage.value = true
  data.content = decodeURIComponent(atob(data.content))
  const messageUserId = data.fromId === userId.value ? data.toId : data.fromId
  if (!messagesMap.value.has(messageUserId))
    messagesMap.value.set(messageUserId, [])
  const list = messagesMap.value.get(messageUserId)
  const distance = chatContentRef.value ? chatContentRef.value.scrollHeight - chatContentRef.value.scrollTop : null
  binaryInsert(list, data, (a, b) => (a.createTime <= b.createTime), (a, b) => (a.id === b.id))
  if (distance !== null)
    nextTick(() => { chatContentRef.value.scrollTop = chatContentRef.value.scrollHeight - distance })
  if (data.fromId === userId.value)
    return
  // 是当前的聊天好友
  if (current.value !== null && data.fromId === current.value.userId) {
    readMessage(data)
  } else {
    friendMap.value.get(data.fromId).unreadNum++
  }
}

/** 处理收到的好友上线通知 */
function handleFriendOnline(userId) {
    friendMap.value.get(userId).online = true
}


/** 处理收到的好友下线通知 */
function handleFriendOffline(userId) {
    friendMap.value.get(userId).online = false
}

/** 发送消息 */
function sendMessage(userId, message) {
  socket.send(JSON.stringify({
    type: 'message',
    content: btoa(encodeURIComponent(message)),
    extra: userId.toString()
  }))
  inputMessage.value = ''
}

/** 发送已读通知 */
function readMessage(message) {
  socket.send(JSON.stringify({
    type: 'read',
    content: message.id.toString(),
    extra: message.fromId.toString()
  }))
}

/** 切换聊天好友 */
function changeCurrent(user) {
  user.unreadNum = 0
  if (current.value != null && user.userId === current.value.userId)
    return
  if (!messagesMap.value.has(user.userId)) {
    messagesMap.value.set(user.userId, [])
    getMessages(user.userId)
  }
  current.value = user
  nextTick(scrollToBottom)
}

/** 滚动到底部 */
function scrollToBottom() {
  chatContentRef.value.scrollTop = chatContentRef.value.scrollHeight
}

/** 处理滚动事件 */
function handleScroll(event) {
  const { scrollTop, clientHeight, scrollHeight } = event.target
  isAtBottom.value = scrollTop + clientHeight + 400 > scrollHeight
  if (isAtBottom.value === true)
    hasNewMessage.value = false
}

</script>

<style scoped lang="scss">
::-webkit-scrollbar-track {
  background: transparent;
}

.app {
  display: flex;
  box-sizing: border-box;
  width: 100vw;
  min-width: 940px;
  height: calc(100vh - 104px);
  border-top: 1px solid #121216;
}

.friends-area {
  width: 300px;
  height: 100%;
  background-color: #1c232c;
}

.friends-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  margin-bottom: 8px;
  box-shadow: inset 1px 1px 0 rgba(71, 71, 71, 0.3);
  background-color: #1c232c;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.friends-header-avatar {
  width: 48px;
  height: 48px;

  img {
    width: 100%;
    height: 100%;
  }
}

.friends-header-nickname {
  color: #6dcff6;
  font-weight: bold;
}

.friends {
  height: calc(100vh - 218px);
  background: radial-gradient(ellipse farthest-corner at 50% 30%, #212329 0%, #1e2025 50%, #1c1d22 100%);
  overflow-y: scroll;
}

.friends-title {
  height: 32px;
  padding: 0 12px;
  color: #b7ccd5;
  background-color: #434953;
  font-size: 13px;
  line-height: 30px;
}

.online-friends-title, .offline-friends-title {
  display: flex;
  align-items: center;
  gap: 4px;
  height: 32px;
  padding: 0 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  margin-top: 12px;
  color: #c5d6d4;
  font-size: 14px;
}

.online-friends-num, .offline-friends-num {
  color: #676666;
  font-size: 12px;
}

.online-friends, .offline-friends {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.online-friend, .offline-friend {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  cursor: default;

  &:hover, &.current {
    background-color: rgba(0, 0, 0, 0.2);
  }
}

.online-friend-avatar, .offline-friend-avatar {
  width: 32px;
  height: 32px;
  cursor: pointer;

  img {
    width: 100%;
    height: 100%;
  }
}

.offline-friend-avatar {
  position: relative;

  &::after {
    content: "";
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.1);
    transition: background-color 0.2s;
  }
}

.offline-friend:hover > .offline-friend-avatar::after {
  background-color: rgba(0, 0, 0, 0);
}

.online-friend-nickname {
  color: #6dcff6;
  font-size: 14px;
}

.offline-friend-nickname {
  color: #a0a0a0;
  font-size: 14px;
}

.online-friend-unread, .offline-friend-unread {
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  min-width: 20px;
  height: 20px;
  padding: 0 4px;
  border-radius: 6px;
  margin-left: auto;
  //box-shadow: 0 0 10px rgba(247, 246, 1, 0.5);
  color: #ffffff;
  background-color: #228fff;
  //background-color: #007bff; /* 明亮的蓝色 */
  //background-color: #28a745; /* 明亮的绿色 */
  //background-color: #fd7e14; /* 明亮的橙色 */
  font-size: 12px;
}

.gap {
  width: 4px;
  height: 100%;
  border: 1px solid #121216;
  border-top: none;
  border-bottom: none;
  background-color: #22252b;
}

.chat-area {
  position: relative;
  width: calc(100% - 304px);
  min-width: 636px;
  overflow: hidden;
}

.chat-title {
  position: relative;
  z-index: 10;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  height: 56px;
  padding: 12px;
  box-shadow: inset 1px 1px 0 rgba(71, 71, 71, 0.3), 0 0 10px rgba(0, 0, 0, 0.2);
  color: #aaaaaa;
  background-color: #22252b;
  font-size: 18px;
}

.chat-content {
  position: relative;
  height: calc(100% - 56px - 63px);
  background: radial-gradient(ellipse farthest-corner at 50% 30%, #212329 0%, #1e2025 50%, #1c1d22 100%);
  overflow-y: scroll;
}

.chat-messages {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px;
}

.get-more-messages {
  align-self: center;
  padding: 4px;
  margin: -4px;
  //color: #2c90ff;
  color: #228fff;
  font-size: 12px;
  cursor: pointer;
  opacity: 0.85;

  &:hover {
    opacity: 1;
  }
}

.chat-message-time {
  align-self: center;
  color: #4f5257;
  font-size: 12px;
}

.chat-message_self, .chat-message_friend {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  width: 100%;
}

.chat-message_self {
  justify-content: flex-end;
}

.chat-message_friend {
  justify-content: flex-start;
}

.chat-message-user_self, .chat-message-user_friend {
  width: 32px;
  height: 32px;

  img {
    width: 100%;
    height: 100%;
  }
}

.chat-message-content_self, .chat-message-content_friend {
  position: relative;
  box-sizing: border-box;
  max-width: calc(100% - 96px);
  padding: 8px 12px;
  border-radius: 2px;
  color: #969aa1;
  background-color: #292c34;
  font-size: 14px;
  white-space: pre-wrap;
  overflow-wrap: break-word;
}

.chat-message-content_self::before {
  content: "";
  position: absolute;
  left: 100%;
  top: 8px;
  box-sizing: border-box;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
  border-left: 8px solid #292c34;
  transform: translateX(-2px);
}

.chat-message-content_friend::before {
  content: "";
  position: absolute;
  right: 100%;
  top: 8px;
  box-sizing: border-box;
  border-top: 8px solid transparent;
  border-right: 8px solid #292c34;
  border-bottom: 8px solid transparent;
  transform: translateX(2px);
}

.chat-bottom {
  position: relative;
  z-index: 10;
  padding: 6px;
  border-bottom: 1px solid #22252b;
  box-shadow: inset 1px 1px 0 rgba(71, 71, 71, 0.3), 0 0 10px rgba(0, 0, 0, 0.4);
  background-color: #22252b;
}

.chat-input-area {
  display: flex;
  box-shadow: inset 0 0 4px #000000;
  background-color: #1b1c20;
}

.chat-input {
  box-sizing: border-box;
  flex-grow: 1;
  height: 50px;
  resize: none;
  padding: 4px 4px 4px 8px;
  border: none;
  outline: none;
  color: #aaaaaa;
  background-color: transparent;
  font-size: 14px;
}

.chat-send {
  display: flex;
  flex-shrink: 0;
  width: 46px;
  height: 46px;
  margin: 2px;
  border: none;
  outline: none;
  background-color: #3d424b;
  cursor: pointer;

  svg {
    width: 24px;
    height: 24px;
    margin: auto;
    fill: #8592a5;
  }

  &:hover svg {
    fill: #f5ffd7;
  }

  &[disabled] {
    background: #22252b;
    pointer-events: none;
  }
}

.scroll-to-bottom-area {
  position: absolute;
  z-index: 11;
  left: 50%;
  bottom: 63px;
  overflow: hidden;
  transform: translateX(calc(-50% + -6px));
  pointer-events: none;
}

.scroll-to-bottom {
  box-sizing: border-box;
  padding: 2px 8px;
  margin: 12px 12px 0;
  border: 1px solid rgba(71, 71, 71, 0.3);
  border-bottom: none;
  border-radius: 4px 4px 0 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  background-color: #22252b;
  font-size: 12px;
  color: #969aa1;
  cursor: pointer;
  pointer-events: auto;

  &:hover {
    color: #cdcdcd;
  }
}

</style>
