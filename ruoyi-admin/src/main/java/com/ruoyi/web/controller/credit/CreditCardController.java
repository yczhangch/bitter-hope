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
import com.ruoyi.credit.domain.CreditCard;
import com.ruoyi.credit.service.ICreditCardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 信用卡Controller
 *
 * @author hope
 * @date 2021-03-12
 */
@RestController
@RequestMapping("/credit/card")
public class CreditCardController extends BaseController {
    @Autowired
    private ICreditCardService creditCardService;

    /**
     * 查询信用卡列表
     */
//    @PreAuthorize("@ss.hasPermi('credit:card:list')")
    @GetMapping("/list")
    public TableDataInfo list(CreditCard creditCard) {
        startPage();
        List<CreditCard> list = creditCardService.selectCreditCardList(creditCard);
        return getDataTable(list);
    }

    /**
     * 导出信用卡列表
     */
    @PreAuthorize("@ss.hasPermi('credit:card:export')")
    @Log(title = "信用卡", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CreditCard creditCard) {
        List<CreditCard> list = creditCardService.selectCreditCardList(creditCard);
        ExcelUtil<CreditCard> util = new ExcelUtil<CreditCard>(CreditCard.class);
        return util.exportExcel(list, "card");
    }

    /**
     * 获取信用卡详细信息
     */
//    @PreAuthorize("@ss.hasPermi('credit:card:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(creditCardService.selectCreditCardById(id));
    }

    /**
     * 新增信用卡
     */
//    @PreAuthorize("@ss.hasPermi('credit:card:add')")
    @Log(title = "信用卡", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CreditCard creditCard) {
        return toAjax(creditCardService.insertCreditCard(creditCard));
    }

    /**
     * 修改信用卡
     */
//    @PreAuthorize("@ss.hasPermi('credit:card:edit')")
    @Log(title = "信用卡", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CreditCard creditCard) {
        return toAjax(creditCardService.updateCreditCard(creditCard));
    }

    /**
     * 删除信用卡
     */
//    @PreAuthorize("@ss.hasPermi('credit:card:remove')")
    @Log(title = "信用卡", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(creditCardService.deleteCreditCardByIds(ids));
    }
}
