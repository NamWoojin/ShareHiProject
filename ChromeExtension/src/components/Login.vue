<template>
  <div class="login-container">
    <img class="logo-image" src="https://github.com/kr2020lbh/TIL/blob/master/0316.assets/logo_transparent.png?raw=true" alt="">
    <input v-model="userInfo.email" class="login-email-input" placeholder="아이디 (이메일)">
    <input v-model="userInfo.password" class="login-password-input" placeholder="비밀번호">
    <button class="login-btn" @click="onClickLogin">로그인</button>
    <div class="login-btn-box">
      <span class="new-tab-link" @click="onClickSignup('https://j4f001.p.ssafy.io/')">회원가입</span>
      <span class="new-tab-link" @click="onClickFindPassword('https://www.google.com/')">비밀번호 찾기</span>
    </div>
  </div>
</template>

<script>

export default {
  name: "Login",
  data () {
    return {
      userInfo : {
        email : '',
        password : ''
      }
    }
  },
  methods : {
    onClickLogin() {
      alert('Clicked Login')
      this.$emit("onClickLogin",true)
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
