<template>
  <div>
    <h2 style="text-align: left; margin: 1rem 0;">비밀번호변경</h2>
      <v-card style="max-width: 60%;">
        <v-container fluid>
          <v-row>
            <v-col cols="5">
              <v-subheader>현재 비밀번호</v-subheader>
            </v-col>
            <v-col cols="7">
              <v-text-field
                solo flat dense outlined required validate-on-blur
                type="password"
                label="현재 비밀번호"
                v-model="currentPassword"
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="5">
              <v-subheader>새 비밀번호</v-subheader>
            </v-col>
            <v-col cols="7">
              <v-text-field
                solo flat dense outlined required
                type="password"
                label="비밀번호"
                v-model="newPassword"
                :rules="[required_password, required_new]"
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="5">
              <v-subheader>비밀번호 확인</v-subheader>
            </v-col>
            <v-col cols="7">
              <v-text-field
                solo flat dense outlined required
                type="password"
                label="비밀번호 확인"
                v-model="newPasswordConfirm"
                :rules="[required_passwordconfirm]"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col offset="4" cols="4">
              <v-btn :disabled="!enable" class="color-box"
                style="font-size: 1.3rem; font-weight: bold;"
                @click="changePassword"
              >비밀번호 변경</v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
  </div>
</template>

<script>
export default {
  name: 'PasswordChange',
  data() {
    return {
      currentPassword: '',
      newPassword: '',
      newPasswordConfirm: '',
    }
  },
  computed: {
    required_new() {
      return () => (this.currentPassword != this.newPassword) || '다른 비밀번호를 입력해주세요.'
    },
    required_password() {
      return () => (this.newPassword.length >= 8 && /[~!@#$%^&*()_+|<>?:{}]/.test(this.newPassword)) || '비밀번호를 특수문자 포함 8자 이상 작성해주세요.'
    },
    required_passwordconfirm() {
      return () => (this.newPassword == this.newPasswordConfirm) || '비밀번호가 일치하지 않습니다.'
    },
    enable() {
      if (this.required_new() == true && this.required_password() == true && this.required_passwordconfirm() == true) {
        return true
      } else {
        return false
      }
    },
  },
  methods: {
    changePassword() {
      // axios
      // goto login page
      this.$message({
        type: 'success',
        message: '비밀번호가 변경되었습니다.'
      })
      this.$store.dispatch('LOGOUT')
      this.$router.push({ name: 'Login' })
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
</style>