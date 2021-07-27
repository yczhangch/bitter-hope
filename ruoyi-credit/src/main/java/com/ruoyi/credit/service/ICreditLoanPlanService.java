package com.ruoyi.credit.service;

import java.util.List;
import com.ruoyi.credit.domain.CreditLoanPlan;

/**
 * 信用卡借款计划Service接口
 *
 * @author hope
 * @date 2021-03-17
 */
public interface ICreditLoanPlanService {
    /**
     * 查询信用卡借款计划
     *
     * @param id 信用卡借款计划ID
     * @return 信用卡借款计划
     */
     CreditLoanPlan selectCreditLoanPlanById(Long id);

    /**
     * 查询信用卡借款计划列表
     *
     * @param creditLoanPlan 信用卡借款计划
     * @return 信用卡借款计划集合
     */
     List<CreditLoanPlan> selectCreditLoanPlanList(CreditLoanPlan creditLoanPlan);

    /**
     * 新增信用卡借款计划
     *
     * @param creditLoanPlan 信用卡借款计划
     * @return 结果
     */
     int insertCreditLoanPlan(CreditLoanPlan creditLoanPlan);

    /**
     * 修改信用卡借款计划
     *
     * @param creditLoanPlan 信用卡借款计划
     * @return 结果
     */
     int updateCreditLoanPlan(CreditLoanPlan creditLoanPlan);

    /**
     * 批量删除信用卡借款计划
     *
     * @param ids 需要删除的信用卡借款计划ID
     * @return 结果
     */
     int deleteCreditLoanPlanByIds(Long[] ids);

    /**
     * 删除信用卡借款计划信息
     *
     * @param id 信用卡借款计划ID
     * @return 结果
     */
     int deleteCreditLoanPlanById(Long id);

    /**
     * 信用卡
     * @param creditLoanPlan creditLoanPlan
     * @return 结果
     */
    int changeLoadStatus(CreditLoanPlan creditLoanPlan);
}
