import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/components/Welcome'
import Login from '@/components/Login'
import PostList from '@/components/PostList'
import Post from '@/components/Post'
import PostForm from '@/components/PostForm'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Welcome',
      component: Welcome
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    }, {
      path: '/post/list',
      name: 'PostList',
      component: PostList
    }, {
      path: '/post/form',
      name: 'PostForm',
      component: PostForm
    }, {
      path: '/post/:id',
      name: 'Post',
      component: Post
    }
  ]
})
