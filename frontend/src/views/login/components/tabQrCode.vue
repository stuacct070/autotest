<template>
  <a-radio-group v-model:active-key="activeName" type="button" class="tabPlatform" @change="handleClick">
    <a-radio v-for="item of orgOptions" :key="item.value" :value="item.value" :v-show="item.label">
      {{ t('project.messageManagement' + item.value) }}
    </a-radio>
    <!--    <a-tab-pane key="lark" :title="t('project.messageManagement.LARK')"></a-tab-pane>
    <a-tab-pane key="larksuite" :title="t('project.messageManagement.LARK_SUITE')"></a-tab-pane>-->
  </a-radio-group>
  <div v-if="activeName === 'WE_COM'" class="login-qrcode">
    <div class="qrcode">
      <wecom-qr v-if="activeName === 'WE_COM'" />
    </div>
  </div>
  <div v-if="activeName === 'DING_TALK'" class="login-qrcode">
    <div class="qrcode">
      <div class="title">
        <MsIcon type="icon-logo_dingtalk" size="24"></MsIcon>
        钉钉登录
      </div>
      <ding-talk-qr v-if="activeName === 'DING_TALK'" />
    </div>
  </div>
  <!--
  <div class="login-qrcode" v-if="activeName === 'lark'">
    <div class="qrcode">
      <lark-qr v-if="activeName === 'lark'"/>
    </div>
  </div>
  <div class="login-qrcode" v-if="activeName === 'larksuite'">
    <div class="qrcode">
      <larksuite-qr v-if="activeName === 'larksuite'"/>
    </div>
  </div>-->
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { useI18n } from 'vue-i18n';

  import MsIcon from '@/components/pure/ms-icon-font/index.vue';
  import dingTalkQr from './dingTalkQrCode.vue';
  import WecomQr from './weComQrCode.vue';

  import { getPlatformParamUrl } from '@/api/modules/user';

  const { t } = useI18n();

  const activeName = ref('');

  interface qrOption {
    value: string;
    label: string;
  }

  const orgOptions = ref<qrOption[]>([]);
  const props = defineProps<{
    tabName: string;
  }>();
  const initActive = () => {
    const qrArray = ['WE_COM', 'DING_TALK', 'lark', 'larksuite'];
    for (let i = 0; i < qrArray.length; i++) {
      const key = qrArray[i];
      if (props.tabName === key) {
        activeName.value = key;
        break;
      }
    }
  };
  function handleClick(val: string | number | boolean) {
    if (typeof val === 'string') {
      activeName.value = val;
    } else {
      activeName.value = 'WE_COM';
    }
  }
  async function initPlatformInfo() {
    try {
      const res = await getPlatformParamUrl();
      orgOptions.value = res.map((e) => ({
        label: e.name,
        value: e.id,
      }));
    } catch (error) {
      // eslint-disable-next-line no-console
      console.log(error);
    }
  }
  onMounted(() => {
    initActive();
    initPlatformInfo();
  });
</script>

<style lang="less" scoped>
  .tabPlatform {
    width: 400px;
    height: 40px;
  }
  .login-qrcode {
    display: flex;
    align-items: center;
    margin-top: 24px;
    min-width: 480px;
    flex-direction: column;
    .qrcode {
      display: flex;
      justify-content: center;
      align-items: center;
      overflow: hidden;
      border-radius: 8px;
      background: #ffffff;
      flex-direction: column;
    }
    .title {
      display: flex;
      justify-content: center;
      align-items: center;
      overflow: hidden;
      font-size: 18px;
      font-weight: 500;
      font-style: normal;
      line-height: 26px;
      margin-bottom: -24px;
      z-index: 100000;
      .ed-icon {
        margin-right: 8px;
        font-size: 24px;
      }
    }
  }
  .radioOneButton {
    width: 200px;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: center;
    font-size: 16px;
    justify-content: center;
  }
</style>
