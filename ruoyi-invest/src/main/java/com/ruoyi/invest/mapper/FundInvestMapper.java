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

    /**
     * 查询投资父节点
     *
     * @param fund 基金
     * @return 父节点
     */
    FundInvest queryParentByFund(String fund);

    /**
     * 获取基金投资序列号
     *
     * @return 序列号
     */
    Integer getFundInvestSeq();

    /**
     * 根据parentId 查询卖出
     * @param parentId parentId
     * @return 卖出
     */
    List<FundInvest> queryChildrenByParentId(Long parentId);

    /**
     * 查询该比基金卖出情况
     *
     * @param parentId parentId
     * @return 基金投资卖出情况
     */
    List<FundInvest> selectFundInvestChildren(Long parentId);
}
