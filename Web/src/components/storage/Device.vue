<template>
  <div style="cursor: pointer; margin-left: 3rem;">
    <div @click="clickDevice">
      <v-img style="width: auto; height: auto; margin: 0;" max-width="150px" :src="squareUrl"></v-img>
      <h2 style="text-align: center;">{{device.name.split('-')[0]}}</h2>
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
      squareUrl: "https://images.samsung.com/sec/smartphones/galaxy-s21-ultra-5g/models/images/galaxy-s21-plus-5g_models_colors_phantom-violet.png",
    }
  },

  methods: {
    clickDevice() {
      // this.$socket.emit(1070, JSON.stringify({
      //   id: this.device.id
      // }))
      // this.$socket.on(1070, () => {
      //   this.$socket.emit(1050)
      // })
      this.$router.push({ name: 'FileBrowser', params: {device: this.device} })
    }
  }
}
</script>

<style>

</style>