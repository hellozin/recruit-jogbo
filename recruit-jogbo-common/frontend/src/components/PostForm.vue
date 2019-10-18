<template>
  <div>
    <h3 class="pt-4 font-weight-bold">후기 작성</h3>
    <b-form @submit="onSubmit" v-if="show" autocomplete="off">

      <a v-b-toggle.collapse-1 class="text-primary"><i class="fas fa-angle-down"></i> 설명</a>
      <b-collapse id="collapse-1" class="mt-0">
        <b-card>
          <table>
            <tr v-for="guide in guides" :key="guide.id">
              <td class="pr-3">{{ guide.name }}</td><td class="text-muted">{{ guide.description }}</td>
            </tr>
          </table>
        </b-card>
      </b-collapse>

      <b-row>
        <b-col cols="3">
          <b-form-group label="기업명">
            <b-form-input v-model="form.companyName" required placeholder="ex) 카카오"></b-form-input>
          </b-form-group>
        </b-col>

        <b-col cols="3">
          <b-form-group label="추가정보">
            <b-form-input v-model="form.companyDetail" placeholder="ex) 플랫폼 개발"></b-form-input>
          </b-form-group>
        </b-col>

        <b-col cols="3">
          <b-form-group label="날짜">
            <b-form-input v-model="form.deadLine" required type="date"></b-form-input>
          </b-form-group>
        </b-col>
      </b-row>

      <b-form-group label="전형 종류">
        <b-form-checkbox-group v-model="form.recruitTypes">
          <b-form-checkbox v-for="type in recruitTypes" :key="type.id" :value="type.key">{{ type.value }}</b-form-checkbox>
        </b-form-checkbox-group>
      </b-form-group>

      <b-form-group label="후기">
        <b-form-textarea v-model="form.review" rows="3" max-rows="15" placeholder="여기에 후기를 입력해 주세요."></b-form-textarea>
      </b-form-group>

      <b-button type="submit" variant="primary">저장</b-button>
    </b-form>
  </div>
</template>

<script>
export default {
  data () {
    return {
      form: {
        companyName: '',
        companyDetail: '',
        deadLine: new Date().toISOString().slice(0, 10),
        recruitTypes: [],
        review: ''
      },
      recruitTypes: [],
      guides: [
        { name: '기업명', description: '카카오, 삼성전자, 한국전력공사 등 가장 보편적인 이름을 적어주세요.' },
        { name: '추가정보', description: '지원 부서명, 채용 공지 이름 등 추가적인 내용을 적어주세요.' },
        { name: '날짜', description: '전형이 시작되는 날짜를 입력해 주세요. (서류 마감일, 코딩테스트 날짜, 시험 날짜 등)' },
        { name: '전형 종류', description: '후기에 포함된 전형을 모두 체크해 주세요.' }
      ],
      show: true
    }
  },
  methods: {
    onSubmit (event) {
      event.preventDefault()
      const formData = JSON.stringify(this.form)
      console.log(formData)
      this.$axios.post(`http://localhost:8080/api/post`, formData)
        .then(res => {
          this.$router.push('/post/list')
        }).catch(error => {
          console.log(error.response)
          const errors = error.response.data.response.errorMessage.split('\n')
          for (var i in errors) {
            this.$bvToast.toast(errors[i], {
              title: '포스트 저장 실패',
              variant: 'danger'
            })
          }
        })
    }
  },
  created: function () {
    const token = localStorage.getItem('apiToken')
    console.log(token)
    this.$axios.get(`http://localhost:8080/api/recruit-types`).then(res => {
      console.log(res)
      this.recruitTypes = res.data
    }).catch(error => {
      this.$bvToast.toast(error.response, {
        title: '전형 정보 로드 실패',
        variant: 'danger'
      })
    })
  }
}
</script>
