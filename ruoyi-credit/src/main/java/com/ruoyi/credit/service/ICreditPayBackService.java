package com.ruoyi.credit.service;

import java.util.List;
import com.ruoyi.credit.domain.CreditPayBack;

/**
 * 信用卡还款日提醒Service接口
 *
 * @author hope
 * @date 2021-03-12
 */
public interface ICreditPayBackService {
    /**
     * 查询信用卡还款日提醒
     *
     * @param id 信用卡还款日提醒ID
     * @return 信用卡还款日提醒
     */
     CreditPayBack selectCreditPayBackById(Long id);

    /**
     * 查询信用卡还款日提醒列表
     *
     * @param creditPayBack 信用卡还款日提醒
     * @return 信用卡还款日提醒集合
     */
     List<CreditPayBack> selectCreditPayBackList(CreditPayBack creditPayBack);

    /**
     * 新增信用卡还款日提醒
     *
     * @param creditPayBack 信用卡还款日提醒
     * @return 结果
     */
     int insertCreditPayBack(CreditPayBack creditPayBack);

    /**
     * 修改信用卡还款日提醒
     *
     * @param creditPayBack 信用卡还款日提醒
     * @return 结果
     */
     int updateCreditPayBack(CreditPayBack creditPayBack);

    /**
     * 批量删除信用卡还款日提醒
     *
     * @param ids 需要删除的信用卡还款日提醒ID
     * @return 结果
     */
     int deleteCreditPayBackByIds(Long[] ids);

    /**
     * 删除信用卡还款日提醒信息
     *
     * @param id 信用卡还款日提醒ID
     * @return 结果
     */
     int deleteCreditPayBackById(Long id);
}
