<template>

</template>

<script setup>
const socket = new WebSocket('ws://localhost:8080/chat')

socket.onopen = () => {
  socket.send('Hi~')
}

socket.onmessage = ({ data }) => {
  console.log(data)
}

</script>

<style scoped lang="scss">

</style>
