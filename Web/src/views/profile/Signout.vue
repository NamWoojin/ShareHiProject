<template>
  <div>
    <h2 style="text-align: left; margin: 1rem 0;">회원탈퇴</h2>
    <v-card style="text-align: left;">
      <v-card-title style="color: red; font-size: 2rem; font-weight: bold; margin: 1rem;">경고</v-card-title>
      <h4 style="margin: 2rem;">탈퇴할 아이디 <span style="font-weight: bold; color: green">{{member.mem_email}}</span></h4>
      <div style="margin: 2rem;">
        <span>회원탈퇴를 진행하면 해당 아이디의 회원정보 및 서비스 이용기록도 모두 삭제되어 복구가 불가합니다.</span>
        <br>
        <span>사용하고 계신 아이디 ({{member.mem_email}})를 정말로 탈퇴하시겠습니까?</span>
      </div>
      <div style="display: flex; justify-content: center;">
        <v-dialog v-model="dialog" width="500">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="error"
              style="margin-bottom: 2rem; text-align: center;"
              v-bind="attrs"
              v-on="on"
            >회원탈퇴</v-btn>
          </template>

          <v-card>
            <v-card-title>
              회원탈퇴
            </v-card-title>
            <v-card-text>
              삭제를 진행할 이메일을 입력해주세요.
              <v-text-field
                solo flat dense outlined required
                :label="member.mem_email"
                v-model="emailCheck"
              ></v-text-field>
            </v-card-text>
            <v-divider></v-divider>

            <v-card-actions>
              <v-spacer></v-spacer>
                <v-btn
                  color="error"
                  :disabled="member.mem_email != emailCheck"
                  @click="signout"
                >회원탈퇴</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
    </v-card>

  </div>
  
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
export default {
  name: 'Signout',
  data() {
    return {
      dialog: false,
      emailCheck: '',
    }
  },
  computed: {
    ...mapState([
      'member',
    ])
  },
  methods: {
    signout() {
      // axios delte 계정삭제
      axios.delete(`https://j4f001.p.ssafy.io/api/member/signout`, {
        params: {
          'mem_id': this.member.mem_id
        }
      })
        .then(res => {
          if (res.data.message == 'SUCCESS') {
            this.$message({
              type: 'success',
              message: '계정이 삭제되었습니다.'
            })
            this.$store.dispatch('LOGOUT')
            this.$router.push({ name: 'Main' })
          } else {
            this.$message({
              type: 'info',
              message: '실패하였습니다.'
            })
          }
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style>

</style>