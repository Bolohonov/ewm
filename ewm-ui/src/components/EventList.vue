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
  props: ['externalRef', 'listTitle', 'events', 'componentVisibilityMenu'],
  emits: ['createEvent'],
  data() {
    return {
        headers: [
          { label: 'Заголовок', key: 'title' },
          { label: 'Аннотация', key: 'annotation' },
          { label: 'Дата события', key: 'eventDate' },
          { label: 'Рейтинг события', key: 'rating' },
        ],
      componentVisibility: {
        menu: true
      }
    }
  },
  created() {
    if (!this.$ewmapi.isAuthorized()) {
      console.log('Not authorized!');
      this.$router.push('/login');
    }

    // this.updateList();
  },
  methods: {
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