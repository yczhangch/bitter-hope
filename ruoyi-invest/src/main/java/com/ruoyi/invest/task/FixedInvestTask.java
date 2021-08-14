package com.ruoyi.invest.task;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.invest.domain.FundInvest;
import com.ruoyi.invest.manager.FundInvestManager;
import com.ruoyi.invest.mapper.FundInvestMapper;
import com.ruoyi.invest.service.IFundInvestService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class FixedInvestTask {

    @Resource
    private IFundInvestService fundInvestService;

    @Resource
    private FundInvestMapper fundInvestMapper;

    @Resource
    private FundInvestManager fundInvestManager;

    /**
     * 每个工作日跑
     *
     * @param fundCode
     * @param fixedMoney
     */
    public void fixedInvest(String fundCode, BigDecimal fixedMoney) {
        // 以确认定投时间为准 +1工作日

        // 定投
        FundInvest fundInvest = new FundInvest();
        fundInvest.setTradeType("2");
        RealTimeFundInfo realTimeFundInfo = fundInvestManager.getRealTimeFundInfo(fundCode);
        if (realTimeFundInfo != null) {
            String jzrq = realTimeFundInfo.getJzrq();
            Date jzrqDate = DateUtils.parseDate(jzrq);
//            LocalDate yesterday = jzrqDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.systemDefault());
            Date today = Date.from(zonedDateTime.toInstant());
            if(today.getTime()- jzrqDate.getTime() == 60*60*24*1000L){

            }
            // 上一工作日单位净值
                fundInvest.setDealPrice(new BigDecimal(realTimeFundInfo.getDwjz()));

        }


        fundInvestService.insertFundInvest(fundInvest);

    }


    //todo 每日更新

//http://fund.eastmoney.com/pingzhongdata/519983.js?v=20160518155842


    public static void main(String[] args) {
        //  fixedInvest("163407", BigDecimal.ZERO);
        System.out.println(BigDecimal.ONE.negate());
    }
}
