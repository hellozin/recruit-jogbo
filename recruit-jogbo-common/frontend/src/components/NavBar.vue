<template>
  <b-navbar toggleable="lg" type="light" variant="light" class="p-3">
    <b-navbar-brand v-on:click="forward('/')">Recruit Jogbo</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item v-on:click="forward('/post/list')">후기 게시판</b-nav-item>
        <b-nav-item v-on:click="forward('/')">꿀팁 게시판</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-item v-if="!loged" v-on:click="login()">로그인</b-nav-item>
        <b-nav-item v-if="loged" v-on:click="logout()">로그아웃</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
export default {
  data () {
    return {
      loged: false
    }
  },
  methods: {
    forward (link) {
      this.$router.push(link)
    },
    login () {
      this.$router.push('/login')
    },
    logout () {
      localStorage.removeItem('apiToken')
      this.loged = this.checkIsLogged()
      this.$router.push('/login')
    },
    checkIsLogged () {
      const apiToken = localStorage.getItem('apiToken')
      if (apiToken) {
        return true
      } else {
        return false
      }
    }
  },
  created: function () {
    this.$bus.$on('logged', () => {
      this.loged = this.checkIsLogged()
    })
  }
}
</script>
