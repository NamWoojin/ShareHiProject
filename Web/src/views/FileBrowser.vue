<template>
  <div>
    <router-link :to="{ name: 'Storage' }">
      <el-button type="danger" icon="el-icon-back" circle></el-button>
    </router-link>
      <div>
        <v-container>
          <v-row>
            <v-col cols="4">
              <el-tree
                style="border: 1px solid black; border-radius: 5px;"
                :data="data"
                node-key="id"
                :props="defaultProps"
                @node-click="nodeClick"
              >
              </el-tree>
            </v-col>
            <v-col cols="8">
              <div v-if="node.folder" style="display: flex; flex-wrap: wrap;">
                <div v-for="child in node.folder" :key="child.id" @contextmenu="openMenu($event)" @click="clickOuter">
                  <div style="margin: 50px;">
                    
                      <div @click="selectItem(child)" @contextmenu="selectRightclick(child);openMenu($event)">
                        <div v-if="child.folder">
                          <div id="inner" class="box" :class="{selectBox: selectitem.includes(child)}" style="background-color: lightyellow">ThisIsFolder<br />{{child.label}}</div>
                        </div>
                        <div v-else>
                          <div id="inner" class="box" :class="{selectBox: selectitem.includes(child)}" style="background-color: lightgreen">ThisIsFile<br />{{child.label}}</div>
                        </div>
                      </div>
                    
                  </div>
                </div>
              </div>
              <ul id="right-click-menu" ref="right" tabindex="-1" v-if="viewMenu" @blur="closeMenu" :style="{top: top, left: left}">
                <li>Upload new File</li>
                <li v-if="selectitem.length > 0" @click="remove">Remove these Items</li>
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
      selectitem: [],
      data: [{
        id: 1,
        label: 'Level one 1',
        folder: [{
          id: 4,
          label: 'Level two 1-1',
          folder: [{
            id: 9,
            label: 'Level three 1-1-1'
          }, {
            id: 10,
            label: 'Level three 1-1-2'
          }, {
            id: 110,
            label: 'Level three 1-1-3'
          }, {
            id: 111,
            label: 'Level three 1-1-4'
          }, {
            id: 112,
            label: 'Level three 1-1-5'
          }, {
            id: 113,
            label: 'Level three 1-1-6'
          }, {
            id: 114,
            label: 'Level three 1-1-7'
          }, {
            id: 115,
            label: 'Level three 1-1-8'
          }, {
            id: 116,
            label: 'Level three 1-1-9'
          }, {
            id: 117,
            label: 'Level three 1-1-10'
          }]
        },
        {
          id: 16,
          label: 'Level two 1-2',

        }]
      }, 
      {
        id: 11,
        label: 'Level one 4'
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
      // let largestHeight = window.innerHeight - this.$$right.offsetHeight - 25;
      // let largestWidth = window.innerWidth - this.$$right.offsetWidth - 25;
      
      // if (top > largestHeight) top = largestHeight;
      // if (left > largestWidth) left = largestWidth;
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
    
    border: 4px solid black;
  }
</style>