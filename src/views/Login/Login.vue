<template>
  <div style="height: 80vh; overflow: hidden">
    <div style="width: 380px; height: 400px; background-color: white; border-radius: 10px;
      margin: 150px auto; padding: 50px">
      <div style="margin: 30px; text-align: center; font-size: 30px; font-weight: bold; color: cornflowerblue">账号登录</div>
      <el-form :model="admin" :rules="rules" ref="loginForm">
        <el-form-item prop="username">
          <el-input placeholder="请输入用户名" prefix-icon="el-icon-user" size="medium" v-model="admin.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" show-password prefix-icon="el-icon-lock" size="medium" v-model="admin.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" size="medium" type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";
import Cookies from 'js-cookie'

export default {
  name:'LOGIN',
  data() {
    return{
      admin:{},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          { min:6, max:12, message: '长度请在6~12个字符', trigger: 'blur'}
        ],
        password:[
          {required: true, message: '请输入密码', trigger: 'blur'},
          { min:6, max:12, message: '长度请在6~12个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    login() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {

          request.post('/admin/login', this.admin).then(res => {
            console.log('后端返回的数据:', res);  // 打印完整响应数据

            // 根据 code 字段判断是否登录成功
            if (res.code === '200') {
              this.$notify.success("登录成功");
              if (res.data !== null) {
                Cookies.set('admin', JSON.stringify(res.data))
              }
              this.$router.push('/');
            } else {
              // 如果 code 不等于 '200'，打印错误信息
              console.error('错误信息:', res);
              this.$notify.error(res.msg);
            }
          }).catch(error => {
            // 错误处理
            console.error('登录错误:', error);
            if (error.response) {
              // 从后端返回的错误信息
              console.error("后端错误响应:", error.response);
              this.$notify.error(error.response.data.msg);
            } else {
              this.$notify.error("登录请求失败");
            }
          });
        }
      })
    }
  }
}

</script>
