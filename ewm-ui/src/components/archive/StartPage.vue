<template>
  <w-app>
    <w-flex no-grow>
      <div class="skipper xl1"></div>
      <w-toolbar shadow vertical absolute left>
        <div class="xs12 gray--bg"><img alt="Logo" src="../assets/ewm-logo.png" :width="80" :height="80"></div>
        <div class="spacer no-grow mx3"></div>
        <a href="#" class="l2-a" v-on:click="activateComponent">
          <span  menutarget="eventPage" class="ml2 pr2 l2-menu-item fill-height">События</span>
        </a>
        <a href="/login" class="l2-a"><span class="ml2">выйти из системы</span></a>
        <div class="skipper xl1"></div>
        <w-divider class="mya"></w-divider>
        <w-divider class="mya"></w-divider>
        <w-button icon="mdi mdi-home" text xl class="pa16"></w-button>
        <w-button icon="mdi mdi-cart" text xl class="pa16"></w-button>
        <w-button icon="mdi mdi-email" text xl class="pa16"></w-button>
        <w-button icon="mdi mdi-chat" text xl class="pa16"></w-button>
      </w-toolbar>
      <div class="skipper xl1"></div>
    </w-flex>

    <w-flex>
      <!--        <div class="skipper xl1"></div>-->
      <main class="xl9">
        <EventPage ref="eventPage" external-ref="eventPage" v-show="componentVisibility.eventPage"></EventPage>
        <EventList ref="eventList" external-ref="eventList"
                   v-show="componentVisibility.eventList" @create-event="onCreateEvent"></EventList>
      </main>
    </w-flex>
  </w-app>
</template>

<script>
import '@mdi/font/css/materialdesignicons.min.css'
import EventPage from "./EventPage";
import EventList from "./EventList";
export default {
  name: "StartPage",
  components: {EventList, EventPage},
  data() {
    return {
      components: {
        eventPage: EventPage,
        eventList: EventList
      },
      componentVisibility: {
        eventPage: false,
        eventList: true
      },
    }
  },
  created() {
    if (!this.$ewmapi.isAuthorized()) {
      console.log('Not authorized!');
      this.$router.push('/login');
    }
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
    onCreateEvent() {
      // this.componentVisibility.requestList = false;
      // this.componentVisibility.requestPage = true;

      let a = this.$ewmapi.createEvent();

      a.then(response => {
        if (response.success) {
          this.$waveui.notify({message:'Черновик события сохранен', timeout:3000, type:'info'});
          let eventId = parseInt(response.data.id);
          this.$router.push('/event/'+eventId);
        } else {
          console.log(response);
          if (response.status === 401) {
            this.$router.push('/login');
          } else {
            this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
          }
        }
      });
    }
  }
}
</script>

<style>

/*.l2-menu-item:hover {*/
/*    background-color: #064475;*/
/*}*/

/*.w-card {*/
/*  margin: auto;*/
/*  max-width: 550px;*/
/*  height: 200px;*/
/*}*/

a:visited {
  color: #064475;
}

</style>