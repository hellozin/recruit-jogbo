<template>
  <div class="login">
    <div class="border shadow mx-auto p-5">
      <p class="display-4 text-center p-3">Recruit Jogbo</p>

      <b-form class="w-75 mx-auto" @submit="onSubmit">
        <b-form-group>
          <b-form-input v-model="form.principal" required placeholder="ID"></b-form-input>
        </b-form-group>

        <b-form-group>
          <b-form-input v-model="form.credentials" required type="password" placeholder="Password"></b-form-input>
        </b-form-group>

        <b-button class="w-100 my-3" type="submit" variant="primary">로그인</b-button>

        <router-link to="/join" class="btn btn-outline-info w-100">회원가입</router-link>
      </b-form>

      <div class="p-3">
        <p class="text-center lead mb-0">Manged by Hellozin</p>
        <p class="text-center blockquote-footer">paul26375@gmail.com</p>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      form: {
        principal: '',
        credentials: ''
      }
    }
  },
  methods: {
    onSubmit (event) {
      event.preventDefault()
      const formData = JSON.stringify(this.form)
      this.$axios.post(`auth`, formData)
        .then(res => {
          const response = res.data.response
          localStorage.setItem('apiToken', response.apiToken)
          localStorage.setItem('username', response.member.username)
          this.$bus.$emit('logged', true)
          this.$router.push('/')
        }).catch(error => {
          if (error.response) {
            const response = error.response.data.response
            this.$bvToast.toast(response.errorMessage, {
              title: '로그인 실패',
              variant: 'danger'
            })
          }
        })
    }
  }
}
</script>

<style scoped>
.login {
  width: 500px;
  position: absolute;
  top: 50%;
  left: 50%;
  -moz-transform: translateX(-50%) translateY(-50%);
  -webkit-transform: translateX(-50%) translateY(-50%);
  transform: translateX(-50%) translateY(-50%);
}
</style>
