<template>
  <div>
    <v-carousel hide-delimiters>
      <v-carousel-item src="@/assets/main_ad/main.png" />
      <v-carousel-item src="@/assets/main_ad/1.png" />
      <v-carousel-item src="@/assets/main_ad/2.jpg" />
      <v-carousel-item src="@/assets/main_ad/3.jpg" />
    </v-carousel>
  </div>
</template>

<script>
export default {
  name: 'Advertisement',
  data() {
    return {
      
    }
  }
}
</script>

<style scoped>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>