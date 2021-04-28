package com.ruoyi.invest.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.invest.domain.BtcInvest;
import com.ruoyi.invest.service.IBtcInvestService;
import org.springframework.stereotype.Service;
import com.ruoyi.invest.mapper.BtcInvestMapper;

import javax.annotation.Resource;

/**
 * 比特币投资Service业务层处理
 *
 * @author hope
 * @date 2021-04-15
 */
@Service
public class BtcInvestServiceImpl implements IBtcInvestService {
    @Resource
    private BtcInvestMapper btcInvestMapper;

    /**
     * 查询比特币投资
     *
     * @param id 比特币投资ID
     * @return 比特币投资
     */
    @Override
    public BtcInvest selectBtcInvestById(Long id) {
        return btcInvestMapper.selectBtcInvestById(id);
    }

    /**
     * 查询比特币投资列表
     *
     * @param btcInvest 比特币投资
     * @return 比特币投资
     */
    @Override
    public List<BtcInvest> selectBtcInvestList(BtcInvest btcInvest) {
        return btcInvestMapper.selectBtcInvestList(btcInvest);
    }

    /**
     * 新增比特币投资
     *
     * @param btcInvest 比特币投资
     * @return 结果
     */
    @Override
    public int insertBtcInvest(BtcInvest btcInvest) {
        btcInvest.setCreateTime(DateUtils.getNowDate());
        return btcInvestMapper.insertBtcInvest(btcInvest);
    }

    /**
     * 修改比特币投资
     *
     * @param btcInvest 比特币投资
     * @return 结果
     */
    @Override
    public int updateBtcInvest(BtcInvest btcInvest) {
        btcInvest.setUpdateTime(DateUtils.getNowDate());
        return btcInvestMapper.updateBtcInvest(btcInvest);
    }

    /**
     * 批量删除比特币投资
     *
     * @param ids 需要删除的比特币投资ID
     * @return 结果
     */
    @Override
    public int deleteBtcInvestByIds(Long[] ids) {
        return btcInvestMapper.deleteBtcInvestByIds(ids);
    }

    /**
     * 删除比特币投资信息
     *
     * @param id 比特币投资ID
     * @return 结果
     */
    @Override
    public int deleteBtcInvestById(Long id) {
        return btcInvestMapper.deleteBtcInvestById(id);
    }
}
