package com.ruoyi.credit.mapper;

import java.util.List;
import com.ruoyi.credit.domain.Pos;

/**
 * pos机Mapper接口
 *
 * @author hope
 * @date 2021-03-16
 */
public interface PosMapper {
    /**
     * 查询pos机
     *
     * @param id pos机ID
     * @return pos机
     */
     Pos selectPosById(Long id);

    /**
     * 查询pos机列表
     *
     * @param pos pos机
     * @return pos机集合
     */
     List<Pos> selectPosList(Pos pos);

    /**
     * 新增pos机
     *
     * @param pos pos机
     * @return 结果
     */
     int insertPos(Pos pos);

    /**
     * 修改pos机
     *
     * @param pos pos机
     * @return 结果
     */
     int updatePos(Pos pos);

    /**
     * 删除pos机
     *
     * @param id pos机ID
     * @return 结果
     */
     int deletePosById(Long id);

    /**
     * 批量删除pos机
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     int deletePosByIds(Long[] ids);
}
