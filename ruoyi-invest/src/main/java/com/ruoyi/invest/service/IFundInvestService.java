package com.ruoyi.invest.service;

import com.ruoyi.invest.domain.FundInvest;

import java.util.List;

/**
 * 基金投资Service接口
 *
 * @author hope
 * @date 2021-07-30
 */
public interface IFundInvestService {
    /**
     * 查询基金投资
     *
     * @param id 基金投资ID
     * @return 基金投资
     */
    FundInvest selectFundInvestById(Long id);

    /**
     * 查询基金投资列表
     *
     * @param fundInvest 基金投资
     * @return 基金投资集合
     */
    List<FundInvest> selectFundInvestList(FundInvest fundInvest);

    /**
     * 新增基金投资
     *
     * @param fundInvest 基金投资
     * @return 结果
     */
    int insertFundInvest(FundInvest fundInvest);

    /**
     * 修改基金投资
     *
     * @param fundInvest 基金投资
     * @return 结果
     */
    int updateFundInvest(FundInvest fundInvest);

    /**
     * 批量删除基金投资
     *
     * @param ids 需要删除的基金投资ID
     * @return 结果
     */
    int deleteFundInvestByIds(Long[] ids);

    /**
     * 删除基金投资信息
     *
     * @param id 基金投资ID
     * @return 结果
     */
    int deleteFundInvestById(Long id);
}
