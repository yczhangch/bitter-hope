<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="卡号" prop="cardNum">
        <el-input
          v-model="queryParams.cardNum"
          placeholder="请输入卡号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="信用卡名" prop="cardName">
        <el-input
          v-model="queryParams.cardName"
          placeholder="请输入信用卡名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="额度(元)" prop="creditLimit">
        <el-input
          v-model="queryParams.creditLimit"
          placeholder="请输入额度(元)"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单日" prop="billDay">
        <el-input
          v-model="queryParams.billDay"
          placeholder="请输入账单日"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="还款日" prop="repayDay">
        <el-input
          v-model="queryParams.repayDay"
          placeholder="请输入还款日"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
<!--      </el-form-item>-->
<!--      <el-form-item label="年费(元)" prop="annualFee">-->
<!--        <el-input-->
<!--          v-model="queryParams.annualFee"-->
<!--          placeholder="请输入年费(元)"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
      </el-form-item>
      <el-form-item label="发卡银行" prop="bank">
        <el-input
          v-model="queryParams.bank"
          placeholder="请输入发卡银行"
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
          v-hasPermi="['credit:card:add']"
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
          v-hasPermi="['credit:card:edit']"
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
          v-hasPermi="['credit:card:remove']"
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
          v-hasPermi="['credit:card:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="逻辑主键" align="center" prop="id" />-->
      <el-table-column label="信用卡名" align="center" prop="cardName"/>
      <el-table-column label="卡号" align="center" prop="cardNum"/>
      <el-table-column label="额度(元)" align="center" prop="creditLimit"/>
      <el-table-column label="账单日" align="center">
        <template v-if="scope.row.billDay!=null" slot-scope="scope">每月{{ scope.row.billDay}}日</template>
      </el-table-column>
      <el-table-column label="还款日" align="center">
        <template v-if="scope.row.repayDay!=null" slot-scope="scope">次月{{ scope.row.repayDay}}日</template>
      </el-table-column>
      <el-table-column label="年费(元)" align="center" prop="annualFee"/>
      <el-table-column label="发卡银行" align="center" prop="bank"/>
      <el-table-column label="备注" align="center" :show-overflow-tooltip="true" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['credit:card:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['credit:card:remove']"
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

    <!-- 添加或修改信用卡对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="信用卡名" prop="cardName">
          <el-input v-model="form.cardName" placeholder="请输入信用卡名"/>
        </el-form-item>
        <el-form-item label="卡号" prop="cardNum">
          <el-input v-model="form.cardNum" placeholder="请输入卡号"/>
        </el-form-item>
        <el-form-item label="额度(元)" prop="creditLimit">
          <el-input v-model="form.creditLimit" placeholder="请输入额度(元)"/>
        </el-form-item>
        <el-form-item label="账单日" prop="billDay">
          <el-input v-model="form.billDay" placeholder="请输入账单日"/>
        </el-form-item>
        <el-form-item label="还款日" prop="repayDay">
          <el-input v-model="form.repayDay" placeholder="请输入还款日"/>
        </el-form-item>
        <el-form-item label="年费(元)" prop="annualFee">
          <el-input v-model="form.annualFee" placeholder="请输入年费(元)"/>
        </el-form-item>
        <el-form-item label="发卡银行" prop="bank">
          <el-input v-model="form.bank" placeholder="请输入发卡银行"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注"/>
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
  import { listCard, getCard, delCard, addCard, updateCard, exportCard } from '@/api/credit/card'

  export default {
    name: 'Card',
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
        // 信用卡表格数据
        cardList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          cardNum: null,
          cardName: null,
          creditLimit: null,
          billDay: null,
          repayDay: null,
          annualFee: null,
          bank: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          cardNum: [
            { required: true, message: '卡号不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      /** 查询信用卡列表 */
      getList() {
        this.loading = true
        listCard(this.queryParams).then(response => {
          this.cardList = response.rows
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
          cardNum: null,
          cardName: null,
          creditLimit: null,
          billDay: null,
          repayDay: null,
          annualFee: null,
          bank: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          remark: null
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
        this.title = '添加信用卡'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const id = row.id || this.ids
        getCard(id).then(response => {
          this.form = response.data
          this.open = true
          this.title = '修改信用卡'
        })
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateCard(this.form).then(response => {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
            } else {
              addCard(this.form).then(response => {
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
        this.$confirm('是否确认删除信用卡编号为"' + ids + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delCard(ids)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams
        this.$confirm('是否确认导出所有信用卡数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return exportCard(queryParams)
        }).then(response => {
          this.download(response.msg)
        })
      }
    }
  }
</script>
