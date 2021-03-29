<template>
  <div>
    <input type="file" @change="fileUploadTest">
    <div class="directory">
    </div>
  </div>
</template>

<script>
import io from 'socket.io-client';

const config = {
  host: 'http://j4f001.p.ssafy.io:9002',
  uploadedSize: 0,
  uploadTotalSize: 0,
  savePath: './',
  saveName: 'sample',
  saveExt: '.mp4',
  saveFile: '',
  filename: '',
  downLoadSize: 0,
  downLoadTotalSize: 0,
}

const file = {
  name : '',
  data : '',
}

const socket = io.connect(config.host, { transports: ['websocket'] });

socket.on('connect', () => {
  if (socket.connected) console.log('서버로 성공적으로 연결되었습니다 : ' + config.host);
  else console.log('서버의 연결이 끊겼습니다 : ' + config.host);
});

socket.on(7000, (data) => {
  console.log('7000')
  data = JSON.parse(data)
  console.log(data)
  const rawData = new ArrayBuffer()
  const reader = new FileReader()
  // const slice = file.data.slice(0,100000)
  // console.log('slice',file.data)
  // reader.readAsArrayBuffer(slice)
  reader.onload = (e) => {
    rawData = e.target.result;
    console.log('rawData',rawData)
    // const arrayBuffer = reader.result
    // socket.emit(7001,arrayBuffer)
    socket.emit(7001,rawData)
  }
  reader.readAsArrayBuffer(file.data)
})

socket.on(4000, (data) => {
  console.log('4000')
  console.log(file)
  data = JSON.parse(data)
  // if (data.name === file.name) {
  //   const reader = new FileReader()
  //   const slice = file.data.slice(0,100000)
  //   reader.readAsArrayBuffer(slice)
  //   reader.onload = () => {
  //     const arrayBuffer = reader.result
  //     socket.emit(7001,arrayBuffer)
  //   }
  // }
})

socket.on(7001,(data) => {
  console.log('7001')
  console.log(data)
})

export default {
  name: "Directory",
  props: {
    directoryData: Object,
  },
  data() {
    return {
      file : '',
    }
  },
  mounted () {
    this.rootDataParsing()
  },
  methods : {
    fileUploadTest(e) {
      console.log(e.target)
      const fileName = e.target.value
      const fileNameWithoutPath = fileName.substr(fileName.lastIndexOf('\\')+1)
        // 'path' : fileName.substr(0,fileName.lastIndexOf('\\')+1),
      // const file = e.target.files[0],
      const fileData = {
        'path' : './',
        'name' : fileNameWithoutPath.split('.')[0],
        'ext' : fileNameWithoutPath.split('.')[1],
        'size' : e.target.files[0].size,
      }
      file.data = e.target.files[0]
      file.name = fileNameWithoutPath.split('.')[0]
      console.log('fileData', fileData)
      socket.emit(7000, JSON.stringify(fileData));
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
          const ul = this.elementSetting('ul')
          folderDiv.innerText = folder.name
          folderDiv.addEventListener('click', () => {
            ul.classList.toggle('closed')
            // path에 대해서 하위 내용 구조 통신하여 받는거 추가하기
            
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
  }
}
</script>

