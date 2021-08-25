package com.ruoyi.invest.constant;

public class InvestConstant {

    public static final String NOT_NONE = "N";

    public static final String HAS_NONE = "Y";

    public static final int MONEY_SCALE = 2;

    /**
     * 百分比  0.1234  12.34%
     */
    public static final int RATIO_SCALE = 4;

    public static final String FUND_NAME_CODE_SPLITTER = "-";

    public static final String EMPTY_FUND_INFO = "jsonpgz();";

    public enum FundTradeType {
        Buy("1"),
        Sell("0"),
        Fixed("2"),
        Bonus("3");

        private final String tradeType;

        FundTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        public String getTradeType() {
            return tradeType;
        }
    }
}
