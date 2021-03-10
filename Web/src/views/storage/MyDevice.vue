<template>
  <div>
    <h2>MyDevice</h2>
    현재 접속중인 Device 목록들을 보여주고, Device 클릭 시 Browser 페이지로 이동
    <div style="margin-top: 2rem;">
      <Device />
    </div>
  </div>
</template>

<script>
import Device from '../../components/storage/Device.vue'
export default {
  components: { Device },
  name: 'MyDevice',
}
</script>

<style>

</style>