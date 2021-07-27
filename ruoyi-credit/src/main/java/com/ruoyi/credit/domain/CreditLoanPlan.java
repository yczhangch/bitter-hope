package com.ruoyi.credit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用卡借款计划对象 t_credit_loan_plan
 *
 * @author hope
 * @date 2021-03-17
 */
public class CreditLoanPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 逻辑主键
     */
    private Long id;

    /**
     * 信用卡id
     */
    @Excel(name = "信用卡id")
    private Long cardId;

    /**
     * 借款日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借款日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loanDate;

    /**
     * 借款金额
     */
    @Excel(name = "借款金额")
    private BigDecimal loadMoney;

    /**
     * pos机编号
     */
    @Excel(name = "pos机编号")
    private Long posId;

    /**
     * 费用
     */
    @Excel(name = "费用")
    private BigDecimal fee;

    /**
     * 是否借款
     */
    @Excel(name = "是否借款")
    private String isLoaded;

    /**
     * pos机
     */
    private String posName;

    /**
     * 信用卡
     */
    private String cardName;

    /**
     * 账单日
     */
    private String billDay;

    public String getBillDay() {
        return billDay;
    }

    public void setBillDay(String billDay) {
        this.billDay = billDay;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    /**
     * 信用卡号
     */
    private String cardNum;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoadMoney(BigDecimal loadMoney) {
        this.loadMoney = loadMoney;
    }

    public BigDecimal getLoadMoney() {
        return loadMoney;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getPosId() {
        return posId;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setIsLoaded(String isLoaded) {
        this.isLoaded = isLoaded;
    }

    public String getIsLoaded() {
        return isLoaded;
    }

    @Override
    public String toString() {
        return "CreditLoanPlan{" +
                "id=" + id +
                ", cardId=" + cardId +
                ", loanDate=" + loanDate +
                ", loadMoney=" + loadMoney +
                ", posId=" + posId +
                ", fee=" + fee +
                ", isLoaded='" + isLoaded + '\'' +
                ", posName='" + posName + '\'' +
                ", cardName='" + cardName + '\'' +
                ", cardNum='" + cardNum + '\'' +
                '}';
    }
}
