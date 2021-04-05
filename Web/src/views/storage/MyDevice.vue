<template>
  <div>
    <h2>MyDevice</h2>
    현재 접속중인 Device 목록들을 보여주고, Device 클릭 시 Browser 페이지로 이동
    <hr>
    <div style="margin-top: 2rem; display: flex;">
      <Device 
        v-for="(device, idx) in devices" 
        :key="idx"
        :device="device" 
      />
    </div>
  </div>
</template>

<script>
import Device from '../../components/storage/Device.vue'
export default {
  components: { Device },
  name: 'MyDevice',
  data() {
    return {
      devices: [],
    }
  },
  mounted() {
    console.log('Emit items');
  },
  created() {
    console.log('created MyDevice Page')
    this.$socket.on(1050, (data) => {
      data = JSON.parse(data);
      this.devices = data.devices;
    })
    // this.$socket.emit(1050)
    this.$socket.emit(1050)


  }
}
</script>

<style>

</style>