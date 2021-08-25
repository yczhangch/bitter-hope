package com.ruoyi.invest.service.impl;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.invest.constant.InvestConstant;
import com.ruoyi.invest.domain.FundInvest;
import com.ruoyi.invest.manager.FundInvestManager;
import com.ruoyi.invest.mapper.FundInvestMapper;
import com.ruoyi.invest.service.IFundInvestService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.ruoyi.invest.constant.InvestConstant.NOT_NONE;

/**
 * 基金投资Service业务层处理
 *
 * @author hope
 * @date 2021-07-30
 */
@Service
public class FundInvestServiceImpl implements IFundInvestService {
    @Resource
    private FundInvestMapper fundInvestMapper;

    @Resource
    private FundInvestManager fundInvestManager;

    /**
     * 查询基金投资
     *
     * @param id 基金投资ID
     * @return 基金投资
     */
    @Override
    public FundInvest selectFundInvestById(Long id) {
        return fundInvestMapper.selectFundInvestById(id);
    }

    /**
     * 查询基金投资列表
     *
     * @param fundInvest 基金投资查询条件
     * @param pageNum    页码
     * @param pageSize   每页数
     * @return 基金投资
     */
    @Override
    public List<FundInvest> selectFundInvestList(FundInvest fundInvest, Integer pageNum, Integer pageSize) {
        // 查询前更新投资收益:未投资完成
        PageHelper.startPage(pageNum, pageSize);
        // fundInvestManager.updateFundInvestProfit(fundInvest);
        List<FundInvest> fundInvests = fundInvestMapper.selectFundInvestList(fundInvest);
        List<FundInvest> childrenAll = new ArrayList<>();      
        List<FundInvest> updatedInvests = new ArrayList<>();
        for (FundInvest invest : fundInvests) {
            // 获取基金最新价格
            final BigDecimal gsz = fundInvestManager.getRealTimePrice(invest.getFund().split(InvestConstant.FUND_NAME_CODE_SPLITTER)[1]);
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
                childrenAll.addAll(children);
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
            updatedInvests.add(invest);
            fundInvestMapper.updateFundInvest(invest);
        }
        updatedInvests.addAll(childrenAll);
        return updatedInvests;
    }

    /**
     * 新增基金投资
     *
     * @param fundInvest 基金投资
     * @return 结果
     */
    @Override
    public int insertFundInvest(FundInvest fundInvest) {
        BigDecimal money = fundInvest.getMoney();
        if (money == null) {
            throw new CustomException("投资金额 / 卖出金额不能为空");
        }
        fundInvest.setCreateTime(DateUtils.getNowDate());
        // 卖出不计投资时间,投资完成
        if (Objects.equals(InvestConstant.FundTradeType.Sell.getTradeType(), fundInvest.getTradeType())) {
            fundInvest.setInvestTime(null);
            fundInvest.setIsDone(null);

            // 设置parentId
            setParent(fundInvest);
            return 1;
        } else if (Objects.equals(InvestConstant.FundTradeType.Bonus.getTradeType(), fundInvest.getTradeType())) {
            // 分红，父节点设置为投资未完成的第一笔
            fundInvest.setIsDone(null);
            fundInvest.setParentId(queryBonusParent(fundInvest.getFund()));
        } else {
            // 买入默认投资未完成
            fundInvest.setIsDone(NOT_NONE);
            fundInvest.setParentId(NumberUtils.LONG_ZERO);
            fundInvest.setInvestNo(String.valueOf(fundInvestMapper.getFundInvestSeq()));
        }
        return fundInvestMapper.insertFundInvest(fundInvest);
    }

    /**
     * 查询分红的父级
     *
     * @param fund 基金
     * @return 父级
     */
    private Long queryBonusParent(String fund) {
        FundInvest parent = fundInvestMapper.queryParentByFund(fund);
        if (parent != null) {
            return parent.getId();
        }
        return null;
    }

    /**
     * @param fundInvestSell 卖出
     */
    private void setParent(FundInvest fundInvestSell) {
        // 确认份额
        BigDecimal dealAmount = fundInvestSell.getDealAmount();

        // 根据fund查询parent
        FundInvest parent = fundInvestMapper.queryParentByFund(fundInvestSell.getFund());
        if (parent == null) {
            throw new CustomException("请检查录入是否正确！");
        }
        Long parentId = parent.getId();
        fundInvestSell.setParentId(parentId);

        // 查询该parent的卖出
        List<FundInvest> fundSellChildren = fundInvestMapper.selectFundInvestChildren(parentId);
        BigDecimal remain = parent.getDealAmount();
        BigDecimal hasSellMoney = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(fundSellChildren)) {
            BigDecimal totalSellAmounts = fundSellChildren.stream().map(FundInvest::getDealAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            hasSellMoney = fundSellChildren.stream().map(FundInvest::getMoney)
                    .filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

            remain = parent.getDealAmount().subtract(totalSellAmounts);
        }
        if (remain.compareTo(dealAmount) > 0) {
            // 计算收益、收益率
            fundInvestSell.setProfit(fundInvestManager.getSellFundProfit(fundInvestSell, parent));
            fundInvestSell.setProfitRatio(fundInvestManager.getSellProfitRatio(fundInvestSell, parent));
            fundInvestMapper.insertFundInvest(fundInvestSell);
        } else {
            BigDecimal oldDealAmount = fundInvestSell.getDealAmount();
            BigDecimal oldSellMoney = fundInvestSell.getMoney();
            fundInvestSell.setDealAmount(remain);
            // 卖出金额调整
            fundInvestSell.setMoney(oldSellMoney.multiply(remain).divide(oldDealAmount, 2, RoundingMode.HALF_UP));
            // 计算收益、收益率
            fundInvestSell.setProfit(fundInvestManager.getSellFundProfit(fundInvestSell, parent));
            fundInvestSell.setProfitRatio(fundInvestManager.getSellProfitRatio(fundInvestSell, parent));
            fundInvestMapper.insertFundInvest(fundInvestSell);

            // 更新parent
            parent.setIsDone("Y");
            parent.setUpdateTime(new Date());
            // 重新计算实际收益和收益率
            // 卖出金额总和 - 初始投资额
            parent.setProfit(hasSellMoney.add(fundInvestSell.getMoney()).subtract(parent.getMoney()));
            parent.setProfitRatio(parent.getProfit().divide(parent.getMoney(), 2, RoundingMode.HALF_UP));
            fundInvestMapper.updateFundInvest(parent);

            // 将该笔卖出拆分为多个
            fundInvestSell.setDealAmount(dealAmount.subtract(remain));
            fundInvestSell.setMoney(oldSellMoney.multiply(fundInvestSell.getDealAmount()).divide(oldDealAmount, 2, RoundingMode.HALF_UP));
            setParent(fundInvestSell);
        }
    }

    /**
     * 修改基金投资
     *
     * @param fundInvest 基金投资
     * @return 结果
     */
    @Override
    public int updateFundInvest(FundInvest fundInvest) {
        if (Objects.equals("1", fundInvest.getTradeType())) {
            fundInvest.setIsDone(null);
        }
        fundInvest.setUpdateTime(DateUtils.getNowDate());
        return fundInvestMapper.updateFundInvest(fundInvest);
    }

    /**
     * 批量删除基金投资
     *
     * @param ids 需要删除的基金投资ID
     * @return 结果
     */
    @Override
    public int deleteFundInvestByIds(Long[] ids) {
        return fundInvestMapper.deleteFundInvestByIds(ids);
    }

    /**
     * 删除基金投资信息
     *
     * @param id 基金投资ID
     * @return 结果
     */
    @Override
    public int deleteFundInvestById(Long id) {
        return fundInvestMapper.deleteFundInvestById(id);
    }
}
