package com.ruoyi.invest.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.invest.domain.FundInvest;
import com.ruoyi.invest.mapper.FundInvestMapper;
import com.ruoyi.invest.service.IFundInvestService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
        List<FundInvest> fundInvests = fundInvestMapper.selectFundInvestList(fundInvest);
        final Long investId = fundInvest.getId();
        if (investId != null) {
            // 查询该投资的父级和子集

        }
        for (FundInvest next : fundInvests) {
            // 卖出不显示基金名，投资日期,id
            if (Objects.equals(next.getTradeType(), "1")) {
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
        // 卖出不计投资时间和投资额,投资完成
        if (Objects.equals("1", fundInvest.getTradeType())) {
            fundInvest.setMoney(null);
            fundInvest.setInvestTime(null);
            fundInvest.setIsDone(null);
            // 设置parentId
            setParent(fundInvest);
            return 1;
        } else if (Objects.equals("0", fundInvest.getTradeType())) {
            // 买入默认投资未完成
            fundInvest.setIsDone("N");
            fundInvest.setParentId(NumberUtils.LONG_ZERO);
            fundInvest.setInvestNo(String.valueOf(fundInvestMapper.getFundInvestSeq()));
        }
        return fundInvestMapper.insertFundInvest(fundInvest);
    }

    /**
     * @param fundInvest 卖出
     */
    private void setParent(FundInvest fundInvest) {
        // 成交数量
        BigDecimal dealAmount = fundInvest.getDealAmount();
        // 根据fund查询parent
        FundInvest parent = fundInvestMapper.queryParentByFund(fundInvest.getFund());
        // 查询该parent的卖出
        FundInvest fundInvestQuery = new FundInvest();
        fundInvestQuery.setParentId(parent.getId());
        List<FundInvest> fundSellChildren = fundInvestMapper.selectFundInvestList(fundInvestQuery);
        BigDecimal remain = parent.getDealAmount();
        if (CollectionUtils.isNotEmpty(fundSellChildren)) {
            BigDecimal totalSellAmounts = fundSellChildren.stream().map(FundInvest::getDealAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            remain = parent.getDealAmount().subtract(totalSellAmounts);
        }
        if (remain.compareTo(dealAmount) > 0) {
            fundInvest.setParentId(parent.getId());
            fundInvestMapper.insertFundInvest(fundInvest);
        } else {
            // 更新parent
            parent.setIsDone("Y");
            parent.setUpdateTime(new Date());
            fundInvestMapper.updateFundInvest(parent);
            fundInvest.setDealAmount(remain);
            fundInvestMapper.insertFundInvest(fundInvest);
            // 将该笔卖出拆分为多个
            fundInvest.setDealAmount(dealAmount.subtract(remain));
            setParent(fundInvest);
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
