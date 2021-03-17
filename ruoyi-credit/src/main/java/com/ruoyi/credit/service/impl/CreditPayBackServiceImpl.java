package com.ruoyi.credit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.credit.mapper.CreditPayBackMapper;
import com.ruoyi.credit.domain.CreditPayBack;
import com.ruoyi.credit.service.ICreditPayBackService;

/**
 * 信用卡还款日提醒Service业务层处理
 *
 * @author hope
 * @date 2021-03-12
 */
@Service
public class CreditPayBackServiceImpl implements ICreditPayBackService {
    @Autowired
    private CreditPayBackMapper creditPayBackMapper;

    /**
     * 查询信用卡还款日提醒
     *
     * @param id 信用卡还款日提醒ID
     * @return 信用卡还款日提醒
     */
    @Override
    public CreditPayBack selectCreditPayBackById(Long id) {
        return creditPayBackMapper.selectCreditPayBackById(id);
    }

    /**
     * 查询信用卡还款日提醒列表
     *
     * @param creditPayBack 信用卡还款日提醒
     * @return 信用卡还款日提醒
     */
    @Override
    public List<CreditPayBack> selectCreditPayBackList(CreditPayBack creditPayBack) {
        return creditPayBackMapper.selectCreditPayBackList(creditPayBack);
    }

    /**
     * 新增信用卡还款日提醒
     *
     * @param creditPayBack 信用卡还款日提醒
     * @return 结果
     */
    @Override
    public int insertCreditPayBack(CreditPayBack creditPayBack) {
        creditPayBack.setCreateTime(DateUtils.getNowDate());
        return creditPayBackMapper.insertCreditPayBack(creditPayBack);
    }

    /**
     * 修改信用卡还款日提醒
     *
     * @param creditPayBack 信用卡还款日提醒
     * @return 结果
     */
    @Override
    public int updateCreditPayBack(CreditPayBack creditPayBack) {
        creditPayBack.setUpdateTime(DateUtils.getNowDate());
        return creditPayBackMapper.updateCreditPayBack(creditPayBack);
    }

    /**
     * 批量删除信用卡还款日提醒
     *
     * @param ids 需要删除的信用卡还款日提醒ID
     * @return 结果
     */
    @Override
    public int deleteCreditPayBackByIds(Long[] ids) {
        return creditPayBackMapper.deleteCreditPayBackByIds(ids);
    }

    /**
     * 删除信用卡还款日提醒信息
     *
     * @param id 信用卡还款日提醒ID
     * @return 结果
     */
    @Override
    public int deleteCreditPayBackById(Long id) {
        return creditPayBackMapper.deleteCreditPayBackById(id);
    }
}
