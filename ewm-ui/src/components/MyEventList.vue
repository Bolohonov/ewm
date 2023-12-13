<template>
  <w-app>
    <w-flex no-grow>
      <div class="skipper xl1"></div>
    </w-flex>
    <w-flex>
      <LeftMenu v-show="componentVisibilityMenu"> </LeftMenu>
      <w-flex class="column pa3 align-start">
        <div class="grow">
          <div class="title3 mb6 blue-dark3 size--xl">{{listTitle}}</div>
          <w-button
              class="mr2 mb1"
              @click="table.activeFilter = 0"
              round
              :outline="table.activeFilter !== 0">
            Без фильтра
          </w-button>

          <w-button
              class="mr2 mb1"
              @click="table.activeFilter = 1"
              round
              :outline="table.activeFilter !== 1">
            Я - инициатор
          </w-button>

          <w-button
              class="mr2 mb1"
              @click="table.activeFilter = 2"
              round
              :outline="table.activeFilter !== 2">
            ID >= 10
          </w-button>
          <w-table :headers="table.headers"
                   :items="table.events"
                   fixed-headers
                   @row-select="onEventRowClick"
                   :selectable-rows=1
                   class="align-center blue-dark3"
                   :filter="table.filters[table.activeFilter]"
                   fixed-footer
                   :pagination="table.pagination">
          </w-table>
        </div>
      </w-flex>
    </w-flex>
  </w-app>
</template>

<script>
import LeftMenu from "./LeftMenu";
export default {
  name: "EventList",
  components: {LeftMenu},
  props: ['externalRef', 'listTitle', 'searchList', 'componentVisibilityMenu'],
  emits: ['createEvent'],
  data() {
    return {
      currentUserName:'',
      table: {
        headers: [
          { label: 'Заголовок', key: 'title' },
          { label: 'Аннотация', key: 'annotation' },
          { label: 'Категория', key: 'categoryName' },
          { label: 'Дата события', key: 'eventDate' },
          { label: 'Статус', key: 'state' },
          { label: 'Инициатор события', key: 'initiatorName' },
        ],
        events:[],
        // loading: false,
        // pagination: {
        //   itemsPerPage: 50,
        //   total: 500
        // }
        pagination: {
          itemsPerPage: 50,
          total: 300
        },
        filters: [
          null,
          item => item.initiatorName === this.currentUserName,
          item => item.title === '3637356'
        ],
        activeFilter: 0
      },
      componentVisibility: {
        menu: true
      },
    }
  },
  created() {
    if (!this.$ewmapi.isAuthorized()) {
      console.log('Not authorized!');
      this.$router.push('/login');
    }
    this.currentUserName = this.$ewmapi.getCurrentUserName();
    this.uploadEvents()

    console.log(this.currentUserName)
  },
  methods: {
    uploadEvents() {
      let a = this.$ewmapi.getEvents();

      a.then(response => {
        if (response.success) {
          console.log(response);
          this.table.events = response.data;
        } else {
          console.log(response);
          if (response.status === 401) {
            this.$router.push('/login');
          } else {
            this.$waveui.notify({message: 'Ошибка сервера', timeout: 3000, type: 'error'});
          }
        }
      });
    },
    colorClass(state) {
      if ( state in this.colors ) {
        return this.colors[state];
      } else {
        return "";
      }
    },
    onCreateEvent() {
    },
    onEventRowClick(item) {
      this.$router.push('/events/' + item.item.id);
    }
  }
}
</script>

<style scoped>

</style>