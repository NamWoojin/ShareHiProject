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
                v-model="form.email"
                :disabled="emailCertification"
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
                v-model="form.password"
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
                >
                  로그인
                </v-btn>
              </v-col>
              <v-col cols="12">
                <div style="display: flex; justify-content: space-evenly">
                  <router-link :to="{ name: 'Signup'}" style="text-decoration: none;">
                    <span class="link-text">회원가입</span>
                  </router-link>
                  <span class="link-text">비밀번호찾기</span>
                </div>
              </v-col>
              <v-col cols="12">
              <v-divider></v-divider>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </div>
  </v-app>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      form: {
        email: '',
        password: '',
      }
    }
  },
  computed: {
    required_email() {
      return () => /.+@.+\..+/.test(this.form.email) || '이메일 형식으로 입력해주세요.'
    },
    required_password() {
      return () => (this.form.password.length >= 8 && /[~!@#$%^&*()_+|<>?:{}]/.test(this.form.password)) || '비밀번호를 특수문자 포함 8자 이상 작성해주세요.'
    },
    enable_login() {
      if (this.required_email() == true && this.required_password() == true) {
        return true
      } else {
        return false
      }
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