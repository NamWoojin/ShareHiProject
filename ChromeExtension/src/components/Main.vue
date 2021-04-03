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
        <button class="nav-item-btn" id="device-click" @click="onClickDevice">디바이스({{deviceCnt}})</button>
        <div v-if="devices" class="nav-item-device">
        </div >
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
    <Directory :directoryData="directoryData" :deviceChanged="deviceChanged" :deviceRemoved="deviceRemoved" :rootPath="rootPath" ref="directory" v-else />
  </div>
</template>

<script>
import Directory from "@/components/Directory.vue";
import SearchResult from "@/components/SearchResult.vue";
import  {test} from "@/assets/api/test.js";
// import  {getOnlineDevice} from "@/assets/api/user.js";
export default {
  name: "Main",
  components: { Directory,SearchResult },
  data() {
    return {
      deviceCnt : 0,
      searchFlag : false,
      searchInputValue : '',
      devices : [],
      rootPath : "",
      deviceChanged : 0,
      deviceRemoved  :0,
      currentDeviceId : '',
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
  mounted(){
    this.$socket.on(1050,(data) => {
      data = JSON.parse(data)
      console.log('-------------this.$socket.on(1050)-------------')
      console.log('this.$socket.on(1050) data')
      console.log(data)
      this.deviceCnt = data.devices.length
      let deviceCheckFlag = false
      for (let i=0;i<data.devices.length;i++) {
        const device = data.devices[i]
        if (device.id === this.currentDeviceId) {
          deviceCheckFlag = true
          break
        }
      }
      if (!deviceCheckFlag) {
        this.deviceRemoved += 1
      }
      const deviceBtn = document.querySelector('#shadowElement').shadowRoot.querySelector('#device-click')
      const navItem = this.findNavitem(deviceBtn.parentNode)
      navItem.innerHTML = ''
      if (!navItem) {
        alert('오류가 있습니다!')
        return
      }
      if(!data.devices || !data.devices.length) {
        navItem.classList.remove('nav-item-display')
        return
      }
      this.createNavcontent(data.devices,navItem)
    })
    this.$socket.on(1070,(data)=>{
      data = JSON.parse(data)
      console.log('-------------this.$socket.on(1070)-------------')
      console.log('this.$socket.on(1070) data')
      console.log(data)
      this.deviceChanged += 1;
      this.rootPath = data.path
    })
    this.$socket.emit(1050)
  },
  methods : {

    onClickDevice() {
      console.log('-------------this.$socket.emit(1050)-------------')
      const deviceBtn = document.querySelector('#shadowElement').shadowRoot.querySelector('#device-click')
      const navItem = this.findNavitem(deviceBtn.parentNode)
      navItem.classList.toggle('nav-item-display')
      navItem.innerHTML = ''
      this.$socket.emit(1050)
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
    },
    onClicktest() {
      test();
    },
    findNavitem(pNode) {
      for (let i=0;i<pNode.childNodes.length;i++) {
        if(pNode.childNodes[i].classList.contains('nav-item-device')) {
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
        contentDiv.innerText=device.id
        const data = {id : device.id}
        contentDiv.addEventListener('click',()=>{
          console.log('-------------this.$socket.emit(1070, JSON.stringify(data))-------------')
          this.$socket.emit(1070, JSON.stringify(data))
          const deviceBtn = document.querySelector('#shadowElement').shadowRoot.querySelector('#device-click')
          const navItem = this.findNavitem(deviceBtn.parentNode)
          this.currentDeviceId = device.id
          navItem.classList.toggle('nav-item-display')
        })
        navItem.appendChild(contentDiv)
      }
    }
  }
};
</script>

