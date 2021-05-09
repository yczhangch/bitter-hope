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
import com.ruoyi.credit.domain.PosTradeHistory;
import com.ruoyi.credit.service.IPosTradeHistoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * pos机交易历史Controller
 *
 * @author hope
 * @date 2021-05-09
 */
@RestController
@RequestMapping("/credit/posHistory")
public class PosTradeHistoryController extends BaseController {
    @Autowired
    private IPosTradeHistoryService posTradeHistoryService;

    /**
     * 查询pos机交易历史列表
     */
   // @PreAuthorize("@ss.hasPermi('credit:posHistory:list')")
    @GetMapping("/list")
    public TableDataInfo list(PosTradeHistory posTradeHistory) {
        startPage();
        List<PosTradeHistory> list = posTradeHistoryService.selectPosTradeHistoryList(posTradeHistory);
        return getDataTable(list);
    }

    /**
     * 导出pos机交易历史列表
     */
   // @PreAuthorize("@ss.hasPermi('credit:posHistory:export')")
    @Log(title = "pos机交易历史", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PosTradeHistory posTradeHistory) {
        List<PosTradeHistory> list = posTradeHistoryService.selectPosTradeHistoryList(posTradeHistory);
        ExcelUtil<PosTradeHistory> util = new ExcelUtil<>(PosTradeHistory.class);
        return util.exportExcel(list, "posHistory");
    }

    /**
     * 获取pos机交易历史详细信息
     */
  //  @PreAuthorize("@ss.hasPermi('credit:posHistory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(posTradeHistoryService.selectPosTradeHistoryById(id));
    }

    /**
     * 新增pos机交易历史
     */
   // @PreAuthorize("@ss.hasPermi('credit:posHistory:add')")
    @Log(title = "pos机交易历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PosTradeHistory posTradeHistory) {
        return toAjax(posTradeHistoryService.insertPosTradeHistory(posTradeHistory));
    }

    /**
     * 修改pos机交易历史
     */
    @PreAuthorize("@ss.hasPermi('credit:posHistory:edit')")
    @Log(title = "pos机交易历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PosTradeHistory posTradeHistory) {
        return toAjax(posTradeHistoryService.updatePosTradeHistory(posTradeHistory));
    }

    /**
     * 删除pos机交易历史
     */
  //  @PreAuthorize("@ss.hasPermi('credit:posHistory:remove')")
    @Log(title = "pos机交易历史", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(posTradeHistoryService.deletePosTradeHistoryByIds(ids));
    }
}
