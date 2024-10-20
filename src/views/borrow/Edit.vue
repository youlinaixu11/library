<template>
  <div style="margin-left: 25px;padding: 20px;width: 80%">

    <div style="margin-bottom: 30px">编辑图书</div>

    <el-form :inline="true" :rules="rules"  ref="ruleForm" :model="form" label-width="100px">

      <el-form-item label="借阅人卡号" prop="userId">
        <el-select v-model="form.username" clearable filterable placeholder="请选择" @change="selUser">
          <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.name + ' ( '+ item.username + ' ) '"
              :value="item.username">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="姓名" prop="userName">
        <el-input v-model="form.userName" ></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="userPhone">
        <el-input v-model="form.userPhone"></el-input>
      </el-form-item>
      <el-form-item label="图书名称" prop="bookName">
        <el-input v-model="form.bookName" placeholder="请输入名称"></el-input>
      </el-form-item>
    </el-form>

    <div style="text-align: center;margin-top: 30px">
      <el-button type="primary" @click="save" size="medium">确定修改</el-button>
    </div>

  </div>
</template>


<script>

import request from "@/utils/request";

export default {
  name:'EditBook',
  data() {
    return{
      form: {},
      categories: [],
      isLoading:false,
      users:[],
      rules: {
        userId: [{required: true, message: '请输入图书名称', trigger: 'blur'}]
      }
    }
  },
  created() {
    request.get('/user/tree').then(res => {
      this.categories = res.data
    })
    const id = this.$route.query.id
    request.get('/borrow/' + id).then(res => {
      this.form = res.data
    })
  },

  methods: {
    save() {
      request.put('/borrow/update', this.form).then(res => {
        if (res.code === '200') {
          this.$notify.success('修改成功');
          this.$router.push("/bookList");
        } else {
          this.$notify.error(res.msg);
        }
      })
    },
    selUser() {
      const user = this.users.find(v => v.username === this.form.username)
      this.form.userName = user.name
      this.form.userPhone = user.phone
    }
  }
}
</script>