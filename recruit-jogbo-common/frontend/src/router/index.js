import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/components/Welcome'
import Login from '@/components/Login'
import Join from '@/components/Join'
import Confirm from '@/components/Confirm'
import PostList from '@/components/PostList'
import Post from '@/components/Post'
import PostForm from '@/components/PostForm'
import Member from '@/components/Member'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Welcome',
      component: Welcome
    }, {
      path: '/login',
      name: 'Login',
      component: Login
    }, {
      path: '/join',
      name: 'Join',
      component: Join
    }, {
      path: '/confirm',
      name: 'Confirm',
      component: Confirm
    }, {
      path: '/post/list',
      name: 'PostList',
      component: PostList
    }, {
      path: '/post/form',
      name: 'PostCreateForm',
      component: PostForm
    }, {
      path: '/post/form/:id',
      name: 'PostUpdateForm',
      component: PostForm
    }, {
      path: '/post/:id',
      name: 'Post',
      component: Post
    }, {
      path: '/member',
      name: 'Member',
      component: Member
    }
  ]
})
