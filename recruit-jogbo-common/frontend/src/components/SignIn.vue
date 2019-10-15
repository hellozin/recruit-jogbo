<template>
  <div class="login">
    <div class="border shadow mx-auto p-5">
      <p class="display-4 text-center p-3">Recruit Jogbo</p>

      <b-form class="w-75 mx-auto" @submit="onSubmit" v-if="show">
        <b-form-group>
          <b-form-input v-model="form.principal" required placeholder="ID"></b-form-input>
        </b-form-group>

        <b-form-group>
          <b-form-input v-model="form.credentials" required type="password" placeholder="Password"></b-form-input>
        </b-form-group>

        <b-button class="w-100 my-3" type="submit" variant="primary">로그인</b-button>

        <router-link to="/signup" class="btn btn-outline-info w-100">회원가입</router-link>

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
      },
      show: true
    }
  },
  methods: {
    onSubmit (event) {
      event.preventDefault()
      const formData = JSON.stringify(this.form)
      console.log(formData)
      this.$axios.post(`http://localhost:8080/api/auth`, formData)
        .then(res => {
          localStorage.setItem('apiToken', res.data.response.apiToken)
          this.$router.push('/')
        }).catch(error => {
          this.$bvToast.toast(error.response.data.response.errorMessage, {
            title: '로그인 에러',
            variant: 'danger'
          })
        })
    }
  }
}
</script>

<style scoped>
.login {
    position: absolute;
    top: 50%;
    left: 50%;
    -moz-transform: translateX(-50%) translateY(-50%);
    -webkit-transform: translateX(-50%) translateY(-50%);
    transform: translateX(-50%) translateY(-50%);
}
</style>
