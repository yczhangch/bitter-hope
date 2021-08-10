package com.ruoyi.invest.manager;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.http.HttpUtils;
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

    // 求卖出收益 = （卖出价格-买入价格）*卖出数量*（1-0.005）
    public BigDecimal getSellFundProfit(FundInvest sell, FundInvest buy) {
        return (sell.getDealPrice().subtract(buy.getDealPrice())).multiply(sell.getDealAmount())
                .multiply(BigDecimal.ONE.subtract(new BigDecimal("0.005")));
    }

    // 求卖出收益率 = （卖出价格-买入价格）/买入价格 - 0.005
    public BigDecimal getSellProfitRatio(FundInvest sell, FundInvest buy) {
        return (sell.getDealPrice().subtract(buy.getDealPrice())).divide(buy.getDealPrice(), 2, RoundingMode.DOWN).subtract(new BigDecimal("0.005"));
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
    public void updateFundInvestProfit() {
        // 扫描所有未完成投资
        FundInvest fundInvest = new FundInvest();
        fundInvest.setIsDone("N");
        List<FundInvest> fundInvests = fundInvestMapper.selectFundInvestList(fundInvest);
        for (FundInvest invest : fundInvests) {
            // 获取基金最新价格
            final BigDecimal gsz = getRealTimePrice(invest.getFund().split("-")[1]);
            if (gsz == null) {
                continue;
            }
            List<FundInvest> child = fundInvestMapper.queryChildrenByParentId(invest.getId());
            if (CollectionUtils.isEmpty(child)) {
                invest.setProfit((gsz.subtract(invest.getDealPrice())).multiply(invest.getDealAmount())
                        .multiply(BigDecimal.ONE.subtract(new BigDecimal("0.005"))));
                invest.setProfitRatio((gsz.subtract(invest.getDealPrice())).divide(invest.getDealPrice(), 2, RoundingMode.HALF_UP).subtract(new BigDecimal("0.005")));
            } else {
                // 已卖出金额
                BigDecimal sellMoney = child.stream().map(FundInvest::getMoney).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                // 已卖出份额
                BigDecimal sellAmount = child.stream().map(FundInvest::getDealAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                // 剩余份额
                invest.setDealAmount(invest.getDealAmount().subtract(sellAmount));
                // 预估收益 = 已实现收益 + 未实现预估收益
                BigDecimal profit = getRealTimeFundProfit(invest).add(sellMoney.negate()).subtract(invest.getMoney());
                invest.setProfit(profit);
                invest.setProfitRatio(profit.divide(fundInvest.getMoney(), 2, RoundingMode.HALF_UP));
                fundInvestMapper.updateFundInvest(fundInvest);
            }
        }
    }

    public RealTimeFundInfo getRealTimeFundInfo(String fundCode) {
        String url = "http://fundgz.1234567.com.cn/js/%s.js";
        String param = "rt=" + new Date().getTime();
        String realTimeFundInfo = HttpUtils.sendGet(String.format(url, fundCode), param);
        if (StringUtils.isEmpty(realTimeFundInfo)) {
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
