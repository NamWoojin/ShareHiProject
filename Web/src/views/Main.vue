<template>
  <div style="width: 100%;">
    <router-link :to="{ name: 'Storage'}" style="text-decoration: none; color: black;">Storage</router-link>
    |
    <router-link :to="{ name: 'Profile'}" style="text-decoration: none; color: black;">Profile</router-link>
    |
    <router-link :to="{ name: 'Signup'}" style="text-decoration: none; color: black;">Signup</router-link>
    |
    <router-link :to="{ name: 'Login'}" style="text-decoration: none; color: black;">Login</router-link>
    <h2>Main (Advertisement)</h2>
    <Advertisement />
    <Footer />
  </div>
</template>

<script>
import Advertisement from '../components/main/Advertisement.vue'
import Footer from '../components/main/Footer.vue'
export default {
  components: { Footer, Advertisement },
  name: 'Main',
}
</script>

<style>

</style>