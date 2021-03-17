package com.ruoyi.credit.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author zycstart
 * @create 2020-03-12 22:05
 */
public class CalculateRepayDayServiceImpl {

    /**
     * 获取指定日期的后多少天
     *
     * @param year       年
     * @param month      月
     * @param dayOfMonth 日
     * @param days       增加天
     * @return 后多少天
     */
    public static LocalDate getPlusDay(int year, int month, int dayOfMonth, int days) {
        return LocalDate.of(year, month, dayOfMonth).plusDays(days);
    }

    public static int daysBetweenDates(LocalDate start, LocalDate end) {
        return (int) Math.abs(ChronoUnit.DAYS.between(start, end));
    }

    public static void main(String[] args) {
        System.out.println(getPlusDay(2020, 1, 20, 25));
        System.out.println(getPlusDay(2020, 2, 8, 25));
        System.out.println(getPlusDay(2020, 3, 20, 25));
        System.out.println(getPlusDay(2020, 4, 20, 25));
        System.out.println(getPlusDay(2020, 5, 20, 25));
        System.out.println(getPlusDay(2020, 6, 20, 25));
        System.out.println(getPlusDay(2020, 7, 20, 25));
        System.out.println(getPlusDay(2020, 8, 20, 25));
        System.out.println(getPlusDay(2020, 9, 20, 25));
        System.out.println(getPlusDay(2020, 10, 20, 25));
        System.out.println(getPlusDay(2020, 11, 20, 25));
        System.out.println(getPlusDay(2020, 12, 20, 25));

        System.out.println(daysBetweenDates(LocalDate.of(2020, 2, 20), LocalDate.of(2020, 3, 17)));
        System.out.println(daysBetweenDates(LocalDate.of(2021, 2, 8), LocalDate.of(2021, 3, 28)));

        System.out.println(LocalDate.of(2021, 3, 28).toEpochDay() - LocalDate.of(2021, 2, 8).toEpochDay());
        System.out.println(LocalDate.of(2021, 3, 17).toEpochDay() - LocalDate.of(2021, 2, 20).toEpochDay());
        System.out.println(LocalDate.of(2021, 3, 17).toEpochDay() - LocalDate.of(2021, 3, 16).toEpochDay());

        System.out.println(cacl(new int[]{1, 2, 1, 2, 3}));
        System.out.println(cacl(new int[]{1, 2, 1, 2, 3}));

    }

    public static int cacl(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            return arr[i]^arr[i+1];
        }
        return 0;
    }
}
