package io.metersphere.functional.controller;

import io.metersphere.functional.domain.*;
import io.metersphere.functional.dto.CaseCustomFieldDTO;
import io.metersphere.functional.dto.FunctionalCaseStepDTO;
import io.metersphere.functional.dto.FunctionalMinderTreeDTO;
import io.metersphere.functional.dto.MinderOptionDTO;
import io.metersphere.functional.mapper.FunctionalCaseBlobMapper;
import io.metersphere.functional.mapper.FunctionalCaseMapper;
import io.metersphere.functional.mapper.FunctionalCaseModuleMapper;
import io.metersphere.functional.mapper.MindAdditionalNodeMapper;
import io.metersphere.functional.request.*;
import io.metersphere.sdk.util.JSON;
import io.metersphere.sdk.util.Translator;
import io.metersphere.system.base.BaseTest;
import io.metersphere.system.controller.handler.ResultHolder;
import io.metersphere.system.dto.sdk.BaseTreeNode;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class FunctionalCaseMinderControllerTest extends BaseTest {

    //用例
    public static final String FUNCTIONAL_CASE_LIST_URL = "/functional/mind/case/list";

    public static final String FUNCTIONAL_CASE_EDIT_URL = "/functional/mind/case/edit";

    public static final String FUNCTIONAL_CASE_NODE_MODULE_URL = "/functional/mind/case/tree";



    //评审
    public static final String FUNCTIONAL_CASE_REVIEW_LIST_URL = "/functional/mind/case/review/list";

    //测试计划
    public static final String FUNCTIONAL_CASE_PLAN_LIST_URL = "/functional/mind/case/plan/list";


    @Resource
    private FunctionalCaseBlobMapper functionalCaseBlobMapper;
    @Resource
    private FunctionalCaseMapper functionalCaseMapper;
    @Resource
    private FunctionalCaseModuleMapper functionalCaseModuleMapper;
    @Resource
    private MindAdditionalNodeMapper mindAdditionalNodeMapper;

    @Test
    @Order(1)
    @Sql(scripts = {"/dml/init_file_minder_test.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void testGetPageList() throws Exception {
        FunctionalCaseMindRequest request = new FunctionalCaseMindRequest();
        request.setProjectId("project-case-minder-test");
        MvcResult mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_LIST_URL, request);
        String contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResultHolder resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        Assertions.assertNotNull(resultHolder);
        request = new FunctionalCaseMindRequest();
        request.setProjectId("project-case-minder-test");
        request.setModuleId("TEST_MINDER_MODULE_ID_GYQ");
        mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_LIST_URL, request);
        contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        Assertions.assertNotNull(resultHolder);
        FunctionalCaseBlob functionalCaseBlob = new FunctionalCaseBlob();
        functionalCaseBlob.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_2");
        functionalCaseBlob.setSteps(JSON.toJSONString(new ArrayList<>()).getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob.setTextDescription(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob.setExpectedResult(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob.setPrerequisite(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob.setDescription(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlobMapper.insert(functionalCaseBlob);
        FunctionalCaseBlob functionalCaseBlob6 = new FunctionalCaseBlob();
        functionalCaseBlob6.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_1");
        functionalCaseBlob6.setSteps(JSON.toJSONString(new ArrayList<>()).getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob6.setTextDescription(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob6.setExpectedResult(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob6.setPrerequisite(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob6.setDescription(StringUtils.EMPTY.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlobMapper.updateByPrimaryKeyWithBLOBs(functionalCaseBlob6);
        mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_LIST_URL, request);
        contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        Assertions.assertNotNull(resultHolder);
        List<FunctionalCaseStepDTO> list = new ArrayList<>();
        FunctionalCaseStepDTO functionalCaseStepDTO = new FunctionalCaseStepDTO();
        functionalCaseStepDTO.setId("12455");
        functionalCaseStepDTO.setNum(0);
        functionalCaseStepDTO.setDesc("");
        functionalCaseStepDTO.setResult("步骤一结果");
        list.add(functionalCaseStepDTO);
        functionalCaseStepDTO = new FunctionalCaseStepDTO();
        functionalCaseStepDTO.setId("12ddd455");
        functionalCaseStepDTO.setNum(1);
        functionalCaseStepDTO.setDesc("步骤二");
        functionalCaseStepDTO.setResult("");
        list.add(functionalCaseStepDTO);
        String textDescription = "hahahahah，这是文本描述";
        String expectedResult = "";
        String prerequisite = "前置条件";
        String description = "备注";

        functionalCaseBlob = new FunctionalCaseBlob();
        functionalCaseBlob.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_2");
        functionalCaseBlob.setSteps(JSON.toJSONString(list).getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob.setTextDescription(textDescription.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob.setExpectedResult(null);
        functionalCaseBlob.setPrerequisite(prerequisite.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob.setDescription(description.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlobMapper.updateByPrimaryKeyWithBLOBs(functionalCaseBlob);
        functionalCaseBlob6 = new FunctionalCaseBlob();
        functionalCaseBlob6.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_1");
        functionalCaseBlob6.setSteps(JSON.toJSONString(list).getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob6.setTextDescription(textDescription.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob6.setExpectedResult(null);
        functionalCaseBlob6.setPrerequisite(prerequisite.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlob6.setDescription(description.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlobMapper.updateByPrimaryKeyWithBLOBs(functionalCaseBlob6);
        mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_LIST_URL, request);
        contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        Assertions.assertNotNull(resultHolder);
        expectedResult = "文本描述的结果";
        functionalCaseBlob = new FunctionalCaseBlob();
        functionalCaseBlob.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_2");
        functionalCaseBlob.setExpectedResult(expectedResult.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlobMapper.updateByPrimaryKeyWithBLOBs(functionalCaseBlob);
        functionalCaseBlob6 = new FunctionalCaseBlob();
        functionalCaseBlob6.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_1");
        functionalCaseBlob6.setExpectedResult(expectedResult.getBytes(StandardCharsets.UTF_8));
        functionalCaseBlobMapper.updateByPrimaryKeyWithBLOBs(functionalCaseBlob6);


        mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_LIST_URL, request);
        contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        List<FunctionalMinderTreeDTO> baseTreeNodes = JSON.parseArray(JSON.toJSONString(resultHolder.getData()), FunctionalMinderTreeDTO.class);
        Assertions.assertNotNull(baseTreeNodes);
        Assertions.assertEquals(3, baseTreeNodes.size());
    }

    @Test
    @Order(2)
    public void testEditList() throws Exception {
        FunctionalCaseMinderEditRequest request = new FunctionalCaseMinderEditRequest();
        request.setProjectId("project-case-minder-test");
        request.setVersionId("ffff");
        List<FunctionalCaseChangeRequest> caseChangeRequests = new ArrayList<>();
        FunctionalCaseChangeRequest caseChangeRequest = new FunctionalCaseChangeRequest();
        caseChangeRequest.setId("12344");
        caseChangeRequest.setName("新增用例");
        caseChangeRequest.setModuleId("TEST_MINDER_MODULE_ID_GYQ2");
        caseChangeRequest.setMoveMode("AFTER");
        caseChangeRequest.setTargetId("TEST_FUNCTIONAL_MINDER_CASE_ID_3");
        caseChangeRequest.setTemplateId("100001");
        caseChangeRequest.setType("ADD");
        caseChangeRequest.setPrerequisite("前置条件");
        caseChangeRequest.setCaseEditType("TEXT");
        List<CaseCustomFieldDTO> customFields = new ArrayList<>();
        CaseCustomFieldDTO customFieldDTO = new CaseCustomFieldDTO();
        customFieldDTO.setFieldId("custom_field_minder_gyq_id_3");
        customFieldDTO.setValue("P0");
        customFields.add(customFieldDTO);
        caseChangeRequest.setCustomFields(customFields);
        caseChangeRequests.add(caseChangeRequest);
        caseChangeRequest = new FunctionalCaseChangeRequest();
        caseChangeRequest.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_1");
        caseChangeRequest.setName("TEST_MINDER_MODULE_ID_GYQ_更新");
        caseChangeRequest.setModuleId("TEST_MINDER_MODULE_ID_GYQ");
        caseChangeRequest.setTemplateId("100001");
        caseChangeRequest.setMoveMode("BEFORE");
        caseChangeRequest.setTargetId("TEST_FUNCTIONAL_MINDER_CASE_ID_2");
        caseChangeRequest.setType("UPDATE");
        caseChangeRequest.setPrerequisite("前置条件");
        caseChangeRequest.setCaseEditType("TEXT");
        customFields = new ArrayList<>();
        customFieldDTO = new CaseCustomFieldDTO();
        customFieldDTO.setFieldId("custom_field_minder_gyq_id_3");
        customFieldDTO.setValue("P3");
        customFields.add(customFieldDTO);
        caseChangeRequest.setCustomFields(customFields);
        caseChangeRequests.add(caseChangeRequest);
        request.setUpdateCaseList(caseChangeRequests);
        List<FunctionalCaseModuleEditRequest> functionalCaseModuleEditRequests = new ArrayList<>();
        FunctionalCaseModuleEditRequest functionalCaseModuleEditRequest = new FunctionalCaseModuleEditRequest();
        functionalCaseModuleEditRequest.setId("uuuId");
        functionalCaseModuleEditRequest.setType("ADD");
        functionalCaseModuleEditRequest.setMoveMode("AFTER");
        functionalCaseModuleEditRequest.setTargetId("TEST_MINDER_MODULE_ID_GYQ8");
        functionalCaseModuleEditRequest.setName("新增9");
        functionalCaseModuleEditRequest.setParentId("TEST_MINDER_MODULE_ID_GYQ");
        functionalCaseModuleEditRequests.add(functionalCaseModuleEditRequest);
        functionalCaseModuleEditRequest = new FunctionalCaseModuleEditRequest();
        functionalCaseModuleEditRequest.setId("TEST_MINDER_MODULE_ID_GYQ7");
        functionalCaseModuleEditRequest.setType("UPDATE");
        functionalCaseModuleEditRequest.setMoveMode("BEFORE");
        functionalCaseModuleEditRequest.setTargetId("TEST_MINDER_MODULE_ID_GYQ8");
        functionalCaseModuleEditRequest.setName("移动7");
        functionalCaseModuleEditRequest.setParentId("TEST_MINDER_MODULE_ID_GYQ");
        functionalCaseModuleEditRequests.add(functionalCaseModuleEditRequest);
        request.setUpdateModuleList(functionalCaseModuleEditRequests);
        List<MindAdditionalNodeRequest> additionalNodeList = new ArrayList<>();
        MindAdditionalNodeRequest mindAdditionalNodeRequest = new MindAdditionalNodeRequest();
        mindAdditionalNodeRequest.setId("weyyg");
        mindAdditionalNodeRequest.setType("ADD");
        mindAdditionalNodeRequest.setName("新增空白");
        mindAdditionalNodeRequest.setParentId("TEST_MINDER_MODULE_ID_GYQ");
        additionalNodeList.add(mindAdditionalNodeRequest);
        mindAdditionalNodeRequest = new MindAdditionalNodeRequest();
        mindAdditionalNodeRequest.setId("additional2");
        mindAdditionalNodeRequest.setType("UPDATE");
        mindAdditionalNodeRequest.setName("additional2");
        mindAdditionalNodeRequest.setParentId("TEST_MINDER_MODULE_ID_GYQ");
        additionalNodeList.add(mindAdditionalNodeRequest);
        request.setAdditionalNodeList(additionalNodeList);
        List<MinderOptionDTO> deleteResourceList = new ArrayList<>();
        MinderOptionDTO minderOptionDTO = new MinderOptionDTO();
        minderOptionDTO.setId("TEST_FUNCTIONAL_MINDER_CASE_ID_9");
        minderOptionDTO.setType(Translator.get("minder_extra_node.case"));
        deleteResourceList.add(minderOptionDTO);
        minderOptionDTO = new MinderOptionDTO();
        minderOptionDTO.setId("TEST_MINDER_MODULE_ID_GYQ9");
        minderOptionDTO.setType(Translator.get("minder_extra_node.module"));
        deleteResourceList.add(minderOptionDTO);
        minderOptionDTO = new MinderOptionDTO();
        minderOptionDTO.setId("additional1");
        minderOptionDTO.setType("NONE");
        deleteResourceList.add(minderOptionDTO);
        request.setDeleteResourceList(deleteResourceList);
        this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_EDIT_URL, request);
        MindAdditionalNode mindAdditionalNode = mindAdditionalNodeMapper.selectByPrimaryKey("additional2");
        Assertions.assertTrue(StringUtils.equalsIgnoreCase(mindAdditionalNode.getParentId(),"TEST_MINDER_MODULE_ID_GYQ"));
        FunctionalCaseExample functionalCaseExample = new FunctionalCaseExample();
        functionalCaseExample.createCriteria().andNameEqualTo("新增用例");
        List<FunctionalCase> functionalCases = functionalCaseMapper.selectByExample(functionalCaseExample);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(functionalCases));
        Assertions.assertTrue(functionalCases.get(0).getPos() > 0L);
        FunctionalCaseModuleExample functionalCaseModuleExample = new FunctionalCaseModuleExample();
        functionalCaseModuleExample.createCriteria().andNameEqualTo("新增9");
        List<FunctionalCaseModule> functionalCaseModules = functionalCaseModuleMapper.selectByExample(functionalCaseModuleExample);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(functionalCaseModules));
        Assertions.assertTrue(functionalCaseModules.get(0).getPos() > 0L);
        request = new FunctionalCaseMinderEditRequest();
        request.setProjectId("project-case-minder-test");
        request.setVersionId("ffff");
        this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_EDIT_URL, request);
        functionalCases = functionalCaseMapper.selectByExample(functionalCaseExample);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(functionalCases));
    }

    @Test
    @Order(3)
    public void testGetCaseModuleNodeList() throws Exception {
        FunctionalCaseMindRequest request = new FunctionalCaseMindRequest();
        request.setProjectId("project-case-minder-test");
        MvcResult mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_NODE_MODULE_URL, request);
        String contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResultHolder resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        List<BaseTreeNode> baseTreeNodes = JSON.parseArray(JSON.toJSONString(resultHolder.getData()), BaseTreeNode.class);
        Assertions.assertNotNull(baseTreeNodes);
        request.setModuleId("TEST_MINDER_MODULE_ID_GYQ");
        mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_NODE_MODULE_URL, request);
        contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        baseTreeNodes = JSON.parseArray(JSON.toJSONString(resultHolder.getData()), BaseTreeNode.class);
        Assertions.assertNotNull(baseTreeNodes);
    }

    @Test
    @Order(4)
    public void testGetCaseReviewList() throws Exception {
        FunctionalCaseReviewMindRequest request = new FunctionalCaseReviewMindRequest();
        request.setProjectId("project-case-minder-test");
        request.setModuleId("TEST_MINDER_MODULE_ID_GYQ4");
        request.setReviewId("TEST_MINDER_REVIEW_ID_GYQ");
        MvcResult mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_REVIEW_LIST_URL, request);
        String contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResultHolder resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        List<FunctionalMinderTreeDTO> baseTreeNodes = JSON.parseArray(JSON.toJSONString(resultHolder.getData()), FunctionalMinderTreeDTO.class);
        Assertions.assertNotNull(baseTreeNodes);
        Assertions.assertEquals(1, baseTreeNodes.size());
        request = new FunctionalCaseReviewMindRequest();
        request.setProjectId("project-case-minder-test");
        request.setModuleId("TEST_MINDER_MODULE_ID_GYQ4");
        request.setReviewId("TEST_MINDER_REVIEW_ID_GYQ2");
        request.setViewFlag(true);
        request.setViewStatusFlag(true);
        mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_REVIEW_LIST_URL, request);
        contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        baseTreeNodes = JSON.parseArray(JSON.toJSONString(resultHolder.getData()), FunctionalMinderTreeDTO.class);
        Assertions.assertNotNull(baseTreeNodes);
        Assertions.assertEquals(1, baseTreeNodes.size());
    }

    @Test
    @Order(5)
    public void testGetCasePlanList() throws Exception {
        FunctionalCasePlanMindRequest request = new FunctionalCasePlanMindRequest();
        request.setProjectId("project-case-minder-test");
        request.setModuleId("TEST_MINDER_MODULE_ID_GYQ4");
        request.setPlanId("TEST_MINDER_PLAN_ID_1");
        MvcResult mvcResultPage = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_PLAN_LIST_URL, request);
        String contentAsString = mvcResultPage.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResultHolder resultHolder = JSON.parseObject(contentAsString, ResultHolder.class);
        List<FunctionalMinderTreeDTO> baseTreeNodes = JSON.parseArray(JSON.toJSONString(resultHolder.getData()), FunctionalMinderTreeDTO.class);
        Assertions.assertNotNull(baseTreeNodes);
        Assertions.assertEquals(2, baseTreeNodes.size());
        System.out.println(baseTreeNodes);
    }

}
