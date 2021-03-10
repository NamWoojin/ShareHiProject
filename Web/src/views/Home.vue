<template>
  <el-container>
    <el-header>
      <Header />
    </el-header>
    
    <el-container>
      <router-view />
    </el-container>
  </el-container>
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
