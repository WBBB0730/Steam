<template>
  <div style="height: 100vh" @mousemove="handleMove($event)" @mouseup="handleEnd($event)">
    <div ref="app" class="app">
      <div v-for="(item, index) in list" :key="item.a" class="box" :class="{ dragging: draggingIndex === index }"
           :style="{ left: draggingIndex === index ? left + 'px' : (20 + index * 120) + 'px'}"
           @mousedown="handleStart($event, index)">
        {{ item.a }}
      </div>
    </div>
  </div>
  <div style="color: #ffffff">{{ list }}</div>
</template>

<script setup>
import { nextTick, ref } from 'vue'

const list = ref([{ a: 1 }, { a: 2 }, { a: 3 }, { a: 4 }])
// const list = ref([{ a: 1 }])

const app = ref(null)
const left = ref(0)
const draggingIndex = ref(-1)
// let startX = 0

function handleStart(event, index) {
  console.log('start', index)
  const rect = app.value.getBoundingClientRect()
  draggingIndex.value = index
  // startX = event.offsetX
  left.value = event.x - rect.x - 50
}

function handleMove(event) {
  if (draggingIndex.value === -1)
    return
  console.log('move', event.offsetX)
  const rect = app.value.getBoundingClientRect()
  left.value = event.x - rect.x - 50
  const newIndex = getIndex(left.value)
  if (newIndex !== draggingIndex.value) {
    const temp = list.value[draggingIndex.value]
    list.value.splice(draggingIndex.value, 1)
    list.value.splice(newIndex, 0, temp)
    draggingIndex.value = newIndex
    // startX = left.value
  }
}

function handleEnd(event) {
  console.log('end')
  draggingIndex.value = -1
}

function getIndex(x) {
  let index = Math.floor((x - 20) / 120)
  if (index < draggingIndex.value)
    index++
  if (index < 0)
    return 0
  if (index >= list.value.length)
    return list.value.length - 1
  return index
}

</script>

<style scoped lang="scss">
.app {
  position: relative;
  height: 120px;
  padding: 50px;
}

.box {
  position: absolute;
  top: 0;
  display: inline-block;
  width: 100px;
  height: 100px;
  background-color: #ffffff;
  font-size: 40px;
  text-align: center;
  line-height: 100px;
  user-select: none;
  cursor: pointer;
  transition: left 0.5s;

  left: var(--left--);

  &.dragging {
    transition: left 0s;
  }
}
</style>
