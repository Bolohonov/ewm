<template>
  <w-flex gap="12">
    <!--        <div class="skipper xl1"></div>-->
    <div class="xl4 mt4 ml4">
      <w-card title="Информация о событии" title-class="blue-light5--bg">
        <w-input v-model="eventData.title">Название события</w-input>
        <w-input v-model="eventData.annotation">Описание события</w-input>
        <w-input :disabled="!innChecked" v-model="requestData.KPP">КПП</w-input>
        <w-input :disabled="!innChecked" v-model="requestData.OGRN">ОГРН</w-input>
        <w-input :disabled="!innChecked" v-model="requestData.OKATO">ОКАТО</w-input>
        <w-input :disabled="!innChecked" v-model="requestData.OKTMO">ОКТМО</w-input>
        <w-input :disabled="!innChecked" v-model="requestData.OKFS">ОКФС</w-input>
        <w-input :disabled="!innChecked" v-model="requestData.OKOGU">ОКОГУ</w-input>
        <w-flex gap="12">
          <w-confirm
              class="mla mt4"
              bg-color="success"
              question="Вы уверены?"
              cancel="Нет"
              confirm="Да"
              @confirm="save">Сохранить</w-confirm>
        </w-flex>
      </w-card>
    </div>
    <!--    <div class="xl4 mt4">-->
    <!--      <w-card title="Загрузить PDF с отчетностью" title-class="blue-light5&#45;&#45;bg">-->
    <!--                <span v-if="requestData.status === 'INN_CHECKED'">-->
    <!--                    <LUpload files-limit="1"-->
    <!--                             type="pdf"-->
    <!--                             accept=".pdf"-->
    <!--                             @send-files="fileSendHandler"></LUpload>-->
    <!--                </span>-->
    <!--        <span v-if="requestData.status !== 'INN_CHECKED'">-->
    <!--                    Для отправки файлов необходимо сохранить заявку с корректным ИНН!-->
    <!--                </span>-->
    <!--      </w-card>-->
    <!--      <w-card title="Загрузить EXCEL с отчетностью" title-class="blue-light5&#45;&#45;bg">-->
    <!--                <span v-if="requestData.status === 'INN_CHECKED'">-->
    <!--                    <LUpload files-limit="1"-->
    <!--                             type="excel"-->
    <!--                             accept=".xlsx, .xls"-->
    <!--                             @send-files="fileSendHandler"></LUpload>-->
    <!--                </span>-->
    <!--        <span v-if="requestData.status !== 'INN_CHECKED'">-->
    <!--                    Для отправки файлов необходимо сохранить заявку с корректным ИНН!-->
    <!--                </span>-->
    <!--      </w-card>-->
    <!--      <w-card title="Загрузить изображение с отчетностью" title-class="blue-light5&#45;&#45;bg">-->
    <!--                <span v-if="requestData.status === 'INN_CHECKED'">-->
    <!--                    <LUpload files-limit="1"-->
    <!--                             type="img"-->
    <!--                             accept=".png, .bmp, .jpeg, .tiff, .gif"-->
    <!--                             @send-files="fileSendHandler"></LUpload>-->
    <!--                </span>-->
    <!--        <span v-if="requestData.status !== 'INN_CHECKED'">-->
    <!--                    Для отправки файлов необходимо сохранить заявку с корректным ИНН!-->
    <!--                </span>-->
    <!--      </w-card>-->
    <!--    </div>-->
  </w-flex>
</template>

<script>
export default {
  name: "EventInfo",
  props: ["eventId"],
  emits: ["eventDataUpdated", "sendingEvent", "eventComplete"],
  data() {
    return {
      formValid: null,
      validators: {
        required: value => !!value || 'Обязательное поле',
        // requiredFile: array => array.length > 0 || 'Обязательное поле'
      },
      session: {
        title: '',
      },
      eventData: {
        "title" : '',
        "annotation" : '',
        "category":'1',
        "description":'',
        "eventDate":'',
        "paid":'',
        "participantLimit":'',
        "location":'',
      },
      innRegexpMask: /[^0-9]/g,
      sending: false,
    }
  },
  mounted() {
    let session = this.$ewmsession.load(this);

    if (session) {
      this.session = session;
    }

    if (this.eventId && this.eventId !== 'undefined') {
      this.loadData();
    }
  },
  methods: {
    loadData() {
      let a = this.$ewmapi.getEvent(this.eventId);

      a.then(response => {
        if (response.success) {
          console.log(response);
          this.eventData = response.data;
          this.emitter.emit("eventNameLoaded", this.eventData.title);
        } else {
          console.log(response);
          if (response.status === 401) {
            this.$router.push('/login');
          } else {
            this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
          }
        }
      });
    },
    save() {
      console.log("saving:");
      console.log(this.eventData);

      let a = this.$ewmapi.saveEvent(this.eventData);

      a.then(response => {
        if (response.success) {
          this.$nextTick(() => {
            this.eventData.status = response.data.status;
          });
        } else {
          console.log(response);
          if (response.status === 401) {
            this.$router.push('/login');
          } else {
            this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
          }
        }
      });
    }

  }
}
</script>

<style scoped>

</style>