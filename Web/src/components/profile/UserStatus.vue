<template>
    <v-row no-gutters>
      <v-col>
        <v-card
          class="pa-2 text-center cursor-pointer"
          @click="state = 0"
          :class="{ selected: state==0}"
          flat
          tile
        >
          <v-card-text class="pb-0 pt-2">팔로워</v-card-text>
          <v-card-text class="pb-2 pt-0" style="font-weight: bold; color: black;">{{follower_len}}</v-card-text>
        </v-card>
      </v-col>
      <v-col>
        <v-card
          class="pa-2 text-center cursor-pointer"
          @click="state = 1"
          :class="{ selected: state==1}"
          flat
          tile
        >
          <v-card-text class="pb-0 pt-2">팔로잉</v-card-text>
          <v-card-text class="pb-2 pt-0" style="font-weight: bold; color: black;">{{following_len}}</v-card-text>
        </v-card>
      </v-col>
      <v-col>
        <v-card
          class="pa-2 text-center cursor-pointer"
          @click="state = 2"
          :class="{ selected: state==2}"
          flat
          tile
        >
          <v-card-text class="pb-0 pt-2">디바이스</v-card-text>
          <v-card-text class="pb-2 pt-0" style="font-weight: bold; color: black;">3</v-card-text>
        </v-card>
      </v-col>
    </v-row>
</template>

<script>
export default {
  name: 'UserStatus',
  props: {
    follower_len: Number,
    following_len: Number,
  },
  data() {
    return {
      state: 0,
    }
  },
  watch: {
    state: function () {
      this.$emit('state', this.state)
    }
  }
}
</script>

<style scoped>
  .cursor-pointer {
    cursor: pointer;
  }

  .selected {
    background-color: lightgray;
  }
</style>