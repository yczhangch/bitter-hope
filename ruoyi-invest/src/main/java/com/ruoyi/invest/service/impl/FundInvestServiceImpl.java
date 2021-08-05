package com.ruoyi.invest.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.invest.domain.FundInvest;
import com.ruoyi.invest.mapper.FundInvestMapper;
import com.ruoyi.invest.service.IFundInvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基金投资Service业务层处理
 *
 * @author hope
 * @date 2021-07-30
 */
@Service
public class FundInvestServiceImpl implements IFundInvestService {
    @Autowired
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
        return fundInvestMapper.selectFundInvestList(fundInvest);
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
        return fundInvestMapper.insertFundInvest(fundInvest);
    }

    /**
     * 修改基金投资
     *
     * @param fundInvest 基金投资
     * @return 结果
     */
    @Override
    public int updateFundInvest(FundInvest fundInvest) {
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
