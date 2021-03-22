<template>
  <div>
    <div id="window" class="small-window">
      <v-container>
        <v-row>
          <v-col cols="12" style="border: 1px solid black; margin: -1px 0 0;">
            <div id="windowheader" :class="{ cursor: !browsersize }" style="display: flex; justify-content: flex-end" @mousedown="dragMouseDown">
              <v-icon v-if="browsersize" style="cursor: pointer; margin: 0 10px;" @click="resize">mdi-note-multiple-outline</v-icon>
              <v-icon v-else style="cursor: pointer; margin: 0 10px;" @click="resize">mdi-border-all-variant</v-icon>

              <v-icon style="cursor: pointer;" @click="$router.push({ name: 'MyDevice' })">mdi-close</v-icon>
              
            </div>
          </v-col>
          <v-col cols="12" style="border: 1px solid black; margin: -1px 0;">
            <div style="display: flex; justify-content: space-between">
              <router-link :to="{ name: 'Storage' }">
                <el-button icon="el-icon-back" circle></el-button>
              </router-link>
              <div>
                <v-btn elevation="2" class="navicon" @click="createNewFolder">
                  <img
                    src="@/assets/folder.png"
                    class="navicon"
                  />
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
                <v-btn 
                  class="navicon" 
                  @click="editName"
                  :disabled="selectitem.length != 1"
                >
                  <div style="width: 50px; height: 50px;">
                    <v-icon x-large color="black" style="margin-top: 5px !important">mdi-folder-edit-outline</v-icon>
                  </div>
                </v-btn>
                <!-- <v-btn class="navicon" disabled>
                  <div style="width: 50px; height: 50px;">
                    <p style="margin-top: 15px;">복사</p>
                  </div>
                </v-btn>
                
                <v-btn class="navicon" disabled>
                  <div style="width: 50px; height: 50px;">
                    <p style="margin-top: 15px;">잘라내기</p>
                  </div>
                </v-btn> -->
              </div>

              
            </div>
          </v-col>
          <v-col
            cols="2"
            style="border: 1px solid black; margin: 0;"
          >
            <el-tree
              :data="data"
              :default-expanded-keys="[0]"
              node-key="id"
              :props="defaultProps"
              @node-click="nodeClick"
            >
            </el-tree>
          </v-col>
          <v-col cols="10" style="border: 1px solid black; border-left-style: none;" @contextmenu="openMenu($event)" @click="clickOuter">
            <div v-if="node.folder && node.folder.length > 0" style="display: flex; flex-wrap: wrap;" id="outter">
              <div v-for="child in node.folder" :key="child.id" id="outter">
                <div style="margin: 20px;">
                  
                    <div 
                      @click="selectItem(child)" 
                      @contextmenu="selectRightclick(child);openMenu($event)"
                      class="boxitem"
                    >
                      <div v-if="child.folder" @dblclick="openFolder(child)">
                        <div :class="{selectBox: selectitem.includes(child)}">
                          <div class="box">
                            <img
                              src="@/assets/folder.png"
                              width="100px"
                              height="100px"
                            />
                          </div>
                          <span style="display:inline-block; width: 100px;">{{child.label}}</span>
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
                          <span style="display:inline-block; width: 100px;">{{child.label}}</span>
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
            <v-file-input multiple v-model="fileList" id="fileinput" style="display: none;" />
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
  </div>
</template>

<script>



export default {
  name: 'FileBrowser',
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
      data: 
      [{
        id: 0,
        label: 'root',
        folder: [{
          label: '새 폴더',
          folder: [{
            label: '새 폴더 (1)',
            folder: [{
              label: '파일0'
            }, {
              label: '파일1'
            }, {
              label: '파일2'
            }, {
              label: '파일3'
            }, {
              label: '파일4'
            }, {
              label: '파일5'
            }, {
              label: '파일6'
            }, {
              label: '파일7'
            }, {
              label: '파일8'
            }, {
              label: '파일9'
            }]
          },
          {
            label: '파일0',
          }]
        }, 
        {
          label: '다른파일'
        }],

      }],
      defaultProps: {
        children: 'folder',
        label: 'label'
      },
    node: '',
    viewMenu: false,
    top: '0px',
    left: '0px',
    fileList: null,
    browsersize: false,
    };
  },
  methods: {
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
      if (node.folder) {
        this.node = node
        this.selectitem = []
      }
    },
    setMenu: function(top, left) {
      this.top = top-60 + 'px';
      this.left = left + 'px';
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
            let idx = this.node.folder.indexOf(this.selectitem[i])
            if (idx > -1) this.node.folder.splice(idx, 1)
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
      this.node = c;
      this.selectitem = [];
    },
    checkFolder: function(folderName) {
      for(let i=0; i<this.node.folder.length; i++) {
        if(this.node.folder[i].label == folderName) return false
      }
      return true
    },
    createNewFolder() {
      let label = '새 폴더'
      
      if (!this.checkFolder(label)) {
        let number = 1
        while(!this.checkFolder(label + ' (' + number + ')')) {
          number++
        }
        label = label + ' (' + number + ')'
      }
      
      this.node.folder.push(
        {
          label: label,
          folder: [

          ]
        }
      )
      this.componentKey++
      this.viewMenu = false
    },
    editName() {
      this.viewMenu = false
      this.$prompt('Please edit filename', 'Edit', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        inputValue: this.selectitem[0].label
      })
        .then((value) => {
          if (this.selectitem[0].label == value.value) {
            this.$message({
              type: 'info',
              message: '변경된 내용이 없습니다.'
            })
          } else if (this.checkFolder(value.value)) {
            this.selectitem[0].label = value.value;
            this.$message({
              type: 'success',
              message: '수정되었습니다.'
            });
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
      this.viewMenu = false
      document.getElementById('fileinput').click()
    },
    resize() {
      this.browsersize = !this.browsersize
      document.getElementById('window').classList.toggle('small-window')
      document.getElementById('window').classList.toggle('max-window')
    }
  },
  mounted() {
    this.node = this.data[0];
  },
  watch: {
    fileList: function () {
      if (this.fileList) {
        let cnt = 0;
        for(let i=0; i<this.fileList.length; i++) {
          if (this.checkFolder(this.fileList[i].name)) {
            this.node.folder.push({
              label: this.fileList[i].name
            })
            cnt++
          } else {
            this.$message({
              type: 'info',
              message: this.fileList[i].name + '은 이미 있는 파일입니다.'
            })
          }
        }
        if (cnt > 0) {
          this.$message({
            type: 'success',
            message: cnt + '개의 파일이 추가되었습니다.'
          })
        }
        this.fileList = null;
      }
    }
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
    margin: 0 10px;
  }

  /* .navicon > .v-btn__content {
    
  } */

  .small-window {
    position: absolute;
    width: 50%;
    margin: auto;
  }

  .cursor {
    cursor: move;
  }

  .max-window {
    width: 100%;
  }

  #window {
    
    z-index: 9;
  }

  #windowheader {
    
    z-index: 10;
  }
</style>