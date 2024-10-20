
<template>
  <div>

    <!-- 搜索表单-->
    <div style="margin-bottom: 10px;margin-top: 10px;margin-left: 15px">
      <el-input style="width: 200px" placeholder="请输入分类名称" v-model="params.name"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset"><i class="el-icon-refresh"></i>重置</el-button>
    </div>
    <!--表格-->
    <el-table :data="tableData" stripe row-key="id">
      <el-table-column prop="id" label="编号" width="100"></el-table-column>
      <el-table-column prop="name" label="名称" width="150"></el-table-column>
      <el-table-column prop="remark" label="备注" width="250"></el-table-column>
      <el-table-column prop="createtime" label="创建时间" width="180"></el-table-column>
      <el-table-column prop="updatetime" label="修改时间"></el-table-column>


      <el-table-column label="操作" width="300">

        <template v-slot="scope">

          <el-button type="primary" @click="$router.push('/editCategory?id=' + scope.row.id)">编辑</el-button>

          <el-popconfirm
              style="margin-left: 5px"
              title="确定删除吗（删除后不可恢复）"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>

          <el-button style="width: 110px;margin-left: 5px" type="success" @click="handleAdd(scope.row)">添加次级分类</el-button>

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

    <el-dialog title="添加二级分类" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" lable-width="100px" ref="ruleForm" :rules="rules" style="width: 90%">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分类备注" prop="remark">
          <el-input v-model="form.remark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <span style="margin-left: 15px;font-size:12px ;font-family: 微软雅黑">温馨提示:可点击左端以查看次级分类</span>

  </div>
</template>

<script>


import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'CategoryList',
  data() {
    return {
      admin:Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData:[],
      total:0,
      dialogFormVisible: false,
      form: {},
      pid: null,
      params: {
        pageNum: 1,
        pageSize: 10,
        name: '',
      },
      rules: {
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'},
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods:{

    load() {

      request.get('/category/page' , {
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
      }
      this.load()
    },

    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum
      this.load()
    },

    del(id) {
      if (id === this.category.id) {
        this.$notify.error("不可删除已登录账号")
        return
      }
      request.delete("/category/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$notify.success('删除成功')
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },

    handleAdd(row) {
      // 将当前行的id作为二级分类的pid
      this.pid = row.id
      this.dialogFormVisible = true
    },

    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          this.form.pid = this.pid
          request.post('/category/save',this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('新增成功')
              this.form = {}
              this.dialogFormVisible = false
              this.load()
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

<style scoped>

</style>