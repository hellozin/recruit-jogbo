import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/components/Welcome'
import Login from '@/components/Login'
import Join from '@/components/Join'
import Confirm from '@/components/Confirm'
import ReviewList from '@/components/ReviewList'
import Review from '@/components/Review'
import ReviewForm from '@/components/ReviewForm'
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
      path: '/review/list',
      name: 'ReviewList',
      component: ReviewList
    }, {
      path: '/review/form',
      name: 'ReviewPublishForm',
      component: ReviewForm
    }, {
      path: '/review/form/:id',
      name: 'ReviewUpdateForm',
      component: ReviewForm
    }, {
      path: '/review/:id',
      name: 'Review',
      component: Review
    }, {
      path: '/member',
      name: 'Member',
      component: Member
    }
  ]
})
