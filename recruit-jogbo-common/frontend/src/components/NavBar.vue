<template>
  <b-navbar toggleable="lg" type="light" variant="light" class="p-3">
    <b-navbar-brand v-on:click="forward('/')">Recruit Jogbo</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item v-on:click="forward('/review/list')">후기 게시판</b-nav-item>
        <b-nav-item v-on:click="forward('/')">꿀팁 게시판</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-item-dropdown right>
          <template v-slot:button-content>
            <a>{{ username }} 님 </a>
          </template>
          <b-dropdown-item v-if="loged" v-on:click="forward('/member')">내정보</b-dropdown-item>
          <b-dropdown-item v-if="!loged" v-on:click="forward('/login')">로그인</b-dropdown-item>
          <b-dropdown-item v-if="loged" v-on:click="logout()">로그아웃</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>

    </b-collapse>
  </b-navbar>
</template>

<script>
export default {
  data () {
    return {
      loged: false,
      username: 'User'
    }
  },
  methods: {
    forward (link) {
      if (link !== this.$route.path) {
        this.$router.push(link)
      }
    },
    logout () {
      localStorage.removeItem('apiToken')
      localStorage.removeItem('username')
      this.username = 'User'
      this.loged = this.checkIsLogged()
      this.$router.push('/login')
    },
    checkIsLogged () {
      const apiToken = localStorage.getItem('apiToken')
      if (apiToken) {
        this.username = localStorage.getItem('username')
        return true
      } else {
        return false
      }
    }
  },
  created: function () {
    this.$bus.$on('logged', (isLogged) => {
      if (isLogged) {
        this.loged = this.checkIsLogged()
      } else {
        this.logout()
      }
    })
    const username = localStorage.getItem('username')
    if (username) {
      this.username = username
    }
    this.loged = this.checkIsLogged()
  }
}
</script>
