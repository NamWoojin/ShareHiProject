<template>
  <div class="flex-box">
    <router-link :to="{ name: 'Main'}" style="text-decoration: none; color: black;">
      <img src="@/assets/logo/favicon.png" style="height: 200px; margin: 5px 0;">
    </router-link>
    <div>
      <v-carousel
        cycle
        hide-delimiter-background
        style="width: 400px;"
        :show-arrows="false"
        hide-delimiters
      >
        <v-carousel-item
          v-for="(slide, i) in slides"
          :key="i"
          style="font-size: 2rem;"
        >
          <div style="display: flex; justify-content: center; align-items: center;">
            <span style="color: black; font-weight: bold; margin-top: 200px;">{{slide}}</span>
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
      slides: [
        'Application store Link',
        'Chrome Extension Link',
        'Share Hi Advertisement'
      ]
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