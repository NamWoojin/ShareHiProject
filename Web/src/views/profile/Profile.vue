<template>
  <el-container>
    <el-aside>
      <p>
        <router-link :to="{ name: 'UserModify'}" style="text-decoration: none; color: black;">회원정보수정</router-link>
      </p>
      <p>
        <router-link :to="{ name: 'FollowerPage'}" style="text-decoration: none; color: black;">팔로워관리</router-link>
      </p>
      <p>
        <router-link :to="{ name: 'PasswordChange'}" style="text-decoration: none; color: black;">비밀번호변경</router-link>
      </p>
      <p>
        <router-link :to="{ name: 'Signout'}" style="text-decoration: none; color: black;">회원탈퇴</router-link>
      </p>
    </el-aside>
    <el-container>
      <el-main>
        <router-view />
      </el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Profile',
}
</script>

<style>

</style>