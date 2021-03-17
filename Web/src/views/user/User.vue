<template>
  <div style="height: 100%;">
    <div class="flex-root">
      <div class="banner" v-show="width > 960">
        <UserSidebar />
      </div>
      <div class="Container-form Container-root" style="margin: auto">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import UserSidebar from '../../components/user/UserSidebar.vue';
export default {
  components: { UserSidebar },
  name: 'User',
  data() {
    return {
      width: window.innerWidth,
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize);
  },
  methods: {
    handleResize() {
      this.width = window.innerWidth
    }
  }
}
</script>

<style scoped>
  .flex-root {
    display: flex;
    flex-grow: 1; 
    flex-wrap: wrap;
    height: 100%;
  }

  .banner {
    z-index: 100;
    position: relative;
    min-width: 400px;
    flex-basis: 400px;
    flex-shrink: 0;
    background-color: #3ac569;
  }

  @media (min-width: 960px) {
    .Container-form {
      max-width: 960px;
    }
  }

  @media (min-width: 600px) {
    .Container-root {
      padding-left: 12px;;
      padding-right: 12px;
    }
  }
</style>