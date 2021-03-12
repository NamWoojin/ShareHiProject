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
                required
                prepend-inner-icon="mdi-account-outline"
                label="이름"
                :rules="[required_name]"
                v-model="form.name"
              ></v-text-field> 
              <v-text-field
                solo
                flat
                dense
                outlined
                prepend-inner-icon="mdi-email-outline"
                label="아이디(이메일)"
                :rules="[required_email]"
                v-model="form.email"
                :disabled="emailCertification"
              >
                <template v-slot:append>
                  <v-fade-transition leave-absolute>
                    <v-btn
                      class="color-box"
                      :disabled="!(required_email()==true) || emailCertification"
                      @click="dialog = true; timeCounter = 10; start();"
                    >
                      <span v-if="!emailCertification">인증</span>
                      <span v-else><v-icon>mdi-check</v-icon></span>
                    </v-btn>
                  </v-fade-transition>
                </template>
              </v-text-field> 
              <v-dialog
                v-model="dialog"
                persistent
                max-width="400px"
              >
                <v-card>
                  <v-card-title>
                    <span>이메일을 확인해 주세요.</span>
                    <span style="position: absolute; right: 20px;"><v-icon @click="dialog = false">mdi-close</v-icon></span>
                  </v-card-title>
                  <v-card-text>
                    <p style="color: red; margin-bottom: 1rem;">남은시간 : {{prettyTime()}}</p>
                    <v-text-field
                      solo
                      flat
                      dense
                      outlined
                      label="인증코드"
                      style="max-width: 250px; margin: auto;"
                    >
                      <template v-slot:append>
                        <v-fade-transition leave-absolute>
                          <v-btn
                            class="color-box"
                            @click="emailCertification = true; dialog = false;"
                          >
                            확인
                          </v-btn>
                        </v-fade-transition>
                      </template>
                    </v-text-field> 
                  </v-card-text>
                </v-card>
              </v-dialog>
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
                v-model="form.password"
              ></v-text-field> 

              <v-text-field
                solo
                flat
                dense
                outlined
                required
                prepend-inner-icon="mdi-lock-check-outline"
                type="password"
                label="비밀번호 확인"
                :rules="[required_passwordconfirm]"
                v-model="rePassword"
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
                  :disabled="!enable_signup"
                >
                  가입하기
                </v-btn>
              </v-col>
          </v-row>
        </v-container>
      </v-form>
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'Signup',
  data() {
    return {
      rePassword: '',
      form: {
        name: '',
        email: '',
        password: '',
      },
      dialog: false,
      timeCounter: 30,
      emailCertification: false,
    }
  },
  computed: {
    required_name() {
      return () => !!this.form.name || '이름을 정확히 입력하세요.'
    },
    required_email() {
      return () => /.+@.+\..+/.test(this.form.email) || '이메일 형식으로 입력해주세요.'
    },
    required_password() {
      return () => (this.form.password.length >= 8 && /[~!@#$%^&*()_+|<>?:{}]/.test(this.form.password)) || '비밀번호를 특수문자 포함 8자 이상 작성해주세요.'
    },
    required_passwordconfirm() {
      return () => (this.form.password === this.rePassword) || '비밀번호가 일치하지 않습니다.'
    },
    enable_signup() {
      if (this.required_name() == true && this.required_email() == true && this.required_password() == true && this.required_passwordconfirm() == true) {
        return true
      } else {
        return false
      }
    }
  },
  methods: {
    start() {
      this.polling = setInterval( ()=> {
        this.timeCounter--
        if (this.timeCounter <= 0) this.timeStop()
      }, 1000)
    },
    prettyTime() {
      let time = this.timeCounter / 60
      let minutes = parseInt(time)
      let seconds = Math.round((time - minutes) * 60)
      return this.pad(minutes, 2) + ":" + this.pad(seconds, 2)
    },
    pad(n, width) {
      n = n + '';
      return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n
    },
    timeStop() {
      clearInterval(this.polling)
    },
    
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
</style>