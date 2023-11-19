<template>
  <w-flex wrap class="text-center">
    <div class="grow mx1">
        <w-card column align-center justify-start class="align-center bdrs4 xs4 pa1 mt3" shadow title="Название"
                title-class="blue-dark3 bdrs4 title3">
          {{eventData.title}}
        </w-card>
        <w-card column align-center justify-start class="align-center bdrs4 xs4 pa1 mt3" shadow title="Инициатор"
                title-class="blue-dark3 bdrs4 title3">
          {{eventData.initiator.name}}
        </w-card>

      <w-card column align-center justify-start class="align-center bdrs4 xs4 pa1 mt3" shadow title="Рейтинг"
              title-class="blue-dark3 bdrs4 title3">
        <w-rating></w-rating>
      </w-card>
      <w-card v-if="isEventInitiator" column align-center justify-start class="align-center bdrs4 xs4 pa1 mt3" shadow>
        <w-button column justify-start class="lg mt3" shadow> Изменить событие </w-button>
      </w-card>
    </div>

    <div class="grow mx1">
        <w-card justify-center class="align-center bdrs4 pa3 mt3 grow" shadow title="Описание"
                title-class="blue-dark3 bdrs4 title3">
          {{eventData.description}}
        </w-card>
    </div>



<!--    <div class="xs6 pa1">-->
<!--      <w-card shadow title="Информация о событии" title-class="blue-dark3" class="xs12 py3">-->
<!--      </w-card>-->
<!--      <div class="primary&#45;&#45;bg py3">xs6</div>-->
<!--    </div>-->
<!--    <w-card shadow title="Информация о событии" title-class="blue-dark3" class="xs12 pa1">-->
<!--    </w-card>-->

  </w-flex>
</template>

<script>
export default {
  name: "EventInfo",
  props: ["eventId", "isEventInitiator"],
  data() {
    return {
      eventData: {
        'id': '',
        'title': '',
        'description': '',
        'annotation':'',
        'initiator': {
          'name':''
        }
      },
    }
  },
  mounted() {
    let session = this.$ewmsession.load(this);

    if (session) {
      this.session = session;
    }

    if (this.eventId && this.eventId !== 'undefined') {
      this.loadData();
    }
  },
  methods: {
    loadData() {
      let a = this.$ewmapi.getEvent(this.eventId);

      a.then(response => {
        if (response.success) {
          console.log(response);
          this.eventData = response.data;
        } else {
          console.log(response);
          if (response.status === 401) {
            this.$router.push('/login');
          } else {
            this.$waveui.notify({message: 'Ошибка сервера', timeout: 3000, type: 'error'});
          }
        }
      });
    }
  }
}
</script>
