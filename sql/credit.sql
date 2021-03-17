-- ----------------------------
-- 1、信用卡表
-- ----------------------------
drop table if exists t_credit_card;
CREATE TABLE `t_credit_card` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
     `card_num` varchar(20) NOT NULL COMMENT '卡号',
     `card_name` varchar(60) DEFAULT NULL COMMENT '信用卡名',
     `credit_limit` int(11) DEFAULT NULL COMMENT '额度(元)',
     `bill_day` varchar(20) DEFAULT NULL COMMENT '账单日',
     `repay_day` varchar(20) DEFAULT NULL COMMENT '还款日',
     `annual_fee` decimal(8,2) DEFAULT NULL COMMENT '年费(元)',
     `bank` varchar(20) DEFAULT NULL COMMENT '发卡银行',
     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
     `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='信用卡表';

-- ----------------------------
-- 2、 信用卡借款计划
-- ----------------------------
drop table if exists t_credit_loan_plan;
CREATE TABLE `t_credit_loan_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
  `card_id` bigint(20) DEFAULT NULL COMMENT '信用卡id',
  `loan_date` datetime DEFAULT NULL COMMENT '借款日',
  `load_money` decimal(8,2) DEFAULT NULL COMMENT '借款金额',
  `pos_id` bigint(20) DEFAULT NULL COMMENT 'pos机编号',
  `fee` decimal(6,2) DEFAULT NULL COMMENT '费用',
  `is_loaded` varchar(4) DEFAULT NULL COMMENT '是否借款',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='信用卡借款计划';

-- ----------------------------
-- 3、 信用卡还款日提醒
-- ----------------------------
drop table if exists t_credit_pay_back;
CREATE TABLE `t_credit_pay_back` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
 `card_id` bigint(20) NOT NULL COMMENT '信用卡id',
 `year` int(4) DEFAULT NULL COMMENT '年',
 `month` int(2) DEFAULT NULL COMMENT '账单月份',
 `bill_day` datetime DEFAULT NULL COMMENT '账单日',
 `pay_back_day` datetime DEFAULT NULL COMMENT '还款日',
 `bill` decimal(8,2) DEFAULT NULL COMMENT '账单金额(元)',
 `is_payed` varchar(2) DEFAULT NULL COMMENT '是否还款',
 `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
 `remark` varchar(255) DEFAULT NULL COMMENT '备注',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='信用卡还款日提醒';

-- ----------------------------
-- 4、 pos机表
-- ----------------------------
drop table if exists t_pos;
CREATE TABLE `t_pos` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
     `pos_name` varchar(20) NOT NULL COMMENT 'pos机名',
     `fee_level_one` decimal(6,2) DEFAULT NULL COMMENT '小额费率',
     `fee_level_two` decimal(6,2) DEFAULT NULL COMMENT '刷卡费率',
     `fixed_cost` decimal(6,2) DEFAULT NULL COMMENT '固定费用',
     `apply_year` int(11) DEFAULT NULL COMMENT '申请年份',
     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
     `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='pos机';

commit;
