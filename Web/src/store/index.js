import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    member: null,
    login: false,
  },
  mutations: {
    login(state, data) {
      state.member = data;
      state.login = true
    },
    logout(state) {
      state.member = null;
      state.login = false;
    }
  },
  actions: {
    LOGIN({ commit }, data) {
      commit("login", data)
    },
    LOGOUT({ commit }) {
      commit("logout")
      localStorage.removeItem("token")
    }
  },
  modules: {
  },
  plugins: [
    createPersistedState()
  ]
})
