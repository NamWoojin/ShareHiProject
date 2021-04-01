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
        <button class="nav-item-btn" @click="onClickDevice">디바이스</button>
        <div v-if="devices" class="nav-item-content">
          <div>디바이스1</div>
        </div >
      </div>
      <div class="nav-item" >
        <button class="nav-item-btn" @click="onClickSearch">🔍</button>
      </div>
      <div class="nav-item" >
        <button class="more-btn nav-item-btn">더 보기</button>
        <div class="nav-item-content nav-item-last">
          <a href="#" @click="onClickOpen">모든 폴더 열기</a>
          <a href="#" @click="onClickClose">모든 폴더 닫기</a>
          <a href="#">계정설정</a>
          <a href="#" @click="onClickLogout">로그아웃</a>
        </div>
      </div>
    </nav>
    <SearchResult :directoryData="directoryData" v-if="searchFlag" />
    <Directory :socket="socket" :directoryData="directoryData" ref="directory" v-else />
  </div>
</template>

<script>
import Directory from "@/components/Directory.vue";
import SearchResult from "@/components/SearchResult.vue";
import  {test} from "@/assets/api/test.js";
import  {getOnlineDevice} from "@/assets/api/user.js";
export default {
  name: "Main",
  components: { Directory,SearchResult },
  props: {
    socket: Object,
  },
  data() {
    return {
      searchFlag : false,
      searchInputValue : '',
      devices : [],
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
    onClickDevice(e) {
      socket.on(1050)
      console.log('click target',e.target.parentNode)
      const clickTargetParent = e.target.parentNode
      // getOnlineDevice(26,
      // (res) => {
      //   if(!res.data.content.device) {
      //     alert('연결된 디바이스가 없습니다')
      //     return
      //   }
      //   this.devices = res.data.content.device
      //   console.log(res.data)
      //   const navItem = this.findNavitem(clickTargetParent)
      //   navItem.innerHTML = ''
      //   navItem.classList.toggle('nav-item-display')
      //   this.createNavcontent(res.data.content.device,navItem)
      // },
      // (err) => {
      //   console.log(err)
      //   alert('디바이스 목록 가져오기 오류!')
      // })
    },
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
    onClicktest() {
      test();
    },
    findNavitem(pNode) {
      for (let i=0;i<pNode.childNodes.length;i++) {
        if(pNode.childNodes[i].classList.contains('nav-item-content')) {
          console.log('find class name',pNode.childNodes[i])
          return pNode.childNodes[i]
        }
      }
      console.log('no class name')
      return null
    },
    createNavcontent(devices,navItem) {
      for (let i=0;i<devices.length;i++) {
        const device = devices[i]
        const contentDiv = document.createElement('div')
        console.log('contentDiv',contentDiv)
        console.dir('contentDiv',contentDiv)
        console.log('device.dev_name',device.dev_name)
        contentDiv.innerText=device.dev_name
        navItem.appendChild(contentDiv)
      }
    }
  }
};
</script>

