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
* {
  box-sizing: border-box;
  margin : 0;
  padding : 0;
  font-size : 16px;
}
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
  overflow-y:scroll;
  overflow-x: scroll;
}
.nav {
  display: flex;
  justify-content: space-around;
  background-color : #3ac569;
  padding : 5px;
}
.nav-item {
  position: relative;
  display: inline-block;
}
.nav-item-content {
  display: none;
  position: absolute;
  background-color: white;
  min-width: 0px;
  overflow: auto;
  box-shadow: 0px 8px 5px 0px rgba(0,0,0,0.1);
}
.nav-item-last {
  right: 0;
}
.nav-item-btn {
  border : none;
  outline : none;
  color : white;
  cursor : pointer;
  background : none;
}
.nav-item:hover .nav-item-content {
  display : block;
}
.nav-item-content a {
  color: black;
  width: max-content;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  animation: rotateMenu 500ms ease-in-out forwards;
  transform-origin: top center;
}
.nav-item-content a:hover {
  background-color : rgba(0,0,0,0.2);
}
.directory ul {
  margin-left : 10px;
  list-style-type: none;
}
.directory li {
  margin-left : 10px;
  list-style-type: none;
}
.dir::before {
  content : '📁';
}
.file::before {
  content : '📄';
}
.directory a {
  text-decoration: none;
}
.dir {
  width : 100%;
  line-height : 30px;
  cursor : pointer;
  white-space: nowrap;
}
.dir:hover {
  background-color : rgba(0,0,0,0.2);
}
.file {
  width : 100%;
  line-height : 30px;
  cursor : pointer;
  white-space: nowrap;
}
.file:hover {
  background-color : rgba(0,0,0,0.2);
}
.closed {
  height : 0;
  display : none;
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
  margin-bottom : 10px;
}
.logo-image {
  position : absolute;
  top: 20%;
  width: 200px;
}
.login-email-input {
  min-height : 40px;
  padding-left : 5px;
}
.login-email-input::placeholder {
  padding-left: 10px;
}
.login-password-input {
  min-height : 40px;
  padding-left : 5px;
}
.login-password-input::placeholder {
  padding-left: 10px;
}
.login-btn {
  border : none;
  outline : none;
  min-height : 40px;
  background-color : #3ac569;
  color : white;
  cursor : pointer;
}
.login-btn-box {
  display : flex;
  justify-content : space-around; 
}
.new-tab-link {
  cursor: pointer;
}
.context-menu-overlay {
  position : fixed;
  top : 0;
  left : 0;
  width : 100vw;
  height : 100vh;
  z-index : 900000000;
}
.context-menu {
  background-color : white;
  position : absolute;
  width : auto;
  height : auto;
  box-shadow : 0px 0px 5px 1px rgb(0 0 0 / 20%);
  z-index : 900000000;
  display : flex;
  flex-direction: column;
  padding : 5px;
}
.context-menu-content {
  margin : 5px;
  cursor : pointer;
}
.context-menu-content:hover {
  color : #3ac569;
  font-weight : bold;
}
.modal-overlay {
  position : fixed;
  top : 0;
  left : 0;
  width : 100vw;
  height : 100vh;
  z-index : 900000000;
  background-color : rgba(0,0,0,0.2);
}
.modal {
  position: absolute;
  margin-left: auto;
  margin-right: auto;
  margin-top : auto;
  margin-bottom : auto;
  left: 0;
  right: 0;
  top : 0;
  bottom : 0;
  z-index : 900000000;
  background-color : white;
  box-shadow : 0px 0px 5px 1px rgb(0 0 0 / 20%);
  border-radius : 15px;
  display : flex;
  flex-direction : column;
  padding : 20px;
  height : fit-content;
  width : fit-content;
}
.modal-input {
  margin-top: 15px;
}
.btn-container {
  margin-top : 15px;
  display : flex;
  justify-content : space-around;
}
.confirm-btn {
  background-color : #3ac569;
  cursor : pointer;
  padding: 2px 10px;
  border-radius: 5px;
  color: white;
  box-shadow: 0px 0px 0px 0.5px rgb(0 0 0 / 20%);
}
.cancel-btn {
  background-color : white;
  cursor : pointer;
  padding: 2px 10px;
  border-radius: 5px;
  box-shadow: 0px 0px 0px 0.5px rgb(0 0 0 / 20%);
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