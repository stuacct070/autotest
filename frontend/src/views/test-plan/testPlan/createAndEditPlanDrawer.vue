<template>
  <MsDrawer
    v-model:visible="innerVisible"
    :title="modelTitle"
    :width="800"
    unmount-on-close
    :ok-text="okText"
    :save-continue-text="t('case.saveContinueText')"
    :show-continue="!props.planId?.length"
    :ok-loading="drawerLoading"
    @confirm="handleDrawerConfirm(false)"
    @continue="handleDrawerConfirm(true)"
    @cancel="handleCancel"
  >
    <a-form ref="formRef" :model="form" layout="vertical">
      <a-form-item
        field="name"
        :label="t('caseManagement.featureCase.planName')"
        :rules="[{ required: true, message: t('testPlan.planForm.nameRequired') }]"
        class="w-[732px]"
      >
        <a-input v-model="form.name" :max-length="255" :placeholder="t('testPlan.planForm.namePlaceholder')" />
      </a-form-item>
      <a-form-item field="description" :label="t('common.desc')" class="w-[732px]">
        <a-textarea v-model:model-value="form.description" :placeholder="t('common.pleaseInput')" :max-length="1000" />
      </a-form-item>
      <a-form-item :label="t('common.belongModule')" class="w-[436px]">
        <a-tree-select
          v-model:modelValue="form.moduleId"
          :data="props.moduleTree"
          :field-names="{ title: 'name', key: 'id', children: 'children' }"
          :tree-props="{
            virtualListProps: {
              height: 200,
              threshold: 200,
            },
          }"
          allow-search
          :filter-tree-node="filterTreeNode"
        >
          <template #tree-slot-title="node">
            <a-tooltip :content="`${node.name}`" position="tl">
              <div class="inline-flex w-full">
                <div class="one-line-text w-[240px] text-[var(--color-text-1)]">
                  {{ node.name }}
                </div>
              </div>
            </a-tooltip>
          </template>
        </a-tree-select>
      </a-form-item>
      <a-form-item
        field="cycle"
        :label="t('testPlan.planForm.planStartAndEndTime')"
        asterisk-position="end"
        class="w-[436px]"
      >
        <a-range-picker
          v-model:model-value="form.cycle"
          show-time
          value-format="timestamp"
          :separator="t('common.to')"
          :time-picker-props="{
            defaultValue: tempRange,
          }"
          :disabled-time="disabledTime"
          @select="handleTimeSelect"
        />
      </a-form-item>
      <a-form-item field="tags" :label="t('common.tag')" class="w-[436px]">
        <MsTagsInput v-model:model-value="form.tags" :max-tag-count="10" />
      </a-form-item>
      <MsMoreSettingCollapse>
        <template #content>
          <div v-for="item in switchList" :key="item.key" class="mb-[24px] flex items-center gap-[8px]">
            <a-switch v-model="form[item.key]" size="small" />
            {{ t(item.label) }}
            <a-tooltip :position="item.tooltipPosition">
              <template #content>
                <div v-for="descItem in item.desc" :key="descItem">{{ t(descItem) }}</div>
              </template>
              <IconQuestionCircle class="h-[16px] w-[16px] text-[--color-text-4] hover:text-[rgb(var(--primary-5))]" />
            </a-tooltip>
          </div>
          <a-form-item field="passThreshold" :label="t('testPlan.planForm.passThreshold')">
            <a-input-number
              v-model:model-value="form.passThreshold"
              size="small"
              mode="button"
              class="w-[120px]"
              :min="1"
              :max="100"
              :precision="2"
              :default-value="100"
            />
            <template #label>
              {{ t('testPlan.planForm.passThreshold') }}
              <a-tooltip position="tl" :content="t('testPlan.planForm.passThresholdTip')">
                <IconQuestionCircle
                  class="h-[16px] w-[16px] text-[--color-text-4] hover:text-[rgb(var(--primary-5))]"
                />
              </a-tooltip>
            </template>
          </a-form-item>
        </template>
      </MsMoreSettingCollapse>
    </a-form>
  </MsDrawer>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { FormInstance, Message, TreeNodeData, ValidatedError } from '@arco-design/web-vue';
  import { cloneDeep } from 'lodash-es';
  import dayjs from 'dayjs';

  import MsDrawer from '@/components/pure/ms-drawer/index.vue';
  import MsMoreSettingCollapse from '@/components/pure/ms-more-setting-collapse/index.vue';
  import MsTagsInput from '@/components/pure/ms-tags-input/index.vue';

  import { addTestPlan, getTestPlanDetail, updateTestPlan } from '@/api/modules/test-plan/testPlan';
  import { useI18n } from '@/hooks/useI18n';
  import useAppStore from '@/store/modules/app';

  import { ModuleTreeNode } from '@/models/common';
  import type { AddTestPlanParams, SwitchListModel } from '@/models/testPlan/testPlan';
  import { testPlanTypeEnum } from '@/enums/testPlanEnum';

  import { DisabledTimeProps } from '@arco-design/web-vue/es/date-picker/interface';

  const props = defineProps<{
    planId?: string;
    moduleTree?: ModuleTreeNode[];
    moduleId?: string;
  }>();
  const innerVisible = defineModel<boolean>('visible', {
    required: true,
  });

  const emit = defineEmits<{
    (e: 'close'): void;
    (e: 'loadPlanList'): void;
  }>();

  const { t } = useI18n();
  const appStore = useAppStore();

  const drawerLoading = ref(false);
  const formRef = ref<FormInstance>();
  const initForm: AddTestPlanParams = {
    groupId: 'NONE',
    name: '',
    projectId: '',
    moduleId: 'root',
    cycle: [],
    tags: [],
    description: '',
    testPlanning: false,
    automaticStatusUpdate: true,
    repeatCase: false,
    passThreshold: 100,
    type: testPlanTypeEnum.TEST_PLAN,
    baseAssociateCaseRequest: { selectIds: [], selectAll: false, condition: {} },
  };
  const form = ref<AddTestPlanParams>(cloneDeep(initForm));

  function filterTreeNode(searchValue: string, nodeData: TreeNodeData) {
    return (nodeData as ModuleTreeNode).name.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
  }

  const tempRange = ref<(Date | string | number)[]>(['00:00:00', '00:00:00']);

  function makeLessNumbers(value: number, isSecond = false) {
    const res = [];
    for (let i = 0; i < value; i++) {
      res.push(i);
    }
    return isSecond && res.length === 0 ? [0] : res; // 秒至少相差 1 秒
  }

  function disabledTime(current: Date, type: 'start' | 'end'): DisabledTimeProps {
    if (type === 'end') {
      const currentDate = dayjs(current);
      const startDate = dayjs(tempRange.value[0]);
      // 结束时间至少比开始时间多一秒
      return {
        disabledHours: () => {
          if (currentDate.isSame(startDate, 'D')) {
            return makeLessNumbers(startDate.get('h'));
          }
          return [];
        },
        disabledMinutes: () => {
          if (currentDate.isSame(startDate, 'D') && currentDate.isSame(startDate, 'h')) {
            return makeLessNumbers(startDate.get('m'));
          }
          return [];
        },
        disabledSeconds: () => {
          if (
            currentDate.isSame(startDate, 'D') &&
            currentDate.isSame(startDate, 'h') &&
            currentDate.isSame(startDate, 'm')
          ) {
            return makeLessNumbers(startDate.get('s'), true);
          }
          return [];
        },
      };
    }
    return {};
  }

  function handleTimeSelect(value: (Date | string | number | undefined)[]) {
    if (value) {
      // const start = dayjs(value[0]);
      // const end = dayjs(value[1]);
      // if (start.isSame(end, 'D') && end.hour() === 0 && end.minute() === 0 && end.second() === 0) {
      //   const newEnd = end.hour(23).minute(59).second(59);
      //   value[1] = newEnd.valueOf();
      // }
      tempRange.value = value as number[];
    }
  }

  const switchList: SwitchListModel[] = [
    {
      key: 'repeatCase',
      label: 'testPlan.planForm.associateRepeatCase',
      tooltipPosition: 'bl',
      desc: ['testPlan.planForm.repeatCaseTip1', 'testPlan.planForm.repeatCaseTip2'],
    },
  ];

  function handleCancel() {
    innerVisible.value = false;
    formRef.value?.resetFields();
    form.value = cloneDeep(initForm);
    emit('close');
  }

  function handleDrawerConfirm(isContinue: boolean) {
    formRef.value?.validate(async (errors: undefined | Record<string, ValidatedError>) => {
      if (!errors) {
        drawerLoading.value = true;
        try {
          const params: AddTestPlanParams = {
            ...cloneDeep(form.value),
            groupId: 'NONE',
            plannedStartTime: form.value.cycle ? form.value.cycle[0] : undefined,
            plannedEndTime: form.value.cycle ? form.value.cycle[1] : undefined,
            projectId: appStore.currentProjectId,
          };
          if (!props.planId?.length) {
            await addTestPlan(params);
            Message.success(t('common.createSuccess'));
          } else {
            await updateTestPlan(params);
            Message.success(t('common.updateSuccess'));
          }
          emit('loadPlanList');
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log(error);
        } finally {
          drawerLoading.value = false;
        }
        if (!isContinue) {
          handleCancel();
        }
        form.value = { ...cloneDeep(initForm), moduleId: form.value.moduleId };
      }
    });
  }

  async function getDetail() {
    try {
      if (props.planId?.length) {
        const result = await getTestPlanDetail(props.planId);
        form.value = cloneDeep(result);

        form.value.cycle = [result.plannedStartTime as number, result.plannedEndTime as number];
        form.value.passThreshold = parseFloat(result.passThreshold.toString());
      }
    } catch (error) {
      // eslint-disable-next-line no-console
      console.log(error);
    }
  }

  watch(
    () => innerVisible.value,
    (val) => {
      if (val) {
        form.value = cloneDeep(initForm);
        getDetail();
        if (!props.planId && props.moduleId) {
          form.value.moduleId = props.moduleId === 'all' ? 'root' : props.moduleId;
        }
      }
    }
  );

  const modelTitle = computed(() => {
    return props.planId ? t('testPlan.testPlanIndex.updateTestPlan') : t('testPlan.testPlanIndex.createTestPlan');
  });

  const okText = computed(() => {
    return props.planId ? t('common.update') : t('common.create');
  });
</script>
