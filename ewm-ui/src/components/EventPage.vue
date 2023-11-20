<template>
  <w-app>
    <w-flex>
      <LeftMenu></LeftMenu>
      <EventInfo v-show="componentVisibility.eventInfo"
                 :event-id="eventId"
                 :event-data="eventData"
                 :is-event-initiator="isEventInitiator" ></EventInfo>
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
      isEventInitiator: false,
      title: 'Событие',
      menuItems: [
        { id: 'eventInfo', title: "Информация о событии", action: this.menuClick },
      ],
      componentVisibility: {
        eventInfo: true,
        eventAdd: false
      },
      userId: null,
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
    let username = this.$ewmapi.getCurrentUserName();

    if (username !== 'undefined') {
      this.$ewmapi.isEventInitiator(this.eventId, username)
          .then(response => {
            if (response.success) {
              console.log(response);
              this.isEventInitiator = response.data;
        }
      });
    }

  }
}
</script>
