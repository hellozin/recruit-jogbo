<template>
  <b-navbar toggleable="lg" type="light" variant="light" class="p-3">
    <b-navbar-brand>Recruit Jogbo</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item><router-link to="/post/list">후기 게시판</router-link></b-nav-item>
        <b-nav-item><router-link to="/">꿀팁 게시판</router-link></b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-item disabled>로그아웃</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>
