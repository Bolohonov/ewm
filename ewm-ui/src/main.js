import { createApp } from 'vue'
// import Vuex from 'vuex'
import App from './App.vue'
import WaveUI from 'wave-ui'
import 'wave-ui/dist/wave-ui.css'
import { createRouter, createWebHashHistory } from "vue-router";
import mitt from "mitt";

const emitter = mitt();

import StartPage from "@/components/StartPage";
import LoginPage from "@/components/LoginPage";

import ewmapi from "@/plugins/ewmapi";
import ewmsession from "@/plugins/ewmsession";
import EventPage from "@/components/EventPage";

const app = createApp(App);

const routes = [
    { path: '/', name: "Main", component: StartPage },
    { path: '/events/:id', name: "Event", component: EventPage },
    { path: '/login', name: "Login", component:  LoginPage}
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

app.use(router);
app.use(ewmapi);
app.use(ewmsession);
// app.use(Vuex);
app.use(WaveUI, {
    theme: 'dark'
});

app.component("event-page", EventPage);

// new WaveUI(app, {});

app.config.globalProperties.emitter = emitter;

app.mount("#app");


