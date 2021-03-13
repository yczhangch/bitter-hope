<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="信用卡id" prop="cardId">
        <el-select v-model="queryParams.cardId" placeholder="请选择信用卡id" clearable size="small">
          <el-option
            v-for="dict in cardIdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年" prop="year">
        <el-input
          v-model="queryParams.year"
          placeholder="请输入年"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单月份" prop="month">
        <el-input
          v-model="queryParams.month"
          placeholder="请输入账单月份"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单日" prop="billDay">
        <el-date-picker clearable size="small"
          v-model="queryParams.billDay"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择账单日">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="还款日" prop="payBackDay">
        <el-date-picker clearable size="small"
          v-model="queryParams.payBackDay"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择还款日">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="是否还款" prop="isPayed">
        <el-select v-model="queryParams.isPayed" placeholder="请选择是否还款" clearable size="small">
          <el-option
            v-for="dict in isPayedOptions"
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
          v-hasPermi="['credit:payback:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['credit:payback:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['credit:payback:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['credit:payback:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paybackList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="逻辑主键" align="center" prop="id" />
      <el-table-column label="年" align="center" prop="year" />
      <el-table-column label="账单月份" align="center" prop="month" />
      <el-table-column label="账单日" align="center" prop="billDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.billDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="还款日" align="center" prop="payBackDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payBackDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否还款" align="center" prop="isPayed"
                        >
      <!--  :formatter="isPayedFormat"-->
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isPayed"
            active-value="Y"
            inactive-value="N"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['credit:payback:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['credit:payback:remove']"
          >删除</el-button>
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

    <!-- 添加或修改信用卡还款日提醒对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="信用卡id" prop="cardId">
          <el-select v-model="form.cardId" placeholder="请选择信用卡id">
            <el-option
              v-for="dict in cardIdOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年" prop="year">
          <el-input v-model="form.year" placeholder="请输入年" />
        </el-form-item>
        <el-form-item label="账单月份" prop="month">
          <el-input v-model="form.month" placeholder="请输入账单月份" />
        </el-form-item>
        <el-form-item label="账单日" prop="billDay">
          <el-date-picker clearable size="small"
            v-model="form.billDay"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择账单日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="还款日" prop="payBackDay">
          <el-date-picker clearable size="small"
            v-model="form.payBackDay"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择还款日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否还款" prop="isPayed">
          <el-select v-model="form.isPayed" placeholder="请选择是否还款">
            <el-option
              v-for="dict in isPayedOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listPayback, getPayback, delPayback, addPayback, updatePayback, exportPayback } from "@/api/credit/payback";

export default {
  name: "Payback",
  components: {
  },
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
      // 信用卡还款日提醒表格数据
      paybackList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 信用卡id字典
      cardIdOptions: [],
      // 是否还款字典
      isPayedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardId: null,
        year: null,
        month: null,
        billDay: null,
        payBackDay: null,
        isPayed: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cardId: [
          { required: true, message: "信用卡id不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_yes_no").then(response => {
      this.cardIdOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isPayedOptions = response.data;
    });
  },
  methods: {
    /** 查询信用卡还款日提醒列表 */
    getList() {
      this.loading = true;
      listPayback(this.queryParams).then(response => {
        this.paybackList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 信用卡id字典翻译
    cardIdFormat(row, column) {
      return this.selectDictLabel(this.cardIdOptions, row.cardId);
    },
    // 是否还款字典翻译
    isPayedFormat(row, column) {
      return this.selectDictLabel(this.isPayedOptions, row.isPayed);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        cardId: null,
        year: null,
        month: null,
        billDay: null,
        payBackDay: null,
        isPayed: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加信用卡还款日提醒";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPayback(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改信用卡还款日提醒";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePayback(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPayback(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除信用卡还款日提醒编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPayback(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有信用卡还款日提醒数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPayback(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    // 修改是否还款
    handleStatusChange(row) {
      let text = row.status === "Y" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.userName + '"用户吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
  }
};
</script>
