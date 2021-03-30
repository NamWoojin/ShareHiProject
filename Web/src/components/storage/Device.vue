<template>
  <div>
    <div @click="clickDevice">
      <v-img width="150px" height="150px" :src="squareUrl"></v-img>
      <p>{{device.name}}</p>
    </div>
    
  </div>
</template>

<script>
export default {
  name: 'Device',
  props: {
    device: Object,
  },
  data () {
    return {
      squareUrl: "https://logodix.com/logo/14982.png",
    }
  },
  methods: {
    clickDevice() {
      this.$socket.emit(1070, JSON.stringify({
        id: this.device.id
      }))
      this.$socket.on(1070, () => {
        this.$socket.emit(1050)
      })
      this.$router.push({ name: 'FileBrowser' })
    }
  }
}
</script>

<style>

</style>