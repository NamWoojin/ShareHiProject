<template>
  <div>
    <div class="directory">
    </div>
    <div v-show="progress" class="progress">
      <div class="progress-filename">sdfsdf</div>
      <div class="progress-percent">%</div>
    </div>
  </div>
</template>

<script>

const file = {
  name : '',
  data : '',
}

export default {
  name: "Directory",
  props: {
    directoryData: Object,
  },
  data() {
    return {
      file : '',
      progress : false,
      percent : 0,
    }
  },
  mounted () {
    this.rootDataParsing();

    this.$socket.on(7000, (data) => {
      console.log('7000')
      data = JSON.parse(data)
      let size = file.data.size;
      let tmpfileSize = data.tmpfileSize;
      let CHUNK_SIZE = 64 * 1024;
      let start = tmpfileSize;
      let fileReader = new FileReader();

      let tmp;
      if(start + CHUNK_SIZE < size) {
        tmp = file.data.slice(start, start + CHUNK_SIZE);
        start += CHUNK_SIZE;
        fileReader.readAsArrayBuffer(tmp);
      } else {
        tmp = file.data.slice(start, size);
        start = size;
        fileReader.readAsArrayBuffer(tmp);
      }

      fileReader.onloadend = (e) => {
        this.$socket.emit(7001, e.target.result);

        if(start == size) return;
        if(start + CHUNK_SIZE < size) {
          tmp = file.data.slice(start, start + CHUNK_SIZE);
          start += CHUNK_SIZE;
          e.target.readAsArrayBuffer(tmp);
        } else {
          tmp = file.data.slice(start, size);
          start = size;
          e.target.readAsArrayBuffer(tmp);
        }
      };
    })

    this.$socket.on(4000, (data) => {
      console.log('4000 Nodevice')
      data = JSON.parse(data)
    })

    this.$socket.on(2000,(data) => {
      data = JSON.parse(data)
      console.log('2000 StorageResponse In Directory')
      console.log(data)
      console.log('')
      console.log(JSON.parse(data.data))
      console.log('')
    })
  },
  methods : {
    socketStorageTreeRequest(path,name) {
      const data = {
        path,
        name,
      }
      this.$socket.emit(2000,JSON.stringify(data))
    },
    socketFileUpload(target) {
      console.log(target)
      const fileName = target.value
      const fileNameWithoutPath = fileName.substr(fileName.lastIndexOf('\\')+1)
      const fileData = {
        'path' : './',
        'name' : fileNameWithoutPath.split('.')[0],
        'ext' : fileNameWithoutPath.split('.')[1],
        'size' : target.files[0].size,
      }
      file.data = target.files[0];
      file.name = fileNameWithoutPath.split('.')[0]
      console.log('emit 7000 fileData', fileData)
      console.log('emit 7000 socket', this.$socket)
      this.$socket.emit(7000, JSON.stringify(fileData));
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

    elementSetting(tagName,className=null) {
      const tag = document.createElement(tagName)
      if (className) {
        tag.setAttribute('class',className)
      }
      return tag
    },

    rootDataParsing() {
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      const rootData = this.directoryData
      if (!rootData) {
        console.log('noData',rootData)
        return
      }
      const folders = []
      const files = []
      for (let i=0;i<rootData.directory.length;i++) {
        const element = rootData.directory[i]
        if (element.type === 'folder') {
          folders.push(rootData.directory[i])
        }
        else {
          files.push(rootData.directory[i])
        }
      }
      const rootDiv = this.elementSetting('div','dir')
      rootDiv.innerText = "root"
      const rootUl = this.elementSetting('ul')
      rootDiv.addEventListener('click', () => {
          rootUl.classList.toggle('closed')
        })
      if (folders) {
        folders.forEach(folder => {
          const li = this.elementSetting('li')
          const folderDiv = this.elementSetting('div','dir')
          folderDiv.setAttribute('data-path', folder.path)
          const ul = this.elementSetting('ul')
          folderDiv.innerText = folder.name
          folderDiv.addEventListener('click', () => {
            ul.classList.toggle('closed')
            // path에 대해서 하위 내용 구조 통신하여 받는거 추가하기
            // this.socketStorageTreeRequest(path,name)
            
          })
          folderDiv.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            this.createContextMenu(e.clientX,e.clientY,e.target,'dir');
          })
          li.appendChild(folderDiv)
          li.appendChild(ul)
          rootUl.appendChild(li)        
        });
      }
      if (files) {
        files.forEach(file => {
          const li = this.elementSetting('li')
          const fileDiv = this.elementSetting('div','file')
          fileDiv.innerText = file.name
          fileDiv.setAttribute('data-path', file.path)
          fileDiv.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            this.createContextMenu(e.clientX,e.clientY,e.target,'file');
          })
          li.appendChild(fileDiv)
          li.appendChild(ul)
          rootUl.appendChild(li)        
        });
      }
      directory.appendChild(rootDiv)
      directory.appendChild(rootUl)
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
      modalObj.nameInput.addEventListener('keydown',(e) => {
        e.stopPropagation()
      })
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
          const fileName = modalObj.nameInput.value
          const fileNameWithoutPath = fileName.substr(fileName.lastIndexOf('\\')+1)
          file.data = modalObj.nameInput.files[0]
          file.name = fileNameWithoutPath.split('.')[0]
          const ul = target.nextElementSibling
          const li = document.createElement('li')
          const liDiv = document.createElement('div')
          liDiv.setAttribute('class','file')  
          liDiv.innerText = fileNameWithoutPath
          liDiv.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            this.createContextMenu(e.clientX,e.clientY,e.target,'file');
          })
          const progressFileName = document.querySelector('#shadowElement').shadowRoot.querySelector(".progress-filename")
          const progressPercent = document.querySelector('#shadowElement').shadowRoot.querySelector(".progress-percent")
          this.socketFileUpload(modalObj.nameInput);
          this.$socket.on(7001,(data) => {
            console.log('7001 Progress Bar')
            data = JSON.parse(data)
            if (data.percent === 100) {
              this.progress = false
              progressPercent.innerText = data.percent + '%'
              alert('업로드 완료!')
              li.appendChild(liDiv)
              ul.appendChild(li) 
            }
            else {
              this.progress = true
              progressFileName.innerText = file.name + ' : '
              progressPercent.innerText = data.percent + '%'
            }
          })
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
  }
}
</script>
