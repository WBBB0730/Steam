<template>
  <div class="pagination">
    <div class="last" @click="select(current - 1)">&lt;</div>
    <template v-for="(item, index) in pages" :key="index">
      <div v-if="item !== -1" class="page" :class="{ current: item === current }"
           @click="select(item)">{{ item }}</div>
      <div v-else class="ellipsis">...</div>
    </template>
    <div class="next" @click="select(current + 1)">&gt;</div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  /** 当前页码 */
  current: {
    type: Number,
    required: true
  },
  /** 页面大小 */
  size: {
    type: Number,
    required: true
  },
  /** 内容总数 */
  total: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:current', 'change'])

const pages = computed(() => {
  const pages = []
  const { current, size, total } = props
  const pageNum = Math.ceil(total / size)
  if (pageNum <= 9) {
    for (let i = 1; i <= pageNum; i++)
      pages.push(i)
    return pages
  }
  if (current <= 5) {
    for (let i = 1; i <= 7; i++)
      pages.push(i)
    pages.push(-1, pageNum)
    return pages
  }
  if (pageNum + 4 <= current) {
    for (let i = pageNum; i >= pageNum - 6; i--)
      pages.unshift(i)
    pages.unshift(1, -1)
    return pages
  }
  pages.push(1, -1)
  for (let i = current - 2; i <= current + 2; i++)
    pages.push(i)
  pages.push(-1, pageNum)
  return pages
})

function select(pageIndex) {
  pageIndex = Math.min(Math.max(pageIndex, 0), Math.ceil(props.total / props.size))
  emit('update:current', pageIndex)
  emit('change')
}

</script>

<style scoped lang="scss">
.pagination {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.last, .next {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  color: #67c1f5;
  background-color: rgba(103, 193, 245, 0.2);
  font-weight: bold;
  text-align: center;
  line-height: 30px;
  cursor: pointer;

  &:hover {
    background-color: rgba(103, 193, 245, 0.4);
  }
}

.page {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  color: #67c1f5;
  text-align: center;
  line-height: 30px;
  cursor: pointer;
  transition: background-color 0.1s;

  &.page:hover {
    background-color: rgba(103, 193, 245, 0.4);
  }

  &.current {
    background-color: rgba(103, 193, 245, 0.2);
  }
}

.ellipsis {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  color: #67c1f5;
  text-align: center;
  line-height: 30px;
  cursor: default;
}

</style>
