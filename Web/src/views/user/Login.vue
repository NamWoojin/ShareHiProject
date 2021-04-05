<template>
  <v-app>
    <div>
      <v-form class="form-box">
        <v-container style="width: 500px;">
          <v-row>
            <v-col
              cols="12"
              style="padding-bottom: 0;"
            >
              <div style="margin: 2rem 0;">
                <strong style="font-size: 1.5rem;">회원정보를 입력해주세요</strong>
              </div>
              <v-text-field
                solo
                flat
                dense
                outlined
                prepend-inner-icon="mdi-email-outline"
                label="아이디(이메일)"
                :rules="[required_email]"
                v-model="form.mem_email"
              ></v-text-field> 
              
              <v-text-field
                solo
                flat
                dense
                outlined
                required
                prepend-inner-icon="mdi-lock-outline"
                type="password"
                label="비밀번호"
                :rules="[required_password]"
                v-model="form.mem_password"
                @keypress.enter="login"
              ></v-text-field> 
            </v-col>
            <v-col cols="12">
              <v-divider></v-divider>
            </v-col>
              <v-col cols="12">
                <v-btn 
                  class="color-box"
                  style="font-size: 1.3rem; font-weight: bold; 
                  width: 100%; height: 3rem;"
                  :disabled="!enable_login"
                  @click="login"
                >
                  로그인
                </v-btn>
              </v-col>
              <v-col cols="12">
                <div style="display: flex; justify-content: space-evenly">
                  <router-link :to="{ name: 'Signup'}" style="text-decoration: none;">
                    <span class="link-text">회원가입</span>
                  </router-link>
                  <span style="cursor: pointer;" @click="dialog=true" class="link-text">비밀번호찾기</span>
                </div>
              </v-col>
              <v-col cols="12">
                <v-divider></v-divider>
              </v-col>
              <v-col cols="12">
                  <div id="google-signin-btn"></div>
              </v-col>
              <v-col>
                <UserFooter />
              </v-col>
          </v-row>
        </v-container>
      </v-form>
      <v-dialog
        v-model="dialog"
        persistent
        max-width="600px"
      >
        <v-card>
          <v-card-title>
            <span>이메일을 입력해 주세요.</span>
            <span style="position: absolute; right: 20px;"><v-icon @click="dialog = false">mdi-close</v-icon></span>
          </v-card-title>
          <v-card-text>
              <v-text-field
                solo
                flat
                dense
                outlined
                required
                label="이메일 주소"
                v-model="findpasswordemail"
                @keypress.enter="login"
              >
                <template v-slot:append>
                  <v-fade-transition leave-absolute>
                    <v-btn
                      class="color-box"
                      @click="resendpassword"
                    >
                      임시비밀번호전송
                    </v-btn>
                  </v-fade-transition>
                </template>
              </v-text-field>
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>
  </v-app>
</template>

<script>

import axios from 'axios'
import { mapState } from 'vuex'
import UserFooter from '../../components/user/UserFooter.vue';

export default {
  name: 'Login',
  components: {
    UserFooter
  },
  data() {
    return {
      form: {
        mem_email: '',
        mem_password: '',
      },
      dialog: false,
      findpasswordemail: '',
    }
  },
  created() {
    if (this.loginstate) {
      this.$router.push({ name: 'Main' })
    }
  },
  // mounted() {
  //   window.gapi.signin2.render("google-signin-btn", {
  //     onsuccess: this.onSignIn,
  //   });
    
  // },
  computed: {
    required_email() {
      return () => /.+@.+\..+/.test(this.form.mem_email) || '이메일 형식으로 입력해주세요.'
    },
    required_password() {
      return () => (this.form.mem_password.length >= 8 && /[~!@#$%^&*()_+|<>?:{}]/.test(this.form.mem_password)) || '비밀번호를 특수문자 포함 8자 이상 작성해주세요.'
    },
    enable_login() {
      if (this.required_email() == true && this.required_password() == true) {
        return true
      } else {
        return false
      }
    },
    ...mapState({
      loginstate: 'login',
    })
  },
  methods: {
    onSignIn(googleUser) {
      var profile = googleUser.getBasicProfile();
      let googleform = {
        'mem_email': profile.getEmail(),
        'mem_name': profile.getName(),
        'mem_image': profile.getImageUrl(),
      }
      axios.post(`https://j4f001.p.ssafy.io/api/login/social`, googleform, {})
        .then(res => {
          if (res.data.message == 'SUCCESS') {
            localStorage.setItem('token', res.data.content.token)
            this.$store.dispatch("LOGIN", res.data.content.member)
            this.$router.push({ name: 'Storage' })
          }
        })
    },
    login() {
      if (this.enable_login) {
        axios.post(`https://j4f001.p.ssafy.io/api/login/basic`, this.form, {})
          .then(res => {
            if (res.data.message == 'SUCCESS') {
              localStorage.setItem('token', res.data.content.token)
              this.$store.dispatch("LOGIN", res.data.content.member)
              this.$router.push({ name: 'Storage' })
            } else {
              this.$message({
                type: 'info',
                message: '이메일과 비밀번호를 확인해주세요.'
              })
            }
          })

      }
      // login and router link
    },
    resendpassword() {
      this.dialog = false
      axios.post(`https://j4f001.p.ssafy.io/api/member/findPW`, {
        mem_email: this.findpasswordemail
      })
        .then(res => {
          if (res.data.message == 'SUCCESS') {
            this.$message({
              type: 'success',
              message: '임시비밀번호를 발송하였습니다.'
            })
          }
        })
    }
  }
}
</script>

<style scoped>
  .form-box {
    box-shadow: 0 0 0 1px rgb(0 20 61 / 8%), 0 3px 3px 0 rgb(0 20 61 / 4%);
  }

  .input-box {
    width: 100%;
    border: none;
    height: 48px;
    outline: none;
    font-size: 14px;
    box-shadow: 0 0 0 1px rgb(0, 20, 61 / 8%);
    box-sizing: border-box;
    transition: all .2s;
    line-height: 1.43;
    border-radius: 4px;
    letter-spacing: -0.4px;
  }

  .el-input {
    padding: 1rem !important;
  }

  >>> .v-text-field__slot {
    margin: 10px;
  }

  >>> .v-messages__message {
    margin-top: 2px;
  }

  >>> .primary--text {
    color: #3ac569 !important;
    caret-color: #3ac569;
  }

  .color-box {
    background-color: #3ac569 !important; 
    color: white; 
  }

  .link-text {
    color: #0073E9
  }
</style>