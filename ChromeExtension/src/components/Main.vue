<template>
  <div>
    <nav class="nav">
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
        <button class="nav-item-btn">더 보기</button>
        <div class="nav-item-content nav-item-last">
            <a href="#" @click="onClickOpenAllDir">모든 폴더 열기</a>
            <a href="#" @click="onClickCloseAllDir">모든 폴더 닫기</a>
            <a href="#">계정설정</a>
            <a href="#" @click="onClickLogout">로그아웃</a>
        </div>
      </div>
    </nav>
    <div class="directory">
      <a href="#" class="dir">root</a>
      <ul>
        <li>
          <a href="#" class="dir">폴더1</a>
          <ul>
            <li>
              <a href="#" class="dir">폴더1-1</a>
              <ul>
                <li><a href="#" class="dir">폴더1-1-1</a></li>
                <li><a href="#" class="dir">폴더1-1-2</a></li>
                <li><a href="#" class="file">파일1-1-1</a></li>
              </ul>
            </li>
            <li>
              <a href="#" class="dir">폴더1-2</a>                
              <ul>
                <li><a href="#" class="dir">폴더1-2-1</a></li>
                <li><a href="#" class="dir">폴더1-2-2</a></li>
                <li><a href="#" class="file">파일1-2-1</a></li>
              </ul>
            </li>
            <li><a href="#" class="file">파일1-1</a></li>
          </ul>
        </li>
        <li>
          <a href="#" class="dir">폴더2</a>
          <ul>
            <li><a href="#" class="dir">폴더2-1</a></li>
            <li><a href="#" class="dir">폴더2-2</a></li>
            <li><a href="#" class="file">파일2-1</a></li>
          </ul>
        </li>
        <li>
          <a href="#" class="dir">폴더3</a>
          <ul>
            <li><a href="#" class="dir">폴더3-1</a></li>
            <li><a href="#" class="dir">폴더3-2</a></li>
            <li><a href="#" class="file">파일3-1</a></li>
          </ul>
        </li>
        <li>
          <a href="#" class="dir">폴더4</a>
          <ul>
            <li>
              <a href="#" class="dir">폴더4-1</a>
              <ul>
                <li>
                  <a href="#" class="dir">폴더4-1-1</a>
                  <ul>
                    <li><a href="#" class="dir">폴더4-1-1-1</a></li>
                    <li><a href="#" class="dir">폴더4-1-1-2</a></li>
                    <li><a href="#" class="dir">폴더4-1-1-3</a></li>
                  </ul>
                </li> 
                <li><a href="#" class="dir">폴더4-1-2</a></li> 
                <li><a href="#" class="dir">폴더4-1-3</a></li> 
                <li><a href="#" class="file">파일4-1-1</a></li> 
                <li><a href="#" class="file">파일4-1-2</a></li> 
                <li><a href="#" class="file">파일4-1-3</a></li> 
                <li><a href="#" class="file">파일4-1-4</a></li> 
              </ul>
            </li>
            <li><a href="#" class="dir">폴더4-2</a></li>
            <li><a href="#" class="file">파일4-1</a></li>
          </ul>
        </li>
        <li>
          <a href="#" class="file">파일1</a>
        </li>
      </ul>  
    </div>
  </div>
</template>

<script>
export default {
  name: "Main",
  data() {
    return {
    }
  },
  methods : {
    onClickLogout() {
      alert("logout")
      this.$emit("onClickLogout",false)
    },
    onClickOpenAllDir() {
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      const uls = directory.querySelectorAll('ul')
      for (let idx=0; idx<uls.length; idx++) {
        uls[idx].classList.remove('closed')
      } 
    },
    onClickCloseAllDir() {
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      const uls = directory.querySelectorAll('ul')
      for (let idx=0; idx<uls.length; idx++) {
        uls[idx].classList.add('closed')
      } 
    }
  },
  mounted() {
    const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")

    const uls = directory.querySelectorAll('ul')
    for (let idx=0; idx<uls.length; idx++) {
      uls[idx].classList.add('closed')
    } 

    const dirs = directory.querySelectorAll('.dir')
    for (let idx=0; idx<dirs.length; idx++) {
      const ul = dirs[idx].nextElementSibling
      if (ul) {
        dirs[idx].addEventListener('click', () => {
          ul.classList.toggle('closed')
        })
      }
    }

  }
};
</script>

