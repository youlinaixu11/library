<template>
  <div style="margin-left: 25px;padding: 20px;width: 80%">

    <div style="margin-bottom: 30px">新增用户</div>

    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px" class="demo-form-inline" >
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio v-model="form.sex" label="男">男</el-radio>
        <el-radio v-model="form.sex" label="女">女</el-radio>
      </el-form-item>


      </el-form>

    <div style="text-align: center;margin-top: 30px">
      <el-button type="primary" @click="save" size="medium ">提交</el-button>
    </div>
    <span style="margin-left: 15px;font-size:12px ;font-family: 微软雅黑">温馨提示:卡号将自动生成,唯一且不可更改</span>
  </div>
</template>


<script>

import request from "@/utils/request";

export default {
  name:'addUser',
  data() {
    const checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'));
      }
      if (!/^[0-9]+$/.test(value)) {
        callback(new Error('请输入数字值'));
      }
      if (parseInt(value) > 150 || parseInt(value) <= 0) {
        callback(new Error('请正确输入年龄'));
      }
      callback()
    };
    const checkphone = (rule, value, callback) => {
      if (!/^1[3,4,5,6,7,8,9][0-9]{9}$/.test(value)) {
        callback(new Error('请正确输入手机号'));
      }
      callback()
    };
    return{
      form: {},
      rules: {
        name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
        age:[{ validator: checkAge, trigger: 'blur' }],
        phone:[{ validator: checkphone, trigger: 'blur' }]
      }
    }
  },
  methods: {
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/user/save',this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('新增成功')
              this.form = {}
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>