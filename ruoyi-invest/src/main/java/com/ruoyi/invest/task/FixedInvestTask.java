package com.ruoyi.invest.task;

import com.ruoyi.invest.domain.FundInvest;
import com.ruoyi.invest.manager.FundInvestManager;
import com.ruoyi.invest.mapper.FundInvestMapper;
import com.ruoyi.invest.service.IFundInvestService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

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

        fundInvestService.insertFundInvest(fundInvest);

    }




    //todo 每日更新

//http://fund.eastmoney.com/pingzhongdata/519983.js?v=20160518155842



    public static void main(String[] args) {
        //  fixedInvest("163407", BigDecimal.ZERO);
        System.out.println(BigDecimal.ONE.negate());
    }
}
