<template>
  <div class="app">
    <div class="join">
      <div class="join-content">
        <div class="join-title">创建您的账户</div>
        <!--          电子邮件地址-->
        <div class="email">
          <div class="label">电子邮件地址</div>
          <div class="input-area">
            <input v-model="email" class="input" :class="{ error: emailErrMsg }"
                   @blur="validateEmail(email)" @focus="emailErrMsg = ''">
            <div v-show="emailErrMsg" class="err-msg">{{ emailErrMsg }}</div>
          </div>
        </div>
        <!--        Steam 账户名称-->
        <div class="username">
          <div class="label">Steam 账户名称</div>
          <div class="input-area">
            <input v-model="username" class="input" :class="{ error: usernameErrMsg }" maxlength="64"
                   @blur="validateUsername(username)" @focus="usernameErrMsg = ''">
            <div v-show="usernameErrMsg" class="err-msg">{{ usernameErrMsg }}</div>
          </div>
        </div>
        <!--        选择密码-->
        <div class="password">
          <div class="label">选择密码</div>
          <div class="input-area">
            <input v-model="password" class="input" :class="{ error: passwordErrMsg }" type="password"
                   maxlength="64" autocomplete="new-password"
                   @blur="validatePassword(password)" @focus="passwordErrMsg = ''">
            <div v-show="passwordErrMsg" class="err-msg">{{ passwordErrMsg }}</div>
          </div>
        </div>
        <!--        确认密码-->
        <div class="confirm-password">
          <div class="label">确认密码</div>
          <div class="input-area">
            <input v-model="confirmPassword" class="input" :class="{ error: confirmPasswordErrMsg }" type="password"
                   maxlength="64" autocomplete="new-password"
                   @blur="validateConfirmPassword(password, confirmPassword)" @focus="confirmPasswordErrMsg = ''">
            <div v-show="confirmPasswordErrMsg" class="err-msg">{{ confirmPasswordErrMsg }}</div>
          </div>
        </div>
        <!--        同意条款-->
        <label class="agree">
          <input v-model="agree" class="agree-input" :class="{ error: agreeErrMsg }" type="checkbox"
                 @change="agreeErrMsg = ''">
          我已年满 13 周岁并同意<a href="" target="_blank">《Steam 订户协议》</a>和<a href="" target="_blank">《Valve
          隐私政策》</a>的条款。
          <div v-show="agreeErrMsg" class="err-msg">{{ agreeErrMsg }}</div>
        </label>
        <div v-loading="loading" class="join-button" @click="join()">完成</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { isEmailValid, isPasswordValid } from '@/utils/validate'
import { checkUsernameAvailableApi, joinApi } from '@/api/user'
import { useRoute, useRouter } from 'vue-router'

import Md5 from 'crypto-js/md5'

const loading = ref(false)

const email = ref('')
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const agree = ref(false)

const emailErrMsg = ref('')
const usernameErrMsg = ref('')
const passwordErrMsg = ref('')
const confirmPasswordErrMsg = ref('')
const agreeErrMsg = ref('')

const route = useRoute()
const router = useRouter()

function validateEmail(emailValue) {
  const valid = isEmailValid(emailValue)
  emailErrMsg.value = valid ? '' : '请输入有效的电子邮件地址'
  return valid
}

async function validateUsername(usernameValue) {
  if (!usernameValue) {
    usernameErrMsg.value = '请输入账户名称'
    return false
  }
  const { data: valid } = await checkUsernameAvailableApi(usernameValue)
  usernameErrMsg.value = valid ? '' : '账户名称不可用'
  return valid
}

function validatePassword(passwordValue) {
  const valid = isPasswordValid(passwordValue)
  passwordErrMsg.value = valid ? '' : '密码必须包含至少 8 个字符'
  return valid
}

function validateConfirmPassword(passwordValue, confirmPasswordValue) {
  const valid = (passwordValue === confirmPasswordValue)
  confirmPasswordErrMsg.value = valid ? '' : '密码不符'
  return valid
}

function validateAgree(agreeValue) {
  agreeErrMsg.value = agreeValue ? '' : '您必须同意《Steam 订户协议》才能继续'
  return agreeValue
}

async function validateAll(email, username, password, confirmPassword, agree) {
  return (validateEmail(email) + validatePassword(password) + validateConfirmPassword(password, confirmPassword) + validateAgree(agree) + await validateUsername(username) === 5)
}

async function join() {
  const emailValue = email.value
  const usernameValue = username.value
  const passwordValue = password.value
  const confirmPasswordValue = confirmPassword.value
  const agreeValue = agree.value
  loading.value = true
  if (!await validateAll(emailValue, usernameValue, passwordValue, confirmPasswordValue, agreeValue)) {
    loading.value = false
    return
  }
  joinApi({ email: emailValue, username: usernameValue, password: Md5(passwordValue).toString() }).then(() => {
    router.replace({
      name: 'login',
      query: { redir: route.query.redir }
    })
  }).catch((reason) => {
    loading.value = false
    if (reason.code === 400) {
      emailErrMsg.value = '请输入有效的电子邮件地址'
    } else if (reason.code === 409) {
      usernameErrMsg.value = '账户名称不可用'
    }
  })
}
</script>
<style scoped lang="scss">
.app {
  background-color: #1f2428;
  min-height: calc(100vh - 104px);
  font-family: "Motiva Sans", sans-serif;
}

.join {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  row-gap: 32px;
  padding: 80px 0 150px 0;
  background-image: url("@/assets/acct_creation_bg.jpg");
  background-position: -150% top;
  background-repeat: no-repeat;
}

.join-content {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 25px;
  width: 800px;
  padding: 0 36px;
  margin: 0 auto;
}

.join-title {
  margin-bottom: 10px;
  color: #ffffff;
  font-size: 32px;
  font-weight: 200;
}

.label {
  margin-bottom: 2px;
  color: #b8b6b4;
  font-size: 14px;
}

.input-area {
  position: relative;
  display: inline-block;
}

.input {
  box-sizing: border-box;
  width: 300px;
  padding: 8px;
  border: none;
  border-radius: 2px;
  color: #ffffff;
  font-size: 15px;
  font-family: Arial, sans-serif;
  background-color: #32353c;

  &:hover {
    background-color: #393c44;
  }

  &.error {
    outline: 1px solid #c15755;
  }
}

.err-msg {
  position: absolute;
  left: calc(100% + 20px);
  top: 50%;
  padding: 8px;
  border-radius: 4px;
  color: #ffffff;
  background-color: #a0382b;
  font-size: 12px;
  white-space: nowrap;
  transform: translateY(-50%);

  &::before {
    content: "";
    position: absolute;
    right: 100%;
    top: 50%;
    display: inline-block;
    border-top: 8px solid transparent;
    border-right: 8px solid #a0382b;
    border-bottom: 8px solid transparent;
    transform: translate(1px, -50%);
  }
}

.agree {
  position: relative;
  display: flex;
  align-items: center;
  width: max-content;
  color: #b8b6b4;
  font-size: 14px;
  cursor: pointer;

  a {
    color: #ffffff;
    text-decoration: none;

    &:hover {
      color: #66c0f4;
    }
  }
}

.agree-input {
  position: relative;
  box-sizing: border-box;
  width: 20px;
  height: 20px;
  padding: 10px;
  margin: 0 6px 0 0;
  //border: 1px solid #;
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

  &.error {
    outline: 1px solid #c15755;
  }
}

.join-button {
  box-sizing: border-box;
  width: 130px;
  border-radius: 2px;
  margin-top: 10px;
  color: #ffffff;
  font-size: 15px;
  line-height: 36px;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}
</style>
