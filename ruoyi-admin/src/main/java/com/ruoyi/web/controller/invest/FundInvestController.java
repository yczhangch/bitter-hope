package com.ruoyi.web.controller.invest;

import java.util.List;

import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
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
import com.ruoyi.invest.domain.FundInvest;
import com.ruoyi.invest.service.IFundInvestService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 基金投资Controller
 *
 * @author hope
 * @date 2021-07-30
 */
@RestController
@RequestMapping("/invest/fundInvest")
public class FundInvestController extends BaseController {
    @Autowired
    private IFundInvestService fundInvestService;

    /**
     * 查询基金投资列表
     */
   // @PreAuthorize("@ss.hasPermi('invest:fundInvest:list')")
    @GetMapping("/list")
    public TableDataInfo list(FundInvest fundInvest) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<FundInvest> list = fundInvestService.selectFundInvestList(fundInvest,pageNum,pageSize);
        return getDataTable(list);
    }

    /**
     * 导出基金投资列表
     */
   // @PreAuthorize("@ss.hasPermi('invest:fundInvest:export')")
    @Log(title = "基金投资", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(FundInvest fundInvest) {
        List<FundInvest> list = fundInvestService.selectFundInvestList(fundInvest, null, null);
        ExcelUtil<FundInvest> util = new ExcelUtil<>(FundInvest.class);
        return util.exportExcel(list, "fundInvest");
    }

    /**
     * 获取基金投资详细信息
     */
  //  @PreAuthorize("@ss.hasPermi('invest:fundInvest:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(fundInvestService.selectFundInvestById(id));
    }

    /**
     * 新增基金投资
     */
   // @PreAuthorize("@ss.hasPermi('invest:fundInvest:add')")
    @Log(title = "基金投资", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FundInvest fundInvest) {
        return toAjax(fundInvestService.insertFundInvest(fundInvest));
    }

    /**
     * 修改基金投资
     */
    // @PreAuthorize("@ss.hasPermi('invest:fundInvest:edit')")
    @Log(title = "基金投资", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FundInvest fundInvest) {
        return toAjax(fundInvestService.updateFundInvest(fundInvest));
    }

    /**
     * 删除基金投资
     */
  //  @PreAuthorize("@ss.hasPermi('invest:fundInvest:remove')")
    @Log(title = "基金投资", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(fundInvestService.deleteFundInvestByIds(ids));
    }
}
