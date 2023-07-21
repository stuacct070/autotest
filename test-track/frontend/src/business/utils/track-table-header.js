import {CUSTOM_TABLE_HEADER} from "@/business/utils/sdk-utils";

//测试计划-功能用例
const TRACK_HEADER = {
  TEST_PLAN_FUNCTION_TEST_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'name', key: '2', label: 'commons.name'},
    {id: 'priority', key: 'd', label: 'test_track.case.priority'},
    {id: 'versionId', key: 'b', label: 'project.version.name', xpack: true},
    {id: 'tags', key: '3', label: 'commons.tag'},
    {id: 'nodePath', key: '4', label: 'test_track.case.module'},
    {id: 'projectName', key: '5', label: 'test_track.review.review_project'},
    {id: 'issuesContent', key: '6', label: 'test_track.issue.issue'},
    {id: 'executor', key: '7', label: 'test_track.plan_view.executor'},
    {id: 'maintainerName', key: 'c', label: 'test_track.plan.plan_principal'},
    {id: 'status', key: '8', label: 'test_track.plan_view.execute_result'},
    {id: 'updateTime', key: '9', label: 'commons.update_time'},
    {id: 'createTime', key: 'a', label: 'commons.create_time'},
  ],
  //测试计划
  TEST_PLAN_LIST: [
    {id: 'name', key: '1', label: 'commons.name'},
    {id: 'status', key: '3', label: 'test_track.plan.plan_status'},
    {id: 'stage', key: '4', label: 'test_track.plan.plan_stage'},
    {id: 'testRate', key: '5', label: 'test_track.home.test_rate'},
    {id: 'projectName', key: '6', label: 'test_track.plan.plan_project'},
    {id: 'plannedStartTime', key: '7', label: 'test_track.plan.planned_start_time'},
    {id: 'plannedEndTime', key: '8', label: 'test_track.plan.planned_end_time'},
    {id: 'actualStartTime', key: '9', label: 'test_track.plan.actual_start_time'},
    {id: 'actualEndTime', key: 'a', label: 'test_track.plan.actual_end_time'},
    {id: 'tags', key: 'b', label: 'commons.tag'},
    {id: 'scheduleStatus', key: 'c', label: 'commons.trigger_mode.schedule'},
    {id: 'passRate', key: 'e', label: 'commons.pass_rate'},
    {id: 'createUser', key: 'f', label: 'commons.create_user'},
    {id: 'testPlanTestCaseCount', key: 'g', label: 'test_track.plan.test_plan_test_case_count'},
    {id: 'testPlanApiCaseCount', key: 'h', label: 'test_track.plan.test_plan_api_case_count'},
    {id: 'testPlanApiScenarioCount', key: 'i', label: 'test_track.plan.test_plan_api_scenario_count'},
    {id: 'testPlanUiScenarioCount', key: 'l', label: 'test_track.plan.test_plan_ui_scenario_count', xpack: true},
    {id: 'testPlanLoadCaseCount', key: 'j', label: 'test_track.plan.test_plan_load_case_count'},
    {id: 'principalName', key: 'k', label: 'test_track.plan.plan_principal'},
  ],
  //测试计划-api用例
  TEST_PLAN_API_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'name', key: '2', label: 'api_test.definition.api_name'},
    {id: 'versionId', key: 'd', label: 'commons.version'},
    {id: 'priority', key: '3', label: 'test_track.case.priority'},
    {id: 'path', key: '4', label: 'api_test.definition.api_path'},
    {id: 'createUser', key: '5', label: 'api_test.creator'},
    {id: 'tags', key: '7', label: 'commons.tag'},
    {id: 'execResult', key: '8', label: 'test_track.plan.execute_result'},
    {id: 'maintainer', key: '9', label: 'api_test.definition.request.responsible'},
    {id: 'updateTime', key: 'a', label: 'commons.update_time'},
    {id: 'createTime', key: 'b', label: 'commons.create_time'},
    {id: 'environmentName', key: 'c', label: 'commons.environment'},
  ],
  //测试计划-性能用例
  TEST_PLAN_LOAD_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'caseName', key: '2', label: 'commons.name'},
    {id: 'versionId', key: '9', label: 'commons.version'},
    {id: 'projectName', key: '3', label: 'load_test.project_name'},
    {id: 'userName', key: '4', label: 'load_test.user_name'},
    {id: 'createTime', key: '5', label: 'commons.create_time'},
    // {id: 'status', key: '6', label: 'commons.status'},
    {id: 'caseStatus', key: '7', label: 'test_track.plan.load_case.execution_status'},
    {id: 'loadReportId', key: '8', label: 'test_track.plan.load_case.report'},
  ],
  //测试计划-场景用例
  TEST_PLAN_SCENARIO_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'name', key: '2', label: 'api_test.automation.scenario_name'},
    {id: 'versionId', key: 'd', label: 'commons.version'},
    {id: 'level', key: '3', label: 'api_test.automation.case_level'},
    {id: 'tagNames', key: '4', label: 'api_test.automation.tag'},
    {id: 'stepTotal', key: '7', label: 'api_test.automation.step'},
    {id: 'envs', key: '8', label: 'commons.environment'},
    {id: 'passRate', key: '9', label: 'api_test.automation.passing_rate'},
    {id: 'maintainer', key: 'a', label: 'api_test.definition.request.responsible'},
    {id: 'createUser', key: '5', label: 'api_test.automation.creator'},
    {id: 'updateTime', key: '6', label: 'commons.update_time'},
    {id: 'createTime', key: 'b', label: 'commons.create_time'},
    {id: 'lastResult', key: 'c', label: 'api_test.automation.last_result'},
  ],
  //测试计划-UI用例
  TEST_PLAN_UI_SCENARIO_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'name', key: '2', label: 'api_test.automation.scenario_name'},
    {id: 'versionId', key: 'd', label: 'commons.version'},
    {id: 'level', key: '3', label: 'api_test.automation.case_level'},
    {id: 'envs', key: '8', label: 'commons.environment'},
    {id: 'tagNames', key: '4', label: 'api_test.automation.tag'},
    {id: 'stepTotal', key: '7', label: 'api_test.automation.step'},
    {id: 'passRate', key: '9', label: 'api_test.automation.passing_rate'},
    {id: 'maintainer', key: 'a', label: 'api_test.definition.request.responsible'},
    {id: 'createUser', key: '5', label: 'api_test.automation.creator'},
    {id: 'updateTime', key: '6', label: 'commons.update_time'},
    {id: 'createTime', key: 'b', label: 'commons.create_time'},
    {id: 'lastResult', key: 'c', label: 'api_test.automation.last_result'},
  ],
  //测试用例
  TRACK_TEST_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'name', key: '2', label: 'test_track.case.name'},
    {id: 'reviewStatus', key: '3', label: 'test_track.case.status'},
    {id: 'tags', key: '4', label: 'commons.tag'},
    {id: 'nodePath', key: '5', label: 'test_track.case.module'},
    {id: 'updateTime', key: '6', label: 'commons.update_time'},
    {id: 'createUser', key: '7', label: 'commons.create_user'},
    {id: 'createTime', key: '8', label: 'commons.create_time'},
    {id: 'desc', key: '9', label: 'test_track.case.case_desc'},
    {id: 'lastExecuteResult', key: '0', label: 'test_track.plan_view.execute_result'},
    {id: 'versionId', key: 'a', label: 'project.version.name', xpack: true},
  ],
  // 公共用例库
  TRACK_PUBLIC_TEST_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'name', key: '2', label: 'commons.name'},
    {id: 'reviewStatus', key: '3', label: 'test_track.case.status'},
    {id: 'tags', key: '4', label: 'commons.tag'},
    {id: 'versionId', key: 'b', label: 'project.version.name', xpack: true},
    {id: 'projectName', key: '5', label: 'test_track.case.project'},
    {id: 'updateTime', key: '6', label: 'commons.update_time'},
    {id: 'createUser', key: '7', label: 'commons.create_user'},
    {id: 'createTime', key: '8', label: 'commons.create_time'},
    {id: 'desc', key: '9', label: 'test_track.case.case_desc'},
    {id: 'lastExecuteResult', key: '0', label: 'test_track.plan_view.execute_result'},
  ],
  //缺陷列表
  ISSUE_LIST: [
    {id: 'num', key: '1', label: 'test_track.issue.id'},
    {id: 'title', key: '2', label: 'test_track.issue.title'},
    {id: 'platformStatus', key: '3', label: 'test_track.issue.platform_status', width: 120},
    {id: 'platform', key: '4', label: 'test_track.issue.issue_platform'},
    {id: 'creatorName', key: '5', label: 'custom_field.issue_creator'},
    {id: 'resourceName', key: '6', label: 'test_track.issue.issue_resource'},
    {id: 'description', key: '7', label: 'test_track.issue.description'},
    {id: 'caseCount', key: '9', label: 'api_test.definition.api_case_number'},
    {id: 'createTime', key: '8', label: 'commons.create_time'},
    {id: 'updateTime', key: '0', label: 'commons.update_time'}
  ],
  //用例评审
  TEST_CASE_REVIEW: [
    {id: 'name', key: '1', label: 'test_track.review.review_name'},
    {id: 'reviewer', key: '2', label: 'test_track.review.reviewer'},
    {id: 'projectName', key: '3', label: 'test_track.review.review_project'},
    {id: 'creatorName', key: '4', label: 'test_track.review.creator'},
    {id: 'status', key: '5', label: 'test_track.review.review_status'},
    {id: 'createTime', key: '6', label: 'commons.create_time'},
    {id: 'endTime', key: '7', label: 'test_track.review.end_time'},
    {id: 'tags', key: '8', label: 'commons.tag'},
    // {id: 'testRate', key: '9', label: 'review.review_rate'},
    {id: 'caseCount', key: 'a', label: 'api_test.definition.api_case_number'},
    {id: 'passRate', key: 'b', label: 'commons.pass_rate'},
  ],
  //用例评审-功能用例
  TEST_CASE_REVIEW_FUNCTION_TEST_CASE: [
    {id: 'num', key: '1', label: 'commons.id'},
    {id: 'name', key: '2', label: 'test_track.case.name'},
    {id: 'priority', key: '3', label: 'test_track.case.priority'},
    {id: 'tags', key: '4', label: 'commons.tag'},
    {id: 'nodePath', key: '5', label: 'test_track.case.module'},
    {id: 'projectName', key: '6', label: 'test_track.review.review_project'},
    {id: 'reviewerName', key: '7', label: 'test_track.review.reviewer'},
    {id: 'reviewStatus', key: '8', label: 'test_track.case.status'},
    {id: 'updateTime', key: '9', label: 'commons.update_time'},
    {id: 'maintainerName', key: 'a', label: 'custom_field.case_maintainer'},
    {id: 'versionId', key: 'b', label: 'commons.version'},
    {id: 'createTime', key: 'c', label: 'commons.create_time'},
  ],
}

Object.assign(CUSTOM_TABLE_HEADER, TRACK_HEADER);
