<template>
  <div>
    <v-row>
      <v-col cols="2">
        <ProfileNav />
      </v-col>
      <v-col cols="10">
        <router-view />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import ProfileNav from '../../components/main/ProfileNav.vue'
export default {
  components: { ProfileNav },
  name: 'Profile',
}
</script>

<style>

</style>