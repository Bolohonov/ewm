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

          </w-form>
        </w-card>

        <w-select
            class="mt4"
            :items="categories"
            :item-label-key="categoriesKey"
            :item-value-key="categoriesValue"
            v-model="categoriesValue"
            label="Категория"
            placeholder="Выберите категорию">
        </w-select>

        <w-card column class="align-left bdrs4" shadow title="Дата и время события"
                title-class="blue-dark3 bdrs4 title3">
          <!--                no-border title="Дата и время" color="primary body text-left"-->
          <VueDatePicker :dark=true class="dp__theme_dark" v-model="selectDate"></VueDatePicker>
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
        {
          id: '',
          name: ''
        }
      ],
      categoriesKey: 'name',
      categoriesValue: 'id',
      selectCategory: {
        id: '',
        name: ''
      },
      selectDate: new Date(),
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
        "participantLimit": 0,
        "location": {
          "lat": 0.0,
          "lon": 0.0
        },
      },
      files:[],
      innRegexpMask: /[^0-9]/g,
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
    onSaveEventClick() {
      this.eventData.category = this.categoriesValue;
      this.eventData.eventDate = new Date(this.selectDate);
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
