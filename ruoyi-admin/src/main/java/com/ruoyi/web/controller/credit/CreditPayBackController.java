package com.ruoyi.web.controller.credit;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.credit.domain.CreditPayBack;
import com.ruoyi.credit.service.ICreditPayBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信用卡还款日提醒Controller
 *
 * @author hope
 * @date 2021-03-12
 */
@RestController
@RequestMapping("/credit/payback")
public class CreditPayBackController extends BaseController {
    @Autowired
    private ICreditPayBackService creditPayBackService;

    /**
     * 查询信用卡还款日提醒列表
     */
//    @PreAuthorize("@ss.hasPermi('credit:payback:list')")
    @GetMapping("/list")
    public TableDataInfo list(CreditPayBack creditPayBack) {
        startPage();
        List<CreditPayBack> list = creditPayBackService.selectCreditPayBackList(creditPayBack);
        return getDataTable(list);
    }

    /**
     * 导出信用卡还款日提醒列表
     */
//    @PreAuthorize("@ss.hasPermi('credit:payback:export')")
    @Log(title = "信用卡还款日提醒", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CreditPayBack creditPayBack) {
        List<CreditPayBack> list = creditPayBackService.selectCreditPayBackList(creditPayBack);
        ExcelUtil<CreditPayBack> util = new ExcelUtil<>(CreditPayBack.class);
        return util.exportExcel(list, "payback");
    }

    /**
     * 获取信用卡还款日提醒详细信息
     */
//    @PreAuthorize("@ss.hasPermi('credit:payback:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(creditPayBackService.selectCreditPayBackById(id));
    }

    /**
     * 新增信用卡还款日提醒
     */
//    @PreAuthorize("@ss.hasPermi('credit:payback:add')")
    @Log(title = "信用卡还款日提醒", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CreditPayBack creditPayBack) {
        return toAjax(creditPayBackService.insertCreditPayBack(creditPayBack));
    }

    /**
     * 修改信用卡还款日提醒
     */
//    @PreAuthorize("@ss.hasPermi('credit:payback:edit')")
    @Log(title = "信用卡还款日提醒", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CreditPayBack creditPayBack) {
        return toAjax(creditPayBackService.updateCreditPayBack(creditPayBack));
    }

    /**
     * 删除信用卡还款日提醒
     */
//    @PreAuthorize("@ss.hasPermi('credit:payback:remove')")
    @Log(title = "信用卡还款日提醒", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(creditPayBackService.deleteCreditPayBackByIds(ids));
    }

}
