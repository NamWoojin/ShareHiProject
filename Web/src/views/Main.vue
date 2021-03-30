<template>
  <div style="width: 100%;">
    <Advertisement />
    <Footer />
  </div>
</template>

<script>
import Advertisement from '../components/main/Advertisement.vue'
import Footer from '../components/main/Footer.vue'

import { mapState } from 'vuex'

export default {
  components: { Footer, Advertisement },
  name: 'Main',
  computed: {
    ...mapState([
      'member',
      'login',
    ])
  },
  created() {
    if (this.login) {
      this.$router.push({ name: 'Storage' })
    }
  }
}
</script>

<style>

</style>