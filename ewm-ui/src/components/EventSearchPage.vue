<template>
  <w-app>
    <w-flex xs6>
      <LeftMenu></LeftMenu>
      <div class="grow xl4 ml4 mt4" >
        <w-select
            class="mt4"
            :items="categories"
            :item-label-key="categoriesKey"
            :item-value-key="categoriesValue"
            multiple
            v-model="selectCategory"
            label="Категория"
            placeholder="Выберите категории для поиска">
        </w-select>

        <VueDatePicker :dark=true class="dp__theme_dark"
                       v-model="selectDateRange"
                       range />

        <w-button class="xs8 pa4 mb4" v-on:click="onSendSearchRequestClick">Искать</w-button>
        <EventList ref="eventList" external-ref="eventList"
                   list-title="Результаты поиска"
                   :events="eventList"
                   :component-visibility-menu="false"
                   v-show="componentVisibility.eventList" ></EventList>

      </div>


    </w-flex>
  </w-app>
</template>

<script>

import LeftMenu from "@/components/LeftMenu.vue";
import VueDatePicker from "@vuepic/vue-datepicker";
import EventList from "@/components/EventList.vue";

export default {
  name: 'EventAddPage',
  components: {
    EventList,
    LeftMenu,
    VueDatePicker
  },
  data() {
    return {
      componentVisibility: {
        eventList: false
      },
      categories: [
        {
          id: '',
          name: ''
        }
      ],
      categoriesKey: 'name',
      categoriesValue: 'id',
      selectCategory: [],
      selectDateRange: [],
      eventSearchData: {
        "categories": [],
        "startDate": '',
        "endDate": '',
      },
      eventList: []
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
    onSendSearchRequestClick() {
      let username = this.$ewmapi.getCurrentUserName();
      this.eventSearchData.categories = this.selectCategory;
      this.eventSearchData.startDate = this.selectDateRange[0].toLocaleString();
      this.eventSearchData.endDate = this.selectDateRange[1].toLocaleString();
      let a = this.$ewmapi.searchEvents(this.eventSearchData, username);

      a.then(response => {
        if (response.success) {
          console.log(response);
          this.eventList = response.data;
        } else {
          console.log('searchList error:');
          console.log(response);
          if (response.status === 401) {
            this.$router.push('/login');
          } else {
            this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
          }
        }
      });

      this.componentVisibility.eventList = true;
    }
  }
}
</script>

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