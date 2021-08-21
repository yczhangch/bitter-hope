package com.ruoyi.invest.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
     * @param fundInvest 基金投资
     * @return 基金投资
     */
    @Override
    public List<FundInvest> selectFundInvestList(FundInvest fundInvest) {
        // 查询前更新投资收益:未投资完成
        fundInvestManager.updateFundInvestProfit();
        // 查询所有投资
        List<FundInvest> fundInvests = fundInvestMapper.selectFundInvestList(fundInvest);
        List<FundInvest> children = new ArrayList<>();
        for (FundInvest invest : fundInvests) {
            List<FundInvest> child = fundInvestMapper.queryChildrenByParentId(invest.getId());
            if (CollectionUtils.isNotEmpty(child)) {
                children.addAll(child);
            }
        }
        fundInvests.addAll(children);
        for (FundInvest next : fundInvests) {
            // 卖出不显示基金名，投资日期,id
            if (Objects.equals(next.getTradeType(), "0")) {
                next.setFund(null);
                next.setMoney(null);
                next.setInvestTime(null);
            }
        }
        return fundInvests;
    }

    /**
     * 新增基金投资
     *
     * @param fundInvest 基金投资
     * @return 结果
     */
    @Override
    public int insertFundInvest(FundInvest fundInvest) {
        fundInvest.setCreateTime(DateUtils.getNowDate());
        // 卖出不计投资时间,投资完成
        if (Objects.equals("0", fundInvest.getTradeType())) {
            fundInvest.setInvestTime(null);
            fundInvest.setIsDone(null);
            BigDecimal money = fundInvest.getMoney();
            if (money == null) {
                // -1000
                fundInvest.setMoney(fundInvest.getDealAmount().multiply(fundInvest.getDealPrice().negate()));
            }
            // 设置parentId
            setParent(fundInvest);
            return 1;
        } else {
            // 买入默认投资未完成
            fundInvest.setIsDone("N");
            fundInvest.setParentId(NumberUtils.LONG_ZERO);
            fundInvest.setInvestNo(String.valueOf(fundInvestMapper.getFundInvestSeq()));
        }
        return fundInvestMapper.insertFundInvest(fundInvest);
    }

    /**
     * @param fundInvestSell 卖出
     */
    private void setParent(FundInvest fundInvestSell) {
        // 成交数量
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
