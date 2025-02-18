<template>
  <MsBaseTable
    v-if="props.showType === 'API'"
    ref="apiTableRef"
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
    <template #[FilterSlotNameEnum.API_TEST_API_REQUEST_METHODS]="{ filterContent }">
      <apiMethodName :method="filterContent.value" />
    </template>
    <template #method="{ record }">
      <apiMethodName :method="record.method" is-tag />
    </template>
    <template #caseTotal="{ record }">
      {{ record.caseTotal }}
    </template>
    <template #createUserName="{ record }">
      <a-tooltip :content="`${record.createUserName}`" position="tl">
        <div class="one-line-text">{{ record.createUserName }}</div>
      </a-tooltip>
    </template>
  </MsBaseTable>
</template>

<script setup lang="ts">
  import { ref } from 'vue';

  import MsButton from '@/components/pure/ms-button/index.vue';
  import MsBaseTable from '@/components/pure/ms-table/base-table.vue';
  import { MsTableColumn } from '@/components/pure/ms-table/type';
  import useTable from '@/components/pure/ms-table/useTable';
  import apiMethodName from '@/views/api-test/components/apiMethodName.vue';

  import { useI18n } from '@/hooks/useI18n';
  import useAppStore from '@/store/modules/app';

  import type { TableQueryParams } from '@/models/common';
  import { RequestMethods } from '@/enums/apiEnum';
  import { CasePageApiTypeEnum } from '@/enums/associateCaseEnum';
  import { CaseLinkEnum } from '@/enums/caseEnum';
  import { FilterRemoteMethodsEnum, FilterSlotNameEnum } from '@/enums/tableFilterEnum';

  import { getPublicLinkCaseListMap } from './utils/page';

  const { t } = useI18n();
  const appStore = useAppStore();
  const props = defineProps<{
    associationType: string; // 关联类型 项目 | 测试计划 | 用例评审
    activeModule: string;
    offspringIds: string[];
    currentProject: string;
    associatedIds?: string[]; // 已关联ids
    activeSourceType: keyof typeof CaseLinkEnum;
    selectorAll?: boolean;
    keyword: string;
    showType: string;
    getPageApiType: keyof typeof CasePageApiTypeEnum; // 获取未关联分页Api
    extraTableParams?: TableQueryParams; // 查询表格的额外参数
    protocols: string[];
  }>();

  const emit = defineEmits<{
    (e: 'getModuleCount', params: TableQueryParams): void;
    (e: 'refresh'): void;
    (e: 'initModules'): void;
  }>();

  const requestMethodsOptions = computed(() => {
    return Object.values(RequestMethods).map((e) => {
      return {
        value: e,
        key: e,
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
      width: 100,
      columnSelectorDisabled: true,
    },
    {
      title: 'apiTestManagement.apiName',
      dataIndex: 'name',
      showTooltip: true,
      sortable: {
        sortDirections: ['ascend', 'descend'],
        sorter: true,
      },
      width: 200,
      columnSelectorDisabled: true,
    },
    {
      title: 'apiTestManagement.apiType',
      dataIndex: 'method',
      slotName: 'method',
      width: 140,
      showDrag: true,
      filterConfig: {
        options: requestMethodsOptions.value,
        filterSlotName: FilterSlotNameEnum.API_TEST_API_REQUEST_METHODS,
      },
    },
    {
      title: 'apiTestManagement.path',
      dataIndex: 'path',
      showTooltip: true,
      width: 200,
      showDrag: true,
    },

    {
      title: 'common.tag',
      dataIndex: 'tags',
      isTag: true,
      isStringTag: true,
      width: 400,
      showDrag: true,
    },
    {
      title: 'apiTestManagement.caseTotal',
      dataIndex: 'caseTotal',
      showTooltip: true,
      width: 100,
      showDrag: true,
      slotName: 'caseTotal',
    },
    {
      title: 'common.creator',
      slotName: 'createUserName',
      dataIndex: 'createUser',
      filterConfig: {
        mode: 'remote',
        loadOptionParams: {
          projectId: appStore.currentProjectId,
        },
        remoteMethod: FilterRemoteMethodsEnum.PROJECT_PERMISSION_MEMBER,
        placeholderText: t('caseManagement.featureCase.PleaseSelect'),
      },
      showInTable: true,
      width: 200,
      showDrag: true,
    },
  ];

  const { propsRes, propsEvent, loadList, setLoadListParams, resetSelector, setPagination, resetFilterParams } =
    useTable(getPublicLinkCaseListMap[props.getPageApiType][props.activeSourceType].API, {
      columns,
      showSetting: false,
      selectable: true,
      showSelectAll: true,
      heightUsed: 310,
      showSelectorAll: true,
    });

  async function getTableParams() {
    return {
      keyword: props.keyword,
      projectId: props.currentProject,
      protocols: props.protocols,
      moduleIds: props.activeModule === 'all' || !props.activeModule ? [] : [props.activeModule, ...props.offspringIds],
      excludeIds: [...(props.associatedIds || [])], // 已经存在的关联的id列表
      condition: {
        keyword: props.keyword,
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

  async function loadApiList() {
    const tableParams = await getTableParams();
    setLoadListParams(tableParams);
    loadList();
    emit('getModuleCount', {
      ...tableParams,
      current: propsRes.value.msPagination?.current,
      pageSize: propsRes.value.msPagination?.pageSize,
    });
  }

  watch(
    () => [() => props.currentProject, () => props.protocols],
    () => {
      loadApiList();
    }
  );

  watch(
    () => props.showType,
    (val) => {
      if (val === 'API') {
        resetSelector();
        resetFilterParams();
        loadApiList();
      }
    }
  );

  watch(
    () => props.activeModule,
    (val) => {
      if (val) {
        loadApiList();
      }
    }
  );

  function getApiSaveParams() {
    const { excludeKeys, selectedKeys, selectorStatus } = propsRes.value;
    const tableParams = getTableParams();
    return {
      ...tableParams,
      excludeIds: [...excludeKeys].concat(...(props.associatedIds || [])),
      selectIds: selectorStatus === 'all' ? [] : [...selectedKeys],
      selectAll: selectorStatus === 'all',
    };
  }

  defineExpose({
    getApiSaveParams,
    loadApiList,
  });
</script>

<style scoped></style>
