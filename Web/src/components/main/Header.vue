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
        <div v-if="!login">
          <router-link :to="{ name: 'Login'}" style="text-decoration: none; color: black;">
            <el-button
              style="background-color: #3ac569; color: white; font-weight: bold;"
              round
            >시작하기</el-button>
          </router-link>
        </div>
        <div v-else style="display: flex;">
          <!-- <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <div>
                <v-icon class="mx-5 cart" style="font-size: 26px; margin-top: 8px" v-bind="attrs" v-on="on">
                  mdi-bell-outline
                </v-icon>
              </div>
            </template>
            <v-list>
              <v-list-item
                v-for="(alert, idx) in alerts"
                :key="idx"
              >
                <v-list-item-title>{{ alert.title }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu> -->

          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <div v-if="member.mem_image == 'default.img'" style="height: 40px;">
                <v-avatar
                  color="success"
                  size="32"
                  v-bind="attrs"
                  v-on="on"
                  style="margin-top: 4px;"
                >
                  <span style="color: white">{{member.mem_name.substring(0,1)}}</span>
                </v-avatar>
              </div>

              <div v-else>
                <v-avatar 
                  style="height:40px"
                  v-bind="attrs"
                  v-on="on"
                >
                  <img 
                    :src="member.mem_image"
                    style="height: 32px; width: 32px;"
                  >
                </v-avatar>
              </div>
            </template>
            <v-list>
              <v-list-item>
                <router-link :to="{ name: 'UserModify'}" style="text-decoration: none; color: black;">
                  <div style="display: flex;">
                    <div v-if="member.mem_image == 'default.img'" style="height: 40px;">
                      <v-avatar
                        color="success"
                        size="32"
                        style="margin-top: 4px;"
                      >
                        <span style="color: white">{{member.mem_name.substring(0,1)}}</span>
                      </v-avatar>
                    </div>

                    <div v-else>
                      <v-avatar 
                        style="height:40px"
                      >
                        <img 
                          :src="member.mem_image"
                          style="height: 32px; width: 32px;"
                        >
                      </v-avatar>
                    </div>
                    <div class="mx-3" style="text-align: left;">
                      <v-list-item-title style="margin-top: 4px;">{{member.mem_name}}</v-list-item-title>
                      {{member.mem_email}}
                    </div>
                  </div>
                </router-link>
              </v-list-item>
              <v-divider style="margin: 1rem 0;"></v-divider>
              <v-list-item>
                <router-link :to="{ name: 'Storage'}" style="text-decoration: none; color: black;">
                  <v-list-item-title>내 저장소</v-list-item-title>
                </router-link>
              </v-list-item>
              <v-list-item>
                <router-link :to="{ name: 'Profile'}" style="text-decoration: none; color: black;">
                  <v-list-item-title>설정</v-list-item-title>
                </router-link>
              </v-list-item>
              <v-list-item>
                <v-list-item-title style="cursor: pointer; text-align: left;" @click="logout">로그아웃</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
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
        <v-list-item v-if="login">
          <div v-if="member.mem_image == 'default.img'" style="height: 40px;">
            <v-avatar
              color="success"
              size="32"
              style="margin-top: 4px;"
            >
              <span style="color: white">{{member.mem_name.substring(0,1)}}</span>
            </v-avatar>
          </div>
          <div v-else>
            <v-avatar 
              style="height: 32px"
            >
              <img 
                :src="member.mem_image"
                style="height: 32px; width: 32px;"
              >
            </v-avatar>
          </div>

          <v-list-item-content>
            <v-list-item-title>{{member.mem_name}}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item v-else @click="$router.push({name:'Login'})">
          <v-list-item-avatar>
            <v-img src=""></v-img>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>로그인해주세요</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <v-list dense>
          <v-list-item>
            <v-list-item-content>
              <v-list-item- @click="logout" style="cursor: pointer;">로그아웃</v-list-item->
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>
    </div>
  </div>
</template>

<script>

import { mapState } from 'vuex'

export default {
  name: 'Header',
  data() {
    return {
      width: window.innerWidth,
      drawer: null,
      alerts: [
        { title: 'Alert1', },
        { title: 'Alert2', },
        { title: 'Alert3', },
        { title: 'Alert4', },
        { title: 'Alert5', },
      ],
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize);
  },
  methods: {
    handleResize() {
      this.width = window.innerWidth
    },
    logout() {
      this.$store.dispatch("LOGOUT")
        .then(() => {
          this.$router.push({ name: 'Main' })
        })
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
    ...mapState([
      'login',
      'member',
    ])
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