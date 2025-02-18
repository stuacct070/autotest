<template>
  <ReportHeader v-if="!props.isDrawer" :detail="detail" :share-id="shareId" />
  <div class="analysis-wrapper">
    <div class="analysis min-w-[238px]">
      <div class="block-title">{{ t('report.detail.api.reportAnalysis') }}</div>
      <ReportMetricsItem
        v-for="analysisItem in reportAnalysisList"
        :key="analysisItem.name"
        :item-info="analysisItem"
      />
    </div>
    <div class="analysis min-w-[410px]">
      <div class="block-title">{{ t('report.detail.executionAnalysis') }}</div>
      <SetReportChart
        size="160px"
        :legend-data="legendData"
        :options="charOptions"
        :request-total="getIndicators(detail.caseTotal) || 0"
      />
    </div>
  </div>

  <div class="analysis-wrapper">
    <div class="analysis min-w-[330px]">
      <div class="block-title">{{ t('report.detail.useCaseAnalysis') }}</div>
      <div class="flex">
        <div class="w-[70%]">
          <SingleStatusProgress :detail="detail" type="FUNCTIONAL" status="pending" />
          <SingleStatusProgress :detail="detail" type="FUNCTIONAL" status="success" />
          <SingleStatusProgress :detail="detail" type="FUNCTIONAL" status="block" />
          <SingleStatusProgress :detail="detail" type="FUNCTIONAL" status="error" />
        </div>
        <div class="relative w-[30%] min-w-[150px]">
          <div class="charts absolute w-full text-center">
            <div class="text-[12px] !text-[var(--color-text-4)]">{{ t('report.passRate') }}</div>
            <a-popover position="bottom" content-class="response-popover-content">
              <div class="flex justify-center text-[18px] font-medium">
                <div class="one-line-text max-w-[80px] text-[var(--color-text-1)]">{{ functionCasePassRate }} </div>
              </div>
              <template #content>
                <div class="min-w-[95px] max-w-[400px] p-4 text-[14px]">
                  <div class="text-[12px] font-medium text-[var(--color-text-4)]">{{ t('report.passRate') }}</div>
                  <div class="mt-2 text-[18px] font-medium text-[var(--color-text-1)]">{{ functionCasePassRate }}</div>
                </div>
              </template>
            </a-popover>
          </div>
          <div class="flex h-full w-full min-w-[150px] items-center justify-center">
            <MsChart width="150px" height="150px" :options="functionCaseOptions"
          /></div>
        </div>
      </div>
    </div>
    <!-- TODO 接口用例&场景用例待联调 -->
    <div class="analysis min-w-[330px]">
      <div class="block-title">{{ t('report.detail.apiUseCaseAnalysis') }}</div>
      <div class="flex">
        <div class="w-[70%]">
          <SingleStatusProgress type="API" :detail="detail" status="pending" />
          <SingleStatusProgress type="API" :detail="detail" status="success" />
          <SingleStatusProgress type="API" :detail="detail" status="fakeError" />
          <SingleStatusProgress type="API" :detail="detail" status="error" />
        </div>
        <div class="relative w-[30%] min-w-[150px]">
          <div class="charts absolute w-full text-center">
            <div class="text-[12px] !text-[var(--color-text-4)]">{{ t('report.passRate') }}</div>
            <a-popover position="bottom" content-class="response-popover-content">
              <div class="flex justify-center text-[18px] font-medium">
                <div class="one-line-text max-w-[80px] text-[var(--color-text-1)]">{{ functionCasePassRate }} </div>
              </div>
              <template #content>
                <div class="min-w-[95px] max-w-[400px] p-4 text-[14px]">
                  <div class="text-[12px] font-medium text-[var(--color-text-4)]">{{ t('report.passRate') }}</div>
                  <div class="mt-2 text-[18px] font-medium text-[var(--color-text-1)]">{{ functionCasePassRate }}</div>
                </div>
              </template>
            </a-popover>
          </div>
          <div class="flex h-full w-full min-w-[150px] items-center justify-center">
            <MsChart width="150px" height="150px" :options="functionCaseOptions"
          /></div>
        </div>
      </div>
    </div>
    <div class="analysis min-w-[330px]">
      <div class="block-title">{{ t('report.detail.scenarioUseCaseAnalysis') }}</div>
      <div class="flex">
        <div class="w-[70%]">
          <SingleStatusProgress type="SCENARIO" :detail="detail" status="pending" />
          <SingleStatusProgress type="SCENARIO" :detail="detail" status="success" />
          <SingleStatusProgress type="SCENARIO" :detail="detail" status="fakeError" />
          <SingleStatusProgress type="SCENARIO" :detail="detail" status="error" />
        </div>
        <div class="relative w-[30%] min-w-[150px]">
          <div class="charts absolute w-full text-center">
            <div class="text-[12px] !text-[var(--color-text-4)]">{{ t('report.passRate') }}</div>
            <a-popover position="bottom" content-class="response-popover-content">
              <div class="flex justify-center text-[18px] font-medium">
                <div class="one-line-text max-w-[80px] text-[var(--color-text-1)]">{{ functionCasePassRate }} </div>
              </div>
              <template #content>
                <div class="min-w-[95px] max-w-[400px] p-4 text-[14px]">
                  <div class="text-[12px] font-medium text-[var(--color-text-4)]">{{ t('report.passRate') }}</div>
                  <div class="mt-2 text-[18px] font-medium text-[var(--color-text-1)]">{{ functionCasePassRate }}</div>
                </div>
              </template>
            </a-popover>
          </div>
          <div class="flex h-full w-full min-w-[150px] items-center justify-center">
            <MsChart width="150px" height="150px" :options="functionCaseOptions"
          /></div>
        </div>
      </div>
    </div>
  </div>
  <Summary
    v-model:richText="richText"
    :share-id="shareId"
    :show-button="showButton"
    @update-summary="handleUpdateReportDetail"
    @cancel="handleCancel"
    @handle-summary="handleSummary"
  />
  <MsCard simple auto-height auto-width>
    <MsTab
      v-model:active-key="activeTab"
      :show-badge="false"
      :content-tab-list="contentTabList"
      no-content
      class="relative mb-[16px] border-b"
    />
    <BugTable v-if="activeTab === 'bug'" :report-id="detail.id" :share-id="shareId" />
    <FeatureCaseTable v-if="activeTab === 'featureCase'" :report-id="detail.id" :share-id="shareId" />
    <ApiCaseTable v-if="activeTab === 'apiCase'" :report-id="detail.id" :share-id="shareId" />
    <ScenarioCaseTable v-if="activeTab === 'scenarioCase'" :report-id="detail.id" :share-id="shareId" />
  </MsCard>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { useRoute } from 'vue-router';
  import { useEventListener } from '@vueuse/core';
  import { Message } from '@arco-design/web-vue';
  import { cloneDeep } from 'lodash-es';

  import MsChart from '@/components/pure/chart/index.vue';
  import MsCard from '@/components/pure/ms-card/index.vue';
  import MsTab from '@/components/pure/ms-tab/index.vue';
  import ReportMetricsItem from './ReportMetricsItem.vue';
  import SetReportChart from '@/views/api-test/report/component/case/setReportChart.vue';
  import SingleStatusProgress from '@/views/test-plan/report/component/singleStatusProgress.vue';
  import ApiCaseTable from '@/views/test-plan/report/detail/component/apiCaseTable.vue';
  import BugTable from '@/views/test-plan/report/detail/component/bugTable.vue';
  import FeatureCaseTable from '@/views/test-plan/report/detail/component/featureCaseTable.vue';
  import ReportHeader from '@/views/test-plan/report/detail/component/reportHeader.vue';
  import ScenarioCaseTable from '@/views/test-plan/report/detail/component/scenarioCaseTable.vue';
  import Summary from '@/views/test-plan/report/detail/component/summary.vue';

  import { updateReportDetail } from '@/api/modules/test-plan/report';
  import { defaultReportDetail, statusConfig } from '@/config/testPlan';
  import { useI18n } from '@/hooks/useI18n';
  import { addCommasToNumber } from '@/utils';

  import type { LegendData } from '@/models/apiTest/report';
  import type {
    countDetail,
    PlanReportDetail,
    ReportMetricsItemModel,
    StatusListType,
  } from '@/models/testPlan/testPlanReport';

  import { getIndicators } from '@/views/api-test/report/utils';

  const { t } = useI18n();

  const route = useRoute();
  const props = defineProps<{
    detailInfo: PlanReportDetail;
    isDrawer?: boolean;
  }>();

  const emit = defineEmits<{
    (e: 'updateSuccess'): void;
  }>();

  const detail = ref<PlanReportDetail>({ ...cloneDeep(defaultReportDetail) });
  const showButton = ref<boolean>(false);
  const richText = ref<{ summary: string; richTextTmpFileIds?: string[] }>({
    summary: '',
  });

  /**
   * 分享share
   */
  const shareId = ref<string>(route.query.shareId as string);

  const legendData = ref<LegendData[]>([]);

  const charOptions = ref({
    tooltip: {
      show: false,
      trigger: 'item',
    },
    legend: {
      show: false,
    },
    series: {
      name: '',
      type: 'pie',
      radius: ['62%', '80%'],
      center: ['50%', '50%'],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'center',
      },
      emphasis: {
        label: {
          show: false,
          fontSize: 40,
          fontWeight: 'bold',
        },
      },
      labelLine: {
        show: false,
      },
      data: [
        {
          value: 0,
          name: t('common.success'),
          itemStyle: {
            color: '#00C261',
          },
        },
        {
          value: 0,
          name: t('common.fakeError'),
          itemStyle: {
            color: '#FFC14E',
          },
        },
        {
          value: 0,
          name: t('common.fail'),
          itemStyle: {
            color: '#ED0303',
          },
        },
        {
          value: 0,
          name: t('common.unExecute'),
          itemStyle: {
            color: '#D4D4D8',
          },
        },
        {
          value: 0,
          name: t('common.block'),
          itemStyle: {
            color: '#B379C8',
          },
        },
      ],
    },
  });

  const functionCaseOptions = ref({
    tooltip: {
      show: false,
      trigger: 'item',
    },
    legend: {
      show: false,
    },
    series: {
      name: '',
      type: 'pie',
      radius: ['62%', '80%'],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'center',
      },
      emphasis: {
        label: {
          show: false,
          fontSize: 40,
          fontWeight: 'bold',
        },
      },
      labelLine: {
        show: false,
      },
      data: [
        {
          value: 0,
          name: t('common.success'),
          itemStyle: {
            color: '#00C261',
          },
        },
      ],
    },
  });

  // 初始化图表
  function initOptionsData() {
    charOptions.value.series.data = statusConfig.map((item: StatusListType) => {
      return {
        value: detail.value.executeCount[item.value] || 0,
        name: t(item.label),
        itemStyle: {
          color: item.color,
          borderWidth: 2,
          borderColor: '#ffffff',
        },
      };
    });
    legendData.value = statusConfig.map((item: StatusListType) => {
      const rate = (detail.value.executeCount[item.value] / detail.value.caseTotal) * 100;
      return {
        ...item,
        label: t(item.label),
        count: detail.value.executeCount[item.value] || 0,
        rote: `${Number.isNaN(rate) ? 0 : rate.toFixed(2)}%`,
      };
    }) as unknown as LegendData[];

    const passRateData = statusConfig.filter((item) => ['success'].includes(item.value));
    const { functionalCount } = detail.value;
    const { success } = functionalCount;
    const valueList = success ? statusConfig : passRateData;

    functionCaseOptions.value.series.data = valueList.map((item: StatusListType) => {
      return {
        value: detail.value.functionalCount[item.value] || 0,
        name: t(item.label),
        itemStyle: {
          color: success ? item.color : '#D4D4D8',
          borderWidth: 2,
          borderColor: '#ffffff',
        },
      };
    });
  }

  async function handleUpdateReportDetail() {
    try {
      await updateReportDetail({
        id: detail.value.id,
        summary: richText.value.summary,
        richTextTmpFileIds: richText.value.richTextTmpFileIds ?? [],
      });
      Message.success(t('common.updateSuccess'));
      showButton.value = false;
      emit('updateSuccess');
    } catch (error) {
      // eslint-disable-next-line no-console
      console.log(error);
    }
  }

  const reportAnalysisList = computed<ReportMetricsItemModel[]>(() => [
    {
      name: t('report.detail.threshold'),
      value: detail.value.passThreshold,
      unit: '%',
      icon: 'threshold',
    },
    {
      name: t('report.detail.reportPassRate'),
      value: detail.value.passRate,
      unit: '%',
      icon: 'passRate',
    },
    {
      name: t('report.detail.performCompletion'),
      value: detail.value.executeRate,
      unit: '%',
      icon: 'passRate',
    },
    {
      name: t('report.detail.totalDefects'),
      value: addCommasToNumber(detail.value.bugCount),
      unit: t('report.detail.number'),
      icon: 'bugTotal',
    },
  ]);

  const functionCasePassRate = computed(() => {
    const { functionalCount } = detail.value;
    const { success, error, pending, block } = functionalCount;
    const successRate = (success / (success + error + pending + block)) * 100;
    return `${Number.isNaN(successRate) ? 0 : successRate.toFixed(2)}%`;
  });

  const activeTab = ref('bug');
  const contentTabList = ref([
    {
      value: 'bug',
      label: t('report.detail.bugDetails'),
    },
    {
      value: 'featureCase',
      label: t('report.detail.featureCaseDetails'),
    },
    {
      value: 'apiCase',
      label: t('report.detail.apiCaseDetails'),
    },
    {
      value: 'scenarioCase',
      label: t('report.detail.scenarioCaseDetails'),
    },
  ]);

  watchEffect(() => {
    if (props.detailInfo) {
      detail.value = cloneDeep(props.detailInfo);
      richText.value.summary = detail.value.summary;
      initOptionsData();
    }
  });

  onMounted(async () => {
    nextTick(() => {
      const editorContent = document.querySelector('.editor-content');
      useEventListener(editorContent, 'click', () => {
        showButton.value = true;
      });
    });
  });

  function getSummaryDetail(detailCount: countDetail) {
    if (detailCount) {
      const { success, error, fakeError, pending, block } = detailCount;
      // 已执行用例
      const hasExecutedCase = success + error + fakeError + block;
      // 用例总数
      const caseTotal = hasExecutedCase + pending;
      // 执行率
      const executedCount = (hasExecutedCase / caseTotal) * 100;
      const apiExecutedRate = `${Number.isNaN(executedCount) ? 0 : executedCount.toFixed(2)}%`;
      // 通过率
      const successCount = (success / caseTotal) * 100;
      const successRate = `${Number.isNaN(successCount) ? 0 : successCount.toFixed(2)}%`;
      return {
        hasExecutedCase,
        caseTotal,
        apiExecutedRate,
        successRate,
        pending,
        success,
      };
    }
    return {
      hasExecutedCase: 0,
      caseTotal: 0,
      apiExecutedRate: 0,
      successRate: 0,
      pending: 0,
      success: 0,
    };
  }

  const summaryContent = computed(() => {
    const { functionalCount, apiCaseCount, apiScenarioCount } = detail.value;
    const functionalCaseDetail = getSummaryDetail(functionalCount);
    const apiCaseDetail = getSummaryDetail(apiCaseCount);
    const apiScenarioDetail = getSummaryDetail(apiScenarioCount);
    const allCaseTotal = functionalCaseDetail.caseTotal + apiCaseDetail.caseTotal + apiScenarioDetail.caseTotal;
    const allHasExecutedCase =
      functionalCaseDetail.hasExecutedCase + apiCaseDetail.hasExecutedCase + apiScenarioDetail.hasExecutedCase;
    const allPendingCase = functionalCaseDetail.pending + apiCaseDetail.pending + apiScenarioDetail.pending;
    const allSuccessCase = functionalCaseDetail.success + apiCaseDetail.success + apiScenarioDetail.success;

    const allExecutedCount = (allHasExecutedCase / allCaseTotal) * 100;
    const allExecutedRate = `${Number.isNaN(allExecutedCount) ? 0 : allExecutedCount.toFixed(2)}%`;

    // 通过率
    const allSuccessCount = (allSuccessCase / allCaseTotal) * 100;
    const allSuccessRate = `${Number.isNaN(allSuccessCount) ? 0 : allSuccessCount.toFixed(2)}%`;
    // 接口用例通过率
    return `<p style=""><span color="" fontsize="">本次完成 ${detail.value.name}的功能测试，接口测试；共 ${allCaseTotal}条 用例，已执行 ${allHasExecutedCase} 条，未执行 ${allPendingCase} 条，执行率为 ${allExecutedRate}%，通过用例 ${allSuccessCase} 条，通过率为 ${allSuccessRate}，达到/未达到通过阈值（通过阈值为${detail.value.passThreshold}%），xxx计划满足/不满足发布要求。<br>
      （1）本次测试包含${functionalCaseDetail.caseTotal}条功能测试用例，执行了${functionalCaseDetail.hasExecutedCase}条，未执行${functionalCaseDetail.pending}条，执行率为${functionalCaseDetail.apiExecutedRate}，通过用例${functionalCaseDetail.success}条，通过率为${functionalCaseDetail.successRate}。共发现缺陷0个。<br>
      （2）本次测试包含${apiCaseDetail.caseTotal}条接口测试用例，执行了${apiCaseDetail.hasExecutedCase}条，未执行${apiCaseDetail.pending}条，执行率为${apiCaseDetail.apiExecutedRate}，通过用例${apiCaseDetail.success}条，通过率为${apiCaseDetail.successRate}。共发现缺陷0个。<br>
      （3）本次测试包含${apiScenarioDetail.caseTotal}条场景测试用例，执行了${apiScenarioDetail.hasExecutedCase}条，未执行${apiScenarioDetail.pending}条，执行率为${apiScenarioDetail.apiExecutedRate}%，通过用例${apiScenarioDetail.success}条，通过率为${apiScenarioDetail.successRate}。共发现缺陷0个</span></p>
  `;
  });

  function handleCancel() {
    richText.value = { summary: detail.value.summary };
    showButton.value = false;
  }

  function handleSummary() {
    richText.value.summary = summaryContent.value;
  }
</script>

<style scoped lang="less">
  .block-title {
    @apply mb-4 font-medium;
  }
  .analysis-wrapper {
    @apply mb-4 flex flex-wrap items-center gap-4;
    .analysis {
      padding: 24px;
      height: 250px;
      box-shadow: 0 0 10px rgba(120 56 135/ 5%);
      @apply flex-1 rounded-xl bg-white;
      .charts {
        top: 36%;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 99;
        margin: auto;
      }
    }
  }
</style>
