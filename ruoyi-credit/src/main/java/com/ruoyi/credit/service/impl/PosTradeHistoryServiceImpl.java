package com.ruoyi.credit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.credit.mapper.PosTradeHistoryMapper;
import com.ruoyi.credit.domain.PosTradeHistory;
import com.ruoyi.credit.service.IPosTradeHistoryService;

/**
 * pos机交易历史Service业务层处理
 *
 * @author hope
 * @date 2021-05-09
 */
@Service
public class PosTradeHistoryServiceImpl implements IPosTradeHistoryService {
    @Autowired
    private PosTradeHistoryMapper posTradeHistoryMapper;

    /**
     * 查询pos机交易历史
     *
     * @param id pos机交易历史ID
     * @return pos机交易历史
     */
    @Override
    public PosTradeHistory selectPosTradeHistoryById(Long id) {
        return posTradeHistoryMapper.selectPosTradeHistoryById(id);
    }

    /**
     * 查询pos机交易历史列表
     *
     * @param posTradeHistory pos机交易历史
     * @return pos机交易历史
     */
    @Override
    public List<PosTradeHistory> selectPosTradeHistoryList(PosTradeHistory posTradeHistory) {
        return posTradeHistoryMapper.selectPosTradeHistoryList(posTradeHistory);
    }

    /**
     * 新增pos机交易历史
     *
     * @param posTradeHistory pos机交易历史
     * @return 结果
     */
    @Override
    public int insertPosTradeHistory(PosTradeHistory posTradeHistory) {
        posTradeHistory.setCreateTime(DateUtils.getNowDate());
        return posTradeHistoryMapper.insertPosTradeHistory(posTradeHistory);
    }

    /**
     * 修改pos机交易历史
     *
     * @param posTradeHistory pos机交易历史
     * @return 结果
     */
    @Override
    public int updatePosTradeHistory(PosTradeHistory posTradeHistory) {
        posTradeHistory.setUpdateTime(DateUtils.getNowDate());
        return posTradeHistoryMapper.updatePosTradeHistory(posTradeHistory);
    }

    /**
     * 批量删除pos机交易历史
     *
     * @param ids 需要删除的pos机交易历史ID
     * @return 结果
     */
    @Override
    public int deletePosTradeHistoryByIds(Long[] ids) {
        return posTradeHistoryMapper.deletePosTradeHistoryByIds(ids);
    }

    /**
     * 删除pos机交易历史信息
     *
     * @param id pos机交易历史ID
     * @return 结果
     */
    @Override
    public int deletePosTradeHistoryById(Long id) {
        return posTradeHistoryMapper.deletePosTradeHistoryById(id);
    }
}