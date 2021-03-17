<template>
  <div>
    <router-link :to="{ name: 'Storage' }">
      <el-button type="danger" icon="el-icon-back" circle></el-button>
    </router-link>
      <div>
        <v-container>
          <v-row>
            <v-col cols="3">
              <el-tree
                style="border: 1px solid black; border-radius: 5px;"
                :key="componentKey"
                :data="data"
                :default-expanded-keys="[0]"
                node-key="id"
                :props="defaultProps"
                @node-click="nodeClick"
              >
              </el-tree>
            </v-col>
            <v-col cols="9" style="border: 1px solid black; border-radius: 5px; margin-top: 12px;" @contextmenu="openMenu($event)" @click="clickOuter">
              <div v-if="node.folder && node.folder.length > 0" style="display: flex; flex-wrap: wrap;">
                <div v-for="child in node.folder" :key="child.id">
                  <div style="margin: 50px;">
                    
                      <div 
                        @click="selectItem(child)" 
                        @contextmenu="selectRightclick(child);openMenu($event)"
                        class="boxitem"
                      >
                        <div v-if="child.folder" @dblclick="openFolder(child)">
                          <div :class="{selectBox: selectitem.includes(child)}">
                            <div class="box">
                              <img
                                id="inner"
                                src="@/assets/folder.png"
                                width="100px"
                                height="100px"
                              />
                            </div>
                            <span id="inner">{{child.label}}</span>
                          </div>
                        </div>
                        <div v-else>
                          <div :class="{selectBox: selectitem.includes(child)}">
                            <div class="box">
                              <img
                                id="inner"
                                src="@/assets/file.png"
                                width="100px"
                                height="100px"
                              />
                            </div>
                            <span id="inner">{{child.label}}</span>
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
              <ul id="right-click-menu" ref="right" tabindex="-1" v-if="viewMenu" @blur="closeMenu" :style="{top: top, left: left}">
                <div v-if="selectitem.length > 1">
                  <li @click="remove">Remove these Items</li>
                </div>
                <div v-else-if="selectitem.length == 1">
                  <li @click="remove">Remove these Items</li>
                  <li id="inner" @click="editName">edit name</li>
                </div>
                <div v-else>
                  <li @click="createNewFolder">New Folder</li>
                  <li>Upload new File</li>
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
    };
  },
  methods: {
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
      if (!(e.target.id == 'inner')) {
        this.selectitem = [];
      }
    },
    remove() {
      for(let i=0; i<this.selectitem.length; i++) {
        let idx = this.node.folder.indexOf(this.selectitem[i])
        if (idx > -1) this.node.folder.splice(idx, 1)
      }
      this.selectitem = [];
      this.viewMenu = false;
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
        console.log(label + ' (' + number + ')')
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
          this.selectitem[0].label = value.value;
          this.$message({
            type: 'success',
            message: '수정되었습니다.'
          });
      },
      )
        .catch(() => {
          this.$message({
            type: 'info',
            message: '취소되었습니다.'
          });       
      });
    },
  },
  mounted() {
    this.node = this.data[0]
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
    width: 250px;
    z-index: 999999;
}

  #right-click-menu li {
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
</style>