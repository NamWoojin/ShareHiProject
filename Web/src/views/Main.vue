<template>
  <div style="width: 100%; position: relative; top: -60px !important;">
    <!-- <Advertisement /> -->
    <full-page id="fullpage" ref="fullpage" :options="options">
      <div class="section" style="position: relative;">
        <div>
          <img src="@/assets/main_ad/2.jpg" alt="" style="max-height: 100%; max-width: 100%; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); opacity: 0.25;">
        </div>
        <div style="max-height: 100%; max-width: 100%; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">
          <p style="font-size: 2rem; color: black; font-weight: bold; white-space: nowrap">언제 어디서든 손쉽게 파일을 공유해 보세요!</p>
          <p style="font-size: 1.1rem; color: black;">파일 크기나 확장자에 관계없이 모든 파일을 디바이스와 공유할 수 있습니다. <br></p>
        </div>
      </div>
      <div class="section">
        <div
          style="position: relative; top: 60px !important; display: flex; justify-content: center; align-items: center; height: 100%;"
        >
          <div>
            <a
              target="_blank" href="https://drive.google.com/u/0/uc?id=1stiFhOzO3pIH5AvKF15PaevcXbGQNGo5&export=download"
            >
              <img src="@/assets/main_ad/android.png">
            </a>
          </div>
          <div style="margin: 3rem;">
            <h2>모바일 안드로이드 앱</h2>
            <h3>이동단말기에 설치해보세요!</h3>
            <a
              target="_blank" href="https://drive.google.com/u/0/uc?id=1stiFhOzO3pIH5AvKF15PaevcXbGQNGo5&export=download"
              style="text-decoration: none;"
            >
              <v-btn
                class="ma-5"
                color="success"
              >
                다운로드
              </v-btn>
            </a>
          </div>
        </div>
      </div>
      <div class="section">
        <div
          style="position: relative; top: 60px !important; display: flex; justify-content: center; align-items: center; height: 100%;"
        >
          <div>
            <h1 style="padding: 0; margin-right: 3rem;">크롬 익스텐션으로 <br />사용할 수 있습니다</h1>
            <a
              target="_blank" href="https://drive.google.com/file/d/1fRJoArnhsh5aCmYHiVt4DCvMegjcQxdY/view?usp=sharing"
              style="text-decoration: none;"
            >
              <v-btn
                color="success"
                class="ma-3"
              >
                다운로드
              </v-btn>
            </a>
          </div>
          <div>
            <img src="@/assets/main_ad/chrome.png">
          </div>
        </div>
      </div>

      <div class="section">
        <div style="position: relative; top: 60px !important;">
          <h1 style="padding: 0; margin: 4rem;">Team Share Hi</h1>
          <v-container style="margin: 2rem 0;">
            <v-row>
              <v-col cols="6">
                <div style="display: flex; align-items: center; text-align: left;">
                  <v-img
                    class="elevation-6 profile"
                    src="@/assets/team/민진.png"
                  ></v-img>
                  <div style="margin-left: 2rem;">
                    <p style="font-weight: bold; font-size: 2rem; margin: 0;">구민진</p>
                    <p>whxorb44@gmail.com</p>
                    <p style="font-size: 1.5rem;">Backend, Infra</p>
                  </div>
                </div>
              </v-col>
              <v-col cols="6">
                <div style="display: flex; align-items: center; text-align: left;">
                  <v-img
                    class="elevation-6 profile"
                    src="@/assets/team/세진.jpg"
                  ></v-img>
                  <div style="margin-left: 2rem;">
                    <p style="font-weight: bold; font-size: 2rem; margin: 0;">권세진</p>
                    <p>kwon_sejin@naver.com</p>
                    <p style="font-size: 1.5rem;">Frontend Web</p>
                  </div>
                </div>
              </v-col>
              <v-col cols="6">
                <div style="display: flex; align-items: center; text-align: left;">
                  <v-img
                    class="elevation-6 profile"
                    src="@/assets/team/우진.png"
                  ></v-img>
                  <div style="margin-left: 2rem;">
                    <p style="font-weight: bold; font-size: 2rem; margin: 0;">남우진</p>
                    <p>nam990304@gmail.com</p>
                    <p style="font-size: 1.5rem;">Android</p>
                  </div>
                </div>
              </v-col>
              <v-col cols="6">
                <div style="display: flex; align-items: center; text-align: left;">
                  <v-img
                    class="elevation-6 profile"
                    src="@/assets/team/재혁.png"
                  ></v-img>
                  <div style="margin-left: 2rem;">
                    <p style="font-weight: bold; font-size: 2rem; margin: 0;">심재혁</p>
                    <p>jaehyukawot@gmail.com</p>
                    <p style="font-size: 1.5rem;">Backend Fileserver</p>
                  </div>
                </div>
              </v-col>
              <v-col cols="6">
                <div style="display: flex; align-items: center; text-align: left;">
                  <v-img
                    class="elevation-6 profile"
                    src="@/assets/team/병훈.png"
                  ></v-img>
                  <div style="margin-left: 2rem;">
                    <p style="font-weight: bold; font-size: 2rem; margin: 0;">이병훈</p>
                    <p>eric99kr@naver.com</p>
                    <p style="font-size: 1.5rem;">Chrome Extension</p>
                  </div>
                </div>
              </v-col>

            </v-row>

          </v-container>
          <Footer />
        </div>
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

  .profile {
    height: auto;
    width: auto;
    max-width: 150px;
    max-height: 150px;
    border-radius: 20px;
  }
</style>