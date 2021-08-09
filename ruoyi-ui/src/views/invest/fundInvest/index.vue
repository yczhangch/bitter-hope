<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="投资编号" prop="investNo">
        <el-input
          v-model="queryParams.investNo"
          placeholder="请输入投资编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="父节点" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          placeholder="请输入父节点"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金" prop="fund">
        <el-select v-model="queryParams.fund" placeholder="请选择基金" clearable size="small">
          <el-option
            v-for="dict in fundOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="投资日期">
        <el-date-picker
          v-model="daterangeInvestTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          <el-option
            v-for="dict in tradeTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否成交" prop="isDone">
        <el-select v-model="queryParams.isDone" placeholder="请选择是否成交" clearable size="small">
          <el-option
            v-for="dict in isDoneOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="成交价格" prop="dealPrice">
        <el-input
          v-model="queryParams.dealPrice"
          placeholder="请输入成交价格"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成交数量" prop="dealAmount">
        <el-input
          v-model="queryParams.dealAmount"
          placeholder="请输入成交数量"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成交时间">
        <el-date-picker
          v-model="daterangeDealTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="收益" prop="profit">
        <el-input
          v-model="queryParams.profit"
          placeholder="请输入收益"
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
          v-hasPermi="['invest:fundInvest:add']"
        >新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="fundInvestList"
      row-key="id"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      :row-class-name="tableRowClassName"
    >
      <el-table-column label="投资编号" align="center" prop="id"/>
      <!--      <el-table-column label="父节点" align="center" prop="parentId" />-->
      <el-table-column label="基金" align="center" width="240" prop="fund" :formatter="fundFormat"/>
      <el-table-column label="投资日期" align="center" prop="investTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.investTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投资金额" align="center" prop="money"/>
      <el-table-column label="投资完成" align="center" prop="isDone" :formatter="isDoneFormat"/>
      <el-table-column label="收益率/预估收益率" width="150" align="center" prop="profit"/>
      <el-table-column label="收益" align="center" prop="profit"/>
      <el-table-column label="成交数量" align="center" prop="dealAmount"/>
      <el-table-column label="成交价格" align="center" prop="dealPrice"/>
      <el-table-column label="交易类型" align="center" prop="tradeType" :formatter="tradeTypeFormat"/>
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
            v-hasPermi="['invest:fundInvest:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['invest:fundInvest:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改基金投资对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="投资编号" prop="investNo">
          <el-input v-model="form.investNo" placeholder="请输入投资编号"/>
        </el-form-item>
        <el-form-item label="父节点" prop="parentId">
          <treeselect v-model="form.parentId" :options="fundInvestOptions" :normalizer="normalizer"
                      placeholder="请选择父节点"/>
        </el-form-item>
        <el-form-item label="基金" prop="fund">
          <el-select v-model="form.fund" placeholder="请选择基金">
            <el-option
              v-for="dict in fundOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="投资日期" prop="investTime">
          <el-date-picker clearable size="small"
                          v-model="form.investTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择投资日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="投资金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入投资金额"/>
        </el-form-item>
        <el-form-item label="交易类型" prop="tradeType">
          <el-select v-model="form.tradeType" placeholder="请选择交易类型">
            <el-option
              v-for="dict in tradeTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否成交" prop="isDone">
          <el-select v-model="form.isDone" placeholder="请选择是否成交">
            <el-option
              v-for="dict in isDoneOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="成交价格" prop="dealPrice">
          <el-input v-model="form.dealPrice" placeholder="请输入成交价格"/>
        </el-form-item>
        <el-form-item label="成交数量" prop="dealAmount">
          <el-input v-model="form.dealAmount" placeholder="请输入成交数量"/>
        </el-form-item>
        <el-form-item label="成交时间" prop="dealTime">
          <el-date-picker clearable size="small"
                          v-model="form.dealTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择成交时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="收益" prop="profit">
          <el-input v-model="form.profit" placeholder="请输入收益"/>
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
  listFundInvest,
  getFundInvest,
  delFundInvest,
  addFundInvest,
  updateFundInvest,
  exportFundInvest
} from '@/api/invest/fundInvest'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'FundInvest',
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 基金投资表格数据
      fundInvestList: [],
      // 基金投资树选项
      fundInvestOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 基金字典
      fundOptions: [],
      // 投资日期时间范围
      daterangeInvestTime: [],
      // 交易类型字典
      tradeTypeOptions: [],
      // 是否成交字典
      isDoneOptions: [],
      // 成交时间时间范围
      daterangeDealTime: [],
      // 查询参数
      queryParams: {
        investNo: null,
        parentId: null,
        fund: null,
        investTime: null,
        money: null,
        tradeType: null,
        isDone: null,
        dealPrice: null,
        dealAmount: null,
        dealTime: null,
        profit: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    }
  },
  created() {
    this.getList()
    this.getDicts('fund_type').then(response => {
      this.fundOptions = response.data
    })
    this.getDicts('t_trade_type').then(response => {
      this.tradeTypeOptions = response.data
    })
    this.getDicts('sys_yes_no').then(response => {
      this.isDoneOptions = response.data
    })
  },
  methods: {
    /** 查询基金投资列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeInvestTime && '' != this.daterangeInvestTime) {
        this.queryParams.params['beginInvestTime'] = this.daterangeInvestTime[0]
        this.queryParams.params['endInvestTime'] = this.daterangeInvestTime[1]
      }
      if (null != this.daterangeDealTime && '' != this.daterangeDealTime) {
        this.queryParams.params['beginDealTime'] = this.daterangeDealTime[0]
        this.queryParams.params['endDealTime'] = this.daterangeDealTime[1]
      }
      listFundInvest(this.queryParams).then(response => {
        this.fundInvestList = this.handleTree(response.data, 'id', 'parentId')
        this.loading = false
      })
    },
    /** 转换基金投资数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.id,
        label: node.investNo,
        children: node.children
      }
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      listFundInvest().then(response => {
        this.fundInvestOptions = []
        const data = { id: 0, investNo: '顶级节点', children: [] }
        data.children = this.handleTree(response.data, 'id', 'parentId')
        this.fundInvestOptions.push(data)
      })
    },
    // 基金字典翻译
    fundFormat(row, column) {
      return this.selectDictLabel(this.fundOptions, row.fund)
    },
    // 交易类型字典翻译
    tradeTypeFormat(row, column) {
      return this.selectDictLabel(this.tradeTypeOptions, row.tradeType)
    },
    // 是否成交字典翻译
    isDoneFormat(row, column) {
      return this.selectDictLabel(this.isDoneOptions, row.isDone)
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
        investNo: null,
        parentId: null,
        fund: null,
        investTime: null,
        money: null,
        tradeType: null,
        isDone: null,
        dealPrice: null,
        dealAmount: null,
        dealTime: null,
        profit: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
      this.resetForm('form')
    },
    // if(row.tradeType ==='0' &&  row.isDone ==='N'){
    //   return 'success-row';
    // }
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row'
      } else if (rowIndex === 3) {
        return 'success-row'
      }
      return ''
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeInvestTime = []
      this.daterangeDealTime = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getTreeselect()
      this.open = true
      this.title = '添加基金投资'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      if (row != null) {
        this.form.parentId = row.id
      }
      getFundInvest(row.id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改基金投资'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFundInvest(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addFundInvest(this.form).then(response => {
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
      this.$confirm('是否确认删除基金投资编号为"' + row.id + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delFundInvest(row.id)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    }
  }
}
</script>
