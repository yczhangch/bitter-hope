package com.ruoyi.credit.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * pos机对象 t_pos
 *
 * @author hope
 * @date 2021-03-16
 */
public class Pos extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 逻辑主键
     */
    private Long id;

    /**
     * pos机名
     */
    @Excel(name = "pos机名")
    private String posName;

    /**
     * 小额费率
     */
    @Excel(name = "小额费率")
    private BigDecimal feeLevelOne;

    /**
     * 刷卡费率
     */
    @Excel(name = "刷卡费率")
    private BigDecimal feeLevelTwo;

    /**
     * 固定费用
     */
    @Excel(name = "固定费用")
    private BigDecimal fixedCost;

    /**
     * 申请年份
     */
    @Excel(name = "申请年份")
    private Long applyYear;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getPosName() {
        return posName;
    }
    public void setFeeLevelOne(BigDecimal feeLevelOne) {
        this.feeLevelOne = feeLevelOne;
    }

    public BigDecimal getFeeLevelOne() {
        return feeLevelOne;
    }
    public void setFeeLevelTwo(BigDecimal feeLevelTwo) {
        this.feeLevelTwo = feeLevelTwo;
    }

    public BigDecimal getFeeLevelTwo() {
        return feeLevelTwo;
    }
    public void setFixedCost(BigDecimal fixedCost) {
        this.fixedCost = fixedCost;
    }

    public BigDecimal getFixedCost() {
        return fixedCost;
    }
    public void setApplyYear(Long applyYear) {
        this.applyYear = applyYear;
    }

    public Long getApplyYear() {
        return applyYear;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("posName", getPosName())
            .append("feeLevelOne", getFeeLevelOne())
            .append("feeLevelTwo", getFeeLevelTwo())
            .append("fixedCost", getFixedCost())
            .append("applyYear", getApplyYear())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
