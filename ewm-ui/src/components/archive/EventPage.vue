<template>
  <w-app>
    <w-overlay v-model="showOverlay"
               bg-color="rgba(35, 71, 129, 0.4)" :persistent="true"></w-overlay>
    <w-flex no-grow>
      <div class="skipper xl1"></div>
      <w-toolbar color="blue-dark3">
        <div class="title3">{{title}}</div>
        <div class="spacer"></div>
        <span v-for="item in menuItems" :key="item.id">
                    <a href="#" class="ml2" :id="`left-menu-${item.id}`" v-on:click="item.action">{{ item.title }} </a>
                    <span class="ml2">|</span>
                </span>
      </w-toolbar>
    </w-flex>

    <w-flex>
      <LeftMenu></LeftMenu>
      <EventInfo v-show="componentVisibility.eventInfo"
                 @event-data-updated="onEventInfoUpdate"
                 @sending-event="onSendingEvent"
                 @event-complete="onEventComplete"
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
  emits: ["eventDataUpdated", "sendingEvent"],
  components: {
    LeftMenu,
    EventInfo,
  },
  data() {
    return {
      eventId: null,
      title: 'Новое событие',
      menuItems: [
        { id: 'eventInfo', title: "Информация о событии", action: this.menuClick },
      ],
      menuActions: {
        indexesMenuClick: this.indexesMenuClick
      },
      session: {
        inn: '',
        indexTables: [],
        dataSent: false,
      },
      componentVisibility: {
        eventInfo: true,
      },
      showOverlay: false,
      dataItems: {},
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
  mounted() {
    this.emitter.on("eventNameLoaded", this.updateTitle);
  },
  methods: {
    menuClick(e) {
      e.preventDefault();
      let id = e.target.id.split('-')[2];
      for (let i in this.componentVisibility) {
        this.componentVisibility[i] = i === id;
      }
    },
    onEventInfoUpdate() {
      this.session.dataSent = true;
    },
    onSendingEvent() {
      this.showOverlay = true;
    },
    onEventComplete() {
      this.showOverlay = false;
    },
    updateTitle(data) {
      if (data && data.length > 0) {
        this.title = data;
      }
    },
  }
}
</script>

<style scoped>

</style>
