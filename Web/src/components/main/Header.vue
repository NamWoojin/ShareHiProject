<template>
  <div class="header-wrapper">
    <div 
      style="max-width: 75%; max-height: 60px; margin: 0px auto; 
            display: flex; justify-content: space-between; align-items: center;"
      v-show="!mobileWidth"
    >
      <div class="logo">
        <router-link :to="{ name: 'Main'}" style="text-decoration: none; color: black;">
          <img src="@/assets/logo/logo_transparent_black.png" style="height: 50px; margin: 5px 0;">
        </router-link>
      </div>
      <div>
        <router-link :to="{ name: 'Login'}" style="text-decoration: none; color: black;">
          <el-button
            style="background-color: #3ac569; color: white; font-weight: bold;"
            round
          >시작하기</el-button>
        </router-link>
      </div>
    </div>
    <div v-show="mobileWidth" style="height: 60px;">
      <router-link :to="{ name: 'Main'}" style="text-decoration: none; color: black;">
        <img src="@/assets/logo/logo_transparent_black.png" style="height: 50px; margin: 5px 0;">
      </router-link>
      <i 
        class="el-icon-menu"
        style="font-size: 40px; position: absolute; left: 10px; top: 10px;"
        @click="drawer = true"
        v-show="!drawer"
      ></i>
      <i 
        class="el-icon-close"
        style="font-size: 40px; position: absolute; left: 10px; top: 10px;"
        @click="drawer = false"
        v-show="drawer"
      ></i>
      <div 
        style="position: absolute; top:60px; left: 0px; background-color: grey; z-index: 10; width: 200px;"
        v-show="drawer"

      >
        <p>item 1</p>
        <p>item 2</p>
        <p>item 3</p>
        <p>item 4</p>
        <p>item 5</p>
      </div>
      
    </div>
  </div>
</template>

<script>

export default {
  name: 'Header',
  data() {
    return {
      width: window.innerWidth,
      drawer: false,
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize);
  },
  methods: {
    handleResize() {
      this.width = window.innerWidth
    }
  },
  computed: {
    mobileWidth() {
      if (this.width <= 768) {
        return true
      } else {
        return false
      }
    },
  }
}
</script>

<style scoped>
  .header-wrapper {
    width: 100%;
    position: fixed;
    background-color: #fff;
    box-shadow: 0 0 10px 0 rgb(0 0 0 / 35%) !important;
    left: 0px;
    top: 0px;
  }

</style>