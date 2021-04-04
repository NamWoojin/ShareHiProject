<template>
  <v-card
    class="mx-auto"
    flat
    tile
  >
    <v-img
      height="150"
      gradient="to top right, rgba(255,255,255,.9), rgba(0,0,0,.1)"
      src="https://img.freepik.com/free-photo/cloud-background-with-pastel-colour_66899-936.jpg?size=626&ext=jpg"
    >
      <v-row
        align="end"
        class="fill-height"
      >
        <v-col
          align-self="start"
          class="my-auto pl-5"
          cols="4"
        >
          <v-avatar
            class="profile"
            color="grey"
            size="100"
            
          >
            <img 
              src="https://www.pphfoundation.ca/wp-content/uploads/2018/05/default-avatar.png"
              v-if="member.mem_image == 'default.img'"
            >
            <img v-else :src="member.mem_image">
          </v-avatar>
        </v-col>

        <v-col
          cols="8"
          class="my-auto"
        >
          <v-list-item
            color="rgba(0, 0, 0, 0.5)"
          >
            <v-list-item-content>
              <v-list-item-title class="title" style="font-weight: bold;">
                {{member.mem_name}}
              </v-list-item-title>
              <v-list-item-subtitle>
                {{member.mem_email}}
              </v-list-item-subtitle>
            </v-list-item-content>

          </v-list-item>

        </v-col>

      </v-row>

    </v-img>
  
  </v-card>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'Profile',
  computed: {
    ...mapState([
      'member'
    ])
  }
}
</script>

<style>

</style>