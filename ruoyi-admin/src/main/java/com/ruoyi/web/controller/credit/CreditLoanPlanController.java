package com.ruoyi.web.controller.credit;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.credit.domain.CreditLoanPlan;
import com.ruoyi.credit.service.ICreditLoanPlanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 信用卡借款计划Controller
 *
 * @author hope
 * @date 2021-03-17
 */
@RestController
@RequestMapping("/credit/plan")
public class CreditLoanPlanController extends BaseController {
    @Autowired
    private ICreditLoanPlanService creditLoanPlanService;

    /**
     * 查询信用卡借款计划列表
     */
   // @PreAuthorize("@ss.hasPermi('credit:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(CreditLoanPlan creditLoanPlan) {
        startPage();
        List<CreditLoanPlan> list = creditLoanPlanService.selectCreditLoanPlanList(creditLoanPlan);
        return getDataTable(list);
    }

    /**
     * 导出信用卡借款计划列表
     */
   // @PreAuthorize("@ss.hasPermi('credit:plan:export')")
    @Log(title = "信用卡借款计划", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CreditLoanPlan creditLoanPlan) {
        List<CreditLoanPlan> list = creditLoanPlanService.selectCreditLoanPlanList(creditLoanPlan);
        ExcelUtil<CreditLoanPlan> util = new ExcelUtil<CreditLoanPlan>(CreditLoanPlan.class);
        return util.exportExcel(list, "plan");
    }

    /**
     * 获取信用卡借款计划详细信息
     */
  //  @PreAuthorize("@ss.hasPermi('credit:plan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(creditLoanPlanService.selectCreditLoanPlanById(id));
    }

    /**
     * 新增信用卡借款计划
     */
   // @PreAuthorize("@ss.hasPermi('credit:plan:add')")
    @Log(title = "信用卡借款计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CreditLoanPlan creditLoanPlan) {
        return toAjax(creditLoanPlanService.insertCreditLoanPlan(creditLoanPlan));
    }

    /**
     * 修改信用卡借款计划
     */
    @PreAuthorize("@ss.hasPermi('credit:plan:edit')")
    @Log(title = "信用卡借款计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CreditLoanPlan creditLoanPlan) {
        return toAjax(creditLoanPlanService.updateCreditLoanPlan(creditLoanPlan));
    }

    /**
     * 删除信用卡借款计划
     */
  //  @PreAuthorize("@ss.hasPermi('credit:plan:remove')")
    @Log(title = "信用卡借款计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(creditLoanPlanService.deleteCreditLoanPlanByIds(ids));
    }
}
