<template>
  <MsBaseTable
    ref="tableRef"
    class="mt-[16px]"
    v-bind="propsRes"
    :action-config="{
      baseAction: [],
      moreAction: [],
    }"
    v-on="propsEvent"
    @filter-change="getModuleCount"
  >
    <template #num="{ record }">
      <MsButton type="text">{{ record.num }}</MsButton>
    </template>
    <template #reviewStatus="{ record }">
      <MsIcon
        :type="statusIconMap[record.reviewStatus]?.icon || ''"
        class="mr-1"
        :class="[statusIconMap[record.reviewStatus].color]"
      ></MsIcon>
      <span>{{ statusIconMap[record.reviewStatus]?.statusText || '' }} </span>
    </template>
    <template #lastExecuteResult="{ record }">
      <ExecuteResult v-if="record.lastExecuteResult" :execute-result="record.lastExecuteResult" />
      <span v-else>-</span>
    </template>
    <template #[FilterSlotNameEnum.CASE_MANAGEMENT_CASE_LEVEL]="{ filterContent }">
      <CaseLevel :case-level="filterContent.value" />
    </template>
    <template #caseLevel="{ record }">
      <CaseLevel :case-level="record.caseLevel" />
    </template>
    <template #[FilterSlotNameEnum.CASE_MANAGEMENT_EXECUTE_RESULT]="{ filterContent }">
      <ExecuteResult :execute-result="filterContent.value" />
    </template>
    <template #lastExecResult="{ record }">
      <ExecuteResult :execute-result="record.lastExecResult" />
    </template>
  </MsBaseTable>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { TableData } from '@arco-design/web-vue';

  import MsButton from '@/components/pure/ms-button/index.vue';
  import MsBaseTable from '@/components/pure/ms-table/base-table.vue';
  import { MsTableColumn } from '@/components/pure/ms-table/type';
  import useTable from '@/components/pure/ms-table/useTable';
  import CaseLevel from '@/components/business/ms-case-associate/caseLevel.vue';
  import ExecuteResult from '@/components/business/ms-case-associate/executeResult.vue';

  import { useI18n } from '@/hooks/useI18n';

  import type { TableQueryParams } from '@/models/common';
  import { CasePageApiTypeEnum } from '@/enums/associateCaseEnum';
  import { CaseLinkEnum } from '@/enums/caseEnum';
  import { FilterSlotNameEnum } from '@/enums/tableFilterEnum';

  import { getPublicLinkCaseListMap } from './utils/page';
  import { casePriorityOptions } from '@/views/api-test/components/config';
  import { executionResultMap, statusIconMap } from '@/views/case-management/caseManagementFeature/components/utils';

  const { t } = useI18n();

  const props = defineProps<{
    associationType: string; // 关联类型 项目 | 测试计划 | 用例评审
    activeModule: string;
    offspringIds: string[];
    currentProject: string;
    associatedIds?: string[]; // 已关联ids
    activeSourceType: keyof typeof CaseLinkEnum;
    keyword: string;
    getPageApiType: keyof typeof CasePageApiTypeEnum; // 获取未关联分页Api
    extraTableParams?: TableQueryParams; // 查询表格的额外参数
  }>();

  const emit = defineEmits<{
    (e: 'getModuleCount', params: TableQueryParams): void;
    (e: 'refresh'): void;
    (e: 'initModules'): void;
  }>();

  const reviewResultOptions = computed(() => {
    return Object.keys(statusIconMap).map((key) => {
      return {
        value: key,
        label: statusIconMap[key].statusText,
      };
    });
  });
  const executeResultOptions = computed(() => {
    return Object.keys(executionResultMap).map((key) => {
      return {
        value: key,
        label: executionResultMap[key].statusText,
      };
    });
  });

  const columns: MsTableColumn = [
    {
      title: 'ID',
      dataIndex: 'num',
      slotName: 'num',
      sortIndex: 1,
      sortable: {
        sortDirections: ['ascend', 'descend'],
        sorter: true,
      },
      fixed: 'left',
      width: 150,
      showTooltip: true,
      columnSelectorDisabled: true,
    },
    {
      title: 'case.caseName',
      dataIndex: 'name',
      showTooltip: true,
      sortable: {
        sortDirections: ['ascend', 'descend'],
        sorter: true,
      },
      width: 180,
      columnSelectorDisabled: true,
    },
    {
      title: 'case.caseLevel',
      dataIndex: 'caseLevel',
      slotName: 'caseLevel',
      filterConfig: {
        options: casePriorityOptions,
        filterSlotName: FilterSlotNameEnum.CASE_MANAGEMENT_CASE_LEVEL,
      },
      width: 150,
      showDrag: true,
    },
    {
      title: 'caseManagement.featureCase.tableColumnReviewResult',
      dataIndex: 'reviewStatus',
      slotName: 'reviewStatus',
      filterConfig: {
        options: reviewResultOptions.value,
        filterSlotName: FilterSlotNameEnum.CASE_MANAGEMENT_REVIEW_RESULT,
      },
      showInTable: true,
      width: 150,
      showDrag: true,
    },
    {
      title: 'caseManagement.featureCase.tableColumnExecutionResult',
      dataIndex: 'lastExecuteResult',
      slotName: 'lastExecuteResult',
      filterConfig: {
        options: executeResultOptions.value,
        filterSlotName: FilterSlotNameEnum.CASE_MANAGEMENT_EXECUTE_RESULT,
      },
      showInTable: true,
      width: 150,
      showDrag: true,
    },
    {
      title: 'caseManagement.featureCase.tableColumnCreateUser',
      slotName: 'createUserName',
      dataIndex: 'createUserName',
      showTooltip: true,
      width: 200,
      showDrag: true,
    },
    {
      title: 'caseManagement.featureCase.tableColumnCreateTime',
      slotName: 'createTime',
      dataIndex: 'createTime',
      sortable: {
        sortDirections: ['ascend', 'descend'],
        sorter: true,
      },
      width: 200,
      showDrag: true,
    },
  ];

  const getPageList = computed(() => {
    return getPublicLinkCaseListMap[props.getPageApiType][props.activeSourceType];
  });

  function getCaseLevel(record: TableData) {
    if (record.customFields && record.customFields.length) {
      const caseItem = record.customFields.find((item: any) => item.fieldName === '用例等级' && item.internal);
      return caseItem?.options.find((item: any) => item.value === caseItem?.defaultValue).text;
    }
    return undefined;
  }

  const { propsRes, propsEvent, loadList, setLoadListParams, resetSelector, setPagination, resetFilterParams } =
    useTable(
      getPageList.value,
      {
        columns,
        showSetting: false,
        selectable: true,
        showSelectAll: true,
        heightUsed: 310,
        showSelectorAll: true,
      },
      (record) => {
        return {
          ...record,
          caseLevel: getCaseLevel(record),
          tags: (record.tags || []).map((item: string, i: number) => {
            return {
              id: `${record.id}-${i}`,
              name: item,
            };
          }),
        };
      }
    );

  async function getTableParams() {
    return {
      keyword: props.keyword,
      projectId: props.currentProject,
      moduleIds: props.activeModule === 'all' || !props.activeModule ? [] : [props.activeModule, ...props.offspringIds],
      excludeIds: [...(props.associatedIds || [])], // 已经存在的关联的id列表
      condition: {
        keyword: props.keyword,
        filter: propsRes.value.filter,
      },
      ...props.extraTableParams,
    };
  }

  async function getModuleCount() {
    const tableParams = await getTableParams();
    emit('getModuleCount', {
      ...tableParams,
      current: propsRes.value.msPagination?.current,
      pageSize: propsRes.value.msPagination?.pageSize,
    });
  }

  async function loadCaseList() {
    const tableParams = await getTableParams();
    setLoadListParams(tableParams);
    loadList();
    emit('getModuleCount', {
      ...tableParams,
      current: propsRes.value.msPagination?.current,
      pageSize: propsRes.value.msPagination?.pageSize,
    });
  }

  const tableRef = ref<InstanceType<typeof MsBaseTable>>();

  function getFunctionalSaveParams() {
    const { excludeKeys, selectedKeys, selectorStatus } = propsRes.value;
    const tableParams = getTableParams();
    return {
      ...tableParams,
      excludeIds: [...excludeKeys].concat(...(props.associatedIds || [])),
      selectIds: selectorStatus === 'all' ? [] : [...selectedKeys],
      selectAll: selectorStatus === 'all',
    };
  }

  watch(
    () => props.activeSourceType,
    (val) => {
      if (val) {
        tableRef.value?.initColumn(columns);
        resetSelector();
        resetFilterParams();
        setPagination({
          current: 1,
        });
      }
    }
  );

  watch(
    () => props.currentProject,
    (val) => {
      if (val) {
        loadCaseList();
      }
    },
    {
      immediate: true,
    }
  );

  watch(
    () => props.activeModule,
    (val) => {
      if (val) {
        loadCaseList();
      }
    }
  );

  defineExpose({
    getFunctionalSaveParams,
    loadCaseList,
  });
</script>

<style scoped></style>
