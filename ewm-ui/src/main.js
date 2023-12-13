import { createApp } from 'vue'
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
import EventAddPage from "@/components/EventAddPage"
import EventSearchPage from "@/components/EventSearchPage"
import MyCalendar from "@/components/MyCalendar.vue"

const app = createApp(App);

const routes = [
    { path: '/', name: "Main", component: StartPage },
    { path: '/my', name: "MyEvents", component: StartPage },
    { path: '/events/:id', name: "Event", component: EventPage },
    { path: '/login', name: "Login", component:  LoginPage},
    { path: '/events/create', name: "EventAddPage", component:  EventAddPage},
    { path: '/events/search', name: "EventSearchPage", component:  EventSearchPage},
    { path: '/calendar', name: "MyCalendar", component:  MyCalendar},
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

app.use(router);
app.use(ewmapi);
app.use(ewmsession);
app.use(WaveUI, {
    theme: 'dark',
    // colors: {
    //     primary: '#245ef5',
    //     secondary: '#637fef',
    // }
});

app.component("event-page", EventPage);
app.component("event-add-page", EventAddPage);
app.component("event-search-page", EventSearchPage);
app.component("my-calendar", MyCalendar);

app.config.globalProperties.emitter = emitter;

app.mount("#app");


