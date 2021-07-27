package com.ruoyi.invest.service.impl;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EMAServiceImpl {

    public BigDecimal getEMA(@NotEmpty List<BigDecimal> closePriceList, String nDay) {
        // 平滑系数
        BigDecimal x = new BigDecimal("2").divide(new BigDecimal(nDay).add(BigDecimal.ONE), 10, RoundingMode.DOWN);

        BigDecimal dividend = BigDecimal.ZERO;
        BigDecimal divide = BigDecimal.ZERO;

        for (int i = 0; i < closePriceList.size(); i++) {
            dividend = closePriceList.get(i).multiply(BigDecimal.ONE.subtract(x)).pow(i).add(dividend);
            divide = (BigDecimal.ONE.subtract(x)).pow(i).add(divide);
        }
        return dividend.divide(divide, 2, RoundingMode.DOWN);
    }


    /**
     * Calculate EMA,
     *
     * @param list :Price list to calculate，the first at head, the last at tail.
     * @return
     */
    public static Double getEXPMA(final List<Double> list, final int number) {
        // 开始计算EMA值，
        Double k = 2.0 / (number + 1.0);// 计算出序数
        Double ema = list.get(0);// 第一天ema等于当天收盘价
        for (int i = 1; i < list.size(); i++) {
            // 第二天以后，当天收盘 收盘价乘以系数再加上昨天EMA乘以系数-1
            ema = list.get(i) * k + ema * (1 - k);
        }
        return ema;
    }

    public static void main(String[] args) {

    }



    /**
     * calculate MACD values
     *
     * @param list        :Price list to calculate，the first at head, the last at tail.
     * @param shortPeriod :the short period value.  30
     * @param longPeriod  :the long period value.  70
     * @param midPeriod   :the mid period value.
     * @return
     */
    public static HashMap<String, Double> getMACD(final List<Double> list, final int shortPeriod, final int longPeriod, int midPeriod) {
        HashMap<String, Double> macdData = new HashMap<>();
        List<Double> diffList = new ArrayList<>();
        Double shortEMA;
        Double longEMA;
        Double dif = 0.0;
        Double dea;

        for (int i = list.size() - 1; i >= 0; i--) {
            List<Double> sublist = list.subList(0, list.size() - i);
            shortEMA = getEXPMA(sublist, shortPeriod);
            longEMA = getEXPMA(sublist, longPeriod);
            dif = shortEMA - longEMA;
            diffList.add(dif);
        }
        dea = getEXPMA(diffList, midPeriod);
        macdData.put("DIF", dif);
        macdData.put("DEA", dea);
        macdData.put("MACD", (dif - dea) * 2);
        return macdData;
    }
}
