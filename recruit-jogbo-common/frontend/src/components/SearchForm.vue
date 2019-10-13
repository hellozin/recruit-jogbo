<template>
  <form class="border border-info p-3 my-3 rounded-lg" th:action="@{/post/list}" th:method="get">
    <div class="row">
      <div class="form-group col my-auto">
        <input class="form-control" id="companyName" placeholder="기업 명" th:name="companyName" type="text">
      </div>

      <div class="form-group col my-auto">
        <select class="form-control" id="recruitTypes" th:name="recruitTypes">
          <option selected value>전형 종류</option>
          <option th:each="recruitTypes : ${recruitTypes}" th:text="${recruitTypes.value()}"
                  th:value="${recruitTypes}"></option>
        </select>
      </div>

      <div class="form-group col my-auto">
        <input class="form-control" id="authorName" placeholder="작성자" th:name="authorName" type="text">
      </div>

      <div class="form-group col-auto my-auto">
        <select class="form-control" id="orders" name="sort">
          <option selected value="createdAt,desc">작성순</option>
          <option value="deadLine,desc">마감일순</option>
        </select>
      </div>

      <div class="form-group col-auto my-auto">
        <button class="btn btn-primary" id="searchBtn" type="submit">검색</button>
      </div>
    </div>
  </form>
</template>
