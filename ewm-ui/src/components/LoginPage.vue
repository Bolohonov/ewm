<template>
  <w-app>
    <w-flex justify-center>
      <div class="xs3 gap3">
        <w-flex align-center no-grow wrap column>
          <div class="sh1">
            <div class="xs12 grey-dark6--bg"><img alt="Logo" src="../assets/ewm-logo.png" :width="250" :height="250"></div>
            <w-input placeholder="username" v-model="username" class="xs12 pa4 align-self-center" />
            <w-input placeholder="password" v-model="password" class="xs12 pa4" type='password' />
            <w-button class="xs8 pa4 mb4" v-on:click="onLoginClick">Login</w-button>
          </div>
        </w-flex>
      </div>
    </w-flex>
  </w-app>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      username:'',
      password:'',
    };
  },
  methods: {
    onLoginClick() {
      let a = this.$ewmapi.login(this.username, this.password);

      a.then(response => {
        if (response.success) {
          this.$router.push('/');
        } else {
          this.$waveui.notify({message:'Ошибка входа', timeout:3000, type:'error'});
        }
      });
    },
  }
}
</script>

<style scoped>

</style>