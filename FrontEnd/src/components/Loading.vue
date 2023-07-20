<template>
  <div class="loading" :style="{ background }" @click.stop>
    <div class="loader" :style="{ fontSize: size }"/>
  </div>
</template>

<script setup>
defineProps({
  size: {
    type: String,
    default: '28px'
  },
  background: {
    type: String,
    default: 'transparent'
  }
})
</script>

<style scoped lang="scss">
.loading {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: default;
  &::before {
    content: "";
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
  }
}
.loader {
  box-sizing: border-box;
  width: 1em;
  height: 1em;
  border: 0.1em solid rgba(255, 255, 255, 0.3);
  border-top-color: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  animation: load 1s linear infinite;
}
@keyframes load {
  0% {transform: rotate(0deg)}
  50% {transform: rotate(180deg)}
  100% {transform: rotate(360deg)}
}
</style>
