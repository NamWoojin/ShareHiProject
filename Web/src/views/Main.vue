<template>
  <div style="width: 100%; position: relative; top: -60px !important;">
    <!-- <Advertisement /> -->
    <full-page id="fullpage" ref="fullpage" :options="options">
      <div class="section">
        <img src="@/assets/main_ad/2.jpg" alt="" style="height: 100%;">
      </div>
      <div class="section">
        <div
          style="position: relative; top: 60px !important; display: flex; justify-content: center; align-items: center; height: 100%;"
        >
          <!-- <img src="@/assets/main_ad/and.png" style="z-index: 99"> -->
          <div>
            <img src="@/assets/main_ad/android.png">
          </div>
          <div>
            <h1 style="padding: 0; margin-left: 3rem; margin-bottom: 2rem;">Share Hi 안드로이드 앱을 다운받아 보세요</h1>
            <a target="_blank" href="https://drive.google.com/uc?id=1os90JXBInUEPp8phPdPOVwiqLQz8jfkq&export=download" style="text-decoration: none;">
              <v-btn x-large>다운로드</v-btn>
            </a>
          </div>
        </div>
      </div>
      <div class="section">
        <div
          style="position: relative; top: 60px !important; display: flex; justify-content: center; align-items: center; height: 100%;"
        >
          <!-- <img src="@/assets/main_ad/and.png" style="z-index: 99"> -->
          <div>
            <img src="@/assets/main_ad/chrome.png">
          </div>
          <div>
            <h1 style="padding: 0; margin-left: 3rem; margin-bottom: 2rem;">크롬 익스텐션을 사용해 보세요</h1>
            <v-btn x-large>Comming Soon</v-btn>
          </div>
        </div>
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
        // sectionsColor: ['#FCFFE0','#FDCEC6', '#B39DDC',  'white']
        sectionsColor: ['white']
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