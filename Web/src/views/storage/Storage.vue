<template>
  <el-container>
    <el-aside>
      <p>
        <router-link :to="{ name: 'MyDevice'}" style="text-decoration: none; color: black;">내 디바이스</router-link>
      </p>
      <p>
        <router-link :to="{ name: 'SharedFolder'}" style="text-decoration: none; color: black;">공유받은폴더</router-link>
      </p>
      <p>
        <router-link :to="{ name: 'SharingFolder'}" style="text-decoration: none; color: black;">공유하는폴더</router-link>
      </p>
    </el-aside>
    <el-container>
      <el-main>
        <router-view />
      </el-main>
      <!-- <el-footer>Footer</el-footer> -->
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Share',
}
</script>

<style>

</style>