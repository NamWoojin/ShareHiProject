<template>
  <div class="flex-box">
    <router-link :to="{ name: 'Main'}" style="text-decoration: none; color: black;">
      <img src="@/assets/logo/favicon.png" style="height: 200px; margin: 5px 0;">
    </router-link>
    <div>
      <v-carousel
        cycle
        hide-delimiter-background
        style="width: 400px; margin-top: 5rem;"
        :show-arrows="false"
        hide-delimiters
      >
        <v-carousel-item
          style="font-size: 1.5rem;"
        >
          <div style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <strong>쉽고 빠른 파일공유 서비스</strong>
            <v-img src="https://cdn1.iconfinder.com/data/icons/pixel-perfect-1/512/36-mobile-pc-devices-device-connection-connect-512.png"
            ></v-img>
            <strong>언제 어디서든 파일을 공유해보세요</strong>
          </div>
        </v-carousel-item>

        <v-carousel-item
          style="font-size: 1.5rem;"
        >
          <div style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <a
              target="_blank" href="https://drive.google.com/u/0/uc?id=1stiFhOzO3pIH5AvKF15PaevcXbGQNGo5&export=download"
            >
              <v-img
                src="@/assets/main_ad/and.png"
                style="max-height: 550px; max-width: 150px; width: auto; height: auto;"
              ></v-img>
            </a>
            <p style="margin: 2rem; font-weight: bold;">모바일 앱을 만나보세요!</p>
          </div>
        </v-carousel-item>



      </v-carousel>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserSidebar',
  data() {
    return {

    }
  }
}
</script>

<style scoped>
  .flex-box {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
</style>