<template>
  <div style="margin-left: 25px;padding: 20px;width: 80%">

    <div style="margin-bottom: 30px">编辑管理员</div>

    <el-form :inline="true" :model="form"  label-width="100px" class="demo-form-inline" >

      <el-form-item label="卡号" prop="username">
        <el-input v-model="form.username" placeholder="请输入卡号"></el-input>
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
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
  name:'EditAdmin',
  data() {
    return{
      form: {},
      isLoading:false
    }
  },
  created() {
    const id = this.$route.query.id
    this.isLoading = true; // 添加加载状态
    request.get("/admin/" + id)
        .then(res => {
          this.form = res.data;
          this.isLoading = false; // 数据加载完毕，取消加载状态
        })
        .catch(error => {
          console.error('Error fetching user:', error);
          this.isLoading = false; // 数据加载失败，取消加载状态
          // 可以在这里添加错误提示的逻辑
        });
  },

  methods: {
    save() {
      // 假设form包含要更新的数据
      request.put('/admin/update', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('修改成功');
              this.$router.push("/adminList");
            } else {
              this.$notify.error(res.msg);
            }
          })
    }
  }
}
</script>