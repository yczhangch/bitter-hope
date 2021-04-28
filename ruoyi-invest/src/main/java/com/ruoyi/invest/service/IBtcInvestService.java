package com.ruoyi.invest.service;

import com.ruoyi.invest.domain.BtcInvest;

import java.util.List;

/**
 * 比特币投资Service接口
 *
 * @author hope
 * @date 2021-04-15
 */
public interface IBtcInvestService {
    /**
     * 查询比特币投资
     *
     * @param id 比特币投资ID
     * @return 比特币投资
     */
     BtcInvest selectBtcInvestById(Long id);

    /**
     * 查询比特币投资列表
     *
     * @param btcInvest 比特币投资
     * @return 比特币投资集合
     */
     List<BtcInvest> selectBtcInvestList(BtcInvest btcInvest);

    /**
     * 新增比特币投资
     *
     * @param btcInvest 比特币投资
     * @return 结果
     */
     int insertBtcInvest(BtcInvest btcInvest);

    /**
     * 修改比特币投资
     *
     * @param btcInvest 比特币投资
     * @return 结果
     */
     int updateBtcInvest(BtcInvest btcInvest);

    /**
     * 批量删除比特币投资
     *
     * @param ids 需要删除的比特币投资ID
     * @return 结果
     */
     int deleteBtcInvestByIds(Long[] ids);

    /**
     * 删除比特币投资信息
     *
     * @param id 比特币投资ID
     * @return 结果
     */
     int deleteBtcInvestById(Long id);
}
