<template>
  <div class="modal-mask" @click="fail()" />
  <div ref="modal" class="modal">
    <div class="modal-title">{{ title }}</div>
    <div class="modal-content" >{{ content }}</div>
    <div class="modal-buttons">
      <div class="modal-confirm-button" @click="success()">{{ confirmText }}</div>
      <div v-if="showCancel" class="modal-cancel-button" @click="fail()">{{ cancelText }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  title: {
    type: String,
    required: true,
    default: ''
  },
  content: {
    type: String,
    required: true,
    default: ''
  },
  showCancel: {
    type: Boolean,
    default: true
  },
  cancelText: {
    type: String,
    default: '取消'
  },
  confirmText: {
    type: String,
    default: '确定'
  }
})

const modal = ref(null)

function success() {
  modal.value.dispatchEvent(new CustomEvent('success', { bubbles: true }))
  modal.value.dispatchEvent(new CustomEvent('complete', { bubbles: true }))
}

function fail() {
  if (!props.showCancel)
    return
  modal.value.dispatchEvent(new CustomEvent('fail', { bubbles: true }))
  modal.value.dispatchEvent(new CustomEvent('complete', { bubbles: true }))
}

</script>

<style scoped lang="scss">
.modal-mask {
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  z-index: 9999;
  background-color: rgba(0, 0, 0, 0.8);
}

.modal {
  position: fixed;
  left: 50%;
  top: 50%;
  z-index: 10000;
  box-sizing: border-box;
  width: 500px;
  padding: 32px;
  background: radial-gradient(circle at top left, rgba(74, 81, 92, 0.4) 0%, rgba(75, 81, 92, 0) 60%), #25282e;
  transform: translate(-50%, -50%);
}

.modal-title {
  margin-bottom: 32px;
  color: #ffffff;
  font-size: 22px;
  font-weight: bold;
}

.modal-content {
  color: #acb2b8;
  font-size: 14px;
  margin-bottom: 32px;
  white-space: pre-wrap;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.modal-confirm-button, .modal-cancel-button {
  position: relative;
  width: 72px;
  height: 32px;
  border-radius: 2px;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
  font-size: 14px;
  text-align: center;
  line-height: 32px;
  cursor: pointer;
  overflow: hidden;

  &:hover {
    color: #ffffff;
    &::before {
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      background-color: rgba(255, 255, 255, 0.2);
    }
  }
}

.modal-confirm-button {
  color: #d2efa9;
  background-color: #6fa720;
}

.modal-cancel-button {
  color: #dfe3e6;
  background: #32363f;
}

</style>
