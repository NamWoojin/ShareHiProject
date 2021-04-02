import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '*',
    redirect: "/404"
  },
  {
    path: "/404",
    name: "notFound",
    component: () => import('../views/NotFound.vue'),
  },
  // user
  {
    path: '/user',
    name: 'User',
    component: () => import('../views/user/User.vue'),
    redirect: { name: 'Login' },
    children: [
      {
        path: 'signup',
        name: 'Signup',
        component: () => import('../views/user/Signup.vue')
      },
      {
        path: 'login',
        name: 'Login',
        component: () => import('../views/user/Login.vue')
      },
    ]
  },

  // with Header
  {
    path: '/',
    component: () => import('../views/Home.vue'),
    children: [
      {
        path: '',
        name: 'Main',
        component: () => import('../views/Main.vue')
      },
      {
        path: 'storage',
        name: 'Storage',
        component: () => import('../views/storage/Storage.vue'),
        redirect: { name: 'MyDevice' },
        children: [
          {
            path: 'my-device',
            name: 'MyDevice',
            component: () => import('../views/storage/MyDevice.vue')
          },
          {
            path: 'shared',
            name: 'SharedFolder',
            component: () => import('../views/storage/SharedFolder.vue')
          },
          {
            path: 'sharing',
            name: 'SharingFolder',
            component: () => import('../views/storage/SharingFolder.vue')
          },
        ]
      },
      {
        path: 'filebrowser',
        name: 'FileBrowser',
        component: () => import('../views/FileBrowser.vue'),
        props: true
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/profile/Profile.vue'),
        redirect: { name: 'FollowerPage' },
        children: [
          {
            path: 'user-modify',
            name: 'UserModify',
            component: () => import('../views/profile/UserModify.vue')
          },
          {
            path: 'follower',
            name: 'FollowerPage',
            component: () => import('../views/profile/FollowerPage.vue')
          },
          {
            path: 'password-change',
            name: 'PasswordChange',
            component: () => import('../views/profile/PasswordChange.vue')
          },
          {
            path: 'signout',
            name: 'Signout',
            component: () => import('../views/profile/Signout.vue')
          },
        ]
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
