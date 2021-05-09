package com.ruoyi.web.controller.invest;

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
import com.ruoyi.invest.domain.BtcInvest;
import com.ruoyi.invest.service.IBtcInvestService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 比特币投资Controller
 *
 * @author hope
 * @date 2021-05-07
 */
@RestController
@RequestMapping("/invest/btc")
public class BtcInvestController extends BaseController {
    @Autowired
    private IBtcInvestService btcInvestService;

    /**
     * 查询比特币投资列表
     */
    // @PreAuthorize("@ss.hasPermi('invest:invest:list')")
    @GetMapping("/list")
    public AjaxResult list(BtcInvest btcInvest) {
        List<BtcInvest> list = btcInvestService.selectBtcInvestList(btcInvest);
        return AjaxResult.success(list);
    }

    /**
     * 导出比特币投资列表
     */
    // @PreAuthorize("@ss.hasPermi('invest:invest:export')")
    @Log(title = "比特币投资", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BtcInvest btcInvest) {
        List<BtcInvest> list = btcInvestService.selectBtcInvestList(btcInvest);
        ExcelUtil<BtcInvest> util = new ExcelUtil<BtcInvest>(BtcInvest.class);
        return util.exportExcel(list, "invest");
    }

    /**
     * 获取比特币投资详细信息
     */
    //  @PreAuthorize("@ss.hasPermi('invest:invest:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(btcInvestService.selectBtcInvestById(id));
    }

    /**
     * 新增比特币投资
     */
    // @PreAuthorize("@ss.hasPermi('invest:invest:add')")
    @Log(title = "比特币投资", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BtcInvest btcInvest) {
        return toAjax(btcInvestService.insertBtcInvest(btcInvest));
    }

    /**
     * 修改比特币投资
     */
    @PreAuthorize("@ss.hasPermi('invest:invest:edit')")
    @Log(title = "比特币投资", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BtcInvest btcInvest) {
        return toAjax(btcInvestService.updateBtcInvest(btcInvest));
    }

    /**
     * 删除比特币投资
     */
    //  @PreAuthorize("@ss.hasPermi('invest:invest:remove')")
    @Log(title = "比特币投资", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(btcInvestService.deleteBtcInvestByIds(ids));
    }
}
