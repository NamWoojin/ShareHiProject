<template>
  <div style="width: 100%;">
    <el-row>
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
            <p style="font-size: 1.2rem;">© Share Hi</p>
          </div>
        </el-col>
        <el-col :md="0" :sm="24">
          <el-divider />
        </el-col>
        <el-col :md="{span: 12, offset: 6}" :sm="24" >
          <el-row>
            <el-col :span="12">
              <a target="_blank" href="https://drive.google.com/u/0/uc?id=1stiFhOzO3pIH5AvKF15PaevcXbGQNGo5&export=download" style="text-decoration: none; color: black">앱 다운로드</a>
            </el-col>
            <el-col :span="12">
              <a target="_blank" href="https://drive.google.com/file/d/1_XAgMlNiZVDekYre5pjozC4dzZ7fvy9j/view?usp=sharing" style="text-decoration: none; color: black">크롬 다운로드</a>
            </el-col>
          </el-row>
        </el-col>
        
        <!-- <el-col :span="24"><el-divider /></el-col> -->
      </el-row>


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