package com.ruoyi.credit.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.credit.domain.CreditCard;
import com.ruoyi.credit.domain.CreditLoanPlan;
import com.ruoyi.credit.mapper.CreditLoanPlanMapper;
import com.ruoyi.credit.service.ICreditCardService;
import com.ruoyi.credit.service.ICreditLoanPlanService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 信用卡借款计划Service业务层处理
 *
 * @author hope
 * @date 2021-03-17
 */
@Service
public class CreditLoanPlanServiceImpl implements ICreditLoanPlanService {
    @Resource
    private CreditLoanPlanMapper creditLoanPlanMapper;

    @Resource
    private ICreditCardService creditCardService;

    private final BigDecimal lowFeeRate = new BigDecimal("0.0038");

    private final BigDecimal highFeeRate = new BigDecimal("0.0058");

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
        // 默认查询近两个月
        Map<String, Object> params = creditLoanPlan.getParams();
        if(params.get("beginLoanDate")==null && params.get("endLoanDate")==null){
            params.put("beginLoanDate",DateUtils.getLastMonthOfFirstDay());
            params.put("endLoanDate",DateUtils.getNextMonthOfLastDay());
        }
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
        BigDecimal loadMoney = creditLoanPlan.getLoadMoney();
        if (loadMoney != null) {
            creditLoanPlan.setFee(loadMoney.compareTo(new BigDecimal("1000")) <= 0 ? loadMoney.multiply(lowFeeRate) : loadMoney.multiply(highFeeRate));
        }
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

    @Override
    public int changeLoadStatus(CreditLoanPlan creditLoanPlan) {
        return creditLoanPlanMapper.updateCreditLoanPlan(creditLoanPlan);
    }

    public void generateCardLoadPlan() {
        // 删除

        // 授信总额度
        BigDecimal totalCreditLimit = creditCardService.getTotalCreditLimit();

        List<CreditCard> creditCards = creditCardService.selectCreditCardList(null);
        if (CollectionUtils.isEmpty(creditCards)) {
            return;
        }

        // 25 + 3 + 1
        for (CreditCard creditCard : creditCards) {
            final BigDecimal creditLimit = creditCard.getCreditLimit();
            final List<BigDecimal> bigLoadMoneyList = new ArrayList<>();
            final List<BigDecimal> loadMoneyList = new ArrayList<>();

            if (creditLimit.compareTo(new BigDecimal("5000")) > 0) {
                // 3笔(单笔不超过额度的30%)中额
                IntStream.range(0, 3).forEach(x -> bigLoadMoneyList.add(getRandomValue30Percent(creditLimit)));
                // 1笔(单笔不超过额度的60%)大额
                bigLoadMoneyList.add(getRandomValue60Percent(creditLimit));
            }
            // 3+1
            bigLoadMoneyList.stream().sorted(Comparator.reverseOrder()).forEach(loadMoney -> genLoadPlan(creditCard, loadMoney, 3));

            // 25笔(100一500元单笔消费)小额
            IntStream.range(0, 25).forEach(x -> loadMoneyList.add(getRandomValueBetween100And500()));

            // 乱序
            Collections.shuffle(loadMoneyList);
            loadMoneyList.forEach(loadMoney -> genLoadPlan(creditCard, loadMoney, 15));
        }

    }

    private void genLoadPlan(CreditCard creditCard, BigDecimal loadMoney, Integer afterDays) {
        CreditLoanPlan creditLoanPlan = new CreditLoanPlan();
        creditLoanPlan.setCardId(creditCard.getId());
        creditLoanPlan.setCardName(creditCard.getCardName());
        creditLoanPlan.setCardNum(creditCard.getCardNum());
        creditLoanPlan.setIsLoaded("N");
        creditLoanPlan.setLoadMoney(loadMoney);
        creditLoanPlan.setLoanDate(getLoadDate(creditCard.getBillDay(), afterDays));
        creditLoanPlan.setCreateTime(new Date());
        creditLoanPlan.setUpdateTime(new Date());

        // fee , pos
        if (loadMoney.compareTo(new BigDecimal("1000")) <= 0) {
            creditLoanPlan.setFee(loadMoney.multiply(lowFeeRate));
        } else {
            creditLoanPlan.setFee(loadMoney.multiply(highFeeRate));
        }

        creditLoanPlan.setPosId(1L);
        creditLoanPlanMapper.insertCreditLoanPlan(creditLoanPlan);
    }

    public static BigDecimal getRandomValueBetween100And500() {
        //  100 + 20*20=500
        Random random = new Random();
        int total = random.nextInt(20) * 20 + 100;
        return new BigDecimal(total);
    }

    public static BigDecimal getRandomValue30Percent(BigDecimal creditLimit) {
        // 10%~30%
        Random random = new Random();
        return new BigDecimal(random.nextInt(20) + 10).multiply(creditLimit).divide(new BigDecimal("100"), 2);
    }

    public static BigDecimal getRandomValue60Percent(BigDecimal creditLimit) {
        // 30%~60%
        Random random = new Random();
        return new BigDecimal(random.nextInt(30) + 30).multiply(creditLimit).divide(new BigDecimal("100"), 2);
    }

    /**
     * 借款日期，25笔小额用15，3+1用3
     *
     * @param billDay   账单日
     * @param afterDays 账单日后几天
     * @return 借款日期
     */
    public static Date getLoadDate(String billDay, Integer afterDays) {
        // 账单日
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD);

        Date billDate = null;
        try {
            billDate = simpleDateFormat.parse(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + billDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateUtils.addDays(billDate, new Random().nextInt(afterDays) + 1);
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 20; i++) {
//            System.out.println(getRandomValueBetween100And500());
            //   System.out.println(getRandomValue60Percent(new BigDecimal("5000")));
        }
        final List<BigDecimal> list = new ArrayList<>();
        final BigDecimal creditLimit = new BigDecimal("5000");
        IntStream.range(0, 3).forEach(x -> list.add(getRandomValue30Percent(creditLimit)));
        System.out.println(list);
        Collections.shuffle(list);
        System.out.println(list);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD);
        Date billDate = simpleDateFormat.parse(Calendar.getInstance().get(Calendar.YEAR) + "-" + 7 + "-" + 20);
        System.out.println(simpleDateFormat.format(billDate));
    }
}
