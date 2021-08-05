package com.ruoyi.invest.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 基金投资对象 t_fund_invest
 *
 * @author hope
 * @date 2021-07-30
 */
public class FundInvest extends TreeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 逻辑主键
     */
    private Long id;

    /**
     * 投资编号
     */
    @Excel(name = "投资编号")
    private String investNo;

    /**
     * 基金
     */
    @Excel(name = "基金")
    private String fund;

    /**
     * 投资日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投资日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date investTime;

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
     * 成交价格
     */
    @Excel(name = "成交价格")
    private BigDecimal dealPrice;

    /**
     * 成交数量
     */
    @Excel(name = "成交数量")
    private BigDecimal dealAmount;

    /**
     * 成交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dealTime;

    /**
     * 收益
     */
    @Excel(name = "收益")
    private BigDecimal profit;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setInvestNo(String investNo) {
        this.investNo = investNo;
    }

    public String getInvestNo() {
        return investNo;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getFund() {
        return fund;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public Date getInvestTime() {
        return investTime;
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

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("investNo", getInvestNo())
                .append("parentId", getParentId())
                .append("fund", getFund())
                .append("investTime", getInvestTime())
                .append("money", getMoney())
                .append("tradeType", getTradeType())
                .append("isDone", getIsDone())
                .append("dealPrice", getDealPrice())
                .append("dealAmount", getDealAmount())
                .append("dealTime", getDealTime())
                .append("profit", getProfit())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
