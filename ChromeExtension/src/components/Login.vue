<template>
  <div class="login-container">
    <img id="logo-image" src="" alt="">
    <input class="login-email-input" placeholder="이메일">
    <input class="login-password-input" placeholder="비밀번호">
    <button class="login-btn" @click="onClickLogin">로그인</button>
    <footer>
      <button>회원가입</button>
      <button>비밀번호 찾기</button>
    </footer>
  </div>
</template>

<script>
export default {
  name: "Login",
  methods : {
    onClickLogin() {
      alert('Clicked Login')
      this.$emit("onClickLogin",true)
    },
  },
  created () {
  }
};
</script>
