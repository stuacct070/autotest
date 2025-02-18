-- set innodb lock wait timeout
SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS platform_source(
    `platform` VARCHAR(50) NOT NULL   COMMENT '平台名称（国际飞书:LARK_SUITE，飞书:LARK，钉钉:DING_TALK，企业微信:WE_COM）' ,
    `config` BLOB NOT NULL   COMMENT '平台信息配置' ,
    `enable` BIT NOT NULL  DEFAULT 1 COMMENT '是否开启' ,
    `valid` BIT NOT NULL  DEFAULT 0 COMMENT '名称' ,
    PRIMARY KEY (platform)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '平台对接保存参数';

CREATE TABLE IF NOT EXISTS mind_additional_node(
    `id` VARCHAR(50) NOT NULL   COMMENT 'ID' ,
    `project_id` VARCHAR(50) NOT NULL   COMMENT '项目ID' ,
    `name` VARCHAR(255) NOT NULL   COMMENT '名称' ,
    `parent_id` VARCHAR(50) NOT NULL  DEFAULT 'NONE' COMMENT '父节点ID' ,
    `pos` BIGINT NOT NULL  DEFAULT 0 COMMENT '同一节点下的顺序' ,
    `create_time` BIGINT NOT NULL   COMMENT '创建时间' ,
    `update_time` BIGINT NOT NULL   COMMENT '更新时间' ,
    `create_user` VARCHAR(50) NOT NULL   COMMENT '创建人' ,
    `update_user` VARCHAR(50) NOT NULL   COMMENT '更新人' ,
    PRIMARY KEY (id)
    )  ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci COMMENT = '平台对接保存参数';


CREATE INDEX idx_project_id ON mind_additional_node(project_id);
CREATE INDEX idx_name ON mind_additional_node(name);
CREATE INDEX idx_pos ON mind_additional_node(pos);
CREATE INDEX idx_parent_id ON mind_additional_node(parent_id);
CREATE INDEX idx_create_user ON mind_additional_node(create_user);
CREATE INDEX idx_update_user ON mind_additional_node(update_user);
CREATE INDEX idx_create_time ON mind_additional_node(create_time);
CREATE INDEX idx_update_time ON mind_additional_node(update_time);
CREATE UNIQUE INDEX uq_name_project_parent ON mind_additional_node(project_id,name,parent_id);

-- 测试计划增加排序字段
alter table test_plan
    ADD COLUMN `pos` BIGINT NOT NULL DEFAULT 0 COMMENT '自定义排序';

-- 修改计划配置表
ALTER TABLE test_plan_config DROP `test_planning`;

-- 移除规划配置表
DROP TABLE `test_plan_allocation`;

-- 修改计划报告详情表字段
UPDATE test_plan_report_summary SET bug_count = 0 WHERE bug_count IS NULL;
ALTER TABLE test_plan_report_summary MODIFY `bug_count` BIGINT NOT NULL  DEFAULT 0 COMMENT '缺陷数量';
ALTER TABLE test_plan_report_summary DROP `report_count`;
ALTER TABLE test_plan_report_summary ADD `plan_count` BIGINT NOT NULL  DEFAULT 0 COMMENT '计划数量';
ALTER TABLE test_plan_report_summary ADD `functional_execute_result` BLOB COMMENT '功能用例执行结果';
ALTER TABLE test_plan_report_summary ADD `api_execute_result` BLOB COMMENT '接口执行结果';
ALTER TABLE test_plan_report_summary ADD `scenario_execute_result` BLOB COMMENT '场景执行结果';
ALTER TABLE test_plan_report_summary ADD `execute_result` BLOB COMMENT '执行结果';

-- 修改计划报告功能用例表字段
UPDATE test_plan_report_function_case SET function_case_bug_count = 0 WHERE function_case_bug_count IS NULL;
ALTER TABLE test_plan_report_function_case MODIFY `function_case_bug_count` BIGINT NOT NULL  DEFAULT 0 COMMENT '功能用例关联缺陷数';

-- 修改计划报告缺陷表字段
UPDATE test_plan_report_bug SET bug_case_count = 0 WHERE bug_case_count IS NULL;
ALTER TABLE test_plan_report_bug MODIFY `bug_case_count` BIGINT NOT NULL  DEFAULT 0 COMMENT '缺陷用例数';

-- 修改测试计划关联接口表字段
ALTER TABLE test_plan_api_case DROP COLUMN num;
ALTER TABLE test_plan_api_case ADD COLUMN test_plan_collection_id VARCHAR(50) NOT NULL COMMENT '测试计划集id';
ALTER TABLE test_plan_api_case ADD COLUMN last_exec_time BIGINT COMMENT '最后执行时间';
CREATE INDEX idx_test_plan_collection_id ON test_plan_api_case(test_plan_collection_id);
CREATE INDEX idx_pos ON test_plan_api_case(pos);

-- 修改测试计划关联场景表字段
ALTER TABLE test_plan_api_scenario DROP COLUMN num;
ALTER TABLE test_plan_api_scenario ADD COLUMN test_plan_collection_id VARCHAR(50) NOT NULL DEFAULT 'NONE' COMMENT '测试计划集id';
ALTER TABLE test_plan_api_scenario ADD COLUMN grouped BIT DEFAULT 0 COMMENT '是否为环境组';
ALTER TABLE test_plan_api_scenario MODIFY COLUMN `environment_id` VARCHAR(50) COMMENT '所属环境或环境组id';
ALTER TABLE test_plan_api_scenario ADD COLUMN last_exec_time BIGINT COMMENT '最后执行时间';
CREATE INDEX idx_test_plan_collection_id ON test_plan_api_scenario(test_plan_collection_id);
CREATE INDEX idx_pos ON test_plan_api_scenario(pos);

-- 修改测试计划关联功能用例表字段
ALTER TABLE test_plan_functional_case ADD COLUMN test_plan_collection_id VARCHAR(50) NOT NULL DEFAULT 'NONE' COMMENT '测试计划集id';
CREATE INDEX idx_test_plan_collection_id ON test_plan_functional_case(test_plan_collection_id);

-- 测试集
CREATE TABLE IF NOT EXISTS test_plan_collection(
    `id` VARCHAR(50) NOT NULL   COMMENT 'ID' ,
    `test_plan_id` VARCHAR(50) NOT NULL   COMMENT '测试计划ID' ,
    `parent_id` VARCHAR(50) NOT NULL  DEFAULT 'NONE' COMMENT '父级ID' ,
    `name` VARCHAR(255) NOT NULL   COMMENT '测试集名称' ,
    `type` VARCHAR(255) NOT NULL   COMMENT '测试集类型(功能/接口/场景)' ,
    `execute_method` VARCHAR(50) NOT NULL  DEFAULT 'SERIAL' COMMENT '执行方式(串行/并行)' ,
    `extended` BIT NOT NULL  DEFAULT 1 COMMENT '是否继承' ,
    `grouped` BIT NOT NULL  DEFAULT 0 COMMENT '是否使用环境组' ,
    `environment_id` VARCHAR(50) NOT NULL  DEFAULT 'NONE' COMMENT '环境ID/环境组ID' ,
    `test_resource_pool_id` VARCHAR(50) NOT NULL   COMMENT '测试资源池ID' ,
    `retry_on_fail` BIT NOT NULL  DEFAULT 0 COMMENT '是否失败重试' ,
    `retry_type` VARCHAR(50) NOT NULL  DEFAULT 'STEP' COMMENT '失败重试类型(步骤/场景)' ,
    `retry_times` INT NOT NULL  DEFAULT 1 COMMENT '失败重试次数' ,
    `retry_interval` INT NOT NULL  DEFAULT 1000 COMMENT '失败重试间隔(单位: ms)' ,
    `stop_on_fail` BIT NOT NULL  DEFAULT 0 COMMENT '是否失败停止' ,
    `create_user` VARCHAR(50) NOT NULL   COMMENT '创建人' ,
    `create_time` BIGINT NOT NULL   COMMENT '创建时间' ,
    `pos` BIGINT NOT NULL   COMMENT '自定义排序，间隔为4096' ,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '测试集';
CREATE INDEX idx_test_plan_id_and_type ON test_plan_collection(test_plan_id,type);
CREATE INDEX idx_parent_id ON test_plan_collection(parent_id);


ALTER TABLE api_report ADD COLUMN exec_status VARCHAR(20) NOT NULL  DEFAULT 'PENDING' COMMENT '执行状态';
ALTER TABLE api_report ALTER COLUMN status set DEFAULT '-';
CREATE INDEX idx_exec_status ON api_report(exec_status);

ALTER TABLE api_scenario_report ADD COLUMN exec_status VARCHAR(20) NOT NULL  DEFAULT 'PENDING' COMMENT '执行状态';
ALTER TABLE api_scenario_report ALTER COLUMN status set DEFAULT '-';
CREATE INDEX idx_exec_status ON api_scenario_report(exec_status);

-- 测试计划队列表
CREATE TABLE IF NOT EXISTS test_plan_execution_queue
(
    `id`                VARCHAR(50) NOT NULL COMMENT 'ID',
    `execute_queue_id`  VARCHAR(50) NOT NULL COMMENT '执行队列唯一ID',
    `test_plan_id`      VARCHAR(50) NOT NULL COMMENT '测试计划id',
    `pos`               BIGINT      NOT NULL COMMENT '排序',
    `prepare_report_id` VARCHAR(50) NOT NULL COMMENT '预生成报告ID',
    `run_mode`          VARCHAR(10) NOT NULL COMMENT '运行模式(SERIAL/PARALLEL)',
    `create_user`       VARCHAR(50) NOT NULL COMMENT '操作人',
    `create_time`       BIGINT      NOT NULL COMMENT '操作时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '测试计划执行队列';

ALTER TABLE api_report CHANGE COLUMN `test_plan_id` `test_plan_case_id` VARCHAR(50) NOT NULL DEFAULT 'NONE' COMMENT '测试计划关联用例表ID';
ALTER TABLE api_report DROP INDEX idx_test_plan_id;
CREATE INDEX idx_test_plan_case_id ON api_report(test_plan_case_id);
ALTER TABLE api_report ADD COLUMN `plan` BIT(1) NOT NULL  DEFAULT 0 COMMENT '是否是测试计划整体执行';
CREATE INDEX idx_plan ON api_report(`plan`);
ALTER TABLE api_scenario_report CHANGE COLUMN `test_plan_id` `test_plan_scenario_id` VARCHAR(50) NOT NULL DEFAULT 'NONE' COMMENT '测试计划关联场景表ID';
ALTER TABLE api_scenario_report DROP INDEX idx_test_plan_id;
CREATE INDEX idx_test_plan_scenario_id ON api_scenario_report(test_plan_scenario_id);
ALTER TABLE api_scenario_report ADD COLUMN `plan` BIT(1) NOT NULL  DEFAULT 0 COMMENT '是否是测试计划整体执行';
CREATE INDEX idx_plan ON api_scenario_report(`plan`);

-- 测试计划配置 增加运行模式
ALTER table test_plan_config
    ADD COLUMN `case_run_mode` VARCHAR(50) NOT NULL DEFAULT 'PARALLEL' COMMENT '不同用例之间的执行方式(串行/并行)';
-- 修改默认资源池id
UPDATE project_test_resource_pool set test_resource_pool_id = '100001100001' WHERE test_resource_pool_id IN (SELECT id FROM test_resource_pool where `name`= '默认资源池');
UPDATE project_application set type_value = '100001100001' WHERE type_value IN  (SELECT id FROM test_resource_pool where `name`= '默认资源池');

--  默认资源池固定ID
update test_resource_pool set id ='100001100001' where `name`= '默认资源池';

-- set innodb lock wait timeout to default
SET SESSION innodb_lock_wait_timeout = DEFAULT;


