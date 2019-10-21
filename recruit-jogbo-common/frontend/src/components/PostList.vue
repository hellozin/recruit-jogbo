<template>
  <div>
    <b-form @submit="onSubmit" class="border border-info rounded p-3 my-3">
      <b-row>
        <b-col cols="0" class="ml-2">
          <span class="btn"><i class="fas fa-search"></i></span>
        </b-col>

        <b-col>
          <b-form-input v-model="form.companyName" placeholder="기업명"></b-form-input>
        </b-col>

        <b-col>
          <b-form-select v-model="form.recruitTypes">
            <option :value="emptyString">전형종류</option>
            <option v-for="type in recruitTypes" :key="type.id" :value="type.key">{{ type.value }}</option>
          </b-form-select>
        </b-col>

        <b-col>
          <b-form-input v-model="form.authorName" placeholder="작성자"></b-form-input>
        </b-col>

        <b-col cols="2">
          <b-form-select v-model="form.sort" :options="sort"></b-form-select>
        </b-col>

        <b-col cols="1">
          <b-button type="submit" variant="primary">조회</b-button>
        </b-col>
      </b-row>
    </b-form>

    <div class="border rounded-lg p-3" style="background-color: azure">
      <router-link to="/post/form" class="btn btn-outline-primary mb-3" role="button">새 후기 작성</router-link>

      <table class="table table-hover" id="postList">
        <thead class="thead-light">
          <tr>
            <th class="text-center">기업명</th>
            <th class="text-center">기업추가정보</th>
            <th class="text-center">전형종류</th>
            <th class="text-center">마감일</th>
            <th class="text-center">작성자</th>
          </tr>
        </thead>

        <tbody>
          <tr class="text-center" v-for="post in postList" :key="post.id" v-on:click="showPost(post.id)">
            <td>{{ post.companyName }}</td>
            <td>{{ post.companyDetail }}</td>
            <td><span class="badge badge-info mx-1" v-for="type in post.recruitTypesEnum" :key="type.id">
              {{ type.value[0] }}
            </span></td>
            <td>{{ post.deadLine }}</td>
            <td>{{ post.author.username }}</td>
          </tr>
        </tbody>
      </table>

      <nav aria-label="Page navigation example" v-if="postList">
        <ul class="pagination justify-content-center">

          <li class="page-item" v-if="currPage > 0" v-on:click="toPage(prevPage)">
            <a class="page-link">이전</a>
          </li>

          <li class="page-item" v-for="page in totalPages" :key="page.id"  :class="{ active: page - 1 === currPage }">
            <a class="page-link" v-on:click="toPage(page - 1)">{{ page }}</a>
          </li>

          <li class="page-item" v-if="nextPage" v-on:click="toPage(nextPage)">
            <a class="page-link">다음</a>
          </li>

          </ul>
        </nav>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PostList',
  data () {
    return {
      postList: [],
      form: {
        companyName: '',
        recruitTypes: '',
        authorName: '',
        sort: ''
      },
      recruitTypes: [],
      sort: [
        { value: '', text: '정렬', disabled: true },
        { value: 'createdAt,desc', text: '작성순' },
        { value: 'deadLine,desc', text: '마감일순' }
      ],
      totalPages: 0,
      currPage: null,
      prevPage: null,
      nextPage: null,
      emptyString: ''
    }
  },
  methods: {
    shortRecruitTypes (recruitTypes) {
      return recruitTypes.map((type) => type[0]).join(' | ')
    },
    showPost (postId) {
      this.$router.push(`${postId}`)
    },
    getPostList (param) {
      const config = {
        params: param
      }
      this.$axios.get(`http://localhost:8080/api/post/list`, config)
        .then(res => {
          const response = res.data.response
          this.postList = response.content
          this.totalPages = response.page.totalPages
          this.currPage = response.page.number
          this.prevPage = this.currPage > 0 ? this.currPage - 1 : null
          this.nextPage = this.currPage < this.totalPages - 1 ? this.currPage + 1 : null
        })
        .catch(error => {
          const errorMessage = error.response.data.response.errorMessage
          this.$bvToast.toast(errorMessage, {
            title: '후기목록 가져오기 실패',
            variant: 'danger'
          })
        })
    },
    toPage (page) {
      const param = { page: page }
      this.getPostList(param)
    },
    toPrevPage () {
      const param = { page: this.prevPage }
      this.getPostList(param)
    },
    toNextPage () {
      const param = { page: this.nextPage }
      this.getPostList(param)
    },
    onSubmit (event) {
      event.preventDefault()
      this.getPostList(this.form)
    }
  },
  created: function () {
    this.$axios.get(`http://localhost:8080/api/recruit-types`)
      .then(res => {
        this.recruitTypes = res.data
      })
      .catch(error => {
        const errorMessage = error.response.data.response.errorMessage
        this.$bvToast.toast(errorMessage, {
          title: '전형정보 가져오기 실패',
          variant: 'danger'
        })
      })
    this.getPostList()
  }
}
</script>
