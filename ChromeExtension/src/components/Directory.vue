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
    rootPath : String,
    deviceChanged : Number,
    deviceRemoved : Number,
  },
  data() {
    return {
      file : '',
      progress : false,
      percent : 0,
      saveFile: '',
      saveFileLengthOrigin: '',
      saveFileLength: '',
      blobArray: [],
      downloadPercentage : 0,
      downloadFile : {
        name : '',
        ext : '',
      }
    }
  },
  watch : {
    deviceChanged() {
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      directory.innerHTML = ''
      console.log(`-------------this.$socket.emit(2000)-------------`)
      console.log(`this.$socket.emit(2000) data`)
      console.log({path: this.rootPath})
      this.$socket.emit(2000, JSON.stringify({
        path: this.rootPath
      }))
    },
    deviceRemoved() {
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      directory.innerHTML = ''
    }
  },
  mounted () {
    this.$socket.on(7000, (data) => {
      data = JSON.parse(data)
      console.log('-------------this.$socket.on(7000)-------------')
      console.log('this.$socket.on(7000) data')
      console.log(data)
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
    this.$socket.on(8000, (data) => {
      const progressFileName = document.querySelector('#shadowElement').shadowRoot.querySelector(".progress-filename")
      const progressPercent = document.querySelector('#shadowElement').shadowRoot.querySelector(".progress-percent")
      this.blobArray.push(new Blob([data]))
      this.saveFileLength = this.saveFileLength - data.byteLength
      this.downloadPercentage = Math.floor(((this.saveFileLengthOrigin - this.saveFileLength)/this.saveFileLengthOrigin)*100)
      if (this.saveFileLength == 0) {
        this.downloadPercentage = 100
        console.log('download complete')
        const a = document.createElement('a');
        a.download = this.downloadFile.name + this.downloadFile.ext
        let blob = new Blob(this.blobArray)
        a.href = URL.createObjectURL(blob)
        a.style.display = 'none';
        a.addEventListener('click', (e) => {
          setTimeout(() => URL.revokeObjectURL(a.href), 30 * 1000);
        });
        a.click();
        this.blobArray = [],
        this.saveFileLength = 0
        this.downloadFile.name = ''
        this.downloadFile.ext = ''
      }
      if (this.downloadPercentage === 100) {
        this.progress = false
        progressPercent.innerText = this.downloadPercentage + '%' 
      }
      else {
        this.progress = true
        progressFileName.innerText = this.downloadFile.name + this.downloadFile.ext + ' : '
        progressPercent.innerText = this.downloadPercentage + '%'
      }
    })
    this.$socket.on(8001, (data) => {
      console.log('this.$socket.on(8001)')
      console.log('8001 data')
      console.log(JSON.parse(data))
      this.saveFileLength = JSON.parse(data).size
      this.saveFileLengthOrigin = JSON.parse(data).size
      this.$socket.emit(8001)
    })
    this.$socket.on(4000, (data) => {
      console.log('4000 Nodevice')
      data = JSON.parse(data)
    })

    this.$socket.on(2000,(data) => {
      data = JSON.parse(data)
      console.log('-------------this.$socket.on(2000)-------------')
      console.log('this.$socket.on(2000) data')
      console.log(JSON.parse(data.data))
      const directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      if (directory.childNodes.length === 0) {
        this.DataParsing(data.data,true)
      }
      else {
        this.DataParsing(data.data,false)
      }
    })
  },
  methods : {
    socketStorageTreeRequest(path,name) {
      const data = {
        path,
        name,
      }
      console.log('-------------this.$socket.emit(2000,JSON.stringify(data))-------------')
      console.log('this.$socket.emit(2000) data')
      console.log(data)
      this.$socket.emit(2000,JSON.stringify(data))
    },
    socketFileUpload(target,clickedFolder) {
      const fileName = target.value
      const fileNameWithoutPath = fileName.substr(fileName.lastIndexOf('\\')+1)
      const fileData = {
        'path' : clickedFolder.getAttribute("data-path"),
        'name' : fileNameWithoutPath.split('.')[0],
        'ext' : '.' + fileNameWithoutPath.split('.')[1],
        'size' : target.files[0].size,
      }
      file.data = target.files[0];
      file.name = fileNameWithoutPath.split('.')[0]
      console.log('this.$socket.emit(7000, JSON.stringify(fileData));')
      console.log('this.$socket.emit(7000) fileData :')
      console.log(fileData)
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

    DataParsing(data,isRoot) {
      data = JSON.parse(data)
      let directory
      if (isRoot) {
        directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory")
      }
      else {
        directory = document.querySelector('#shadowElement').shadowRoot.querySelector(".directory").querySelector(`[data-path="${data.path}"]`) 
      }
      if (!data) {
        console.log('noData',data)
        return
      }
      const folders = []
      const files = []
      for (let i=0;i<data.directory.length;i++) {
        const element = data.directory[i]
        if (element.type === 'folder') {
          folders.push(data.directory[i])
        }
        else {
          files.push(data.directory[i])
        }
      }
      let curDiv
      let curUl
      if (isRoot) {
        curDiv = this.elementSetting('div','dir')
        curDiv.setAttribute('data-path', data.path)
        curDiv.innerText = data.name
        curUl = this.elementSetting('ul')
        curDiv.addEventListener('click', () => {
            curUl.classList.toggle('closed')
        })
        curDiv.addEventListener('contextmenu', (e) => {
          e.preventDefault();
          this.createContextMenu(e.clientX,e.clientY,e.target,'dir');
        })
      }
      else {
        curDiv = directory
        curUl = directory.nextElementSibling
        curUl.innerHTML = ''
      }
      if (folders) {
        folders.forEach(folder => {
          const li = this.elementSetting('li')
          const folderDiv = this.elementSetting('div','dir')
          folderDiv.setAttribute('data-path', folder.path)
          const ul = this.elementSetting('ul','closed')
          folderDiv.innerText = folder.name
          folderDiv.addEventListener('click', () => {
            ul.classList.toggle('closed')
            // path에 대해서 하위 내용 구조 통신하여 받는거 추가하기
            this.socketStorageTreeRequest(folder.path,folder.name)
            
          })
          folderDiv.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            this.createContextMenu(e.clientX,e.clientY,e.target,'dir');
          })
          li.appendChild(folderDiv)
          li.appendChild(ul)
          curUl.appendChild(li)        
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
          curUl.appendChild(li)        
        });
      }
      if (isRoot) {
        directory.appendChild(curDiv)
        directory.appendChild(curUl)
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
    setAddNewFolder(target) {
      const addNewFolderMenu = document.createElement('div')
      addNewFolderMenu.setAttribute('class','context-menu-content')
      addNewFolderMenu.innerText = '폴더추가'
      addNewFolderMenu.addEventListener('click', (e) => {
        let folderName = '새 폴더'
        let folderNumber = 1
        const ul = target.nextElementSibling
        const ChildNodes = ul.children
        while (true) {
          let flag = true
          for (let i=0; i<ChildNodes.length;i++) {
            const child = ChildNodes[i].querySelector('.dir')
            if (child && child.innerText === folderName)  {
              flag = false
              folderName = '새 폴더' + ' (' + folderNumber + ')'
              folderNumber += 1
              break
            }
          }
          if (flag) {
            break
          }
        }
        console.log('this.$socket.emit(2003)')
        const data = {
          path : target.getAttribute("data-path"),
          name : folderName
          }
        console.log({path : target.getAttribute("data-path"),name : folderName})
        this.$socket.emit(2003, JSON.stringify(data))
        this.$socket.emit(2000, JSON.stringify(data))
        this.deleteContextMenu()
      })
      return addNewFolderMenu
    },
    setDownloadMenu(target) {
      const downloadMenu = document.createElement('div')
      downloadMenu.setAttribute('class','context-menu-content')
      downloadMenu.innerText = '다운로드'
      downloadMenu.addEventListener('click', (e) => {
        const dataPath = target.getAttribute("data-path")
        const path = dataPath.substr(0,dataPath.lastIndexOf('\/'))
        const name = dataPath.substr(dataPath.lastIndexOf('\/')+1).split('.')[0]
        const ext = '.' + dataPath.substr(dataPath.lastIndexOf('\/')+1).split('.')[1]
        console.log('this.$socket.emit(8000)')
        this.downloadFile.name = name
        this.downloadFile.ext = ext
        this.$socket.emit(8000, JSON.stringify({
          path,
          name,
          ext,
        }))
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
          const dataPath = target.getAttribute("data-path")
          const data = {
            path: dataPath.substr(0,dataPath.lastIndexOf('\/')),
            name: dataPath.substr(dataPath.lastIndexOf('\/')+1),
          }
          console.log('this.$socket.emit(2002)')
          console.log('this.$socket.emit(2002) data')
          console.log(data)
          this.$socket.emit(2002, JSON.stringify(data))
          this.$socket.emit(2000, JSON.stringify(data))
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
          console.log('target')
          console.log(target)
          const dataPath = target.getAttribute("data-path")
          console.log('this.$socket.emit(2001)')
          console.log('this.$socket.emit(2001) data')
          console.log({
              path: dataPath.substr(0,dataPath.lastIndexOf('\/')),
              name: dataPath.substr(dataPath.lastIndexOf('\/')+1),
              newName: modalObj.nameInput.value
            })
          this.$socket.emit(2001, JSON.stringify({
              path: dataPath.substr(0,dataPath.lastIndexOf('\/')),
              name: dataPath.substr(dataPath.lastIndexOf('\/')+1),
              newName: modalObj.nameInput.value
            }))
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
          this.socketFileUpload(modalObj.nameInput,target);
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
        contextMenu.appendChild(this.setAddNewFolder(target))
      }
      overlay.appendChild(contextMenuOverlay)
      overlay.appendChild(contextMenu)
    }
  }
}
</script>
