
<template>
  <div>

    <!-- 搜索表单-->
    <div style="margin-bottom: 10px;margin-top: 10px;margin-left: 15px">
      <el-input style="width: 200px" placeholder="请输入用户名" v-model="params.username"></el-input>
      <el-input style="width: 200px; margin-left:5px"  placeholder="请输入电话" v-model="params.phone"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
    </div>
    <!--表格-->
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="编号" width="75"></el-table-column>
      <el-table-column prop="username" label="用户名" width="150"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="联系方式" width="170"></el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column prop="updatetime" label="修改时间"></el-table-column>

      <el-table-column prop="updatetime" label="状态" width="150px">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.status"
              @change="changeStatus(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="启用"
              inactive-text="禁用">
          </el-switch>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="240">

        <template v-slot="scope">

          <el-button type="primary" @click="$router.push('/editAdmin?id=' + scope.row.id)">编辑</el-button>

          <el-popconfirm
              style="margin-left: 5px"
              title="确定删除吗（删除后不可恢复）"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>

          <el-button style="margin-left: 5px" size="small" type="warning" @click="handleChangePass(scope.row)">修改密码</el-button>

        </template>
      </el-table-column>
    </el-table>

    <!--分页-->
    <div style="margin-top: 20px">
      <el-pagination
          background
          :page-size="params.pageSize"
          :current-page="params.pageNum"
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" lable-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="新密码" prop="newPass">
          <el-input v-model="form.newPass" autocomplete="off" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePass">确 定</el-button>
      </div>
      <span style="margin-left: 15px;font-size:12px ;font-family: 微软雅黑">温馨提示:如果修改当前帐号密码，则需要重新登录</span>
    </el-dialog>

  </div>
</template>

<script>


import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'AdminList',
  data() {
    return {
      admin:Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData:[],
      total:0,
      form: {},
      dialogFormVisible:false,
      params: {
        pageNum: 1,
        pageSize: 10,
        username: '',
        phone: '',
        // email: ''
      },
      rules: {
        newPass: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 3, max: 10, message: '长度请在3~10个字符', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods:{

    changeStatus(row) {
      if (this.admin.id === row.id && !row.status) {
        row.status = true
        this.$notify.warning('不可禁用已登录账号')
        return
      }
      request.put('/admin/update', row).then(res => {
        if (res.code === '200') {
          this.$notify.success('操作成功');
          this.load()
        } else {
          this.$notify.error(res.msg);
        }
      })
    },

    handleChangePass(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },

    savePass() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          request.put('/admin/password', this.form).then(res => {
           if (res.code === '200') {
             this.$notify.success("修改成功")
             if (this.form.id === this.admin.id) {
               Cookies.remove('admin')
               this.$router.push('/login')
             } else {
               this.load()
               this.dialogFormVisible = false
             }
           } else {
             this.$notify.error("修改失败")
           }
          })
        }
      });
    },


    load() {

      request.get('/admin/page' , {
        params:this.params
      }).then(res => {
        if (res.code === '200'){
          this.tableData = res.data.list
          this.total = res.data.total
        }
      })
    },

    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        username: '',
        phone:'',
        // email: ''
      }
      this.load()
    },

    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum
      this.load()
    },

    del(id) {
      if (id === this.admin.id) {
        this.$notify.error("不可删除已登录账号")
        return
      }
      request.delete("/admin/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}


</script>

<style scoped>

</style>