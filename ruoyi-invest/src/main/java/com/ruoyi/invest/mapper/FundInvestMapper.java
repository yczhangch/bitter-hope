package com.ruoyi.invest.mapper;

import com.ruoyi.invest.domain.FundInvest;

import java.util.List;

/**
 * 基金投资Mapper接口
 *
 * @author hope
 * @date 2021-07-30
 */
public interface FundInvestMapper {
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
     * 删除基金投资
     *
     * @param id 基金投资ID
     * @return 结果
     */
    int deleteFundInvestById(Long id);

    /**
     * 批量删除基金投资
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteFundInvestByIds(Long[] ids);
}
