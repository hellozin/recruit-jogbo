<template>
  <div class="join">
    <div class="border shadow mx-auto p-5">
      <p class="display-4 text-center p-3">Join</p>

      <b-form class="w-75 mx-auto" @submit="onSubmit">
        <b-form-group>
          <b-form-input v-model="form.username" required placeholder="ID"></b-form-input>
        </b-form-group>

        <b-form-group>
          <b-form-input v-model="form.password" required type="password" placeholder="Password"></b-form-input>
        </b-form-group>

        <b-form-group>
          <b-form-input v-model="form.email" required placeholder="webmail@ynu.ac.kr"></b-form-input>
        </b-form-group>

        <b-button class="w-100 my-3" type="submit" variant="primary">회원가입</b-button>
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
        username: '',
        password: '',
        email: '',
        confirmUrl: 'http://localhost:8081/#/confirm'
      }
    }
  },
  methods: {
    onSubmit (event) {
      event.preventDefault()
      const formData = JSON.stringify(this.form)
      this.$axios.post(`member`, formData)
        .then(res => {
          this.$bvModal.msgBoxOk(`가입 요청이 완료되었습니다.\n인증을 위해 ${this.form.email}을 확인해주세요.`)
            .then(value => { this.$router.push('/login') })
        }).catch(error => {
          if (error.response) {
            const response = error.response.data.response
            this.$bvToast.toast(response.errorMessage, {
              title: '가입 실패',
              variant: 'danger'
            })
          }
        })
    }
  }
}
</script>

<style scoped>
.join {
  width: 500px;
  position: absolute;
  top: 50%;
  left: 50%;
  -moz-transform: translateX(-50%) translateY(-50%);
  -webkit-transform: translateX(-50%) translateY(-50%);
  transform: translateX(-50%) translateY(-50%);
}
</style>
