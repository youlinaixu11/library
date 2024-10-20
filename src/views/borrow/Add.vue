<template>
  <div style="margin-left: 25px;padding: 20px;width: 80%">

    <div style="margin-bottom: 30px">新增借阅</div>

    <el-form :inline="true" :rules="rules"  ref="ruleForm" :model="form" label-width="100px">

      <el-form-item label="借阅人卡号" prop="userId">
        <el-select v-model="form.userId" clearable filterable placeholder="请选择" @change="selUser">
          <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.username"
              :value="item.username">
          </el-option>
        </el-select>
      </el-form-item>


<!--      <el-form-item label="借阅人卡号" prop="userId">-->
<!--        <el-input v-model="form.userId" ></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="姓名" prop="userName">
        <el-input v-model="form.userName" ></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="userPhone">
        <el-input v-model="form.userPhone"></el-input>
      </el-form-item>
      <el-form-item label="图书名称" prop="bookName">
        <el-input v-model="form.bookName" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item label="要借的天数" prop="days">
        <el-input-number v-model="form.days" :min="1" :max="35" lable="天数"></el-input-number>
      </el-form-item>
      </el-form>

    <div style="text-align: center;margin-top: 30px">
      <el-button type="primary" @click="save" size="medium">提交</el-button>
    </div>
  </div>
</template>


<script>

import request from "@/utils/request";
import user from "@/views/user/User.vue";

export default {
  name:'AddBook',
  computed: {
    user() {
      return user
    }
  },
  data() {
    return{
      form: { days:1 },
      users: [],
      userId: '',
      rules: {
        userId: [{required: true, message: '请输入用户卡号', trigger: 'blur'}]
      }
    }
  },
  created() {
    request.get('/user/list').then(res => {
      this.users = res.data
    })
  },
  methods: {

    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/borrow/save', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('新增成功');
              this.form = {};
            } else {
              this.$notify.error(res.message);
            }
          }).catch((error) => {
            // 处理请求错误，如网络错误等
            this.$notify.error('出现错误啦');
          })
        }
      })
    },

    selUser() {
      const  user = this.users.find(v => v.username === this.form.userId)
      this.form.userName = user.name
      this.form.userPhone = user.phone
    }
  }

}
</script>