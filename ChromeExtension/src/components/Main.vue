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
              <a href="#" class="dir">폴더1-2</a>                
              <ul>
                <li>
                  <a href="#" class="dir">폴더1-2-1</a>
                  <ul>

                  </ul>
                </li>
                <li>
                  <a href="#" class="dir">폴더1-2-2</a>
                  <ul>

                  </ul>
                </li>
                <li>
                  <a href="#" class="file">파일1-2-1</a>
                </li>
              </ul>
            </li>
            <li>
              <a href="#" class="file">파일1-1</a>
            </li>
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
    },
    addClassToUl() {
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      const uls = directory.querySelectorAll('ul')
      for (let idx=0; idx<uls.length; idx++) {
        uls[idx].classList.add('closed')
      }
    },
    addEventToDir() {
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      const dirs = directory.querySelectorAll('.dir')
      const files = directory.querySelectorAll('.file')
      for (let idx=0; idx<dirs.length; idx++) {
        const ul = dirs[idx].nextElementSibling
        dirs[idx].addEventListener('click', () => {
          ul.classList.toggle('closed')
        })
        dirs[idx].addEventListener('contextmenu', (e) => {
          e.preventDefault();
          this.createContextMenu(e.clientX,e.clientY,e.target,'dir');
        })
      }
      for (let idx=0; idx<files.length; idx++) {
        files[idx].addEventListener('contextmenu', (e) => {
          e.preventDefault();
          this.createContextMenu(e.clientX,e.clientY,e.target,'file');
        })
      }
    },
    setUploadMenu(target) {
      const uploadMenu = document.createElement('div')
      uploadMenu.setAttribute('class','context-menu-content')
      uploadMenu.innerText = '파일추가'
      uploadMenu.addEventListener('click', (e) => {
        console.log(e,target)
        this.deleteContextMenu()
      })
      return uploadMenu
    },
    setDeleteMenu(target) {
      const deleteMenu = document.createElement('div')
      deleteMenu.setAttribute('class','context-menu-content')
      deleteMenu.innerText = '삭제하기'
      deleteMenu.addEventListener('click', (e) => {
        if (confirm("정말 삭제하시겠어요?")) {
          const ul = target.nextElementSibling
          if (ul) {
            ul.innerHTML = '';
            ul.remove()
          }
          target.remove()
          this.deleteContextMenu()
        }
        else {
          this.deleteContextMenu()
        }
      })
      return deleteMenu
    },
    setRenameMenu(target) {
      const reNameMenu = document.createElement('div')
      reNameMenu.setAttribute('class','context-menu-content')
      reNameMenu.innerText = '이름변경'
      reNameMenu.addEventListener('click', (e) => {
        console.log(e,target)
        this.deleteContextMenu()
      })
      return reNameMenu
    },
    setDownloadMenu(target) {
      const downloadMenu = document.createElement('div')
      downloadMenu.setAttribute('class','context-menu-content')
      downloadMenu.innerText = '다운로드'
      downloadMenu.addEventListener('click', (e) => {
        console.log(e,target)
        this.deleteContextMenu()
      })
      return downloadMenu
    },
    deleteContextMenu() {
      const contextMenuOverlay = document.querySelector('#shadowElement').shadowRoot.querySelector('.context-menu-overlay')
      const contextMenu = document.querySelector('#shadowElement').shadowRoot.querySelector('.context-menu')
      if (contextMenuOverlay) {
        contextMenuOverlay.remove()
      }
      if (contextMenu) {
        contextMenu.remove()
      }
    },
    createContextMenu(x,y,target,targetType) {
      const overlay = document.querySelector('#shadowElement').shadowRoot.querySelector('.overlay')
      const contextMenu = document.createElement('div')
      const contextMenuOverlay = document.createElement('div')

      contextMenuOverlay.setAttribute('class','context-menu-overlay')
      contextMenuOverlay.addEventListener('click',()=>{
        this.deleteContextMenu()
      })
      contextMenuOverlay.addEventListener('contextmenu',(e)=>{
        e.preventDefault();
        this.deleteContextMenu()
      })

      contextMenu.setAttribute('class','context-menu')
      contextMenu.style.left = `${x}px`
      contextMenu.style.top =  `${y}px`
      

      contextMenu.appendChild(this.setDeleteMenu(target))
      contextMenu.appendChild(this.setRenameMenu(target))
      contextMenu.appendChild(this.setDownloadMenu(target))

      if (targetType === 'dir') {
        contextMenu.appendChild(this.setUploadMenu(target))
      }
      overlay.appendChild(contextMenuOverlay)
      overlay.appendChild(contextMenu)
      console.log(x,y,target)
    }
  },
  mounted() {
    this.addClassToUl()
    this.addEventToDir()
  }
};
</script>

