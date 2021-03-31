<template>
  <div id="app" class="hidden">
    <div class="overlay" @click="onClickOverlay">
      <div class="sidebar" >
        <Main v-if="loginStatus" @onClickLogout="onClickLogout" />
        <Login v-else @onClickLogin="onClickLogin"  />
      </div>
    </div>
    <router-view />
  </div>
</template>

<script>
import Login from "@/components/Login.vue";
import Main from "@/components/Main.vue";


export default {
  name: "App",
  components: { Login,Main },
  data () {
    return {
      loginStatus : false,
    }
  },
  methods : {
    onClickOverlay(e) {
      const shadow = document.querySelector('#shadowElement').shadowRoot
      const overlay = shadow.querySelector(".overlay")
      if (e.target === overlay) {
        this.toggleHidden()
      }
    },
    onClickLogin(result) {
      this.loginStatus = result
    },
    onClickLogout(result) {
      this.loginStatus = result
    },
    toggleHidden() {
      const shadow = document.querySelector('#shadowElement').shadowRoot
      const app = shadow.querySelector("#app")
      app.classList.toggle("hidden")
    }
  },
created () {
  chrome.runtime.onMessage.addListener(function(msg){
      if(msg === "toggle"){
        const shadow = document.querySelector('#shadowElement').shadowRoot
        const app = shadow.querySelector("#app")
        app.classList.toggle("hidden")
      }
    })
  },
};
</script>
