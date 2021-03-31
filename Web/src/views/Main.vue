<template>
  <div style="width: 100%; position: relative; top: -60px !important;">
    <!-- <Advertisement /> -->
    <full-page id="fullpage" ref="fullpage" :options="options">
      <div class="section">
        <h1>Share hi 기능 소개</h1>
      </div>
      <div class="section">
        <h1>Android 다운 페이지 Link</h1>
      </div>
      <div class="section">
        <h1>Chr extension 다운 페이지 Link</h1>
      </div>
      <div class="section">
        <Footer />
      </div>
    </full-page>
    <div style="color: white; width: 100%; background-color: #444444; margin-top: 2rem; padding: 0.25rem; position: fixed; left: 0; bottom: 0;">
      Copyright © <strong style="color: #3ac569">SHARE HI</strong> {{ new Date().getFullYear() }} All Rights Reserved.
    </div>
  </div>
</template>

<script>
// import Advertisement from '../components/main/Advertisement.vue'
import Footer from '../components/main/Footer.vue'
import { mapState } from 'vuex'


export default {
  components: { Footer },
  name: 'Main',
  computed: {
    ...mapState([
      'member',
      'login',
    ])
  },
  data() {
    return {
      options: {
        afterLoad: this.afterLoad,
        sectionsColor: ['#B39DDC', '#FDCEC6', '#FCFFE0', 'white']
      }
    }
  },
  created() {
    if (this.login) {
      this.$router.push({ name: 'Storage' })
    }
  }
}
</script>

<style scoped>
  h1 {
    padding-top: 30rem;
  }
</style>