package com.ruoyi.credit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.credit.mapper.CreditLoanPlanMapper;
import com.ruoyi.credit.domain.CreditLoanPlan;
import com.ruoyi.credit.service.ICreditLoanPlanService;

/**
 * 信用卡借款计划Service业务层处理
 *
 * @author hope
 * @date 2021-03-17
 */
@Service
public class CreditLoanPlanServiceImpl implements ICreditLoanPlanService {
    @Autowired
    private CreditLoanPlanMapper creditLoanPlanMapper;

    /**
     * 查询信用卡借款计划
     *
     * @param id 信用卡借款计划ID
     * @return 信用卡借款计划
     */
    @Override
    public CreditLoanPlan selectCreditLoanPlanById(Long id) {
        return creditLoanPlanMapper.selectCreditLoanPlanById(id);
    }

    /**
     * 查询信用卡借款计划列表
     *
     * @param creditLoanPlan 信用卡借款计划
     * @return 信用卡借款计划
     */
    @Override
    public List<CreditLoanPlan> selectCreditLoanPlanList(CreditLoanPlan creditLoanPlan) {
        return creditLoanPlanMapper.selectCreditLoanPlanList(creditLoanPlan);
    }

    /**
     * 新增信用卡借款计划
     *
     * @param creditLoanPlan 信用卡借款计划
     * @return 结果
     */
    @Override
    public int insertCreditLoanPlan(CreditLoanPlan creditLoanPlan) {
        creditLoanPlan.setCreateTime(DateUtils.getNowDate());
        return creditLoanPlanMapper.insertCreditLoanPlan(creditLoanPlan);
    }

    /**
     * 修改信用卡借款计划
     *
     * @param creditLoanPlan 信用卡借款计划
     * @return 结果
     */
    @Override
    public int updateCreditLoanPlan(CreditLoanPlan creditLoanPlan) {
        creditLoanPlan.setUpdateTime(DateUtils.getNowDate());
        return creditLoanPlanMapper.updateCreditLoanPlan(creditLoanPlan);
    }

    /**
     * 批量删除信用卡借款计划
     *
     * @param ids 需要删除的信用卡借款计划ID
     * @return 结果
     */
    @Override
    public int deleteCreditLoanPlanByIds(Long[] ids) {
        return creditLoanPlanMapper.deleteCreditLoanPlanByIds(ids);
    }

    /**
     * 删除信用卡借款计划信息
     *
     * @param id 信用卡借款计划ID
     * @return 结果
     */
    @Override
    public int deleteCreditLoanPlanById(Long id) {
        return creditLoanPlanMapper.deleteCreditLoanPlanById(id);
    }
}
