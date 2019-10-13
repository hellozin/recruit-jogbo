<template>
  <div>
    <search-form/>
    <div class="border rounded-lg p-3" style="background-color: azure">
      <router-link to="/post/form" class="btn btn-outline-primary mb-3" role="button">새 후기 작성</router-link>

      <table class="table table-hover" id="postList">
        <thead class="thead-light">
          <tr>
            <th class="w-25 text-center">기업명</th>
            <th class="w-25 text-center">전형종류</th>
            <th class="w-25 text-center">마감일</th>
            <th class="w-25 text-center">작성자</th>
          </tr>
        </thead>

        <tbody>
          <tr class="text-center" v-for="post in postList" :key="post.id" v-on:click="showPost(post.id)">
            <td>{{ post.companyName}}</td>
            <td><span class="badge badge-info mx-1" v-for="type in post.recruitTypesEnum" :key="type.id">
              {{ type[0] }}
            </span></td>
            <td>{{ post.deadLine }}</td>
            <td>{{ post.author.username }}</td>
          </tr>
        </tbody>
      </table>

      <div class="overflow-auto">
        <b-pagination-nav :link-gen="linkGen" :number-of-pages="pageNumber" use-router align="center"></b-pagination-nav>
      </div>
    </div>
  </div>
</template>

<script>
import SearchForm from './SearchForm.vue'

export default {
  name: 'PostList',
  components: {
    SearchForm
  },
  data () {
    return {
      postList: [],
      pageNumber: 1
    }
  },
  methods: {
    linkGen (pageNum) {
      return pageNum === 1 ? '?' : `?page=${pageNum}`
    },
    shortRecruitTypes (recruitTypes) {
      return recruitTypes.map((type) => type[0]).join(' | ')
    },
    showPost (postId) {
      this.$router.push(`post/${postId}`)
    }
  },
  created: function () {
    this.$axios.get(`http://localhost:8080/api/post/list`).then(res => {
      console.log(res.data)
      this.postList = res.data.response.content
      this.pageNumber = res.data.response.page.totalPages
    }).catch(error => {
      console.log(error)
    })
  }
}
</script>
