package com.ruoyi.credit.mapper;

import java.util.List;
import com.ruoyi.credit.domain.PosTradeHistory;

/**
 * pos机交易历史Mapper接口
 *
 * @author hope
 * @date 2021-05-09
 */
public interface PosTradeHistoryMapper {
    /**
     * 查询pos机交易历史
     *
     * @param id pos机交易历史ID
     * @return pos机交易历史
     */
     PosTradeHistory selectPosTradeHistoryById(Long id);

    /**
     * 查询pos机交易历史列表
     *
     * @param posTradeHistory pos机交易历史
     * @return pos机交易历史集合
     */
     List<PosTradeHistory> selectPosTradeHistoryList(PosTradeHistory posTradeHistory);

    /**
     * 新增pos机交易历史
     *
     * @param posTradeHistory pos机交易历史
     * @return 结果
     */
     int insertPosTradeHistory(PosTradeHistory posTradeHistory);

    /**
     * 修改pos机交易历史
     *
     * @param posTradeHistory pos机交易历史
     * @return 结果
     */
     int updatePosTradeHistory(PosTradeHistory posTradeHistory);

    /**
     * 删除pos机交易历史
     *
     * @param id pos机交易历史ID
     * @return 结果
     */
     int deletePosTradeHistoryById(Long id);

    /**
     * 批量删除pos机交易历史
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     int deletePosTradeHistoryByIds(Long[] ids);
}
