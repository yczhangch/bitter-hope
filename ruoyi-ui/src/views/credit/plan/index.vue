<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="信用卡id" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入信用卡id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="借款日" prop="loanDate">
        <el-date-picker clearable size="small"
                        v-model="queryParams.loanDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择借款日">
        </el-date-picker>
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
        <el-input
          v-model="queryParams.posId"
          placeholder="请输入pos机编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
        <el-input
          v-model="queryParams.isLoaded"
          placeholder="请输入是否借款"
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
      <el-table-column label="编号" align="center" prop="id"/>
      <el-table-column label="信用卡" align="center" prop="cardNum"/>
      <el-table-column label="借款日" align="center" prop="loanDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loanDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="借款金额" align="center" prop="loadMoney" :formatter="stateFormat"/>
      <el-table-column label="pos机" align="center" prop="posName"/>
      <el-table-column label="费用" align="center" prop="fee"/>
      <el-table-column label="是否借款" align="center" prop="isLoaded"/>
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
          <!--          <el-input v-model="form.cardId" placeholder="请输入信用卡"/>-->
          <el-select v-model="form.cardId" placeholder="请选择信用卡">
            <el-option v-for="card in creditCard"
                       :key="card.id"
                       :label="card.cardName"
                       :value="card.id">
            </el-option>
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
          <!--          <el-input v-model="form.posId" placeholder="请输入pos机编号"/>-->
          <el-select v-model="form.posId" placeholder="请选择pos机">
            <el-option v-for="pos in posList"
                       :key="pos.id"
                       :label="pos.posName"
                       :value="pos.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="费用" prop="fee">
          <el-input v-model="form.fee" placeholder="请输入费用"/>
        </el-form-item>
        <el-form-item label="是否借款" prop="isLoaded">
          <el-input v-model="form.isLoaded" placeholder="请输入是否借款"/>
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
    listPlan,
    getPlan,
    delPlan,
    addPlan,
    updatePlan,
    exportPlan,
    getCreditCardList,
    getPosList
  } from '@/api/credit/plan'

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
        rules: {},
        creditCard: [],
        //pos机列表
        posList: []
      }
    },
    created() {
      this.getList()
      this.getCreditCardList()
      this.getPosList()
    },
    methods: {
      /** 查询信用卡借款计划列表 */
      getList() {
        this.loading = true
        listPlan(this.queryParams).then(response => {
          this.planList = response.rows
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
      },
      getCreditCardList() {
        getCreditCardList().then(res => this.creditCard = res.data)
      },
      //格式化金额
      stateFormat(row, column, cellValue) {
        if (cellValue === '') {
          cellValue += ''
        } else {
          cellValue += ' 元'
        }
        if (!cellValue.includes('.')) cellValue += '.';
        return cellValue.replace(/(\d)(?=(\d{3})+\.)/g, function($0, $1) {
            return $1 + ','
          }).replace(/\.$/, '');
      },
      /** pos机列表 */
      getPosList() {
        getPosList().then(res => this.posList = res.data)
      }
    }

  }
</script>
