package com.ruoyi.credit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.credit.mapper.CreditCardMapper;
import com.ruoyi.credit.domain.CreditCard;
import com.ruoyi.credit.service.ICreditCardService;

/**
 * 信用卡Service业务层处理
 *
 * @author hope
 * @date 2021-03-12
 */
@Service
public class CreditCardServiceImpl implements ICreditCardService {
    @Autowired
    private CreditCardMapper creditCardMapper;

    /**
     * 查询信用卡
     *
     * @param id 信用卡ID
     * @return 信用卡
     */
    @Override
    public CreditCard selectCreditCardById(Long id) {
        return creditCardMapper.selectCreditCardById(id);
    }

    /**
     * 查询信用卡列表
     *
     * @param creditCard 信用卡
     * @return 信用卡
     */
    @Override
    public List<CreditCard> selectCreditCardList(CreditCard creditCard) {
        return creditCardMapper.selectCreditCardList(creditCard);
    }

    /**
     * 新增信用卡
     *
     * @param creditCard 信用卡
     * @return 结果
     */
    @Override
    public int insertCreditCard(CreditCard creditCard) {
        creditCard.setCreateTime(DateUtils.getNowDate());
        return creditCardMapper.insertCreditCard(creditCard);
    }

    /**
     * 修改信用卡
     *
     * @param creditCard 信用卡
     * @return 结果
     */
    @Override
    public int updateCreditCard(CreditCard creditCard) {
        creditCard.setUpdateTime(DateUtils.getNowDate());
        return creditCardMapper.updateCreditCard(creditCard);
    }

    /**
     * 批量删除信用卡
     *
     * @param ids 需要删除的信用卡ID
     * @return 结果
     */
    @Override
    public int deleteCreditCardByIds(Long[] ids) {
        return creditCardMapper.deleteCreditCardByIds(ids);
    }

    /**
     * 删除信用卡信息
     *
     * @param id 信用卡ID
     * @return 结果
     */
    @Override
    public int deleteCreditCardById(Long id) {
        return creditCardMapper.deleteCreditCardById(id);
    }
}
