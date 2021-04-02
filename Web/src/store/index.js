import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    member: null,
    login: false,
    socket: null,
  },
  mutations: {
    login(state, data) {
      state.member = data;
      state.login = true
    },
    logout(state) {
      state.member = null;
      state.login = false;
    },
    socket(state, data) {
      state.socket = data
    }
  },
  actions: {
    LOGIN({ commit }, data) {
      commit("login", data)
    },
    LOGOUT({ commit }) {
      commit("logout")
      localStorage.removeItem("token")
    },
    SOCKET({ commit }, data) {
      commit("socket", data)
    }
  },
  modules: {
  },
  plugins: [
    createPersistedState()
  ]
})
