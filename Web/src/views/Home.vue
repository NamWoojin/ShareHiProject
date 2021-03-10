<template>
  <el-container>
    <el-header>
      <router-link :to="{ name: 'Main'}" style="text-decoration: none; color: black;">Header</router-link>
    </el-header>
    <el-container>
      <router-view />
    </el-container>
  </el-container>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'Home',
  components: {
    
  }
}
</script>
