
<template>
  <div>

    <!-- 搜索表单-->
    <div style="margin-bottom: 10px;margin-top: 10px;margin-left: 15px">
      <el-input style="width: 200px" placeholder="请输入图书名称" v-model="params.name"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
    </div>

    <!--表格-->
      <el-table :data="tableData" stripe row-key="id">
      <el-table-column prop="id" label="编号" width="70"></el-table-column>
      <el-table-column prop="nums" label="剩余数量" width="70"></el-table-column>
      <el-table-column prop="name" label="图书名称" ></el-table-column>
      <el-table-column prop="author" label="作者" ></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="publishDate" label="出版日期" width="100px"></el-table-column>
        <el-table-column prop="category" label="类型" width="100px"></el-table-column>
        <el-table-column prop="cover" label="封面">
          <template v-slot="scope">
            <el-image :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" width="200px"></el-table-column>
        <el-table-column prop="createtime" label="创建时间" width="100px"></el-table-column>
        <el-table-column prop="updatetime" label="修改时间" width="100px"></el-table-column>

      <el-table-column label="操作" width="300">

        <template v-slot="scope">

          <el-button type="primary" @click="$router.push('/editBook?id=' + scope.row.id)">编辑</el-button>

          <el-popconfirm
              style="margin-left: 5px"
              title="确定删除吗（删除后不可恢复）"
              @confirm="del(scope.row.id)">
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
import Cookies from "js-cookie";

export default {
  name: 'BookList',
  data() {
    return {
      admin:Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData:[],
      total:0,
      params: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        bookNO: ''
      },
    }
  },
  created() {
    this.load()
  },
  methods:{

    load() {

      request.get('/book/page' , {
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
        bookNO:'',
      }
      this.load()
    },

    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum
      this.load()
    },

    del(id) {
      request.delete("/book/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },


  }
}


</script>

<style scoped>

</style>