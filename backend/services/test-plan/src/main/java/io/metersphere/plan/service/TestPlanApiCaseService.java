package io.metersphere.plan.service;

import io.metersphere.api.domain.ApiTestCase;
import io.metersphere.api.domain.ApiTestCaseExample;
import io.metersphere.api.dto.definition.ApiDefinitionDTO;
import io.metersphere.api.dto.definition.ApiTestCaseDTO;
import io.metersphere.api.mapper.ApiTestCaseMapper;
import io.metersphere.api.service.definition.ApiDefinitionModuleService;
import io.metersphere.api.service.definition.ApiDefinitionService;
import io.metersphere.api.service.definition.ApiTestCaseService;
import io.metersphere.functional.dto.FunctionalCaseModuleCountDTO;
import io.metersphere.functional.dto.ProjectOptionDTO;
import io.metersphere.plan.constants.AssociateCaseType;
import io.metersphere.plan.constants.TreeTypeEnums;
import io.metersphere.plan.domain.TestPlanApiCase;
import io.metersphere.plan.domain.TestPlanApiCaseExample;
import io.metersphere.plan.domain.TestPlanCollection;
import io.metersphere.plan.domain.TestPlanCollectionExample;
import io.metersphere.plan.dto.ApiCaseModuleDTO;
import io.metersphere.plan.dto.TestPlanCaseRunResultCount;
import io.metersphere.plan.dto.TestPlanCollectionDTO;
import io.metersphere.plan.dto.TestPlanResourceAssociationParam;
import io.metersphere.plan.dto.request.*;
import io.metersphere.plan.dto.response.TestPlanApiCasePageResponse;
import io.metersphere.plan.dto.response.TestPlanAssociationResponse;
import io.metersphere.plan.mapper.ExtTestPlanApiCaseMapper;
import io.metersphere.plan.mapper.TestPlanApiCaseMapper;
import io.metersphere.plan.mapper.TestPlanCollectionMapper;
import io.metersphere.project.domain.Project;
import io.metersphere.project.domain.ProjectExample;
import io.metersphere.project.dto.ModuleCountDTO;
import io.metersphere.project.mapper.ProjectMapper;
import io.metersphere.sdk.constants.CaseType;
import io.metersphere.sdk.constants.ModuleConstants;
import io.metersphere.sdk.constants.TestPlanResourceConstants;
import io.metersphere.sdk.domain.Environment;
import io.metersphere.sdk.domain.EnvironmentExample;
import io.metersphere.sdk.mapper.EnvironmentMapper;
import io.metersphere.sdk.util.BeanUtils;
import io.metersphere.sdk.util.Translator;
import io.metersphere.system.dto.LogInsertModule;
import io.metersphere.system.dto.sdk.BaseTreeNode;
import io.metersphere.system.service.UserLoginService;
import io.metersphere.system.uid.IDGenerator;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class TestPlanApiCaseService extends TestPlanResourceService {
    @Resource
    private TestPlanApiCaseMapper testPlanApiCaseMapper;
    @Resource
    private ExtTestPlanApiCaseMapper extTestPlanApiCaseMapper;
    @Resource
    private ApiDefinitionService apiDefinitionService;
    @Resource
    private ApiTestCaseService apiTestCaseService;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private EnvironmentMapper environmentMapper;
    @Resource
    private UserLoginService userLoginService;
    @Resource
    private ApiDefinitionModuleService apiDefinitionModuleService;
    private static final String CASE_MODULE_COUNT_ALL = "all";
    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private TestPlanCollectionMapper testPlanCollectionMapper;
    @Resource
    private ApiTestCaseMapper apiTestCaseMapper;

    @Override
    public void deleteBatchByTestPlanId(List<String> testPlanIdList) {
        TestPlanApiCaseExample example = new TestPlanApiCaseExample();
        example.createCriteria().andTestPlanIdIn(testPlanIdList);
        testPlanApiCaseMapper.deleteByExample(example);
    }

    @Override
    public long getNextOrder(String collectionId) {
        Long maxPos = extTestPlanApiCaseMapper.getMaxPosByCollectionId(collectionId);
        if (maxPos == null) {
            return 0;
        } else {
            return maxPos + DEFAULT_NODE_INTERVAL_POS;
        }
    }

    @Override
    public void updatePos(String id, long pos) {
        //        todo
        //        extTestPlanApiCaseMapper.updatePos(id, pos);
    }

    @Override
    public Map<String, Long> caseExecResultCount(String testPlanId) {
        List<TestPlanCaseRunResultCount> runResultCounts = extTestPlanApiCaseMapper.selectCaseExecResultCount(testPlanId);
        return runResultCounts.stream().collect(Collectors.toMap(TestPlanCaseRunResultCount::getResult, TestPlanCaseRunResultCount::getResultCount));
    }

    @Override
    public long copyResource(String originalTestPlanId, String newTestPlanId, String operator, long operatorTime) {
        List<TestPlanApiCase> copyList = new ArrayList<>();
        extTestPlanApiCaseMapper.selectByTestPlanIdAndNotDeleted(originalTestPlanId).forEach(originalCase -> {
            TestPlanApiCase newCase = new TestPlanApiCase();
            BeanUtils.copyBean(newCase, originalCase);
            newCase.setId(IDGenerator.nextStr());
            newCase.setTestPlanId(newTestPlanId);
            newCase.setCreateTime(operatorTime);
            newCase.setCreateUser(operator);
            newCase.setLastExecTime(0L);
            newCase.setLastExecResult(null);
            newCase.setLastExecReportId(null);
            copyList.add(newCase);
        });

        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        TestPlanApiCaseMapper batchInsertMapper = sqlSession.getMapper(TestPlanApiCaseMapper.class);
        copyList.forEach(item -> batchInsertMapper.insert(item));
        sqlSession.flushStatements();
        SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionFactory);
        return copyList.size();
    }

    @Override
    public void refreshPos(String testPlanId) {
        //        todo
        //        List<String> caseIdList = extTestPlanApiCaseMapper.selectIdByTestPlanIdOrderByPos(testPlanId);
        //        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        //        ExtTestPlanApiCaseMapper batchUpdateMapper = sqlSession.getMapper(ExtTestPlanApiCaseMapper.class);
        //        for (int i = 0; i < caseIdList.size(); i++) {
        //            batchUpdateMapper.updatePos(caseIdList.get(i), i * DEFAULT_NODE_INTERVAL_POS);
        //        }
        //        sqlSession.flushStatements();
        //        SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionFactory);
    }

    /**
     * 获取接口列表
     *
     * @param request
     * @param isRepeat
     * @return
     */
    public List<ApiDefinitionDTO> getApiPage(TestPlanApiRequest request, boolean isRepeat) {
        List<ApiDefinitionDTO> list = extTestPlanApiCaseMapper.list(request, isRepeat);
        apiDefinitionService.processApiDefinitions(list);
        return list;
    }


    /**
     * 获取接口用例列表
     *
     * @param request
     * @param isRepeat
     * @return
     */
    public List<ApiTestCaseDTO> getApiCasePage(TestPlanApiCaseRequest request, boolean isRepeat) {
        List<ApiTestCaseDTO> apiCaseLists = apiTestCaseService.page(request, isRepeat, false);
        return apiCaseLists;
    }


    /**
     * 获取已关联的接口用例列表
     *
     * @param request
     * @param deleted
     * @return
     */
    public List<TestPlanApiCasePageResponse> hasRelateApiCaseList(TestPlanApiCaseRequest request, boolean deleted) {
        List<TestPlanApiCasePageResponse> list = extTestPlanApiCaseMapper.relateApiCaseList(request, deleted);
        buildApiCaseResponse(list);
        return list;
    }

    private void buildApiCaseResponse(List<TestPlanApiCasePageResponse> apiCaseList) {
        if (CollectionUtils.isNotEmpty(apiCaseList)) {
            Map<String, String> projectMap = getProject(apiCaseList);
            Map<String, String> envMap = getEnvironmentMap(apiCaseList);
            Map<String, String> userMap = getUserMap(apiCaseList);
            apiCaseList.forEach(apiCase -> {
                apiCase.setProjectName(projectMap.get(apiCase.getProjectId()));
                apiCase.setEnvironmentName(envMap.get(apiCase.getId()));
                apiCase.setCreateUserName(userMap.get(apiCase.getCreateUser()));
            });
        }
    }

    private Map<String, String> getUserMap(List<TestPlanApiCasePageResponse> apiCaseList) {
        List<String> userIds = new ArrayList<>();
        userIds.addAll(apiCaseList.stream().map(TestPlanApiCasePageResponse::getCreateUser).toList());
        userIds.addAll(apiCaseList.stream().map(TestPlanApiCasePageResponse::getExecuteUser).toList());
        return userLoginService.getUserNameMap(userIds.stream().filter(StringUtils::isNotBlank).distinct().toList());
    }

    private Map<String, String> getEnvironmentMap(List<TestPlanApiCasePageResponse> apiCaseList) {
        Map<String, String> envMap = new HashMap<>();
        //默认环境
        List<TestPlanApiCasePageResponse> defaultEnv = apiCaseList.stream().filter(item -> StringUtils.equalsIgnoreCase(ModuleConstants.ROOT_NODE_PARENT_ID, item.getCollectEnvironmentId())).toList();
        if (CollectionUtils.isNotEmpty(defaultEnv)) {
            List<String> defaultEnvIds = defaultEnv.stream().map(TestPlanApiCasePageResponse::getEnvironmentId).distinct().toList();
            Map<String, TestPlanApiCasePageResponse> defaultEnvMap = defaultEnv.stream().collect(Collectors.toMap(TestPlanApiCasePageResponse::getEnvironmentId, testPlanApiCasePageResponse -> testPlanApiCasePageResponse));
            EnvironmentExample environmentExample = new EnvironmentExample();
            environmentExample.createCriteria().andIdIn(defaultEnvIds);
            List<Environment> environments = environmentMapper.selectByExample(environmentExample);
            environments.forEach(item -> {
                TestPlanApiCasePageResponse testPlanApiCasePageResponse = defaultEnvMap.get(item.getId());
                envMap.put(testPlanApiCasePageResponse.getId(), item.getName());
            });
        }
        //非默认环境
        List<TestPlanApiCasePageResponse> collectEnv = apiCaseList.stream().filter(item -> !StringUtils.equalsIgnoreCase(ModuleConstants.ROOT_NODE_PARENT_ID, item.getCollectEnvironmentId())).toList();
        if (CollectionUtils.isNotEmpty(collectEnv)) {
            List<String> collectEnvIds = collectEnv.stream().map(TestPlanApiCasePageResponse::getCollectEnvironmentId).distinct().toList();
            Map<String, List<TestPlanApiCasePageResponse>> collectEnvMap = collectEnv.stream().collect(Collectors.groupingBy(TestPlanApiCasePageResponse::getCollectEnvironmentId));
            EnvironmentExample environmentExample = new EnvironmentExample();
            environmentExample.createCriteria().andIdIn(collectEnvIds);
            List<Environment> environments = environmentMapper.selectByExample(environmentExample);
            environments.forEach(item -> {
                List<TestPlanApiCasePageResponse> list = collectEnvMap.get(item.getId());
                list.forEach(response -> {
                    envMap.put(response.getId(), item.getName());
                });
            });
        }
        return envMap;
    }

    private Map<String, String> getProject(List<TestPlanApiCasePageResponse> apiCaseList) {
        List<String> projectIds = apiCaseList.stream().map(TestPlanApiCasePageResponse::getProjectId).toList();
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andIdIn(projectIds);
        List<Project> projectList = projectMapper.selectByExample(projectExample);
        return projectList.stream().collect(Collectors.toMap(Project::getId, Project::getName));
    }


    public Map<String, Long> moduleCount(TestPlanApiCaseModuleRequest request) {
        switch (request.getTreeType()) {
            case TreeTypeEnums.MODULE:
                return getModuleCount(request);
            case TreeTypeEnums.COLLECTION:
                return getCollectionCount(request);
            default:
                return new HashMap<>();
        }
    }

    /**
     * 已关联接口用例规划视图统计
     *
     * @param request
     * @return
     */
    private Map<String, Long> getCollectionCount(TestPlanApiCaseModuleRequest request) {
        Map<String, Long> projectModuleCountMap = new HashMap<>();
        List<ModuleCountDTO> list = extTestPlanApiCaseMapper.collectionCountByRequest(request.getTestPlanId());
        list.forEach(item -> {
            projectModuleCountMap.put(item.getModuleId(), (long) item.getDataCount());
        });
        long allCount = extTestPlanApiCaseMapper.caseCount(request, false);
        projectModuleCountMap.put(CASE_MODULE_COUNT_ALL, allCount);
        return projectModuleCountMap;
    }

    /**
     * 已关联接口用例模块树统计
     *
     * @param request
     * @return
     */
    private Map<String, Long> getModuleCount(TestPlanApiCaseModuleRequest request) {
        request.setModuleIds(null);
        List<FunctionalCaseModuleCountDTO> projectModuleCountDTOList = extTestPlanApiCaseMapper.countModuleIdByRequest(request, false);
        Map<String, List<FunctionalCaseModuleCountDTO>> projectCountMap = projectModuleCountDTOList.stream().collect(Collectors.groupingBy(FunctionalCaseModuleCountDTO::getProjectId));
        Map<String, Long> projectModuleCountMap = new HashMap<>();
        projectCountMap.forEach((projectId, moduleCountDTOList) -> {
            List<ModuleCountDTO> moduleCountDTOS = new ArrayList<>();
            for (FunctionalCaseModuleCountDTO functionalCaseModuleCountDTO : moduleCountDTOList) {
                ModuleCountDTO moduleCountDTO = new ModuleCountDTO();
                BeanUtils.copyBean(moduleCountDTO, functionalCaseModuleCountDTO);
                moduleCountDTOS.add(moduleCountDTO);
            }
            int sum = moduleCountDTOList.stream().mapToInt(FunctionalCaseModuleCountDTO::getDataCount).sum();
            Map<String, Long> moduleCountMap = getModuleCountMap(projectId, request.getTestPlanId(), moduleCountDTOS);
            moduleCountMap.forEach((k, v) -> {
                if (projectModuleCountMap.get(k) == null || projectModuleCountMap.get(k) == 0L) {
                    projectModuleCountMap.put(k, v);
                }
            });
            projectModuleCountMap.put(projectId, (long) sum);
        });
        //查出全部用例数量
        long allCount = extTestPlanApiCaseMapper.caseCount(request, false);
        projectModuleCountMap.put(CASE_MODULE_COUNT_ALL, allCount);
        return projectModuleCountMap;
    }

    public Map<String, Long> getModuleCountMap(String projectId, String testPlanId, List<ModuleCountDTO> moduleCountDTOList) {
        //构建模块树，并计算每个节点下的所有数量（包含子节点）
        List<BaseTreeNode> treeNodeList = this.getTreeOnlyIdsAndResourceCount(projectId, testPlanId, moduleCountDTOList);
        //通过广度遍历的方式构建返回值
        return apiDefinitionModuleService.getIdCountMapByBreadth(treeNodeList);
    }


    public List<BaseTreeNode> getTreeOnlyIdsAndResourceCount(String projectId, String testPlanId, List<ModuleCountDTO> moduleCountDTOList) {
        //节点内容只有Id和parentId
        List<String> moduleIds = extTestPlanApiCaseMapper.selectIdByProjectIdAndTestPlanId(projectId, testPlanId);
        List<BaseTreeNode> nodeByNodeIds = apiDefinitionModuleService.getNodeByNodeIds(moduleIds);
        return apiDefinitionModuleService.buildTreeAndCountResource(nodeByNodeIds, moduleCountDTOList, true, Translator.get("functional_case.module.default.name"));
    }

    /**
     * 已关联接口用例模块树
     *
     * @param request
     * @return
     */
    public List<BaseTreeNode> getTree(TestPlanApiCaseTreeRequest request) {
        switch (request.getTreeType()) {
            case TreeTypeEnums.MODULE:
                return getModuleTree(request.getTestPlanId());
            case TreeTypeEnums.COLLECTION:
                return getCollectionTree(request.getTestPlanId());
            default:
                return new ArrayList<>();
        }
    }

    /**
     * 已关联接口用例规划视图树
     *
     * @param testPlanId
     * @return
     */
    private List<BaseTreeNode> getCollectionTree(String testPlanId) {
        List<BaseTreeNode> returnList = new ArrayList<>();
        TestPlanCollectionExample collectionExample = new TestPlanCollectionExample();
        collectionExample.createCriteria().andTypeEqualTo(CaseType.API_CASE.getKey()).andParentIdNotEqualTo(ModuleConstants.ROOT_NODE_PARENT_ID).andTestPlanIdEqualTo(testPlanId);
        List<TestPlanCollection> testPlanCollections = testPlanCollectionMapper.selectByExample(collectionExample);
        testPlanCollections.forEach(item -> {
            BaseTreeNode baseTreeNode = new BaseTreeNode(item.getId(), item.getName(), CaseType.API_CASE.getKey());
            returnList.add(baseTreeNode);
        });
        return returnList;
    }

    /**
     * 模块树
     *
     * @param testPlanId
     * @return
     */
    private List<BaseTreeNode> getModuleTree(String testPlanId) {
        List<BaseTreeNode> returnList = new ArrayList<>();
        List<ProjectOptionDTO> rootIds = extTestPlanApiCaseMapper.selectRootIdByTestPlanId(testPlanId);
        Map<String, List<ProjectOptionDTO>> projectRootMap = rootIds.stream().collect(Collectors.groupingBy(ProjectOptionDTO::getName));
        List<ApiCaseModuleDTO> apiCaseModuleIds = extTestPlanApiCaseMapper.selectBaseByProjectIdAndTestPlanId(testPlanId);
        Map<String, List<ApiCaseModuleDTO>> projectModuleMap = apiCaseModuleIds.stream().collect(Collectors.groupingBy(ApiCaseModuleDTO::getProjectId));
        if (MapUtils.isEmpty(projectModuleMap)) {
            projectRootMap.forEach((projectId, projectOptionDTOList) -> {
                BaseTreeNode projectNode = new BaseTreeNode(projectId, projectOptionDTOList.get(0).getProjectName(), Project.class.getName());
                returnList.add(projectNode);
                BaseTreeNode defaultNode = apiDefinitionModuleService.getDefaultModule(Translator.get("functional_case.module.default.name"));
                projectNode.addChild(defaultNode);
            });
            return returnList;
        }
        projectModuleMap.forEach((projectId, moduleList) -> {
            BaseTreeNode projectNode = new BaseTreeNode(projectId, moduleList.get(0).getProjectName(), Project.class.getName());
            returnList.add(projectNode);
            List<String> projectModuleIds = moduleList.stream().map(ApiCaseModuleDTO::getId).toList();
            List<BaseTreeNode> nodeByNodeIds = apiDefinitionModuleService.getNodeByNodeIds(projectModuleIds);
            boolean haveVirtualRootNode = CollectionUtils.isEmpty(projectRootMap.get(projectId));
            List<BaseTreeNode> baseTreeNodes = apiDefinitionModuleService.buildTreeAndCountResource(nodeByNodeIds, !haveVirtualRootNode, Translator.get("functional_case.module.default.name"));
            for (BaseTreeNode baseTreeNode : baseTreeNodes) {
                projectNode.addChild(baseTreeNode);
            }
        });
        return returnList;
    }


    /**
     * 取消关联
     *
     * @param request
     * @param logInsertModule
     * @return
     */
    public TestPlanAssociationResponse disassociate(TestPlanApiCaseBatchRequest request, LogInsertModule logInsertModule) {
        List<String> selectIds = doSelectIds(request);
        return super.disassociate(
                TestPlanResourceConstants.RESOURCE_API_CASE,
                request,
                logInsertModule,
                selectIds,
                this::deleteTestPlanResource);
    }


    public void deleteTestPlanResource(@Validated TestPlanResourceAssociationParam associationParam) {
        TestPlanApiCaseExample testPlanApiCaseExample = new TestPlanApiCaseExample();
        testPlanApiCaseExample.createCriteria().andIdIn(associationParam.getResourceIdList());
        testPlanApiCaseMapper.deleteByExample(testPlanApiCaseExample);
    }


    public List<String> doSelectIds(TestPlanApiCaseBatchRequest request) {
        if (request.isSelectAll()) {
            List<String> ids = extTestPlanApiCaseMapper.getIds(request, false);
            if (CollectionUtils.isNotEmpty(request.getExcludeIds())) {
                ids.removeAll(request.getExcludeIds());
            }
            return ids;
        } else {
            return request.getSelectIds();
        }
    }


    /**
     * 批量更新执行人
     *
     * @param request
     */
    public void batchUpdateExecutor(TestPlanApiCaseUpdateRequest request) {
        List<String> ids = doSelectIds(request);
        if (CollectionUtils.isNotEmpty(ids)) {
            extTestPlanApiCaseMapper.batchUpdateExecutor(ids, request.getUserId());
        }
    }

    @Override
    public void associateCollection(String planId, Map<String, List<BaseCollectionAssociateRequest>> collectionAssociates, String userId) {
        List<TestPlanApiCase> testPlanApiCaseList = new ArrayList<>();
        //处理数据
        handleApiData(collectionAssociates.get(AssociateCaseType.API), userId, testPlanApiCaseList, planId);
        handleApiCaseData(collectionAssociates.get(AssociateCaseType.API_CASE), userId, testPlanApiCaseList, planId);
        testPlanApiCaseMapper.batchInsert(testPlanApiCaseList);
    }

    private void handleApiCaseData(List<BaseCollectionAssociateRequest> apiCaseList, String userId, List<TestPlanApiCase> testPlanApiCaseList, String planId) {
        if (CollectionUtils.isNotEmpty(apiCaseList)) {
            List<String> ids = apiCaseList.stream().flatMap(item -> item.getIds().stream()).toList();
            ApiTestCaseExample example = new ApiTestCaseExample();
            example.createCriteria().andIdIn(ids);
            List<ApiTestCase> apiTestCaseList = apiTestCaseMapper.selectByExample(example);
            apiCaseList.forEach(apiCase -> {
                List<String> apiCaseIds = apiCase.getIds();
                if (CollectionUtils.isNotEmpty(apiCaseIds)) {
                    List<ApiTestCase> apiTestCases = apiTestCaseList.stream().filter(item -> apiCaseIds.contains(item.getId())).collect(Collectors.toList());
                    buildTestPlanApiCase(planId, apiTestCases, apiCase.getCollectionId(), userId, testPlanApiCaseList);
                }
            });
        }
    }

    private void handleApiData(List<BaseCollectionAssociateRequest> apiCaseList, String userId, List<TestPlanApiCase> testPlanApiCaseList, String planId) {
        if (CollectionUtils.isNotEmpty(apiCaseList)) {
            List<String> ids = apiCaseList.stream().flatMap(item -> item.getIds().stream()).toList();
            ApiTestCaseExample example = new ApiTestCaseExample();
            example.createCriteria().andApiDefinitionIdIn(ids);
            List<ApiTestCase> apiTestCaseList = apiTestCaseMapper.selectByExample(example);
            apiCaseList.forEach(apiCase -> {
                List<String> apiCaseIds = apiCase.getIds();
                if(CollectionUtils.isNotEmpty(apiCaseIds)){
                    List<ApiTestCase> apiTestCases = apiTestCaseList.stream().filter(item -> apiCaseIds.contains(item.getApiDefinitionId())).collect(Collectors.toList());
                    buildTestPlanApiCase(planId, apiTestCases, apiCase.getCollectionId(), userId, testPlanApiCaseList);
                }
            });
        }
    }


    /**
     * 构建测试计划接口用例对象
     *
     * @param planId
     * @param apiTestCases
     * @param collectionId
     * @param userId
     * @param testPlanApiCaseList
     */
    private void buildTestPlanApiCase(String planId, List<ApiTestCase> apiTestCases, String collectionId, String userId, List<TestPlanApiCase> testPlanApiCaseList) {
        apiTestCases.forEach(apiTestCase -> {
            TestPlanApiCase testPlanApiCase = new TestPlanApiCase();
            testPlanApiCase.setId(IDGenerator.nextStr());
            testPlanApiCase.setTestPlanCollectionId(collectionId);
            testPlanApiCase.setTestPlanId(planId);
            testPlanApiCase.setApiCaseId(apiTestCase.getId());
            testPlanApiCase.setEnvironmentId(apiTestCase.getEnvironmentId());
            testPlanApiCase.setCreateTime(System.currentTimeMillis());
            testPlanApiCase.setCreateUser(userId);
            testPlanApiCase.setPos(getNextOrder(collectionId));
            testPlanApiCaseList.add(testPlanApiCase);
        });
    }

    @Override
    public void initResourceDefaultCollection(String planId, List<TestPlanCollectionDTO> defaultCollections) {
        TestPlanCollectionDTO defaultCollection = defaultCollections.stream().filter(collection -> StringUtils.equals(collection.getType(), CaseType.API_CASE.getKey())
                && !StringUtils.equals(collection.getParentId(), "NONE")).toList().get(0);
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        TestPlanApiCaseMapper apiBatchMapper = sqlSession.getMapper(TestPlanApiCaseMapper.class);
        TestPlanApiCase record = new TestPlanApiCase();
        record.setTestPlanCollectionId(defaultCollection.getId());
        TestPlanApiCaseExample apiCaseExample = new TestPlanApiCaseExample();
        apiCaseExample.createCriteria().andTestPlanIdEqualTo(planId);
        apiBatchMapper.updateByExampleSelective(record, apiCaseExample);
    }
}
