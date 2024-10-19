-- alatka.ALK_RULE_GROUP_DEFINITION definition

CREATE TABLE `ALK_RULE_GROUP_DEFINITION`
(
    `G_ID`      int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `G_KEY`     varchar(50)  NOT NULL COMMENT '关键字',
    `G_NAME`    varchar(100) NOT NULL COMMENT '名称',
    `G_TYPE`    varchar(20)  NOT NULL COMMENT '类型',
    `G_ENABLED` tinyint(1) NOT NULL COMMENT '是否可用',
    PRIMARY KEY (`G_ID`),
    UNIQUE KEY `ALK_RULE_GROUP_DEFINITION_UNIQUE` (`G_KEY`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则组表';


-- alatka.ALK_RULE_DEFINITION definition

CREATE TABLE `ALK_RULE_DEFINITION`
(
    `R_ID`       int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `R_NAME`     varchar(100)  NOT NULL COMMENT '名称',
    `R_DESC`     varchar(5000) NOT NULL COMMENT '描述',
    `R_PRIORITY` int           NOT NULL COMMENT '优先级',
    `R_SCORE`    int           NOT NULL COMMENT '评分',
    `R_ORDER`    int           NOT NULL COMMENT '顺序',
    `R_ENABLED`  tinyint(1) NOT NULL COMMENT '是否可用',
    `G_KEY`      varchar(50)   NOT NULL COMMENT 'ALK_RULE_GROUP_DEFINITION->G_KEY',
    PRIMARY KEY (`R_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则表';


-- alatka.ALK_RULE_UNIT_DEFINITION definition

CREATE TABLE `ALK_RULE_UNIT_DEFINITION`
(
    `U_ID`         int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `U_EXPRESSION` varchar(10000) NOT NULL COMMENT '表达式',
    `U_ORDER`      int            NOT NULL COMMENT '顺序',
    `U_ENABLED`    tinyint(1) NOT NULL COMMENT '是否可用',
    `D_KEY`        varchar(50) DEFAULT NULL COMMENT 'ALK_RULE_DATASOURCE_DEFINITION->U_KEY',
    `R_ID`         int unsigned NOT NULL COMMENT 'ALK_RULE_DEFINITION->R_ID',
    `G_KEY`        varchar(50)    NOT NULL COMMENT 'ALK_RULE_GROUP_DEFINITION->G_KEY',
    PRIMARY KEY (`U_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则单元表';


-- alatka.ALK_RULE_DATASOURCE_DEFINITION definition

CREATE TABLE `ALK_RULE_DATASOURCE_DEFINITION`
(
    `D_ID`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `D_KEY`         varchar(50)  NOT NULL COMMENT '关键字',
    `D_NAME`        varchar(100) NOT NULL COMMENT '名称',
    `D_TYPE`        varchar(20)  NOT NULL COMMENT '类型',
    `D_SCOPE`       varchar(50)  NOT NULL COMMENT '数据范围',
    `D_RESULT_TYPE` varchar(20)  NOT NULL COMMENT '数据结果类型',
    `D_ENABLED`     tinyint(1) NOT NULL COMMENT '是否可用',
    `G_KEY`         varchar(50)  NOT NULL COMMENT 'ALK_RULE_GROUP_DEFINITION->G_KEY',
    `D_PARAM_K1`    varchar(50)    DEFAULT NULL COMMENT '自定义参数key1',
    `D_PARAM_V1`    varchar(10000) DEFAULT NULL COMMENT '自定义参数value1',
    `D_PARAM_K2`    varchar(50)    DEFAULT NULL COMMENT '自定义参数key2',
    `D_PARAM_V2`    varchar(1000)  DEFAULT NULL COMMENT '自定义参数value2',
    `D_PARAM_K3`    varchar(50)    DEFAULT NULL COMMENT '自定义参数key3',
    `D_PARAM_V3`    varchar(1000)  DEFAULT NULL COMMENT '自定义参数value3',
    `D_PARAM_K4`    varchar(50)    DEFAULT NULL COMMENT '自定义参数key4',
    `D_PARAM_V4`    varchar(1000)  DEFAULT NULL COMMENT '自定义参数value4',
    `D_PARAM_K5`    varchar(50)    DEFAULT NULL COMMENT '自定义参数key5',
    `D_PARAM_V5`    varchar(1000)  DEFAULT NULL COMMENT '自定义参数value5',
    PRIMARY KEY (`D_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则外部数据源表';