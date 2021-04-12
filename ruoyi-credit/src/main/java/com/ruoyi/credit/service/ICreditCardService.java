package com.ruoyi.credit.service;

import java.util.List;
import com.ruoyi.credit.domain.CreditCard;

/**
 * 信用卡Service接口
 *
 * @author hope
 * @date 2021-03-12
 */
public interface ICreditCardService {
    /**
     * 查询信用卡
     *
     * @param id 信用卡ID
     * @return 信用卡
     */
     CreditCard selectCreditCardById(Long id);

    /**
     * 查询信用卡列表
     *
     * @param creditCard 信用卡
     * @return 信用卡集合
     */
     List<CreditCard> selectCreditCardList(CreditCard creditCard);

    /**
     * 新增信用卡
     *
     * @param creditCard 信用卡
     * @return 结果
     */
     int insertCreditCard(CreditCard creditCard);

    /**
     * 修改信用卡
     *
     * @param creditCard 信用卡
     * @return 结果
     */
     int updateCreditCard(CreditCard creditCard);

    /**
     * 批量删除信用卡
     *
     * @param ids 需要删除的信用卡ID
     * @return 结果
     */
     int deleteCreditCardByIds(Long[] ids);

    /**
     * 删除信用卡信息
     *
     * @param id 信用卡ID
     * @return 结果
     */
     int deleteCreditCardById(Long id);

}
