package com.ruoyi.invest.manager;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.invest.constant.InvestConstant;
import com.ruoyi.invest.domain.FundInvest;
import com.ruoyi.invest.mapper.FundInvestMapper;
import com.ruoyi.invest.task.RealTimeFundInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class FundInvestManager {
    @Resource
    private FundInvestMapper fundInvestMapper;

    // 卖出收益 = 卖出金额 - 买入金额/总份额*卖出份额
    public BigDecimal getSellFundProfit(FundInvest sell, FundInvest buy) {
        return sell.getMoney().subtract(buy.getMoney().multiply(sell.getDealAmount()).divide(buy.getDealAmount(), 2, RoundingMode.HALF_UP));
    }

    // 求卖出收益率 = 卖出收益/(买入金额/总份额*卖出份额)
    public BigDecimal getSellProfitRatio(FundInvest sell, FundInvest buy) {
        final BigDecimal purchaseCost = buy.getMoney().multiply(sell.getDealAmount()).divide(buy.getDealAmount(), 2, RoundingMode.HALF_UP);
        return sell.getMoney().subtract(purchaseCost).divide(purchaseCost, 2, BigDecimal.ROUND_HALF_UP);
    }

    // 预估收益 = （预估价格 - 买入价格）* 数量*（1-0.005）
    public BigDecimal getRealTimeFundProfit(FundInvest buy) {
        BigDecimal realTimePrice = getRealTimePrice(buy.getFund().split("-")[1]);
        if (realTimePrice == null) {
            return null;
        }
        return (realTimePrice.subtract(buy.getDealPrice())).multiply(buy.getDealAmount())
                .multiply(BigDecimal.ONE.subtract(new BigDecimal("0.005")));
    }

    // 预估收益率 = (预估价格 - 买入价格）/买入价格 - 0.005
    public BigDecimal getRealTimeProfitRatio(FundInvest buy) {
        BigDecimal realTimePrice = getRealTimePrice(buy.getFund().split("-")[1]);
        if (realTimePrice == null) {
            return null;
        }
        return (realTimePrice.subtract(buy.getDealPrice())).divide(buy.getDealPrice(), 2, RoundingMode.DOWN).subtract(new BigDecimal("0.005"));
    }

    /**
     * 更新投资收益
     */
    public void updateFundInvestProfit(FundInvest fundInvest) {
        List<FundInvest> fundInvests = fundInvestMapper.selectFundInvestList(fundInvest);
        for (FundInvest invest : fundInvests) {
            // 获取基金最新价格
            final BigDecimal gsz = getRealTimePrice(invest.getFund().split(InvestConstant.FUND_NAME_CODE_SPLITTER)[1]);
            if (gsz == null) {
                continue;
            }
            List<FundInvest> children = fundInvestMapper.queryChildrenByParentId(invest.getId());
            if (CollectionUtils.isEmpty(children)) {
                // 估算收益 = 估算值* 数量*（1-0.005） -投资金额
                // invest.setProfit((gsz.subtract(invest.getDealPrice())).multiply(invest.getDealAmount())
                //         .multiply(BigDecimal.ONE.subtract(new BigDecimal("0.005"))).setScale(2, RoundingMode.HALF_UP));
                // TODO: 2021/8/21   根据时间计算费率
                invest.setProfit(gsz.multiply(invest.getDealAmount()).multiply(BigDecimal.ONE.subtract(new BigDecimal("0.005"))).subtract(invest.getMoney()).setScale(InvestConstant.MONEY_SCALE, RoundingMode.HALF_UP));
                invest.setProfitRatio(invest.getProfit().divide(invest.getMoney(), InvestConstant.RATIO_SCALE, RoundingMode.HALF_UP));
            } else {
                // 已卖出金额 = 卖出 + 分红
                BigDecimal sellMoney = children.stream().map(FundInvest::getMoney).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                // 已卖出份额
                BigDecimal sellAmount = children.stream().map(FundInvest::getDealAmount).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                // 剩余份额
                // invest.setDealAmount(invest.getDealAmount().subtract(sellAmount));
                BigDecimal remaining = invest.getDealAmount().subtract(sellAmount);
                // 预估收益 =  剩余份额*估算值 + 已卖出金额  - 初始投资金额
                // BigDecimal profit = getRealTimeFundProfit(invest).add(sellMoney.negate()).subtract(invest.getMoney());

                // BigDecimal profit = getRealTimeFundProfit(invest).add(sellMoney).subtract(sellAmount.multiply(invest.getDealPrice()));
                BigDecimal profit = remaining.multiply(gsz).add(sellMoney).subtract(invest.getMoney()).setScale(InvestConstant.MONEY_SCALE, RoundingMode.HALF_UP);
                invest.setProfit(profit);
                invest.setProfitRatio(profit.divide(invest.getMoney(), InvestConstant.RATIO_SCALE, RoundingMode.HALF_UP));
            }
            fundInvestMapper.updateFundInvest(invest);
        }
    }

    public RealTimeFundInfo getRealTimeFundInfo(String fundCode) {
        String url = "http://fundgz.1234567.com.cn/js/%s.js";
        String param = "rt=" + new Date().getTime();
        String realTimeFundInfo = HttpUtils.sendGet(String.format(url, fundCode), param);
        if (InvestConstant.EMPTY_FUND_INFO.equals(realTimeFundInfo)) {
            return null;
        }
        String fundInfoJsonStr = realTimeFundInfo.substring(realTimeFundInfo.indexOf("{"), realTimeFundInfo.indexOf(");"));
        return JSON.parseObject(fundInfoJsonStr, RealTimeFundInfo.class);
    }


    public BigDecimal getRealTimePrice(String fundCode) {
        RealTimeFundInfo realTimeFundInfo = getRealTimeFundInfo(fundCode);
        if (realTimeFundInfo == null) {
            return null;
        }
        return new BigDecimal(realTimeFundInfo.getGsz());
    }
}
