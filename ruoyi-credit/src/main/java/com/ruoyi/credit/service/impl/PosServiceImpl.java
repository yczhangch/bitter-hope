package com.ruoyi.credit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.credit.mapper.PosMapper;
import com.ruoyi.credit.domain.Pos;
import com.ruoyi.credit.service.IPosService;

/**
 * pos机Service业务层处理
 *
 * @author hope
 * @date 2021-03-16
 */
@Service
public class PosServiceImpl implements IPosService {
    @Autowired
    private PosMapper posMapper;

    /**
     * 查询pos机
     *
     * @param id pos机ID
     * @return pos机
     */
    @Override
    public Pos selectPosById(Long id) {
        return posMapper.selectPosById(id);
    }

    /**
     * 查询pos机列表
     *
     * @param pos pos机
     * @return pos机
     */
    @Override
    public List<Pos> selectPosList(Pos pos) {
        return posMapper.selectPosList(pos);
    }

    /**
     * 新增pos机
     *
     * @param pos pos机
     * @return 结果
     */
    @Override
    public int insertPos(Pos pos) {
        pos.setCreateTime(DateUtils.getNowDate());
        return posMapper.insertPos(pos);
    }

    /**
     * 修改pos机
     *
     * @param pos pos机
     * @return 结果
     */
    @Override
    public int updatePos(Pos pos) {
        pos.setUpdateTime(DateUtils.getNowDate());
        return posMapper.updatePos(pos);
    }

    /**
     * 批量删除pos机
     *
     * @param ids 需要删除的pos机ID
     * @return 结果
     */
    @Override
    public int deletePosByIds(Long[] ids) {
        return posMapper.deletePosByIds(ids);
    }

    /**
     * 删除pos机信息
     *
     * @param id pos机ID
     * @return 结果
     */
    @Override
    public int deletePosById(Long id) {
        return posMapper.deletePosById(id);
    }
}
