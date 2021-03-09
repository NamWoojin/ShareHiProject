<template>
  <div>
    <router-link :to="{ name: 'Storage' }">
      <el-button type="danger" icon="el-icon-back" circle></el-button>
    </router-link>
    <h2>FileBrowser</h2>
  </div>
</template>

<script>
export default {
  name: 'FileBrowser',
}
</script>

<style>

</style>