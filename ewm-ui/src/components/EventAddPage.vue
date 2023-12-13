<template>
  <w-app>
    <w-flex xs6>
      <LeftMenu></LeftMenu>
      <div class="grow xl4 ml4 mt4" >
        <w-card column class="align-left bdrs4" shadow title="Описание события"
                title-class="blue-dark3 bdrs4 title3">
          <w-form>
            <w-input v-model="eventData.title"
                     class="mb4"
                     shadow
                     maxlength="50"
                     :validators="[validators.required]"
                     label="Название"
                     placeholder="Название (максимум 50 знаков)"></w-input>

            <w-input v-model="eventData.annotation"
                     class="mb4"
                     shadow
                     maxlength="150"
                     :validators="[validators.required]"
                     label="Аннотация"
                     placeholder="Аннотация (максимум 150 знаков)"></w-input>

            <w-input v-model="eventData.description"
                     class="mb4"
                     shadow
                     maxlength="500"
                     :validators="[validators.required]"
                     label="Описание"
                     placeholder="Описание (максимум 500 знаков)"></w-input>

            <w-select
                class="mt4"
                :validators="[validators.required]"
                :items="categories"
                :item-label-key="categoriesKey"
                :item-value-key="categoriesValue"
                v-model="selectCategory"
                label="Категория"
                placeholder="Выберите категорию">
            </w-select>

          </w-form>
        </w-card>


        <w-card column class="align-left bdrs4" shadow title="Дата и время события"
                title-class="blue-dark3 bdrs4 title3">
          <VueDatePicker :dark=true
                         class="dp__theme_dark"
                         locale="ru"
                         @update:model-value="handleDate = true"
                         v-model="selectDate">
          </VueDatePicker>
        </w-card>

        <w-card column class="align-left bdrs4" shadow title="Параметры события"
                title-class="blue-dark3 bdrs4 title3">
          <w-form>
            <w-flex class="align-center mb4">
              <w-checkbox v-model="eventData.paid">Требуется оплата участия</w-checkbox>
            </w-flex>

            <w-input v-model="eventData.participantLimit"
                     class="mb4"
                     shadow
                     :validators="[validators.required]"
                     label="Максимальное число участников"
            type="number"></w-input>

          </w-form>
        </w-card>

          <w-flex wrap>
            <w-button class="px4" v-on:click="onClickEventDuration">
              Установить продолжительность
            </w-button>
            <w-divider
                class="mx6"
                vertical
                color="white">
            </w-divider>
            <div class="ml3">
              Дата и время окончания: {{this.dateFromDuration.toLocaleString()}}
            </div>

          </w-flex>
          <w-dialog
              v-model="dialog.show"
              :fullscreen="dialog.fullscreen"
              :width="dialog.width"
              :persistent=true
              title-class="blue-dark3">
            <template #title>
              <w-icon class="mr2">mdi mdi-tune</w-icon>
              Задать продолжительность
            </template>
              <w-slider
                  class="mt4"
                  v-model="durationMinutes"
                  thumb-label="droplet"
                  :step="1"
                  :min="0"
                  :max="60">
              </w-slider>
              <w-slider
                  class="mt12"
                  v-model="durationHours"
                  thumb-label="droplet"
                  :step="1"
                  :min="0"
                  :max="24">
              </w-slider>
              <w-slider
                  class="mt12"
                  v-model="durationDays"
                  thumb-label="droplet"
                  :step="1"
                  :min="0"
                  :max="30">
              </w-slider>

            <w-checkbox
                class="d-flex mt2"
                v-model="dialog.fullscreen"
                label="Fullscreen">
            </w-checkbox>

            <template #actions>
              <div class="spacer" />
              <w-button
                  v-on:click="onSaveEventDuration"
                  bg-color="error">
                Close
              </w-button>
            </template>

<!--            <template #actions>-->
<!--              <div class="spacer" />-->
<!--              <w-button v-model="onSaveEventDuration" >Закрыть</w-button>-->
<!--            </template>-->

          </w-dialog>

        <w-card column class="align-left bdrs4" shadow title="Укажите местоположение"
                title-class="blue-dark3 bdrs4 title3">
        </w-card>

        <w-button class="xs8 pa4 mb4" v-on:click="onSaveEventClick">Сохранить</w-button>
      </div>


    </w-flex>
  </w-app>
</template>

<script>
import LeftMenu from "./LeftMenu";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

export default {
  name: 'EventAddPage',
  components: {
    LeftMenu,
    VueDatePicker
  },
  data() {
    return {
      formValid: null,
      // categories:[],
      categories: [
      ],
      categoriesKey: 'name',
      categoriesValue: 'id',
      selectCategory: {
        id: 0,
        name: ''
      },
      selectDate: new Date(),
      dateFromDuration: '',
      handleDate: false,
      validators: {
        required: value => !!value || 'Обязательное поле',
        requiredFile: array => array.length > 0 || 'Обязательное поле'
      },
      // session: {
      // },
      eventData: {
        "title" : '',
        "annotation": '',
        "description": '',
        "category": 0,
        "paid": false,
        "eventDate": '',
        "eventEndDate":'',
        "participantLimit": 0,
        "location": {
          "lat": 0.0,
          "lon": 0.0
        },
      },
      files:[],
      innRegexpMask: /[^0-9]/g,
      dialog: {
        show: false,
        fullscreen: false,
        width: 550,
      },
      durationMinutes: 0,
      durationHours: 0,
      durationDays: 0,
    }
  },
  created() {
    let a = this.$ewmapi.getCategories();

    a.then(response => {
      if (response.success) {
        console.log(response);
        this.categories = response.data;
      } else {
        console.log('categories update error:');
        console.log(response);
        if (response.status === 401) {
          this.$router.push('/login');
        } else {
          this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
        }
      }
    });

  },
  methods: {
    onClickEventDuration() {
      if (this.handleDate) {
        this.dialog.show = true
      } else {
        this.$waveui.notify(
            {
              message:'Установите дату события',
              bgColor: "error",
              transition: "transition-toggle",
              shadow: true,
              top: true,
              timeout:3000,
              type:'error'});
      }

    },
    onSaveEventDuration() {
      let date = new Date(this.selectDate);
      let a = date.getTime();
      a = a + this.durationMinutes * 60 * 1000;
      a = a + this.durationHours * 60 * 60 * 1000;
      a = a + this.durationDays * 24 * 60 * 60 * 1000
      this.dateFromDuration = new Date(a);
      this.dialog.show = false;
    },
    onSaveEventClick() {
      if (this.selectCategory.id === 0) {
        this.$waveui.notify(
            {
              message:'Выберите категорию события',
              bgColor: "error",
              transition: "transition-toggle",
              shadow: true,
              top: true,
              timeout:3000,
              type:'error'});
      } else {
        this.eventData.category = this.selectCategory;
        this.eventData.eventDate = new Date(this.selectDate);
        this.eventData.eventEndDate = new Date(this.dateFromDuration);
        let username = this.$ewmapi.getCurrentUserName();

        let a = this.$ewmapi.createEvent(this.eventData, username)

        a.then(response => {
          if (response.success) {
            this.$router.push('/');
          } else {
            this.$waveui.notify({message:'Ошибка входа', timeout:3000, type:'error'});
          }
        });
      }
      }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.dp__theme_dark {
  --dp-background-color: #212121;
  --dp-text-color: #fff;
  --dp-hover-color: #484848;
  --dp-hover-text-color: #fff;
  --dp-hover-icon-color: #959595;
  --dp-primary-color: #005cb2;
  --dp-primary-disabled-color: #61a8ea;
  --dp-primary-text-color: #fff;
  --dp-secondary-color: #a9a9a9;
  --dp-border-color: #2d2d2d;
  --dp-menu-border-color: #2d2d2d;
  --dp-border-color-hover: #aaaeb7;
  --dp-disabled-color: #737373;
  --dp-disabled-color-text: #d0d0d0;
  --dp-scroll-bar-background: #212121;
  --dp-scroll-bar-color: #484848;
  --dp-success-color: #00701a;
  --dp-success-color-disabled: #428f59;
  --dp-icon-color: #959595;
  --dp-danger-color: #e53935;
  --dp-marker-color: #e53935;
  --dp-tooltip-color: #3e3e3e;
  --dp-highlight-color: rgb(0 92 178 / 20%);
  --dp-range-between-dates-background-color: var(--dp-hover-color, #484848);
  --dp-range-between-dates-text-color: var(--dp-hover-text-color, #fff);
  --dp-range-between-border-color: var(--dp-hover-color, #fff);
}
</style>
