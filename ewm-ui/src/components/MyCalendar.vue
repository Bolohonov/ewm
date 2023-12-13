<template>
  <w-app>
    <w-flex no-grow>
      <div class="skipper xl1"></div>
    </w-flex>
    <w-flex>
      <LeftMenu> </LeftMenu>
      <w-flex class="def">
        <vue-cal
            class="button"
            :disable-views="['years', 'year']"
            :events="events"
            hide-weekends
            locale="ru"
            style="height: 650px"
            vue-cal>
        </vue-cal>
      </w-flex>
    </w-flex>
  </w-app>
</template>

<script>
import LeftMenu from "./LeftMenu";
import VueCal from 'vue-cal'
import 'vue-cal/dist/vuecal.css'

export default {
  name: "MyCalendar",
  components: {LeftMenu, VueCal},
  // props: ['externalRef', 'listTitle', 'searchList', 'componentVisibilityMenu'],
  // emits: ['createEvent'],
  data() {
    return {
      events: [],
    }
  },
  created() {
    if (!this.$ewmapi.isAuthorized()) {
      console.log('Not authorized!');
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.def{background-color: #476c96
}
.button{text-decoration-color: #a9cff3
}
</style>