import Vue from 'vue';
import App from '../App.vue'
import router from "../router";
import store from "../store";


const shadowElement = document.createElement('div');
shadowElement.setAttribute("id","shadowElement")
shadowElement.attachShadow({ mode: 'open' });
document.body.appendChild(shadowElement);
const style = document.createElement("style")
style.textContent = 
`
#app {
  position: fixed;
  z-index: 900000000;
  top: 0;
  right: 0;
  width :100vw;
  height: 100vh;
  background-color: rgba(0,0,0,0.15);
}
.hidden {
  display: none;
}
.overlay {
  position: fixed;
  width: 100%;
  height: 100%;
}

.sidebar {
  position: fixed;
  top: 0;
  right: 0;
  width: 400px;
  height: 100%;
  background-color:white;
}
.nav {
  display: flex;
  justify-content: space-around;
  background-color: rgba(0,0,0,0.2);
}
.nav-item {
    position: relative;
    display: inline-block;
}
.nav-item-content {
    display: none;
    position: absolute;
    background-color: white;
    min-width: 100px;
    overflow: auto;
    box-shadow: 0px 8px 5px 0px rgba(0,0,0,0.1);
}
.nav-item-last {
  right: 0;
}
.show {
  display: block;
}
.nav-item-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    animation: rotateMenu 500ms ease-in-out forwards;
    transform-origin: top center;
}

.login-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.login-container > * {
  width : 80%;
}
`
shadowElement.shadowRoot.appendChild(style)
const hello = document.createElement('div');
shadowElement.shadowRoot.appendChild(hello);
new Vue({
  router,
  store,
  el : hello,
  render: (h) => h(App)
})