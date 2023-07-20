<template>
  <div class="app">
    <div class="login">
      <div class="login-title">登录</div>
      <div class="login-content">
        <div class="login-content-left">
          <!--账户名称-->
          <div class="username">
            <div class="username-label">用账户名称登录</div>
            <input v-model="username" class="username-input" name="username" @input="errMsg = ''">
          </div>
          <!--密码-->
          <div class="password">
            <div class="password-label">密码</div>
            <input v-model="password" class="password-input" name="password" type="password" @input="errMsg = ''">
          </div>
          <!--记住我-->
          <label class="remember">
            <input v-model="rememberMe" class="remember-input" type="checkbox">
            记住我
          </label>
          <!--登录按钮-->
          <div v-loading="loading" class="login-button" @click="login()">登录</div>
          <!--错误提示-->
          <div class="login-error">{{ errMsg }}</div>
          <!--帮助-->
          <RouterLink class="login-help" to="">请求帮助，我无法登录。</RouterLink>
        </div>

        <div class="login-content-right">
          <!--二维码登录-->
          <div class="qrcode-label">或者用二维码登录</div>
          <div class="qrcode">
            <img src="@/assets/login_qr_code.png" alt="">
          </div>
          <div class="qrcode-tip">通过二维码使用 Steam 手机应用登录</div>
        </div>
      </div>
    </div>
    <div class="bottom">
      <div class="bottom-left">
        <div>加入 Steam，探索数千款精彩游戏。</div>
        <RouterLink class="learn-more" to="about">了解更多</RouterLink>
      </div>
      <div class="bottom-middle">
        <img src="@/assets/join_pc.png" alt="Join Steam">
      </div>
      <div class="bottom-right">
        <!--注册-->
        <RouterLink class="join-button" :to="`/join?redir=${route.query.redir}`">加入 Steam</RouterLink>
        <div>免费加入且简单易用。</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const rememberMe = ref(true)
const errMsg = ref('')
const loading = ref(false)

const store = useStore()
const route = useRoute()
const router = useRouter()

function login() {
  if (!username.value || !password.value) {
    return
  }
  loading.value = true
  store.dispatch('user/login', {
    username: username.value,
    password: password.value,
    rememberMe: rememberMe.value
  }).then(() => {
    router.push({ path: route.query.redir || '/' })
  }).catch((reason) => {
    loading.value = false
    errMsg.value = reason.message
  })
}

function clearErrMsg() {
  errMsg.value = ''
}

</script>

<style scoped lang="scss">
.app {
  background-color: #181a21;
  min-height: calc(100vh - 104px);
  font-family: "Motiva Sans", sans-serif;
}

.login {
  display: flex;
  flex-direction: column;
  align-items: center;
  row-gap: 32px;
  padding: 80px 0 150px 0;
  background-image: url("@/assets/new_login_bg_strong_mask.jpg");
  background-position: center top;
  background-repeat: no-repeat;
}

.login-title {
  box-sizing: border-box;
  width: 700px;
  padding: 8px 16px;
  color: #ffffff;
  font-size: 32px;
  font-weight: 200;
}

.login-content {
  display: flex;
  column-gap: 40px;
  box-sizing: border-box;
  width: 700px;
  padding: 24px 32px;
  border-radius: 4px;
  background-color: #181a21;
}

.login-content-left {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  flex-grow: 1;
}

.username-label, .qrcode-label {
  margin-bottom: 2px;
  color: #1999ff;
  font-size: 12px;
  user-select: none;
}

.password-label {
  margin-bottom: 2px;
  color: #afafaf;
  font-size: 12px;
  user-select: none;
}

.username-input, .password-input {
  box-sizing: border-box;
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 2px;
  outline: none;
  color: #ffffff;
  font-size: 15px;
  font-family: Arial, sans-serif;
  background-color: #32353c;

  &:hover {
    background-color: #393c44;
  }
}

.remember {
  display: flex;
  align-items: center;
  color: #afafaf;
  font-size: 12px;
  cursor: pointer;
}

.remember-input {
  position: relative;
  box-sizing: border-box;
  width: 20px;
  height: 20px;
  padding: 10px;
  margin: 0 6px 0 0;
  border-radius: 2px;
  background-color: #32353c;
  cursor: pointer;
  appearance: none;

  &:checked::after {
    content: "✔";
    position: absolute;
    left: 0;
    top: 0;
    display: inline-block;
    width: 20px;
    height: 20px;
    color: #ffffff;
    font-size: 15px;
    line-height: 20px;
    text-align: center;
  }

  &:hover {
    background-color: #393c44;
  }
}

.login-button {
  align-self: center;
  box-sizing: border-box;
  width: 270px;
  padding: 12px;
  border-radius: 2px;
  color: #ffffff;
  font-size: 16px;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}

.login-error {
  align-self: center;
  height: 16px;
  color: #c15755;
  font-size: 12px;
}

.login-help {
  align-self: center;
  color: #afafaf;
  font-size: 12px;

  &:hover {
    color: #c9c9c9;
  }
}

.qrcode {
  width: 200px;
  height: 200px;
  margin-bottom: 8px;
  border-radius: 8px;
  overflow: hidden;
  user-select: none;

  img {
    width: 100%;
    height: 100%;
  }
}

.qrcode-tip {
  color: #afafaf;
  font-size: 12px;
  text-align: center;
}

.bottom {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 100px;
  color: #b8b6b4;
  font-size: 14px;
}

.bottom-left {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  width: 200px;
  text-align: center;
}

.learn-more {
  color: #ffffff;
  text-decoration: none;

  &:hover {
    color: #66c0f4
  }
}

.bottom-middle {
  width: 200px;

  img {
    width: 100%;
  }
}

.bottom-right {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 200px;
}

.join-button {
  padding: 0 15px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 2px;
  margin: 40px 0 10px 0;
  color: #ffffff;
  font-size: 15px;
  line-height: 30px;
  text-decoration: none;

  &:hover {
    border-color: #ffffff;
  }
}
</style>
