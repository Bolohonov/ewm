<template>
  <w-app>
    <w-flex>
      <div class="skipper xl1"></div>
      <main class="xl9">
        <EventPage ref="eventPage" external-ref="eventPage" v-show="componentVisibility.eventPage"></EventPage>
        <EventList ref="eventList" external-ref="eventList"
                   list-title="Список всех событий"
                   :events="events"
                   :component-visibility-menu="true"
                   v-show="componentVisibility.eventList" ></EventList>
        <MyEventList v-show="componentVisibility.myEventList"
                     list-title="События, где я инициатор или участник">
        </MyEventList>
        <EventAddPage ref="eventAddPage" external-ref="eventAddPage" v-show="componentVisibility.eventAdd"
                      @create-event="onCreateEvent"> </EventAddPage>
      </main>
    </w-flex>
  </w-app>
</template>

<script>
import '@mdi/font/css/materialdesignicons.min.css'
import EventPage from "./EventPage";
import EventList from "./EventList";
import MyEventList from "./MyEventList";
import EventAddPage from "./EventAddPage"

export default {
  name: "IndexPage",
  components: {EventPage, EventList, EventAddPage, MyEventList},
  data() {
    return {
      components: {
        eventPage: EventPage,
        eventList: EventList
      },
      componentVisibility: {
        eventPage: false,
        eventList: true,
        myEventList: false,
        eventAddPage: false
      },
      events: []
    }
  },
  created() {
    if (!this.$ewmapi.isAuthorized()) {
      console.log('Not authorized!');
      this.$router.push('/login');
    }
    this.updateList();
  },
  methods: {
    activateComponent(e) {
      const target = e.target.attributes.menutarget.nodeValue;
      for (let i in this.componentVisibility) {
        this.componentVisibility[i] = (i === target);
      }
    },
    onLogoutClick() {
      this.$ewmapi.logout().then(response => {
        if (response.success) {
          this.$router.push('/login');
        } else {
          this.$waveui.notify({message:'Ошибка', timeout:3000, type:'error'});
        }
      });
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
    onCreateEvent() {

      if (!this.$ewmapi.isAuthorized()) {
        console.log('Not authorized!');
        this.$router.push('/login');
      }

      this.componentVisibility.eventPage = false;
      this.componentVisibility.eventList = false;
      this.componentVisibility.eventAddPage = true;
      this.componentVisibility.myEventList = false;

      this.$router.push("/events/create")

      // a.then(response => {
      //   if (response.success) {
      //     this.$waveui.notify({message:'Черновик события сохранен', timeout:3000, type:'info'});
      //     let eventId = parseInt(response.data.id);
      //     this.$router.push('/events/'+eventId);
      //   } else {
      //     console.log(response);
      //     if (response.status === 401) {
      //       this.$router.push('/login');
      //     } else {
      //       this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
      //     }
      //   }
      // });
    }
  }
}
</script>

<style>

/*.l2-menu-item:hover {*/
/*    background-color: #064475;*/
/*}*/

a:visited {
  color: #064475;
}

</style>