<template>
  <div class="login-container">
    <img class="logo-image" src="https://github.com/kr2020lbh/TIL/blob/master/0316.assets/logo_transparent.png?raw=true" alt="">
    <input v-model="userObj.mem_email" class="login-email-input" placeholder="아이디 (이메일)" @keyup="stopPropagation" @keydown="stopPropagation">
    <div v-if="loginFail.mem_email" class="login-fail">이메일을 입력해주세요.</div>
    <input v-model="userObj.mem_password" type="password" class="login-password-input" placeholder="비밀번호" @keyup="stopPropagation" @keydown="stopPropagation">
    <div v-if="loginFail.mem_password" class="login-fail">비밀번호를 입력해주세요.</div>
    <div v-if="loginFail.mem_validate" class="login-fail">가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.</div>
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
      },
      loginFail : {
        mem_email : false,
        mem_password : false,
        mem_validate : false,
      }
    }
  },
  methods : {
    stopPropagation(e) {
      e.stopPropagation()
    },
    resetLoginFail(flag1,flag2,flag3) {
      this.loginFail.mem_email = flag1
      this.loginFail.mem_password = flag2
      this.loginFail.mem_validate = flag3
    }
    ,
    onClickLogin() {
      if (!this.userObj.mem_email) {
        this.resetLoginFail(true,false,false)
        return
      }
      if (!this.userObj.mem_password) {
        this.resetLoginFail(false,true,false)
        return
      }
      login(
        this.userObj,
        (res) => {
          if (res.data.message === 'SUCCESS') {
            console.log(res.data)
            this.resetLoginFail(false,false,false)
            this.$emit("onClickLogin",true)
          }
          else {
            this.resetLoginFail(false,false,true)
          }
        },
        (err) => {
          console.log(err)
          this.resetLoginFail(false,false,true)
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
