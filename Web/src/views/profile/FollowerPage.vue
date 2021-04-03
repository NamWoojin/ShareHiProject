<template>
  <div style="max-width: 75%; margin: auto;">
    <div>
      <Profile />
      <UserStatus @state="setState" :follower_len="followers.length" :following_len="followings.length" />
      <v-row v-if="state == 0">
        <v-col>
          <div v-for="(follower, idx) in followers" :key="idx">
            <div style="display: flex; align-items: center; margin: 1rem;" class="followcard">
              <img style="width: 60px; height: 60px;" :src="follower.mem_image">
              <div style="text-align: left; margin: 0.5rem;">
                <div style="display: flex;">
                  <p style="margin: 0; font-weight: bold; font-size: 1.1rem;">{{follower.mem_name}}</p>
                  <a
                    v-if="!includeCheck(follower.mem_id)"
                    @click="follow(follower)"
                    style="color: skyblue; position: inline-block; margin: 0 2rem;"
                  >팔로우하기</a>
                </div>
                <p style="margin: 0;">{{follower.mem_email}}</p>
              </div>
            </div>
          </div>
        </v-col>
      </v-row>
      <v-row v-else-if="state == 1">
        <v-col>
          <div v-for="(following, idx) in followings" :key="idx">
            <div style="display: flex; justify-content: space-between; align-items: center; margin: 1rem;" class="followcard">
              <div style="display: flex; align-items: center;" >
                <img 
                  style="width: 60px; height: 60px;"
                  src="https://www.pphfoundation.ca/wp-content/uploads/2018/05/default-avatar.png"
                  v-if="following.mem_image == 'default.img'"
                >
                <img style="width: 60px; height: 60px;" v-else :src="following.mem_image">
                <div style="text-align: left; margin: 0.5rem;">
                  <p style="margin: 0; font-weight: bold; font-size: 1.1rem;">{{following.mem_name}}</p>
                  <p style="margin: 0;">{{following.mem_email}}</p>
                </div>
              </div>
              <div>
                <v-btn @click="cancelFollow(following)">팔로우취소</v-btn>
              </div>
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
    },
    includeCheck(id) {
      for (let i=0; i < this.followings.length; i++) {
        if (this.followings[i].mem_id == id) {
          return true
        }
      }
      return false
    },
    follow(follower) {
      axios.post(`https://j4f001.p.ssafy.io/api/follow/insertFollowing`, {
        mem_id: this.member.mem_id,
        target_mem_id: follower.mem_id
      })
        .then(res => {
          if (res.data.message == 'SUCCESS') {
            axios.get(`https://j4f001.p.ssafy.io/api/member/getUser`, {
              params: {
                mem_id: follower.mem_id
              }
            })
              .then(data => {
                let info = data.data.content.member
                const new_member = {
                  'mem_id': info.mem_id,
                  'mem_name': info.mem_name,
                  'mem_email': info.mem_email,
                  'mem_image': info.mem_image,
                }
                this.followings.push(new_member)
              })
          }
        })
    },
    cancelFollow(following) {
      axios.delete(`https://j4f001.p.ssafy.io/api/follow/deleteFollowing`, {
        params: {
          mem_id: this.member.mem_id,
          target_mem_id: following.mem_id
        }
      })
        .then(res => {
          if (res.data.message == 'SUCCESS') {
            this.followings.splice(this.followings.indexOf(following), 1)
          }
        })
    }
  },
  computed: {
    ...mapState([
      'member'
    ])
  }
}
</script>

<style scoped>
  .followcard {
    border: 1px solid black !important;
    border-radius: 6px;
    padding: 1rem;
  }
</style>