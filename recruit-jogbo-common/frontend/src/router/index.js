import Vue from 'vue'
import Router from 'vue-router'
import SignIn from '@/components/SignIn'
import PostList from '@/components/PostList'
import Post from '@/components/Post'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/signin',
      name: 'SignIn',
      component: SignIn
    }, {
      path: '/',
      name: 'PostList',
      component: PostList
    }, {
      path: '/post/:id',
      name: 'Post',
      component: Post
    }
  ]
})
