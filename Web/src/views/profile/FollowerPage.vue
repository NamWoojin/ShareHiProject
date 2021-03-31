<template>
  <div>
    <div>
      {{people}}
      <!-- profile bar -->
    </div>
    <div>
      <v-autocomplete
        :items="people"
        solo
        flat
        dense
        outlined
        chips
        color="blue-grey lighten-2"
        label="Select"
        item-text="mem_name"
        item-value="mem_name"
        multiple
      >
        <template v-slot:selection="data">
          <v-chip
            v-bind="data.attrs"
            :input-value="data.selected"
            close
            @click="data.select"
            @click:close="remove(data.item)"
          >
            <v-avatar left>
              <v-img :src="data.item.mem_image"></v-img>
            </v-avatar>
            {{ data.item.name }}
          </v-chip>
        </template>
        <template v-slot:item="data">
          <template v-if="typeof data.item !== 'object'">
            <v-list-item-content v-text="data.item"></v-list-item-content>
          </template>
          <template v-else>
            <v-list-item-avatar>
              <img :src="data.item.avatar">
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title v-html="data.item.name"></v-list-item-title>
              <v-list-item-subtitle v-html="data.item.group"></v-list-item-subtitle>
            </v-list-item-content>
          </template>
        </template>
      </v-autocomplete>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'FollowerPage',
  data() {
    return {
      people: [],
    }
  },
  created() {
    axios.get(`https://j4f001.p.ssafy.io/api/follow/searchMember`, {
      params: {
        'searchWord': '',
      }
    })
      .then(res => {
        this.people = res.data.content.member
      })
  }
}
</script>

<style>

</style>