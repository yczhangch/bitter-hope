package com.ruoyi.credit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * pos机交易历史对象 t_pos_trade_history
 *
 * @author hope
 * @date 2021-05-09
 */
public class PosTradeHistory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 交易日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交易日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tradeTime;

    /**
     * 0-支付宝扫码，1-微信扫码，2-刷卡收款，3-云闪付
     */
    @Excel(name = "0-支付宝扫码，1-微信扫码，2-刷卡收款，3-云闪付")
    private String posTradeType;

    /**
     * pos机
     */
    @Excel(name = "pos机")
    private Long posId;

    /**
     * pos机
     */
    private String posName;

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    /**
     * 交易金额
     */
    @Excel(name = "交易金额")
    private BigDecimal money;

    /**
     * 到账金额
     */
    @Excel(name = "到账金额")
    private BigDecimal received;

    /**
     * 手续费
     */
    @Excel(name = "手续费")
    private BigDecimal fee;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setPosTradeType(String posTradeType) {
        this.posTradeType = posTradeType;
    }

    public String getPosTradeType() {
        return posTradeType;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getPosId() {
        return posId;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setReceived(BigDecimal received) {
        this.received = received;
    }

    public BigDecimal getReceived() {
        return received;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tradeTime", getTradeTime())
                .append("posTradeType", getPosTradeType())
                .append("posId", getPosId())
                .append("money", getMoney())
                .append("received", getReceived())
                .append("fee", getFee())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
