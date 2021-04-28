<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="投资日期" prop="investTime">
        <el-date-picker clearable size="small"
                        v-model="queryParams.investTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择投资日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="币种" prop="currencyType">
        <el-input
          v-model="queryParams.currencyType"
          placeholder="请输入币种"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投资金额" prop="money">
        <el-input
          v-model="queryParams.money"
          placeholder="请输入投资金额"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易类型" prop="tradeType">
        <el-select v-model="queryParams.tradeType" placeholder="请选择交易类型" clearable size="small">
          <el-option label="请选择字典生成" value=""/>
        </el-select>
      </el-form-item>
      <el-form-item label="是否成交" prop="isDone">
        <el-input
          v-model="queryParams.isDone"
          placeholder="请输入是否成交"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成交时间" prop="dealTime">
        <el-date-picker clearable size="small"
                        v-model="queryParams.dealTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择成交时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:invest:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:invest:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:invest:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:invest:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="investList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="逻辑主键" align="center" prop="id"/>
      <el-table-column label="投资日期" align="center" prop="investTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.investTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="币种" align="center" prop="currencyType"/>
      <el-table-column label="投资金额" align="center" prop="money"/>
      <el-table-column label="交易类型" align="center" prop="tradeType"/>
      <el-table-column label="是否成交" align="center" prop="isDone"/>
      <el-table-column label="成交时间" align="center" prop="dealTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dealTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:invest:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:invest:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改比特币投资对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="投资日期" prop="investTime">
          <el-date-picker clearable size="small"
                          v-model="form.investTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择投资日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="币种" prop="currencyType">
          <el-input v-model="form.currencyType" placeholder="请输入币种"/>
        </el-form-item>
        <el-form-item label="投资金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入投资金额"/>
        </el-form-item>
        <el-form-item label="交易类型" prop="tradeType">
          <el-select v-model="form.tradeType" placeholder="请选择交易类型">
            <el-option label="请选择字典生成" value=""/>
          </el-select>
        </el-form-item>
        <el-form-item label="是否成交" prop="isDone">
          <el-input v-model="form.isDone" placeholder="请输入是否成交"/>
        </el-form-item>
        <el-form-item label="成交时间" prop="dealTime">
          <el-date-picker clearable size="small"
                          v-model="form.dealTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择成交时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listInvest,
  getInvest,
  delInvest,
  addInvest,
  updateInvest,
  exportInvest
} from '@/api/invest/btcInvest'

export default {
  name: 'Invest',
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 比特币投资表格数据
      investList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        investTime: null,
        currencyType: null,
        money: null,
        tradeType: null,
        isDone: null,
        dealTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询比特币投资列表 */
    getList() {
      this.loading = true
      listInvest(this.queryParams).then(response => {
        this.investList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        investTime: null,
        currencyType: null,
        money: null,
        tradeType: null,
        isDone: null,
        dealTime: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加比特币投资'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getInvest(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改比特币投资'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInvest(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addInvest(this.form).then(response => {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除比特币投资编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delInvest(ids)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有比特币投资数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return exportInvest(queryParams)
      }).then(response => {
        this.download(response.msg)
      })
    }
  }
}
</script>
