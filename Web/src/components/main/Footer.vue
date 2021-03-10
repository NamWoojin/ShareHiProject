<template>
  <div style="">
    <el-row>
      <el-col :span="24"><el-divider /></el-col>
      <el-row style="width: 75%; margin: 0px auto;">
        <el-col
          :md="3"
          :sm="24"
          style="height: 5rem;"
        >
          <div>
            <img src="@/assets/logo/logo_transparent_black.png" style="height: 5rem;">
          </div>
        </el-col>
        <el-col
          :md="{span: 3, offset: 18}"
          :sm="24"
          style="margin-top: 1.5rem;"
        >
          <router-link :to="{ name: 'Login'}" style="text-decoration: none; color: black; margin: auto;">
            <el-button
              style="color: black; font-weight: bold; width: 100%; margin: 0px auto !important"
              round
            >시작하기</el-button>
          </router-link>
        </el-col>
        <el-col :span="24"><el-divider /></el-col>
        
        <el-col :md="6" :sm="24">
          <div style="text-align: left;">
            <span>emailaddress@gmail.com</span>
            <br>
            <span>© Share Hi</span>
          </div>
        </el-col>
        <el-col :md="0" :sm="24">
          <el-divider />
        </el-col>
        <el-col :md="{span: 12, offset: 6}" :sm="24" >
          <el-row>
            <el-col :span="12">Item1</el-col>
            <el-col :span="12">Item2</el-col>
            <el-col :span="12">Item3</el-col>
            <el-col :span="12">Item4</el-col>
          </el-row>
        </el-col>
        
        <!-- <el-col :span="24"><el-divider /></el-col> -->
      </el-row>

      <div style="color: white; width=100%; background-color: #444444; margin-top: 2rem; padding: 0.25rem;">
        Copyright © <strong style="color: #3ac569">SHARE HI</strong> {{ new Date().getFullYear() }} All Rights Reserved.
      </div>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Footer',
}
</script>

<style>

</style>