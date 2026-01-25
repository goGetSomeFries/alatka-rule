# 基于AviatorScript实现的规则引擎工具

基于AviatorScript实现的规则引擎包，只包括核心逻辑，小巧灵活，无过多依赖，能够快速与应用集成；可在线部署，支持蓝绿发布；零代码开发，支持多种规则配置方式；支持多种外部数据源配置，适用于风控等场景。

> [AviatorScript](https://github.com/killme2008/aviatorscript) 是一种高性能、轻量级的Java语言实现的表达式计算引擎，它允许在运行时动态执行字符串形式的表达式。

### 功能概述

- **规则配置方式多样**：支持Yaml、Xml、数据库三种配置方式
- **规则动态部署**：零代码开发，可在线部署，支持蓝绿发布
- **支持多种外部数据源**：数据库、redis、elasticsearch、mongodb等
- **小巧灵活**：只包括核心逻辑，整体包大小40k，可快速与应用集成

### 使用场景

- 风控类欺诈风险识别
- 营销类客户评分
- 合规管理审计风险评估

### 项目结构

| 模块                            | 描述                                                      |
|---------------------------------|----------------------------------------------------------|
| alatka-rule-core                | 规则引擎核心模块，包括规则配置解析和规则执行                   |
| alatka-rule-spring-boot-starter | alatka-rule-core模块包装的SpringBoot Starter，支持自动装配  |
| alatka-rule-aviator-extended    | AviatorScript扩展模块，支持AviatorScript自定义函数          |
| alatka-rule-admin               | 后台管理模块                                               |
| alatka-rule-example             | alatka-rule-admin & alatka-rule-spring-boot-starter 示例  |

### 版本对应关系

| alatka-rule  | alatka-dependencies       | alatka                    |
|--------------|---------------------------|---------------------------|
| 2.16.0-jdk17 | 1.68.0-jdk17              | 1.68.0-jdk17              |
| 2.15.0-jdk17 | 1.64.0-jdk17-1.67.0-jdk17 | 1.64.0-jdk17-1.67.0-jdk17 |
| 2.14.0-jdk17 | 1.63.0-jdk17              | 1.63.0-jdk17              |
| 2.13.0-jdk17 | 1.61.0-jdk17-1.62.0-jdk17 | 1.61.0-jdk17-1.62.0-jdk17 |
| 2.12.0-jdk17 | 1.59.0-jdk17-1.60.0-jdk17 | 1.59.0-jdk17-1.60.0-jdk17 |
| 2.11.0-jdk17 | 1.57.0-jdk17-1.58.0-jdk17 | 1.57.0-jdk17-1.58.0-jdk17 |
| 2.10.0-jdk17 | 1.56.0-jdk17              | 1.56.0-jdk17              |
| 2.9.1-jdk17  | 1.54.1-jdk17-1.55.0-jdk17 | 1.54.1-jdk17-1.55.0-jdk17 |
| 2.9.0-jdk17  | 1.54.0-jdk17              | 1.54.0-jdk17              |

`alatka-rule`、`alatka-dependencies`、`alatka`相关制品已上传至阿里云仓库，如需下载可进行如下配置： :point_right: [maven引入相关依赖](https://gitee.com/asuka2001/alatka-rule/wikis/%E4%B8%89%E3%80%81%E5%9F%BA%E7%A1%80%E4%BB%8B%E7%BB%8D/1%E3%80%81maven%E5%9D%90%E6%A0%87%E5%BC%95%E5%85%A5)

### [ :point_right: 访问wiki查看更多教程](https://gitee.com/asuka2001/alatka-rule/wikis)

### github地址

项目同步更新在github；如有需要， :point_right: [点击我访问](https://github.com/goGetSomeFries/alatka-rule)

### 感谢支持

如果觉得好用，欢迎推荐给身边同事同学朋友；也欢迎各位的issues和star，问题会及时回复，再次感谢大家的支持！