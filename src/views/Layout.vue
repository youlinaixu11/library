<template>
  <div>
      <!-- 头部区域 -->
      <div style="height: 60px;line-height: 60px;background-color: antiquewhite;margin-bottom: 2px; display: flex">
        <div style="width: 285px">
          <span style="margin-left: 15px;font-size:30px;font-family: 华文隶书">图书馆管理系统</span>
        </div>

        <div style="flex: 1; text-align: right; padding-right: 25px">
          <el-dropdown size="medium" trigger="click">
              <span class="el-dropdown-link" style="cursor: pointer">
                {{ admin.username }}<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
            <el-dropdown-menu slot="dropdown" style="margin-top: -5px">
              <el-dropdown-item><div style="width: 50px; text-align: center;" @click="logout">退出</div></el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

      </div>

      <div style="display: flex">
        <!-- 侧边栏导航 -->
        <div
            style="width: 200px; min-height: calc(100vh - 62px); overflow: hidden;margin-right: 2px;background-color: white">

          <el-menu

              :default-active="$route.path" router class="el-menu-demo">
            <el-menu-item index="/">
              <i class="el-icon-house"></i>
              <span slot="title">首页</span>
            </el-menu-item>

            <el-submenu index="user">
              <template slot="title">
                <i class="el-icon-s-operation"></i>
                <span>用户管理</span>
              </template>
              <el-menu-item index="/addUser" ><i class="el-icon-circle-plus-outline"></i> 用户添加</el-menu-item>
              <el-menu-item index="/userList"><i class="el-icon-copy-document"></i> 用户列表</el-menu-item>
            </el-submenu>

            <el-submenu index="admin">
              <template slot="title">
                <i class="el-icon-user"></i>
                <span>管理员</span>
              </template>
              <el-menu-item index="/addAdmin" ><i class="el-icon-circle-plus-outline"></i> 管理员添加</el-menu-item>
              <el-menu-item index="/adminList"><i class="el-icon-copy-document"></i> 管理员列表</el-menu-item>
            </el-submenu>


            <el-submenu index="book">
              <template slot="title">
                <i class="el-icon-notebook-1"></i>
                <span>图书管理</span>
              </template>
                <el-menu-item index="/addCategory" ><i class="el-icon-circle-plus-outline"></i> 图书分类添加</el-menu-item>
                <el-menu-item index="/categoryList"><i class="el-icon-copy-document"></i> 图书分类列表</el-menu-item>
              <el-menu-item index="/addBook" ><i class="el-icon-circle-plus-outline"></i> 图书添加</el-menu-item>
              <el-menu-item index="/BookList"><i class="el-icon-copy-document"></i> 图书列表</el-menu-item>
            </el-submenu>

            <el-submenu index="borrow">
              <template slot="title">
                <i class="el-icon-shopping-cart-2"></i>
                <span>借阅管理</span>
              </template>
              <el-menu-item index="/addBorrow" ><i class="el-icon-circle-plus-outline"></i> 添加借阅</el-menu-item>
              <el-menu-item index="/borrowList"><i class="el-icon-copy-document"></i> 借阅列表</el-menu-item>
              <el-menu-item index="/returList"><i class="el-icon-copy-document"></i> 还书记录</el-menu-item>
            </el-submenu>

          </el-menu>
        </div>

        <!--主体数据 -->
        <div style="flex: 1; width:0;background-color: white; padding: 10px">
          <router-view />
        </div>
      </div>


  </div>
</template>

<script>
// import user from "./user/User.vue";
import Cookies from "js-cookie";

export default {
  name:"Layout.vue",
  data() {
      return {
        admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {}
      }
  },
  methods: {
    logout() {
      Cookies.remove('admin')
      this.$router.push('/login')
    }
  }
}
</script>


<style scoped>

</style>