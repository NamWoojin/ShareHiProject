<template>
  <div>
    <div>
      <!-- {{followers}} -->
      <hr>
      <!-- {{followings}} -->
      <Profile />
      <hr>
      <UserStatus @state="setState" />
      <hr>
      <v-row>
        <v-col>
          <div v-for="(follower, idx) in followers" :key="idx">
            <div style="display: flex; align-items: center; margin: 1rem;">
              <img style="width: 60px; height: 60px;" :src="follower.mem_image">
              <div style="text-align: left;">
                <p style="margin: 0;">{{follower.mem_name}}</p>
                <p style="margin: 0;">{{follower.mem_email}}</p>
              </div>
              <!-- <div>
                <v-btn>팔로우</v-btn>
              </div> -->
            </div>
          </div>
        </v-col>
      </v-row>
      <!-- profile bar -->
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'
import Profile from '../../components/profile/Profile.vue'
import UserStatus from '../../components/profile/UserStatus.vue'
export default {
  components: { Profile, UserStatus },
  name: 'FollowerPage',
  data() {
    return {
      people: [],
      state: 0,
      
      followers: [],
      followings: [],
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
    
    axios.get(`https://j4f001.p.ssafy.io/api/follow/getFollower`, {
      params: {
        'mem_id': this.member.mem_id
      }
    })
      .then((res) => {
        this.followers = res.data.content.member
      })
    
    axios.get(`https://j4f001.p.ssafy.io/api/follow/getFollowing`, {
      params: {
        'target_mem_id': this.member.mem_id
      }
    })
      .then((res) => {
        this.followings = res.data.content.member
      })
    

  },
  methods: {
    setState(data) {
      this.state = data
    }
  },
  computed: {
    ...mapState([
      'member'
    ])
  }
}
</script>

<style>

</style>