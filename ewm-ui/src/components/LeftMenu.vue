<template>
    <aside class="xl1">
        <w-toolbar shadow vertical left>
          <div class="xs12 gray--bg"><img alt="Logo" src="../assets/ewm-logo.png" :width="80" :height="80"></div>
          <div class="spacer no-grow mx3"></div>
            <span v-for="item in menuItems" :key="item.id">
                <w-divider color="blue-light6" class="my6"></w-divider>
                <a href="#" :id="`left-menu-${item.id}`" v-on:click="item.action" class="blue">{{ item.title }} </a>
            </span>
        </w-toolbar>
    </aside>
</template>

<script>
    export default {
        name: "LeftMenu",
        data() {
            return {
                menuItems: [
                    {id: 0, title: 'Мои события', action: this.myRequestHandler},
                    {id: 0, title: 'Все события', action: this.allRequestsHandler},
                    {id: 0, title: 'Создать событие', action: this.createRequestHandler},
                    {id: 0, title: 'Выйти из системы', action: this.logoutHandler},
                ]
            }
        },
        methods: {
            myRequestHandler(e) {
                e.preventDefault();
                this.$router.push('/');
            },
            allRequestsHandler(e) {
                e.preventDefault();
                this.$router.push('/');
            },
            createRequestHandler(e) {
                e.preventDefault();
                let a = this.$ewmapi.createEvent();

                a.then(response => {
                    if (response.success) {
                        this.$waveui.notify({message:'Черновик события сохранен', timeout:3000, type:'info'});
                        let eventId = parseInt(response.data.id);
                        this.$router.push('/event/' + eventId);
                    } else {
                        console.log(response);
                        if (response.status === 401) {
                            this.$router.push('/login');
                        } else {
                            this.$waveui.notify({message:'Ошибка сервера', timeout:3000, type:'error'});
                        }
                    }
                });
            },
            logoutHandler(e) {
                e.preventDefault();
                this.$router.push('/login');
            }
        }
    }
</script>

<style scoped>

</style>