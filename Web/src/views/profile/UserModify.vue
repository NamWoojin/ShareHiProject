<template>
  <div>
    <h2 style="text-align: left; margin: 1rem 0;">회원정보수정</h2>
    <table class="simple_table">
      <tbody>
        <tr>
          <th scope="row">프로필 사진</th>
          <td>
            <div 
              v-if="member.mem_image == 'default.img'"
              style="position: relative"
              @click="changeImage"
            >
              <img 
                src="https://www.pphfoundation.ca/wp-content/uploads/2018/05/default-avatar.png"
                style="width: 120px; height: 120px;"
              >
              <v-icon 
                large 
                color="success"
                style="position: absolute; left: 90px;"
                
              >mdi-plus</v-icon>
            </div>
            <div v-else>
              <img :src="member.mem_image">
            </div>
          </td>
        </tr>
        <tr>
          <th scope="row">아이디(이메일)</th>
          <td><strong>{{member.mem_email}}</strong></td>
        </tr>
        <tr>
          <th scope="row">이름</th>
          <td><strong>{{member.mem_name}}</strong></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import { mapState } from 'vuex'

export default {
  name: 'UserModify',
  computed: {
    ...mapState([
      'member'
    ])
  },
  methods: {
    changeImage() {
      console.log('change image')
    }
  }
}
</script>

<style scoped>
  .simple_table { 
    width: 75%; 
    border-top: 2px solid #969696;
    border-bottom: 2px solid #969696;
    border-spacing: 1px;
  }
  
  .simple_table th { 
    width: 25%;
    padding: 20px; 
    background: #EEF1F8; 
    font-weight: bold; 
    text-align: left; 
    border-bottom: 1px solid #ddd;
    border-right: 1px solid #ddd;
  }

  .simple_table td { 
    padding: 15px; 
    border: none; 
    border-bottom: 1px solid #ddd;
    text-align: left; 
  }

  table {
    border-right: 1px solid #ddd;
  }

  input {
    height: 32px;
    width: 150px;
    margin-bottom: -1px;
    padding: 0 0 0 5px;
    border: 1px solid #ddd;
    font-size: 16px;
    color: #333;
  }

  button {
    margin: 0 10px;
    padding: 4px 8px;
    padding-bottom: 3px;
    border: 1px solid #999;
    color: #333;
    border-radius: 1px;
    box-shadow: 0 -2px 0 rgb(0 0 0 / 10%) inset;
    font-size: 14px;
  }

</style>