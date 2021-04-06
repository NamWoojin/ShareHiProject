<template>
  <div>
    <div id="window" class="small-window">
      <v-container>
        <v-row>
          <v-col cols="12" style="border: 1px solid black; border-bottom: none; margin: 0 0 0;" class="headercolor">
            <div 
              id="windowheader" 
              :class="{ cursor: !browsersize }" 
              style="display: flex; justify-content: space-between"
              @mousedown="dragMouseDown"
              @dblclick="resize"
            >
              <span style="font-weight: bold;">{{device.name.split('-')[0]}}</span>
              <div style="display: flex; justify-content: flex-end;">
                <v-icon v-if="browsersize" style="cursor: pointer; margin: 0 10px;" @click="resize">mdi-note-multiple-outline</v-icon>
                <v-icon v-else style="cursor: pointer; margin: 0 10px;" @click="resize">mdi-border-all-variant</v-icon>

                <v-icon style="cursor: pointer;" @click="gotoDevicePage">mdi-close</v-icon>
              </div>
              
            </div>
          </v-col>
          <v-col cols="12" style="border: 1px solid black; border-bottom: none; margin: -1px 0; padding-top: 0; padding-bottom: 0;">
            <div style="display: flex; justify-content: space-between; align-items: center;" class="icon-container">
              <div style="display: flex;">

                <v-btn class="navicon" :disabled="!lastpath" @click="gotoLastpath">
                  <div style="width: 50px; height: 50px;">
                    <v-icon x-large style="margin-top: 5px !important">mdi-keyboard-backspace</v-icon>
                  </div>
                </v-btn>
                <v-btn class="navicon" @click="gotoOriginpath">
                  <div style="width: 50px; height: 50px;">
                    <v-icon x-large style="margin-top: 5px !important">mdi-home</v-icon>
                  </div>
                </v-btn>
              </div>
              <div style="position: relative; width: 50%; border: 1px solid; padding: 0.4rem; text-align: left;">
                <!-- <span style="font-size: 1.5rem;">{{customPath}}</span> -->
                  <v-icon style="margin-right: 1rem;">mdi-folder</v-icon>
                  <span v-for="(link, idx) in customPath" :key="idx">
                    <v-icon v-if="(idx != 0)">mdi-menu-right</v-icon>
                    <span class="link" style="cursor: pointer" @click="gotoSelectpath(idx)">{{link}}</span>
                  </span>
                  <v-icon
                    style="position: absolute; right: 15px; cursor: pointer"
                    @click="reloadThispath"
                  >mdi-reload</v-icon>
              </div>
              <div>
                <v-btn elevation="2" class="navicon" @click="createNewFolder">
                  <img
                    src="@/assets/folder.png"
                    class="navicon"
                  />
                </v-btn>
                    
                <v-btn class="navicon" @click="downloadFile" :disabled="!(selectitem.length == 1 && selectitem[0].type != 'folder')"
                >
                  <div style="width: 50px; height: 50px;">
                    <v-icon x-large style="margin-top: 5px !important">mdi-download</v-icon>
                  </div>
                </v-btn>

                <v-btn class="navicon" @click="uploadFile">
                  <div style="width: 50px; height: 50px;">
                    <v-icon x-large color="success" style="margin-top: 5px !important">mdi-plus</v-icon>
                  </div>
                </v-btn>
                <v-btn 
                  class="navicon" 
                  @click="remove"
                  :disabled="selectitem.length == 0"
                >
                  <div style="width: 50px; height: 50px;">
                    <v-icon x-large color="red" style="margin-top: 5px !important">mdi-close</v-icon>
                  </div>
                </v-btn>
              </div>
            </div>
          </v-col>
          <v-col
            cols="3"
            style="border: 1px solid black; margin: 0;"
          >
            <!-- <el-tree
              :data="data"
              
              :default-expanded-keys="[0]"
              node-key="id"
              :props="defaultProps"
              @node-click="nodeClick"
              :key="componentKey"
            >
            </el-tree> -->
            <v-treeview
              v-model="tree"
              :items="data"
              item-children="directory"
              open-on-click
              hoverable
              :open="initiallyOpen"
              :key="componentKey"
            >
              <!-- <template v-slot:append="{ item }">
                <span @click="selectFolder(item)">{{item.name}}</span>
              </template> -->
              <template v-slot:label="{ item }">
                <span @click="selectFolder(item)">{{item.name}}</span>
              </template>
              <template v-slot:prepend="{ item, open }">
                <v-icon @click="selectFolder(item)" v-if="item.type=='folder' && open && item.directory">
                  mdi-folder-open
                </v-icon>
                <v-icon @click="selectFolder(item)" v-else-if="item.type=='folder'">
                  mdi-folder
                </v-icon>
                <v-icon v-else-if="item.type=='jpg'">
                  mdi-image
                </v-icon>
                <v-icon v-else-if="item.type=='pdf'">
                  mdi-file-pdf
                </v-icon>
                <v-icon v-else-if="item.type=='mp4'">
                  mdi-movie
                </v-icon>
                <v-icon v-else>
                  mdi-file
                </v-icon>
              </template>
            </v-treeview>
          </v-col>
          <v-col 
            cols="9"
            style="border: 1px solid black; border-left-style: none;"
            @contextmenu="openMenu($event)"
            @click="clickOuter"
          >
            <div v-if="node.directory && node.directory.length > 0" style="display: flex; flex-wrap: wrap;" id="outter">
              <div v-for="child in node.directory" :key="child.id" id="outter">
                <div style="margin: 20px;">
                    <div 
                      @click="selectItem(child)" 
                      @contextmenu="selectRightclick(child);openMenu($event)"
                      class="boxitem"
                    >
                      <div v-if="child.type == 'folder'" @dblclick="openFolder(child)">
                        <div :class="{selectBox: selectitem.includes(child)}">
                          <div class="box">
                            <img
                              src="@/assets/folder.png"
                              width="100px"
                              height="100px"
                            />
                          </div>
                          <span class="child-name" :class="{showChild: selectitem.includes(child)}">{{child.name}}</span>
                        </div>
                      </div>

                      <div v-else-if="child.type == 'jpg'">
                        <div :class="{selectBox: selectitem.includes(child)}">
                          <div class="box">
                            <v-icon
                              size="100"
                            >mdi-image</v-icon>
                          </div>
                          <span class="child-name" :class="{showChild: selectitem.includes(child)}">{{child.name}}</span>
                        </div>
                      </div>

                      <div v-else-if="child.type == 'mp4'">
                        <div :class="{selectBox: selectitem.includes(child)}">
                          <div class="box">
                            <v-icon
                              size="100"
                            >mdi-movie</v-icon>
                          </div>
                          <span class="child-name" :class="{showChild: selectitem.includes(child)}">{{child.name}}</span>
                        </div>
                      </div>
                      <div v-else-if="child.type == 'pdf'">
                        <div :class="{selectBox: selectitem.includes(child)}">
                          <div class="box">
                            <v-icon
                              size="100"
                            >mdi-file-pdf</v-icon>
                          </div>
                          <span class="child-name" :class="{showChild: selectitem.includes(child)}">{{child.name}}</span>
                        </div>
                      </div>

                      <div v-else>
                        <div :class="{selectBox: selectitem.includes(child)}">
                          <div class="box">
                            <img
                              src="@/assets/file.png"
                              width="100px"
                              height="100px"
                            />
                          </div>
                          <span class="child-name" :class="{showChild: selectitem.includes(child)}">{{child.name}}</span>
                        </div>
                      </div>
                    </div>
                  
                </div>
              </div>
            </div>
            <div v-else>
              <div>
                이 폴더는 비어 있습니다.
              </div>
            </div>
            <v-file-input v-model="fileList" id="fileinput" style="display: none;" />
            <ul id="right-click-menu" ref="right" tabindex="-1" v-if="viewMenu" @blur="closeMenu" :style="{top: top, left: left}">
              <div v-if="selectitem.length > 1">
                <li @click="remove">삭제</li>
              </div>
              <div v-else-if="selectitem.length == 1">
                <li @click="remove">삭제</li>
                <li @click="editName">이름변경</li>
              </div>
              <div v-else>
                <li @click="createNewFolder">새 폴더</li>
                <li @click="uploadFile">파일 업로드</li>
              </div>
            </ul>
          </v-col>
        </v-row>
      </v-container>
    </div>
    <v-progress-circular
      style="z-index: 99; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);"
      v-if="percent < 100"
      :rotate="360"
      :size="250"
      :width="20"
      :value="percent"
      color="teal"
    >
      {{ percent }}%
    </v-progress-circular>
    <v-progress-circular
      v-if="saveFileLength > 0"
      style="position: absolute; top: 50%; left: 50%; z-index: 99; transform: translate(-50%, -50%);"
      color="teal"
      indeterminate
    >
    </v-progress-circular>
  </div>
</template>


<script>

/*eslint-disable*/
export default {
  name: 'FileBrowser',
  props: {
    device: Object
  },
  data() {
    return {
      positions: {
        clientX: undefined,
        clientY: undefined,
        movementX: 0,
        movementY: 0
      },
      componentKey: 0,
      selectitem: [],
      
      data: [
        {
          "name":"0",
          "path":"\/storage\/emulated\/0",
          "directory":[
          ]
        }
      ],
      defaultProps: {
        'children': 'directory',
        'label': 'name'
      },
      node: '',
      viewMenu: false,
      top: '0px',
      left: '0px',
      fileList: null,
      browsersize: false,
      selection: [],
      initiallyOpen: [0],
      active: [],
      tree: [],
      lastpath: '',
      currentpath: '',
      percent: 100,
      originalpath: '',
      saveFile: '',
      saveFileLengthOrigin: '',
      saveFileLength: '',
      blobArray: [],
      downloadFileNameCopy: '',
      uploadFileNameCopy: '',
      devices: [],
    };
  },
  methods: {
    // async selectWindowFile() {
    //   try {
    // const handle = await window.showSaveFilePicker({
    //   types: [{
    //     description: 'myfile',
    //     accept: {
    //       // Omitted
    //     },
    //   }],
    // })

    // const writable = await handle.createWritable();
    //   } catch (err) {
    //     console.log(err)
    //   }
    // },
    dragMouseDown(event) {
      event.preventDefault()
      this.positions.clientX = event.clientX
      this.positions.clientY = event.clientY
      document.onmousemove = this.elementDrag
      document.onmouseup = this.closeDragElement
    },
    elementDrag(event) {
      event.preventDefault()
      this.positions.movementX = this.positions.clientX - event.clientX
      this.positions.movementY = this.positions.clientY - event.clientY
      this.positions.clientX = event.clientX
      this.positions.clientY = event.clientY
      document.getElementById('window').style.top = (document.getElementById('window').offsetTop - this.positions.movementY) + 'px'
      document.getElementById('window').style.left = (document.getElementById('window').offsetLeft - this.positions.movementX) + 'px'
    },
    closeDragElement() {
      document.onmouseup = null
      document.onmousemove = null
    },
    nodeClick(node) {
      if (node.type == 'folder' || this.data[0] == node) {
        this.$socket.emit(2000, JSON.stringify({
          path: node.path
        }))
        this.node = node
        this.selectitem = []
      }
    },
    setMenu: function(top, left) {
      if (!this.browsersize) {
        let el = document.getElementById('window')
        this.top = top-60 - el.offsetTop + 'px';
        this.left = left - el.offsetLeft + 'px';
      } else {
        this.top = top-60 + 'px';
        this.left = left + 'px';
      }
      
    },

    closeMenu() {
      this.viewMenu = false;
    },
    openMenu: function(e) {
      this.viewMenu = true;
      this.$nextTick(function() {
        let el = document.getElementById('right-click-menu')
        el.focus();
        this.setMenu(e.y, e.x)
      }.bind(this))
      
      e.preventDefault();
    },
    selectItem(c) {
      let idx = this.selectitem.indexOf(c)
      if (idx == -1) {
        this.selectitem.push(c)
      } else {
        this.selectitem.splice(idx, 1)
      }
    },
    selectRightclick(c) {
      if (!this.selectitem.includes(c)) {
        this.selectitem.push(c)
      }
    },
    clickOuter(e) {
      if (e.target.id == 'outter') {
        this.selectitem = [];
      }
    },
    remove() {
      this.viewMenu = false;
      this.$confirm('정말로 삭제하시겠습니까?', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(() => {
          for(let i=0; i<this.selectitem.length; i++) {
            let idx = this.node.directory.indexOf(this.selectitem[i])
            if (idx > -1) this.node.directory.splice(idx, 1)
            this.$socket.emit(2002, JSON.stringify({
              path: this.node.path,
              name: this.selectitem[i].name
            }))
          }
          this.selectitem = [];
          this.$message({
            type: 'success',
            message: '삭제되었습니다.'
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '취소되었습니다.'
          })
        })
      
    },
    openFolder(c) {
      // this.node = c;
      // console.log('openFolder')
      this.$socket.emit(2000, JSON.stringify({
        path: c.path
      }))
      this.selectitem = [];
    },
    checkFolder: function(folderName) {
      if (this.node.directory) {
        for(let i=0; i<this.node.directory.length; i++) {
          if(this.node.directory[i].name == folderName) return false
        }
      }
      return true
    },
    createNewFolder() {
      let name = '새 폴더'
      
      if (!this.checkFolder(name)) {
        let number = 1
        while(!this.checkFolder(name + ' (' + number + ')')) {
          number++
        }
        name = name + ' (' + number + ')'
      }

      if (!this.node.directory) {
        this.node.directory = []
      }
      this.node.directory.push(
        {
          name: name,
          path: this.node.path + '\/' + name,
          type: 'folder'
        }
        )
      this.$socket.emit(2003, JSON.stringify({
        path: this.node.path,
        name: name
      }))
      this.componentKey++
      this.selectitem = [];
      this.viewMenu = false;
    },
    editName() {
      this.viewMenu = false
      this.$prompt('Please edit filename', 'Edit', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        inputValue: this.selectitem[0].name
      })
        .then((value) => {
          if (this.selectitem[0].name == value.value) {
            this.$message({
              type: 'info',
              message: '변경된 내용이 없습니다.'
            })
          } else if (this.checkFolder(value.value)) {
            this.$message({
              type: 'success',
              message: '수정되었습니다.'
            });
            // socket emit edit name
            console.log(this.selectitem[0].name)
            console.log(this.selectitem[0].path)
            this.$socket.emit(2001, JSON.stringify({
              path: this.node.path,
              name: this.selectitem[0].name,
              newName: value.value
            }))
            this.selectitem[0].path = this.node.path + '\/' + value.value;
            this.selectitem[0].name = value.value;
          } else {
            this.$message({
              type: 'info',
              message: '동일한 이름이 있습니다.'
            })
          }
      },
      )
        .catch(() => {
          this.$message({
            type: 'info',
            message: '취소되었습니다.'
          });       
      });
    },
    uploadFile() {
      // console.log(this.node)
      // console.log(this.node.path)
      this.viewMenu = false
      document.getElementById('fileinput').click()
    },
    resize() {
      this.browsersize = !this.browsersize
      document.getElementById('window').classList.toggle('small-window')
      document.getElementById('window').classList.toggle('max-window')
    },
    downloadFile() {
      // console.log('this is downloadFile')
      // let file = this.selectWindowFile();
      this.downloadFileNameCopy = this.selectitem[0].name
      this.$socket.emit(8000, JSON.stringify({
        path: this.node.path,
        name: this.selectitem[0].name.split('.')[0],
        ext: '.' + this.selectitem[0].name.split('.')[1]
      }))
      // this.$socket.emit(8000, JSON.stringify({
      //   path: '\/storage\/emulated\/0\/Download',
      //   name: '다운로드',
      //   ext: '.jpg'
      // }))
    },
    selectFolder(i) {
      // console.log(i)
      this.$socket.emit(2000, JSON.stringify({
        path: i.path
      }))
      // this.node = i
    },
    gotoLastpath() {
      this.$socket.emit(2000, JSON.stringify({
        path: this.lastpath
      }))
    },
    gotoOriginpath() {
      this.$socket.emit(2000, JSON.stringify({
        path: this.originalpath
      }))
    },
    gotoSelectpath(idx) {
      let selectPath = this.currentpath.split('\/').slice(0, this.originalSize + idx + 1).join('\/')
      this.$socket.emit(2000, JSON.stringify({
        path: selectPath
      }))
    },
    gotoDevicePage() {
      // this.$router.replace({ name: 'Main' })
      this.$router.push({ name: 'Main' })
      // this.$router.go(`http://localhost:8080`)
    },
    reloadThispath() {
      this.$socket.emit(2000, JSON.stringify({
        path: this.node.path
      }))
    }
  },
  mounted() {
    const data = {
      id: this.device.id
    }
    this.$socket.emit(1070, JSON.stringify(data))

    // this.node = this.data[0];
    this.devices.push(this.device)
  },
  watch: {
    fileList: function () {
      if (this.fileList.size > 0) {
        this.uploadFileNameCopy = this.fileList.name
        console.log('watch fileList')
        console.log(this.uploadFileNameCopy)
        console.log(this.fileList)
        console.log(this.fileList.name)
        // console.log(this.fileList[0].size)
        const fileData = {
          'path': this.node.path,
          'name': this.fileList.name.split('.')[0],
          'ext': this.fileList.name.substr(this.fileList.name.split('.')[0].length),
          'size': this.fileList.size,
        }
        // console.log(fileData)
        console.log('send 7000')
        this.$socket.emit(7000, JSON.stringify(fileData));
      }
    },
    // saveFile: function () {
    //   console.log(saveFile)
    // }
  },
  computed: {
    originalSize() {
      if (this.originalpath) {
        return this.originalpath.split('\/').length - 1
      }
    },
    customPath() {
      if (this.node.path) {
        return this.node.path.split('\/').slice(this.originalSize)
      }
      // return this.node.path.split('\/').join(' > ')
    },
    originArrayPath() {
      if (this.originalpath) {
        return this.originalpath.split('\/')
      }
    }
  },
  created() {
    // this.$socket.on(1050, (data) => {
    //   data = JSON.parse(data);
    //   this.devices = data.devices;
    //   if (this.devices.indexOf(this.device) == -1) {
    //     this.$message({
    //       type: 'info',
    //       message: '안드로이드와 연결이 중지되었습니다.'
    //     })
    //       // this.$router.go({ name: 'Main' })
    //   }
    // })
    
    this.$socket.on(2000, (data) => {
      data = JSON.parse(data)
      this.currentpath = JSON.parse(data.data).path
      if (this.node.path && (this.currentpath != this.node.path)) {
        this.lastpath = this.node.path
      }
      this.data[0] = JSON.parse(data.data)
      this.data[0].type = 'folder'
      this.data[0].id = 0
      this.componentKey++
      this.node = this.data[0];
    })

    this.$socket.on(1070, (data) => {
      // console.log(1070)
      data = JSON.parse(data)
      this.originalpath = data.path
      this.$socket.emit(2000, JSON.stringify({
        path: data.path
      }))
    })

    this.$socket.on(7000, (data) => {
      // console.log('7000 on')
      console.log('this is 7000 : ', data)
      data = JSON.parse(data)
      // const fileData = this.fileList
 

        let size = this.fileList.size;
        let tmpfileSize = data.tmpfileSize;
        let CHUNK_SIZE = 64 * 1024;
        let start = tmpfileSize;
  
        let fileReader = new FileReader();
  
        // 1. 파일을 슬라이스한다(start ~ start + CHUNK_SIZE) // 만약, (start + CHUNK_SIZE < size) ->
        // slice : start ~ start + CHUNK_SIZE,            slice : start ~ size
        let tmp;
  
        if(start + CHUNK_SIZE < size) {
          tmp = this.fileList.slice(start, start + CHUNK_SIZE);
          start += CHUNK_SIZE;
          fileReader.readAsArrayBuffer(tmp);
        } else {
          tmp = this.fileList.slice(start, size);
          start = size;
          fileReader.readAsArrayBuffer(tmp);
        }
  
        fileReader.onloadend = (e) => {
          this.$socket.emit(7001, e.target.result);
  
          if(start == size) return;
          if(start + CHUNK_SIZE < size) {
            tmp = this.fileList.slice(start, start + CHUNK_SIZE);
            start += CHUNK_SIZE;
            e.target.readAsArrayBuffer(tmp);
          } else {
            tmp = this.fileList.slice(start, size);
            start = size;
            e.target.readAsArrayBuffer(tmp);
          }
        };
        console.log('send', this.uploadFileNameCopy)
        console.log(this.uploadFileNameCopy)
        console.log(this.uploadFileNameCopy.split('.')[1])
        this.node.directory.push({
          'name': this.uploadFileNameCopy,
          'path': this.node.path + '\/' + this.uploadFileNameCopy,
          'type': this.uploadFileNameCopy.split('.')[1]
        })
        // this.$socket.emit(2000, JSON.stringify({
        //   path: this.node.path
        // }))
      
    })
    this.$socket.on(8000, (data) => {
      if (data.byteLength) {
        this.blobArray.push(new Blob([data]))
        this.saveFileLength = this.saveFileLength - data.byteLength
        // console.log(data.byteLength, this.saveFileLength)
        if (this.saveFileLength == 0) {
          console.log(this.downloadFileNameCopy)
          // console.log('download complete')
          const a = document.createElement('a');
          a.download = this.downloadFileNameCopy;
          // a.download = 'my-file.jpg';
          let blob = new Blob(this.blobArray)
          console.log(this.blobArray.length)
          a.href = URL.createObjectURL(blob)
          a.style.display = 'none';
          a.addEventListener('click', (e) => {
            setTimeout(() => URL.revokeObjectURL(a.href), 30 * 1000);
  
          });
          a.click();
          this.blobArray = [];
          // this.saveFileLength = 0
          
        }
      }
    })
    this.$socket.on(8001, (data) => {
      this.saveFileLength = JSON.parse(data).size
      this.saveFileLengthOrigin = JSON.parse(data).size
      // console.log('this is saveFileLength on 8001')
      // console.log(this.saveFileLength)
      this.$socket.emit(8001)
    })
    this.$socket.on(7001, (data) => {
      // console.log('this is bar: ', JSON.parse(data).percent)
      // console.log(typeof JSON.parse(data).percent)
      this.percent = JSON.parse(data).percent
    })
  }
};
</script>

<style scoped>
  .box {
    width: 100px;
    height: 100px;
  }

  #right-click-menu{
    background: #FAFAFA;
    border: 1px solid #BDBDBD;
    box-shadow: 0 2px 2px 0 rgba(0,0,0,.14),0 3px 1px -2px rgba(0,0,0,.2),0 1px 5px 0 rgba(0,0,0,.12);
    display: block;
    list-style: none;
    margin: 0;
    padding: 0;
    position: absolute;
    width: 200px;
    z-index: 999999;
}

  #right-click-menu li {
    text-align: left;
    border-bottom: 1px solid #E0E0E0;
    margin: 0;
    padding: 5px 35px;
  }

  #right-click-menu li:last-child {
      border-bottom: none;
  }

  #right-click-menu li:hover {
      background: #1E88E5;
      color: #FAFAFA;
  }

  .selectBox {
    background-color: lightskyblue;
    border: 2px solid black;
  }

  .boxitem:hover {
    background-color: lightskyblue;
  }

  .navicon {
    width: 50px !important;
    height: 50px !important;
    padding: 0;
    
  }

  /* .navicon > .v-btn__content {
    
  } */

  .small-window {
    position: absolute;
    min-width: 800px;
    max-width: 75%;
    width: 50%;
    margin: auto;
    resize: horizontal;
    overflow: auto;
  }

  .cursor {
    cursor: move;
  }

  .max-window {
    width: 100% !important;
  }

  #window {
    
    z-index: 9;
  }

  #windowheader {
    
    z-index: 10;
  }

  .headercolor {
    background-color: palegoldenrod;
  }

  .icon-container >>> button {
    transform: scale(0.7);
  }

  >>> .v-treeview-node__label {
    text-align: left;
  }

  .child-name {
    display: inline-block; 
    width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .showChild {
    overflow: visible;
    /* white-space: inherit; */
    /* white-space: initial */
    /* white-space: pre-line; */
    white-space: pre-wrap;
  }

  .link:hover {
    background-color: lightskyblue;
  }
</style>