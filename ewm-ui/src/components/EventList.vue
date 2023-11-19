<template>
  <w-app>
    <w-flex no-grow>
      <div class="skipper xl1"></div>
      <w-toolbar color="blue-light6">
<!--        <div class="title3">{{title}}</div>-->
        <div class="spacer"></div>
      </w-toolbar>
    </w-flex>
    <w-flex>
      <LeftMenu></LeftMenu>
      <w-flex class="column pa3 align-start">
        <div class="grow">
          <div class="title3 mb6 blue-dark3 size--xl">{{title}}</div>
<!--          <w-button-->
<!--              class="mr2 mb1"-->
<!--              @click="table.activeFilter = 0"-->
<!--              round-->
<!--              :outline="table.activeFilter !== 0">-->
<!--            No filter-->
<!--          </w-button>-->
          <w-table :headers="headers"
                   :items="events"
                   fixed-headers
                   @row-select="onEventRowClick"
                   :selectable-rows=1 class="align-center blue-dark3"></w-table>
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
  props: ['externalRef'],
  emits: ['createEvent'],
  data() {
    return {
      title: "Список всех событий",
        headers: [
          { label: 'Заголовок', key: 'title' },
          { label: 'Аннотация', key: 'annotation' },
          { label: 'Дата события', key: 'eventDate' },
          { label: 'Рейтинг события', key: 'rating' },
        ],
        events: []
    }
  },
  created() {
    if (!this.$ewmapi.isAuthorized()) {
      console.log('Not authorized!');
      this.$router.push('/login');
    }

    this.title = "Список всех событий";

    this.updateList();
  },
  methods: {
    onCreateEventClick() {
      this.$emit('createEvent');
    },
    updateList() {
      let a = this.$ewmapi.getEvents();

      a.then(response => {
        if (response.success) {
          console.log(response);
          this.events = response.data;
        } else {
          console.log('updateList error:');
          console.log(response);
          if (response.status === 401) {
            this.$router.push('/login');
          } else {
            this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
          }
        }
      });
    },
    onEventRowClick(item) {
      this.$router.push('/events/' + item.item.id);
    }
  }
}
</script>

<style scoped>

</style>