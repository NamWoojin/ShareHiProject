<template>
  <div>
    <nav class="nav">
      <input type="text" @keyup="searchInputChanged" @keydown="searchInputChanged" :class="[searchFlag ? 'show search-input' : 'hidden']">
      <div class="nav-item" >
        <button class="nav-item-btn">알림</button>
        <div v-if="true" class="nav-item-content">
          <a href="#">Alert item1</a>
          <a href="#">Alert item2</a>
          <a href="#">Alert item3</a>
          <a href="#">Alert item4</a>
        </div>
        <div v-else>
          알림이 없습니다
        </div>
      </div>
      <div class="nav-item" >
        <button class="nav-item-btn">디바이스</button>
        <div v-if="true" class="nav-item-content">
          <a href="#">Device1</a>
          <a href="#">Device2</a>
          <a href="#">Device3</a>
          <a href="#">Device4</a>
        </div>
        <div v-else>
          알림이 없습니다
        </div>
      </div>
      <div class="nav-item" >
        <button class="nav-item-btn" @click="onClickSearch">🔍</button>
      </div>
      <div class="nav-item" >
        <button class="nav-item-btn">더 보기</button>
        <div class="nav-item-content nav-item-last">
          <a href="#" @click="onClickOpen">모든 폴더 열기</a>
          <a href="#" @click="onClickClose">모든 폴더 닫기</a>
          <a href="#">계정설정</a>
          <a href="#" @click="onClickLogout">로그아웃</a>
        </div>
      </div>
    </nav>
    <SearchResult :directoryData="directoryData" v-if="searchFlag" />
    <Directory :directoryData="directoryData" ref="directory" v-else />
  </div>
</template>

<script>
import Directory from "@/components/Directory.vue";
import SearchResult from "@/components/SearchResult.vue";

export default {
  name: "Main",
  components: { Directory,SearchResult },
  data() {
    return {
      searchFlag : false,
      searchInputValue : '',
      directoryData : {
        "name":"0",
        "path":"\/storage\/emulated\/0",
        "directory":[
          {
            "name":"Music",
            "path":"\/storage\/emulated\/0\/Music",
            "type":"folder"
          },
          {
            "name":"Podcasts",
            "path":"\/storage\/emulated\/0\/Podcasts",
            "type":"folder"
          },
          {
          "name":"Ringtones",
          "path":"\/storage\/emulated\/0\/Ringtones",
          "type":"folder"
          },
          {
          "name":"Alarms",
          "path":"\/storage\/emulated\/0\/Alarms",
          "type":"folder"
          },
          {
          "name":"Notifications",
          "path":"\/storage\/emulated\/0\/Notifications",
          "type":"folder"
          },
          {
          "name":"Pictures",
          "path":"\/storage\/emulated\/0\/Pictures",
          "type":"folder"
          },
          {
          "name":"Movies",
          "path":"\/storage\/emulated\/0\/Movies",
          "type":"folder"
          },
          {
          "name":"Download",
          "path":"\/storage\/emulated\/0\/Download",
          "type":"folder"
          },
          {
          "name":"DCIM",
          "path":"\/storage\/emulated\/0\/DCIM",
          "type":"folder"
          },
          {
          "name":"Android",
          "path":"\/storage\/emulated\/0\/Android",
          "type":"folder"
          }
        ]
      } 
    }
  },
  methods : {
    onClickSearch() {
      this.searchFlag = !this.searchFlag
    },
    onClickLogout() {
      alert("logout")
      this.$emit("onClickLogout",false)
    },
    onClickOpen() {
      this.$refs.directory.onClickOpenAllDir()
    },
    onClickClose() {
      this.$refs.directory.onClickCloseAllDir()
    },
    searchInputChanged(e) {
      e.stopPropagation();
      console.log(e.target.value)
    },
  }
};
</script>

