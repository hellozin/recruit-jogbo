<template>
  <b-card :header="header" title='인증 결과' class="my-5">

    <b-card-text>
      {{ message }}
    </b-card-text>

    <b-card-text v-if="!isConfirmed">
      로그인 후 우측 상단 <strong>사용자메뉴 > 내 정보</strong> 에서 <strong>인증 메일 재전송</strong>을 클릭해주세요.
    </b-card-text>

    <router-link to="/login" class="btn btn-outline-primary" role="button">로그인으로</router-link>

  </b-card>
</template>

<script>
export default {
  data () {
    return {
      header: 'Header',
      message: 'Message',
      isConfirmed: false
    }
  },
  created: function () {
    const token = this.$route.query.token
    if (token) {
      const param = {
        token: token
      }
      this.$axios.post('member/confirm', param)
        .then(res => {
          const response = res.data.response
          this.header = '이메일 인증이 완료되었습니다.'
          this.message = response
          this.isConfirmed = true
        })
        .catch(error => {
          const response = error.response.data.response
          this.header = '이메일 인증에 실패했습니다.'
          this.message = response.errorMessage
          this.isConfirmed = false
        })
    }
  }
}
</script>
