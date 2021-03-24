<template>
  <div class="login-container">
    <img class="logo-image" src="https://github.com/kr2020lbh/TIL/blob/master/0316.assets/logo_transparent.png?raw=true" alt="">
    <input v-model="userObj.mem_email" class="login-email-input" placeholder="아이디 (이메일)">
    <input v-model="userObj.mem_password" class="login-password-input" placeholder="비밀번호">
    <button class="login-btn" @click="onClickLogin">로그인</button>
    <div class="login-btn-box">
      <span class="new-tab-link" @click="onClickSignup('https://j4f001.p.ssafy.io/user/signup/')">회원가입</span>
      <span class="new-tab-link" @click="onClickFindPassword('https://www.google.com/')">비밀번호 찾기</span>
    </div>
  </div>
</template>

<script>
import {login} from "@/assets/api/user.js"
export default {
  name: "Login",
  data () {
    return {
      userObj : {
        mem_email : '',
        mem_password : ''
      }
    }
  },
  methods : {
    onClickLogin() {
      login(
        this.userObj,
        (res) => {
          console.log(res)
          this.$emit("onClickLogin",true)
        },
        (err) => {
          console.log(err)
        }
      )
    },
    onClickSignup(newURL) {
      window.open(newURL, '_blank');
    },
    onClickFindPassword(newURL) {
      window.open(newURL, '_blank');
    },
    onClickAxiosTest() {
      axiosTest()
    }
  }
};
</script>
