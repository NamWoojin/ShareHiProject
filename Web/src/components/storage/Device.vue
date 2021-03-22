<template>
  <div>
    <router-link :to="{ name: 'FileBrowser' }">
      <v-img width="150px" height="150px" :src="squareUrl"></v-img>
    </router-link>
    
  </div>
</template>

<script>
export default {
  name: 'Device',
  data () {
    return {
      squareUrl: "https://logodix.com/logo/14982.png",
    }
  }
}
</script>

<style>

</style>