<template>
  <w-app>
    <w-flex>
      <LeftMenu></LeftMenu>
      <EventInfo v-show="componentVisibility.eventInfo"
                 :event-id="eventId"
                 :event-data="eventData"></EventInfo>
    </w-flex>
  </w-app>
</template>

<script>
import EventInfo from "./EventInfo";
import LeftMenu from "./LeftMenu";
export default {
  name: "EventPage",
  components: {
    LeftMenu,
    EventInfo,
  },
  data() {
    return {
      eventId: null,
      title: 'Событие',
      menuItems: [
        { id: 'eventInfo', title: "Информация о событии", action: this.menuClick },
      ],
      componentVisibility: {
        eventInfo: true,
      },
      eventData: {}
    }
  },
  created() {
    this.$watch(
        () => this.$route.params,
        (n) => {
          this.eventId = n;
        }
    );
    this.eventId = this.$route.params.id;
  },
}
</script>
