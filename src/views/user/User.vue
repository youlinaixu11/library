
<template>
  <div>

    <!-- 搜索表单-->
    <div style="margin-bottom: 10px;margin-top: 10px;margin-left: 15px">
      <el-input style="width: 200px" placeholder="请输入姓名" v-model="params.name"></el-input>
      <el-input style="width: 200px; margin-left:5px"  placeholder="请输入电话" v-model="params.phone"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
    </div>
    <!--表格-->
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="编号" width="75"></el-table-column>
      <el-table-column prop="name" label="姓名" width="150"></el-table-column>
      <el-table-column prop="age" label="年龄" width="85"></el-table-column>
      <el-table-column prop="phone" label="联系方式" width="170"></el-table-column>
      <el-table-column prop="sex" label="性别" width="80"></el-table-column>
      <el-table-column prop="username" label="卡号" ></el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column prop="updatetime" label="修改时间"></el-table-column>

      <el-table-column label="操作">

        <template v-slot="scope">

          <el-button type="primary" @click="$router.push('/editUser?id=' + scope.row.id)">编辑</el-button>

          <el-popconfirm
              style="margin-left: 5px"
              title="确定删除吗（删除后不可恢复）"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>

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

  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'User',
  data() {
    return {
      tableData:[],
      total:0,
      params: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        phone:''
      }
    }
  },
  created() {
    this.load()
  },
  methods:{
    load() {

      // fetch('http://localhost:9090/user/list').then(res => res.json()).then(res => {
      //   console.log(res)
      //   this.tableData = res
      // })

      request.get('/user/page' , {
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
        name: '',
        phone:''
      }
      this.load()
    },

    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum
      this.load()
    },

    del(id) {
      request.delete("/user/delete/" + id).then(res => {
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