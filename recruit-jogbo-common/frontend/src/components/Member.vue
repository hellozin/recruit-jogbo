<template>
  <div>
    <h3 class="py-4 font-weight-bold">내 정보</h3>

    <b-card>
      <b-form @submit="onSubmit">
        <b-form-group label="아이디">
          <b-form-input v-model="form.username" required :disabled="!edit" placeholder="ID"></b-form-input>
        </b-form-group>

        <b-form-group label="이메일">
          <b-form-input v-model="form.email" required :disabled="!edit" placeholder="webmail@ynu.ac.kr"></b-form-input>
        </b-form-group>

        <b-form-group label="이메일 인증 여부">
          <p v-if="emailConfirmed"><strong class="text-success">완료</strong></p>
          <div v-if="!emailConfirmed">
            <p><strong class="text-danger">미완료</strong></p>
            <b-button type="button" variant="outline-primary" v-on:click="confirmMailResend">인증메일 재전송</b-button>
          </div>
        </b-form-group>

        <b-button :disabled="edit" type="button" variant="primary" v-on:click="toggleEnableEdit">수정하기</b-button>
        <b-button v-if="edit" type="submit" variant="primary">저장</b-button>
        <b-button v-if="edit" type="button" variant="primary" v-on:click="toggleEnableEdit">취소</b-button>
      </b-form>
    </b-card>

    <b-card class="my-4">
      <a v-b-toggle.collapse-1 variant="primary"><i class="fas fa-angle-down"></i><strong class="mx-2">비밀번호 변경</strong></a>
      <b-collapse id="collapse-1">
        <b-form @submit="onPasswordSubmit">
          <b-form-group label="원래 비밀번호">
            <b-form-input type="password" v-model="passwordEditForm.originPassword" required></b-form-input>
          </b-form-group>

          <b-form-group label="새 비밀번호">
            <b-form-input type="password" v-model="passwordEditForm.newPassword" required></b-form-input>
          </b-form-group>

          <b-form-group label="새 비밀번호 확인">
            <b-form-input type="password" v-model="confirmPassword" required></b-form-input>
          </b-form-group>

          <b-button type="submit" variant="primary">변경</b-button>
        </b-form>
      </b-collapse>
    </b-card>

    <b-card class="my-4">
      <p class="mr-2 text-danger"><i class="fas fa-exclamation-triangle"></i> 탈퇴 시 작성한 글도 함께 삭제됩니다.</p>
      <b-button type="button" variant="danger" v-on:click="deleteMember">탈퇴하기</b-button>
    </b-card>
  </div>
</template>

<script>
export default {
  data () {
    return {
      form: {
        username: '',
        email: ''
      },
      emailConfirmed: false,
      passwordEditForm: {
        originPassword: '',
        newPassword: ''
      },
      confirmPassword: '',
      edit: false
    }
  },
  methods: {
    toggleEnableEdit () {
      this.edit = !this.edit
    },
    onSubmit (event) {
      event.preventDefault()
      const formData = JSON.stringify(this.form)
      this.$axios.patch(`member/me`, formData)
        .then(res => {
          this.$bvModal.msgBoxOk('처리되었습니다.')
            .then(value => { this.$bus.$emit('logged', false) })
        }).catch(error => {
          this.edit = false
          if (error.response) {
            const response = error.response.data.response
            this.$bvToast.toast(response.errorMessage, {
              title: '유저정보 수정 실패',
              variant: 'danger'
            })
          }
        })
    },
    checkPasswordConfirm () {
      if (this.passwordEditForm.newPassword !== this.confirmPassword) {
        this.$bvToast.toast('확인 비밀번호가 일치하지 않습니다.', {
          title: '확인 비밀번호 불일치',
          variant: 'danger'
        })
        return false
      } else {
        return true
      }
    },
    onPasswordSubmit () {
      event.preventDefault()
      if (!this.checkPasswordConfirm()) {
        return
      }
      const formData = JSON.stringify(this.passwordEditForm)
      this.$axios.patch(`member/me/password`, formData)
        .then(res => {
          this.$bus.$emit('logged', false)
        }).catch(error => {
          if (error.response) {
            const response = error.response.data.response
            this.$bvToast.toast(response.errorMessage, {
              title: '비밀번호 수정 실패',
              variant: 'danger'
            })
          }
        })
    },
    confirmMailResend () {
      const data = {
        confirmUrl: 'http://localhost:8081/confirm'
      }
      this.$axios.post(`member/confirm/send`, data)
        .then(res => {
          this.$bvModal.msgBoxOk(`인증 메일이 재발송되었습니다.\n인증을 위해 ${this.form.email}을 확인해주세요.`)
        }).catch(error => {
          if (error.response) {
            const response = error.response.data.response
            this.$bvToast.toast(response.errorMessage, {
              title: '메일 전송 실패',
              variant: 'danger'
            })
          }
        })
    },
    deleteMember () {
      this.$axios.delete(`member/me`)
        .then(res => {
          this.$bvModal.msgBoxOk(`탈퇴 요청이 완료되었습니다. 이용해주셔서 감사합니다.`)
            .then(value => { this.$bus.$emit('logged', false) })
        }).catch(error => {
          if (error.response) {
            const response = error.response.data.response
            this.$bvToast.toast(response.errorMessage, {
              title: '탈퇴 실패',
              variant: 'danger'
            })
          }
        })
    }
  },
  created: function () {
    this.$axios.get(`member/me`)
      .then(res => {
        const member = res.data.response
        if (member) {
          this.form.username = member.username
          this.form.password = member.password
          this.form.email = member.email
          this.emailConfirmed = member.emailConfirmed
        }
      })
      .catch(error => {
        const errorMessage = error.response.data.response.errorMessage
        this.$bvToast.toast(errorMessage, {
          title: '유저정보 가져오기 실패',
          variant: 'danger'
        })
      })
  }
}
</script>
