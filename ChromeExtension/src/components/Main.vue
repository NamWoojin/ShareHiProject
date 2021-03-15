<template>
  <div>
    <nav class="nav">
      <div class="nav-item" >
        <button @click="onClickNavItem" class="nav-item-btn">알림</button>
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
        <button @click="onClickNavItem" class="nav-item-btn">디바이스</button>
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
        <button @click="onClickNavItem" class="nav-item-btn">+</button>
        <div class="nav-item-content nav-item-last">
            <a href="#">계정설정</a>
            <a href="#" @click="onClickLogout">로그아웃</a>
        </div>
      </div>
    </nav>
    <div class="directory">
      장치를 연결해주세요
    </div>
  </div>
</template>

<script>
export default {
  name: "Main",
  methods : {
    onClickNavItem(e) {
      const clickedNavItemContent = e.target.nextSibling
      const navItemContents = document.querySelectorAll('.nav-item-content')
      for (let idx = 0; idx<navItemContents.length; idx++ ) {
        if (clickedNavItemContent !== navItemContents[idx]) {
          navItemContents[idx].classList.remove('show')
        }
      }
      clickedNavItemContent.classList.toggle('show')
    },
    onClickLogout() {
      alert("logout")
      this.$emit("onClickLogout",false)
    }
  },
  mounted() {
    window.addEventListener('click', (e)=>{
      const navItemContents = document.querySelectorAll('.nav-item-content')
      if (e.target.nextSibling && e.target.nextSibling.classList.contains("nav-item-content")) {
        return
      }
      else {
        for (let idx = 0; idx<navItemContents.length; idx++ ) {
          navItemContents[idx].classList.remove('show')
        }
      }
    })
  }
};
</script>

