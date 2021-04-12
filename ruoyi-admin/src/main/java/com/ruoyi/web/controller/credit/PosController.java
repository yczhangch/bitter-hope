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
import com.ruoyi.credit.domain.Pos;
import com.ruoyi.credit.service.IPosService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * pos机Controller
 *
 * @author hope
 * @date 2021-03-16
 */
@RestController
@RequestMapping("/credit/pos")
public class PosController extends BaseController {
    @Autowired
    private IPosService posService;

    /**
     * 查询pos机列表
     */
   // @PreAuthorize("@ss.hasPermi('credit:pos:list')")
    @GetMapping("/list")
    public TableDataInfo list(Pos pos) {
        startPage();
        List<Pos> list = posService.selectPosList(pos);
        return getDataTable(list);
    }

    /**
     * 导出pos机列表
     */
   // @PreAuthorize("@ss.hasPermi('credit:pos:export')")
    @Log(title = "pos机", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Pos pos) {
        List<Pos> list = posService.selectPosList(pos);
        ExcelUtil<Pos> util = new ExcelUtil<Pos>(Pos.class);
        return util.exportExcel(list, "pos");
    }

    /**
     * 获取pos机详细信息
     */
  //  @PreAuthorize("@ss.hasPermi('credit:pos:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(posService.selectPosById(id));
    }

    /**
     * 新增pos机
     */
   // @PreAuthorize("@ss.hasPermi('credit:pos:add')")
    @Log(title = "pos机", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Pos pos) {
        return toAjax(posService.insertPos(pos));
    }

    /**
     * 修改pos机
     */
   // @PreAuthorize("@ss.hasPermi('credit:pos:edit')")
    @Log(title = "pos机", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Pos pos) {
        return toAjax(posService.updatePos(pos));
    }

    /**
     * 删除pos机
     */
  //  @PreAuthorize("@ss.hasPermi('credit:pos:remove')")
    @Log(title = "pos机", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(posService.deletePosByIds(ids));
    }

    @GetMapping("/posList")
    public AjaxResult posList() {
        return AjaxResult.success(posService.selectPosList(null));
    }
}
