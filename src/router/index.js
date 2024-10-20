import Vue from 'vue'
import VueRouter from 'vue-router'
import User from "@/views/user/User.vue";
import AddUser from "@/views/user/AddUser.vue";
import EditUser from "@/views/user/EditUser.vue";
import Add from "@/views/admin/Add.vue";
import List from "@/views/admin/List.vue";
import Edit from "@/views/admin/Edit.vue";
import Login from "@/views/Login/Login.vue";
import Layout from "@/views/Layout.vue";
import Cookies from "js-cookie";

Vue.use(VueRouter)


const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}


const routes = [

    // =====登录=====
  {
    path: '/login',
    name: 'Login',
    component: Login
  },



    // ======主页=====
  {

    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/home',
    children: [

      // === User ====
      {path: 'home', name: 'Home' , component:() => import('@/views/home/HomeView.vue'),},

      {path: 'userList',name: 'UserList', component: User},
      {path: 'addUser', name: 'AddUser', component: AddUser},
      {path: 'editUser', name: 'EditUser', component:EditUser},

      // === Admin ===
      {path: 'adminList', name: 'AdminList', component: List},
      {path: 'addAdmin', name: 'AddAdmin', component: Add},
      {path: 'editAdmin', name: 'EditAdmin', component:Edit},

      // === category ===

      {path: 'categoryList', name: 'CategoryList', component: () => import('@/views/category/List.vue')},
      {path: 'addCategory', name: 'AddCategory', component: () => import('@/views/category/Add.vue')},
      {path: 'editCategory', name: 'EditCategory', component:() => import('@/views/category/Edit.vue')},

      // === book ===

      {path: 'bookList', name: 'BookList', component: () => import('@/views/book/List.vue')},
      {path: 'addBook', name: 'AddBook', component: () => import('@/views/book/Add.vue')},
      {path: 'editBook', name: 'EditBook', component:() => import('@/views/book/Edit.vue')},

      // === borrow ===

      {path: 'borrowList', name: 'BorrowList', component: () => import('@/views/borrow/List.vue')},
      {path: 'addBorrow', name: 'AddBorrow', component: () => import('@/views/borrow/Add.vue')},
      {path: 'editBorrow', name: 'EditBorrow', component:() => import('@/views/borrow/Edit.vue')},

      // === Return ===

      {path: 'returList', name: 'BorrowList', component: () => import('@/views/retur/List.vue')},

    ]
  },

  {
    path: "*",
    component: () => import('@/views/404.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') next()
  const admin = Cookies.get("admin")
  if (!admin && to.path !== '/login') return next("/login")
  next()
})


export default router
