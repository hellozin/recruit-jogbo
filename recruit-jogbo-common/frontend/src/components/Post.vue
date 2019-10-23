<template>
  <div>
    <h2 class="py-4 font-weight-bold">후기</h2>

    <table class="table table-sm w-auto">
      <tr>
        <td class="font-weight-bold">작성자</td>
        <td v-if="post">{{ post.author.username }}</td>
      </tr>
      <tr>
        <td class="font-weight-bold">기업 명</td>
        <td v-if="post">{{ post.companyName }}</td>
      </tr>
      <tr>
        <td class="font-weight-bold">전형 종류</td>
        <td v-if="post"><span class="mr-2" v-for="type in post.recruitTypesEnum" :key="type.id">{{ type.value }}</span></td>
      </tr>
      <tr>
        <td class="font-weight-bold">마감일</td>
        <td v-if="post">{{ post.deadLine }}</td>
      </tr>
    </table>

    <p class="border p-3 lead" style="white-space: pre-line;" v-if="post">{{ post.review }}</p>

    <button class="btn btn-primary" v-if="this.post.author.username === this.username" v-on:click="edit()">수정</button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      post: undefined,
      username: ''
    }
  },
  methods: {
    edit () {
      const postId = this.post.id
      this.$router.push(`/post/form/${postId}`)
    }
  },
  created: function () {
    const postId = this.$route.params.id
    this.$axios.get(`post/${postId}`).then(res => {
      this.post = res.data.response
    }).catch(error => {
      if (error.response) {
        const response = error.response.data.response
        this.$bvToast.toast(response.errorMessage, {
          title: '포스트 저장 실패',
          variant: 'danger'
        })
      }
    })
    const username = localStorage.getItem('username')
    if (username) {
      this.username = username
    }
  }
}
</script>
