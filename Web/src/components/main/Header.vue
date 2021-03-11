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
    <div v-show="mobileWidth">
      <v-app-bar>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
        <router-link :to="{ name: 'Main'}" style="text-decoration: none; color: black; display: block; margin: auto;">
          <img src="@/assets/logo/logo_transparent_black.png" style="height: 50px;">
        </router-link>
      </v-app-bar>
      <v-navigation-drawer
        v-model="drawer"
        absolute
        temporary
      >
        <v-list-item>
          <v-list-item-avatar>
            <v-img src="https://randomuser.me/api/portraits/men/78.jpg"></v-img>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>John Leider</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <v-list dense>
          <v-list-item
            v-for="item in items"
            :key="item.title"
            link
          >
            <v-list-item-icon>
              <v-icon>{{ item.icon }}</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Header',
  data() {
    return {
      width: window.innerWidth,
      drawer: null,
      items: [
        { title: 'Home', icon: 'mdi-view-dashboard' },
        { title: 'About', icon: 'mdi-forum' },
      ]
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
      if (this.width <= 892) {
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
    z-index: 99;
  }

  >>> .v-navigation-drawer {
    height: unset !important;
  }

  >>> .v-app-bar {
    background-color: #fff !important;
    height: 60px !important;
  }

  >>> .v-sheet {
    box-shadow: unset !important;
  }
</style>