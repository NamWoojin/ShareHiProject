<template>
  <div>
    <router-link :to="{ name: 'FileBrowser' }">
      <el-image :src="squareUrl"></el-image>
    </router-link>
    
  </div>
</template>

<script>
export default {
  name: 'Device',
  data () {
    return {
      squareUrl: "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
    }
  }
}
</script>

<style>

</style>