<template>
  <div class="swiper" @mouseenter="pause()" @mouseleave="start()">
    <div class="content">
      <template v-for="i in num" :key="i">
        <div class="swiper-item" :class="{ current: current === i - 1 }">
          <slot :name="i - 1"/>
        </div>
      </template>
    </div>
    <div class="dots">
      <div v-for="i in num" :key="i" class="dot" :class="{ current: current === i - 1 }" @click="select(i - 1)"/>
    </div>
    <div class="last" @click="last()">
      <img src="@/assets/arrow_left.png" alt="">
    </div>
    <div class="next" @click="next()">
      <img src="@/assets/arrow_right.png" alt="">
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'

const props = defineProps({
  num: {
    type: Number,
    required: true
  }
})

const current = ref(0)

let timer

onMounted(() => {
  start()
})

function last() {
  current.value = (current.value - 1 + props.num) % props.num
}

function next() {
  current.value = (current.value + 1) % props.num
}

function pause() {
  clearInterval(timer)
}

function start() {
  timer = setInterval(() => {
    next()
  }, 5000)
}

function select(index) {
  current.value = index
}
</script>

<style scoped lang="scss">
.swiper {
  position: relative;
  padding-bottom: 32px;
}

.content {
  display: flex;
  width: 100%;
  height: 100%;
}

.swiper-item {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.5s;

  &.current {
    opacity: 1;
    pointer-events: auto;
  }
}

.last, .next {
  position: absolute;
  top: 50%;
  padding: 36px 12px;
  transform: translateY(-50%);
  cursor: pointer;
  user-select: none;

  img {
    width: 23px;
    height: 36px;
  }
}

.last {
  right: 100%;
  background: linear-gradient(to right, rgba(0, 0, 0, 0.3) 5%, rgba(0, 0, 0, 0) 95%);

  &:hover {
    background: linear-gradient(to right, rgba(171, 218, 244, 0.3) 5%, rgba(171, 218, 244, 0) 95%);
  }
}

.next {
  left: 100%;
  background: linear-gradient(to right, rgba(0, 0, 0, 0) 5%, rgba(0, 0, 0, 0.3) 95%);

  &:hover {
    background: linear-gradient(to right, rgba(171, 218, 244, 0) 5%, rgba(171, 218, 244, 0.3) 95%);
  }
}

.dots {
  position: absolute;
  left: 50%;
  bottom: 12px;
  display: flex;
  gap: 4px;
  transform: translateX(-50%);
}

.dot {
  width: 16px;
  height: 9px;
  border-radius: 2px;
  background-color: rgba(255, 255, 255, 0.2);
  cursor: pointer;

  &:hover {
    background-color: rgba(255, 255, 255, 0.3);
  }

  &.current {
    background-color: rgba(255, 255, 255, 0.4);
  }
}
</style>
