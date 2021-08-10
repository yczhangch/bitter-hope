package com.ruoyi.invest.task;

import lombok.Data;

@Data
public class RealTimeFundInfo {
    /**
     * 基金代码
     */
    private String fundcode;
    /**
     * 基金名称
     */
    private String name;
    /**
     * 净值日期
     */
    private String jzrq;
    /**
     * 单位净值
     */
    private String dwjz;
    /**
     * 估算值
     */
    private String gsz;
    /**
     * 估算涨跌幅
     */
    private String gszzl;
    /**
     * 估算时间
     */
    private String gztime;
}
