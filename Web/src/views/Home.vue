<template>
  <v-app>
    <Header />

    <v-main>
      <v-container>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Header from '../components/main/Header.vue'
// @ is an alias to /src

export default {
  name: 'Home',
  components: {
    Header
    
  }
}
</script>
