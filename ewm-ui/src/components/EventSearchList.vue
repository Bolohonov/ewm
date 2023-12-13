<template>
  <w-app>
    <w-flex no-grow>
      <div class="skipper xl1"></div>
      <w-toolbar color="blue-light6">
        <div class="spacer"></div>
      </w-toolbar>
    </w-flex>
    <w-flex>
      <LeftMenu v-show="componentVisibilityMenu"> </LeftMenu>
      <w-flex class="column pa3 align-start">
        <div class="grow">
          <div class="title3 mb6 blue-dark3 size--xl">{{listTitle}}</div>
          <w-input
              v-model="table.keyword"
              placeholder="Поиск по названию, аннотации и категории..."
              inner-icon-left="wi-search"
              class="mb3">
          </w-input>
          <w-table :headers="table.headers"
                   :items="searchResult"
                   fixed-headers
                   @row-select="onEventRowClick"
                   :selectable-rows=1
                   class="align-center blue-dark3"
                   :filter="table.keywordFilter(table.keyword)"
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
  props: {externalRef: String, listTitle: String, searchResult: {type: Array, required: false},
    componentVisibilityMenu: Boolean},
  emits: ['createEvent'],
  data() {
    return {
      table: {
        headers: [
          { label: 'Заголовок', key: 'title' },
          { label: 'Аннотация', key: 'annotation' },
          { label: 'Категория', key: 'categoryName' },
          { label: 'Дата события', key: 'eventDate' },
          { label: 'Статус', key: 'state' },
          { label: 'Рейтинг события', key: 'rating' },
        ],
        events:[],
        // loading: false,
        // pagination: {
        //   itemsPerPage: 50,
        //   total: 500
        // },
        keyword: '',
        keywordFilter: keyword => item => {
          const allTheColumns = `${item.title} ${item.annotation} ${item.categoryName}`
          return new RegExp(keyword, 'i').test(allTheColumns)
        },
        pagination: {
          itemsPerPage: 50,
          total: 500
        }
      },
      componentVisibility: {
        menu: true,
      },
    }
  },
  created() {
    if (!this.$ewmapi.isAuthorized()) {
      console.log('Not authorized!');
      this.$router.push('/login');
    }
    // if (this.props.searchResult() !== null && this.props.searchResult().length !== 0) {
    //   console.log("TEST")
    // }
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
    // fetch ({ page, start, end, total, itemsPerPage, sorting }) {
    //   this.table.loading = 'header'
    //   let a = this.$ewmapi.getEventsWithPagination(start, itemsPerPage);
    //
    //   a.then(response => {
    //     if (response.success) {
    //       console.log(response);
    //       this.table.events = response.data;
    //     } else {
    //       console.log(response);
    //       if (response.status === 401) {
    //         this.$router.push('/login');
    //       } else {
    //         this.$waveui.notify({message: 'Ошибка сервера', timeout: 3000, type: 'error'});
    //       }
    //     }
    //   });
    //   this.table.loading = false
    //
    //
    //   if (sorting.length) {
    //     const sortKey = sorting[0].substring(1)
    //     this.table.events.sort((a, b) => {
    //       if (sorting[0][0] === '+') return a[sortKey] < b[sortKey] ? -1 : 1
    //       else return a[sortKey] > b[sortKey] ? -1 : 1
    //     })
    //   }
    //   console.log(page)
    //   console.log(start)
    //   console.log(end)
    //   console.log(total)
    //   console.log(itemsPerPage)
    //   console.log(sorting)
    //
    // },
    // colorClass(state) {
    //   if ( state in this.colors ) {
    //     return this.colors[state];
    //   } else {
    //     return "";
    //   }
    // },
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