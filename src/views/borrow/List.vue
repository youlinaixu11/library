
<template>
  <div>

    <!-- 搜索表单-->
    <div style="margin-bottom: 10px;margin-top: 10px;margin-left: 15px">
      <el-input style="width: 200px" placeholder="请输入图书名称" v-model="params.bookName"></el-input>
      <el-input style="width: 200px;margin-left: 5px" placeholder="请输入用户名称" v-model="params.userName"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
    </div>

    <!--表格-->
          <el-table :data="tableData" stripe row-key="id">
          <el-table-column prop="id" label="编号" width="70"></el-table-column>
          <el-table-column prop="bookName" label="图书名称" ></el-table-column>
          <el-table-column prop="userName" label="用户名称"></el-table-column>
          <el-table-column prop="userId" label="借阅人卡号" ></el-table-column>
          <el-table-column prop="userPhone" label="用户联系方式"></el-table-column>
          <el-table-column prop="createtime" label="借阅时间"></el-table-column>
          <el-table-column prop="updatetime" label="应还书时间"></el-table-column>
          <el-table-column prop="days" label="总借阅时间(天)"></el-table-column>
          <el-table-column prop="returnDate" label="实际还书时间"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>
          <el-table-column prop="note" label="还书提醒">
            <template v-slot="scope">
              <el-tag type="success" v-if="scope.row.note === '正常借阅'" >{{scope.row.note}}</el-tag>
              <el-tag type="primary" v-if="scope.row.note === '即将到期'" >{{scope.row.note}}</el-tag>
              <el-tag type="warning" v-if="scope.row.note === '已到期'" >{{scope.row.note}}</el-tag>
              <el-tag type="danger" v-if="scope.row.note === '逾期未还'" >{{scope.row.note}}</el-tag>
            </template>
          </el-table-column>

      <el-table-column label="操作" width="140px">
        <template v-slot="scope">
          <el-button type="primary" @click="returnBooks(scope.row)" v-if="scope.row.status === '借出' ">还书</el-button>
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
  name: 'BorrowList',
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

      request.get('/borrow/page' , {
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
        userName: '',
        bookName:'',
      }
      this.load()
    },

    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum
      this.load()
    },

    del(id) {
      request.delete("/borrow/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    returnBooks(row) {
      request.post("/borrow/saveRetur", row).then(res => {
        if (res.code === '200') {
          this.$notify.success('还书成功')
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