<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="交易类型" prop="posTradeType">
        <el-select v-model="queryParams.posTradeType" placeholder="请选择" clearable size="small">
          <el-option
            v-for="dict in posTradeTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="pos机" prop="posId">
        <el-select v-model="queryParams.posId" placeholder="请选择pos机" clearable size="small">
          <el-option label="请选择字典生成" value=""/>
        </el-select>
      </el-form-item>
      <el-form-item label="交易金额" prop="money">
        <el-input
          v-model="queryParams.money"
          placeholder="请输入交易金额"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="到账金额" prop="received">
        <el-input
          v-model="queryParams.received"
          placeholder="请输入到账金额"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手续费" prop="fee">
        <el-input
          v-model="queryParams.fee"
          placeholder="请输入手续费"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['credit:posHistory:add']"
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
          v-hasPermi="['credit:posHistory:edit']"
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
          v-hasPermi="['credit:posHistory:remove']"
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
          v-hasPermi="['credit:posHistory:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="posHistoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编号" align="center" prop="id"/>
      <el-table-column label="交易时间" align="center" prop="createTime"/>
      <el-table-column label="pos机" align="center" prop="posId"/>
      <el-table-column label="交易类型" align="center" prop="posTradeType" :formatter="posTradeTypeFormat"/>
      <el-table-column label="交易金额" align="center" prop="money"/>
      <el-table-column label="到账金额" align="center" prop="received"/>
      <el-table-column label="手续费" align="center" prop="fee"/>
      <el-table-column label="备注(失败原因、其他)" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['credit:posHistory:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['credit:posHistory:remove']"
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

    <!-- 添加或修改pos机交易历史对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="交易类型" prop="posTradeType">
          <el-select v-model="form.posTradeType" placeholder="请选择">
            <el-option
              v-for="dict in posTradeTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="pos机" prop="posId">
          <!--          <el-input v-model="form.posId" placeholder="请输入pos机编号"/>-->
          <el-select v-model="form.posId" placeholder="请选择pos机">
            <el-option v-for="pos in posList"
                       :key="pos.id"
                       :label="pos.posName"
                       :value="pos.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入交易金额"/>
        </el-form-item>
        <el-form-item label="到账金额" prop="received">
          <el-input v-model="form.received" placeholder="请输入到账金额"/>
        </el-form-item>
        <el-form-item label="手续费" prop="fee">
          <el-input v-model="form.fee" placeholder="请输入手续费"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入失败原因、其他注意事项"/>
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
  listPosHistory,
  getPosHistory,
  delPosHistory,
  addPosHistory,
  updatePosHistory,
  exportPosHistory,
  getPosList
} from '@/api/credit/posHistory'

export default {
  name: 'PosHistory',
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
      // pos机交易历史表格数据
      posHistoryList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 0-支付宝扫码，1-微信扫码，2-刷卡收款，3-云闪付字典
      posTradeTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        posTradeType: null,
        posId: null,
        money: null,
        received: null,
        fee: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  created() {
    this.getList()
    this.getDicts('t_pos_trade_type').then(response => {
      this.posTradeTypeOptions = response.data
    })
    this.getPosList()
  },
  methods: {
    /** 查询pos机交易历史列表 */
    getList() {
      this.loading = true
      listPosHistory(this.queryParams).then(response => {
        this.posHistoryList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 0-支付宝扫码，1-微信扫码，2-刷卡收款，3-云闪付字典翻译
    posTradeTypeFormat(row, column) {
      return this.selectDictLabel(this.posTradeTypeOptions, row.posTradeType)
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
        posTradeType: null,
        posId: null,
        money: null,
        received: null,
        fee: null,
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
      this.title = '添加pos机交易历史'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPosHistory(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改pos机交易历史'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePosHistory(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addPosHistory(this.form).then(response => {
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
      this.$confirm('是否确认删除pos机交易历史编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delPosHistory(ids)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有pos机交易历史数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return exportPosHistory(queryParams)
      }).then(response => {
        this.download(response.msg)
      })
    },
    /** pos机列表 */
    getPosList() {
      getPosList().then(res => this.posList = res.data)
    }
  }
}
</script>
