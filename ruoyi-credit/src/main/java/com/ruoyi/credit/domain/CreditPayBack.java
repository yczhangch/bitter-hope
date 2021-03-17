package com.ruoyi.credit.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 信用卡还款日提醒对象 t_credit_pay_back
 *
 * @author hope
 * @date 2021-03-12
 */
public class CreditPayBack extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /**
     * 逻辑主键
     */
    private Long id;

    /**
     * 信用卡id
     */
    private Long cardId;

    /**
     * 年
     */
    @Excel(name = "年")
    private Integer year;

    /**
     * 账单月份
     */
    @Excel(name = "账单月份")
    private Integer month;

    /**
     * 账单日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "账单日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date billDay;

    /**
     * 还款日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "还款日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payBackDay;

    /**
     * 是否还款
     */
    @Excel(name = "是否还款")
    private String isPayed;

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
    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }
    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getMonth() {
        return month;
    }
    public void setBillDay(Date billDay) {
        this.billDay = billDay;
    }

    public Date getBillDay() {
        return billDay;
    }
    public void setPayBackDay(Date payBackDay) {
        this.payBackDay = payBackDay;
    }

    public Date getPayBackDay() {
        return payBackDay;
    }
    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    public String getIsPayed() {
        return isPayed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cardId", getCardId())
            .append("year", getYear())
            .append("month", getMonth())
            .append("billDay", getBillDay())
            .append("payBackDay", getPayBackDay())
            .append("isPayed", getIsPayed())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
