<template>
  <v-card
    width="100%;"
    flat
    class="card-css"
  >
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title title-padding">
            계정설정
          </v-list-item-title>
          <v-list-item-subtitle>
            <a>
              <router-link :to="{ name: 'UserModify'}" style="text-decoration: none; color: black;">회원정보수정</router-link>
            </a>
          </v-list-item-subtitle>
          <v-list-item-subtitle>
            <a>
              <router-link :to="{ name: 'PasswordChange'}" style="text-decoration: none; color: black;">비밀번호변경</router-link>
            </a>
          </v-list-item-subtitle>
          <v-list-item-subtitle>
            <a>
              <router-link :to="{ name: 'Signout'}" style="text-decoration: none; color: black;">회원탈퇴</router-link>
            </a>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title title-padding">
            친구관리
          </v-list-item-title>
          <v-list-item-subtitle>
            <a>
              <router-link :to="{ name: 'FollowerPage'}" style="text-decoration: none; color: black;">팔로워관리</router-link>
            </a>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>


  </v-card>
</template>

<script>
export default {
  name: 'ProfileNav',
}
</script>

<style scoped>
  .card-css {
    background-color: #f0f0f0 !important;
    border-width: 0px 1px 1px !important;
    
    border-color: rgb(225, 228, 230) !important;
  }

  .title-padding {
    padding-top: 1rem;
    padding-bottom: 1rem;
    font-weight: bold;
  }

  a {
    color: black;
    font-size: 1rem !important;
    text-decoration: none;
  }

  a:hover {
    color: #3D82F7 !important;
  }
</style>