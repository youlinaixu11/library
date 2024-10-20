<template>
  <div style="margin-left: 25px;padding: 20px;width: 80%">

    <div style="margin-bottom: 30px">新增图书</div>

    <el-form :inline="true" :rules="rules"  ref="ruleForm" :model="form" label-width="100px">

      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="form.author" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item label="数量" prop="nums">
        <el-input v-model="form.nums" placeholder="请输入数量"></el-input>
      </el-form-item>
      <el-form-item label="出版日期" prop="publishDate">
        <el-date-picker
            v-model="form.publishDate"
            align="right"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择出版日期"
            :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="出版社" prop="publisher">
        <el-input v-model="form.publisher" placeholder="请输入出版社"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="category">
        <el-cascader
            v-model="form.categories"
            :options="categories"
            :props="{ value: 'name', label: 'name' }"
            @change="handleChange">
        </el-cascader>
      </el-form-item>
      <el-form-item label="封面" prop="cover">
        <el-input v-model="form.cover" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" style="width: 365px" v-model="form.description" placeholder="请输入描述"></el-input>
      </el-form-item>

      </el-form>

    <div style="text-align: center;margin-top: 30px">
      <el-button type="primary" @click="save" size="medium">提交</el-button>
    </div>
  </div>
</template>


<script>

import request from "@/utils/request";

export default {
  name:'AddBook',
  data() {
    const checkNums = (rule, value, callback) => {
      value = parseInt(value)
      if (!Number.isInteger(value) || value < 0) {
        callback(new Error('请正确输入数量'));
      }
      callback()
    }
    return{
      form: {},
      categories: [],
      rules: {
        name: [{required: true, message: '请输入书籍名称', trigger: 'blur'}],
        nums: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {validate:checkNums, trigger: 'blur'}]
      }
    }
  },
  created() {
    request.get('/category/tree').then(res => {
     this.categories = res.data
    })
  },
  methods: {
    // options,
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/book/save',this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('新增成功')
              this.form = {}
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    },
    handleChange(val) {
      console.log(val)
    }
  }
}
</script>