package com.ruoyi.credit.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 信用卡对象 t_credit_card
 *
 * @author hope
 * @date 2021-03-12
 */
public class CreditCard extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 逻辑主键
     */
    private Long id;

    /**
     * 卡号
     */
    @Excel(name = "卡号")
    private String cardNum;

    /**
     * 信用卡名
     */
    @Excel(name = "信用卡名")
    private String cardName;

    /**
     * 额度(元)
     */
    @Excel(name = "额度(元)")
    private Long creditLimit;

    /**
     * 账单日
     */
    @Excel(name = "账单日")
    private String billDay;

    /**
     * 还款日
     */
    @Excel(name = "还款日")
    private String repayDay;

    /**
     * 年费(元)
     */
    @Excel(name = "年费(元)")
    private BigDecimal annualFee;

    /**
     * 发卡银行
     */
    @Excel(name = "发卡银行")
    private String bank;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardNum() {
        return cardNum;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }
    public void setCreditLimit(Long creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Long getCreditLimit() {
        return creditLimit;
    }
    public void setBillDay(String billDay) {
        this.billDay = billDay;
    }

    public String getBillDay() {
        return billDay;
    }
    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public String getRepayDay() {
        return repayDay;
    }
    public void setAnnualFee(BigDecimal annualFee) {
        this.annualFee = annualFee;
    }

    public BigDecimal getAnnualFee() {
        return annualFee;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank() {
        return bank;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cardNum", getCardNum())
            .append("cardName", getCardName())
            .append("creditLimit", getCreditLimit())
            .append("billDay", getBillDay())
            .append("repayDay", getRepayDay())
            .append("annualFee", getAnnualFee())
            .append("bank", getBank())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
