<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="信用卡" prop="cardId">
        <el-select v-model="queryParams.cardId" placeholder="请选择信用卡" clearable size="small">
          <el-option
            v-for="dict in cardIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="借款日">
        <el-date-picker
          v-model="daterangeLoanDate"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="借款金额" prop="loadMoney">
        <el-input
          v-model="queryParams.loadMoney"
          placeholder="请输入借款金额"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="pos机" prop="posId">
        <el-select v-model="queryParams.posId" placeholder="请选择pos机" clearable size="small">
          <el-option
            v-for="dict in posIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="费用" prop="fee">
        <el-input
          v-model="queryParams.fee"
          placeholder="请输入费用"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否借款" prop="isLoaded">
        <el-select v-model="queryParams.isLoaded" placeholder="请选择是否借款" clearable size="small">
          <el-option
            v-for="dict in isLoadedOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
          v-hasPermi="['credit:plan:add']"
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
          v-hasPermi="['credit:plan:edit']"
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
          v-hasPermi="['credit:plan:remove']"
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
          v-hasPermi="['credit:plan:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
<!--      <el-table-column label="逻辑主键" align="center" prop="id"/>-->
      <el-table-column label="信用卡" align="center" prop="cardId" :formatter="cardIdFormat"/>
      <el-table-column label="借款日" align="center" prop="loanDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loanDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账单日" align="center">
        <template v-if="scope.row.billDay!=null" slot-scope="scope">每月{{ scope.row.billDay}}日</template>
      </el-table-column>
      <el-table-column label="借款金额" align="center" prop="loadMoney"/>
      <el-table-column label="pos机" align="center" prop="posId" :formatter="posIdFormat"/>
      <el-table-column label="费用" align="center" prop="fee"/>
      <el-table-column label="是否借款" align="center" prop="isLoaded" :formatter="isLoadedFormat"/>
      <el-table-column label="完成状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isLoaded"
            active-value="N"
            active-color = "#67C23A"
            inactive-value="Y"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
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
            v-hasPermi="['credit:plan:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['credit:plan:remove']"
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

    <!-- 添加或修改信用卡借款计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="信用卡" prop="cardId">
          <el-select v-model="form.cardId" placeholder="请选择信用卡">
            <el-option
              v-for="dict in cardIdOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="借款日" prop="loanDate">
          <el-date-picker clearable size="small"
                          v-model="form.loanDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择借款日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="借款金额" prop="loadMoney">
          <el-input v-model="form.loadMoney" placeholder="请输入借款金额"/>
        </el-form-item>
        <el-form-item label="pos机" prop="posId">
          <el-select v-model="form.posId" placeholder="请选择pos机">
            <el-option
              v-for="dict in posIdOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="费用" prop="fee">
          <el-input v-model="form.fee" placeholder="请输入费用"/>
        </el-form-item>
        <el-form-item label="是否借款" prop="isLoaded">
          <el-select v-model="form.isLoaded" placeholder="请选择是否借款">
            <el-option
              v-for="dict in isLoadedOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
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
import { listPlan, getPlan, delPlan, addPlan, updatePlan, exportPlan,changeLoadStatus } from '@/api/credit/plan'

export default {
  name: 'Plan',
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
      // 信用卡借款计划表格数据
      planList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 信用卡字典
      cardIdOptions: [],
      // 借款日时间范围
      daterangeLoanDate: [],
      // pos机字典
      posIdOptions: [],
      // 是否借款字典
      isLoadedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardId: null,
        loanDate: null,
        loadMoney: null,
        posId: null,
        fee: null,
        isLoaded: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cardId: [
          { required: true, message: '信用卡不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getDicts('credit_card_list').then(response => {
      this.cardIdOptions = response.data
    })
    this.getDicts('pos_list').then(response => {
      this.posIdOptions = response.data
    })
    this.getDicts('sys_yes_no').then(response => {
      this.isLoadedOptions = response.data
    })
  },
  methods: {
    /** 查询信用卡借款计划列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeLoanDate && '' != this.daterangeLoanDate) {
        this.queryParams.params['beginLoanDate'] = this.daterangeLoanDate[0]
        this.queryParams.params['endLoanDate'] = this.daterangeLoanDate[1]
      }
      listPlan(this.queryParams).then(response => {
        this.planList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 信用卡字典翻译
    cardIdFormat(row, column) {
      return this.selectDictLabel(this.cardIdOptions, row.cardId)
    },
    // pos机字典翻译
    posIdFormat(row, column) {
      return this.selectDictLabel(this.posIdOptions, row.posId)
    },
    // 是否借款字典翻译
    isLoadedFormat(row, column) {
      return this.selectDictLabel(this.isLoadedOptions, row.isLoaded)
    },
    // 状态修改
    handleStatusChange(row) {
      let text = row.isLoaded === 'Y' ? "确认完成？" :"确认取消？"
      this.$confirm(text, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return changeLoadStatus(row.isLoaded,row.id);
      }).then(() => {
        this.msgSuccess("成功");
      }).catch(function() {
        row.isLoaded = row.isLoaded === "Y" ? "N" : "Y";
      });
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
        cardId: null,
        loanDate: null,
        loadMoney: null,
        posId: null,
        fee: null,
        isLoaded: null,
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
      this.daterangeLoanDate = []
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
      this.title = '添加信用卡借款计划'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPlan(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改信用卡借款计划'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePlan(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addPlan(this.form).then(response => {
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
      this.$confirm('是否确认删除信用卡借款计划编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delPlan(ids)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有信用卡借款计划数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return exportPlan(queryParams)
      }).then(response => {
        this.download(response.msg)
      })
    }
  }
}
</script>
