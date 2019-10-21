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

    <button class="btn btn-primary" v-on:click="edit()">수정</button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      post: undefined
    }
  },
  methods: {
    edit () {
      this.$router.push({
        name: 'PostForm',
        params: this.post
      })
    }
  },
  created: function () {
    const postId = this.$route.params.id
    this.$axios.get(`http://localhost:8080/api/post/${postId}`).then(res => {
      this.post = res.data.response
    }).catch(error => {
      const errorMessage = error.response.data.response.errorMessage
      this.$bvToast.toast(errorMessage, {
        title: '포스트 저장 실패',
        variant: 'danger'
      })
    })
  }
}
</script>
