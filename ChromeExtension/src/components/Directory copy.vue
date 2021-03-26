<template>
  <div>
    <div class="directory">
      <div class="dir">root</div>
      <ul>
        <li>
          <div class="dir">폴더1</div>
          <ul>
            <li>
              <div class="dir">폴더1-2</div>                
              <ul>
                <li>
                  <div class="dir">폴더1-2-1</div>
                  <ul>

                  </ul>
                </li>
                <li>
                  <div class="dir">폴더1-2-2</div>
                  <ul>

                  </ul>
                </li>
                <li>
                  <div class="file">파일1-2-1</div>
                </li>
              </ul>
            </li>
            <li>
              <div class="file">파일1-1</div>
            </li>
          </ul>
        </li>
        <li>
          <div class="file">파일1</div>
        </li>
      </ul>  
    </div>
  </div>
</template>

<script>
export default {
  name: "Directory",
  props: {
    directoryData: Object,
  },
  data() {
    return {
    }
  },
  methods : {
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
    setDeleteMenu(target) {
      const deleteMenu = document.createElement('div')
      deleteMenu.setAttribute('class','context-menu-content')
      deleteMenu.innerText = '삭제하기'
      deleteMenu.addEventListener('click', (e) => {
        this.createModal('정말 삭제하시겠어요?',target,0)
        this.deleteContextMenu()
      })
      return deleteMenu
    },
    setRenameMenu(target) {
      const reNameMenu = document.createElement('div')
      reNameMenu.setAttribute('class','context-menu-content')
      reNameMenu.innerText = '이름변경'
      reNameMenu.addEventListener('click', (e) => {
        this.createModal('변경할 이름을 입력하세요',target,1)
        this.deleteContextMenu()
      })
      return reNameMenu
    },
    setUploadMenu(target) {
      const uploadMenu = document.createElement('div')
      uploadMenu.setAttribute('class','context-menu-content')
      uploadMenu.innerText = '파일추가'
      uploadMenu.addEventListener('click', (e) => {
        this.createModal('업로드할 파일을 선택하세요',target,2)
        this.deleteContextMenu()
      })
      return uploadMenu
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
    createModal(message,target=null,type=null) {
      const modalObj = {
        'nameInput' : document.createElement('input'),
      }
      const overlay = document.querySelector('#shadowElement').shadowRoot.querySelector('.overlay')
      const modalOverlay = document.createElement('div')
      modalOverlay.setAttribute('class','modal-overlay')
      const modal = document.createElement('div')
      modal.setAttribute('class','modal')

      const messageContainer = document.createElement('div')
      messageContainer.innerText = message
      modal.appendChild(messageContainer)

      if (type === 1) {
        modalObj.nameInput.setAttribute('class','modal-input')
        modalObj.nameInput.placeholder = '이름을 입력하세요'
        modalObj.nameInput.value = target.innerText
        modal.appendChild(modalObj.nameInput)
      }

      if (type === 2) {
        modalObj.nameInput.setAttribute('class','modal-input')
        modalObj.nameInput.type = 'file'
        modal.appendChild(modalObj.nameInput)
      }

      const btnContainer = document.createElement('div')
      btnContainer.setAttribute('class','btn-container')

      const confirmBtn = document.createElement('div')
      confirmBtn.innerText = "확인"
      confirmBtn.setAttribute('class','confirm-btn')
      confirmBtn.addEventListener('click', ()=>{
      
        if (type === 0) { // 파일 및 폴더 삭제
          target.parentNode.remove()
        }
        
        else if (type === 1) { // 파일 및 폴더 이름 변경
          // target의 현재 디렉토리에 중복 이름이 있는지 확인 
          const targetDirectory = target.parentNode.parentNode
          const targetClassName = target.className
          const ChildNodes = targetDirectory.children
          for (let i=0; i<ChildNodes.length;i++) {
            const child = ChildNodes[i].querySelector('div')
            if (child !== target && targetClassName === child.className && child.innerText === modalObj.nameInput.value)  {
              alert('같은 이름이 있습니다.')      
              return       
            }
          }
          target.innerText = modalObj.nameInput.value
        }

        else if (type === 2) { // 파일 업로드
          if (!modalObj.nameInput.files.length) {
            alert('파일을 선택해주세요')
            return
            }

          // 파일 전송
          // let formData = new FormData()
          // formData.append("data",modalObj.nameInput.files[0])

          const fileName = modalObj.nameInput.value
          const fileNameWithoutPath = fileName.substr(fileName.lastIndexOf('\\')+1)
          const ul = target.nextElementSibling
          const li = document.createElement('li')
          const liDiv = document.createElement('div')
          liDiv.setAttribute('class','file')
          liDiv.innerText = fileNameWithoutPath
          liDiv.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            this.createContextMenu(e.clientX,e.clientY,e.target,'file');
          })
          li.appendChild(liDiv)
          ul.appendChild(li) 
        }
        modalOverlay.remove()
        modal.remove()
      })
        
      btnContainer.appendChild(confirmBtn)

      const cancelBtn = document.createElement('div')
      cancelBtn.innerText = "취소"
      cancelBtn.setAttribute('class','cancel-btn')
      cancelBtn.addEventListener('click',() => {
        modalOverlay.remove()
        modal.remove()
      })
      btnContainer.appendChild(cancelBtn)

      modal.appendChild(btnContainer)

      modalOverlay.addEventListener('click',()=>{
        modalOverlay.remove()
        modal.remove()
      })
      overlay.appendChild(modalOverlay)
      overlay.appendChild(modal)
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
    }
  },
  mounted() {
    this.addClassToUl()
    this.addEventToDir()
  }
};
</script>

