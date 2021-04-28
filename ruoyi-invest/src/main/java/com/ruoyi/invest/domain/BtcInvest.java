package com.ruoyi.invest.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 比特币投资对象 t_btc_invest
 *
 * @author hope
 * @date 2021-04-15
 */
public class BtcInvest extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 逻辑主键
     */
    private Long id;

    /**
     * 投资日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投资日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date investTime;

    /**
     * 币种
     */
    @Excel(name = "币种")
    private String currencyType;

    /**
     * 投资金额
     */
    @Excel(name = "投资金额")
    private BigDecimal money;

    /**
     * 交易类型（买入/卖出）
     */
    @Excel(name = "交易类型", readConverterExp = "买=入/卖出")
    private String tradeType;

    /**
     * 是否成交（Y/N）
     */
    @Excel(name = "是否成交", readConverterExp = "Y=/N")
    private String isDone;

    /**
     * 成交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dealTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public Date getInvestTime() {
        return investTime;
    }
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrencyType() {
        return currencyType;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeType() {
        return tradeType;
    }
    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public String getIsDone() {
        return isDone;
    }
    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("investTime", getInvestTime())
            .append("currencyType", getCurrencyType())
            .append("money", getMoney())
            .append("tradeType", getTradeType())
            .append("isDone", getIsDone())
            .append("dealTime", getDealTime())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
