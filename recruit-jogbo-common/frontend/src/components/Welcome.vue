<template>
  <div>
    <div id="jumbotron" class="rounded my-3 pt-5 px-5 py-3">
      <h1 class="display-3">Recruit Jogbo</h1>
      <p class="lead">For 영남대학교 컴퓨터공학과</p>
      <hr>
      <p>취업 후기와 노하우를 공유하는 공간입니다.</p>
    </div>

    <b-card no-body>
      <b-tabs card>

        <b-tab title="공지사항" active>
          <b-card-text style="white-space: pre-line"><p>공지사항입니다.</p></b-card-text>
        </b-tab>

        <b-tab title="업데이트">
          <b-card-text style="white-space: pre-line"><p>업데이트는 여기에</p></b-card-text>
        </b-tab>

        <b-tab title="Contact Us">
          <b-card-text style="white-space: pre-line">
            <h1><i class="fas fa-envelope"></i> Mail</h1>
            <p>paul26375@gmail.com</p>

            <h1><i class="fab fa-github-square"></i> Github</h1>
            <hr>
            <b-link href="https://github.com/hellozin/recruit-jogbo">https://github.com/hellozin/recruit-jogbo</b-link>
            Issue, Pull Request 환영

            <h1><i class="fas fa-server"></i> 데모 서버</h1>
            <hr>

            <h1><i class="fab fa-slack"></i> Slack</h1>
            <hr>
          </b-card-text>
        </b-tab>
      </b-tabs>
    </b-card>
  </div>
</template>

<script>
export default {
  data () {
    return {
    }
  }
}
</script>

<style scoped>
#jumbotron {
  background-color: #D5F2E3
}
</style>
