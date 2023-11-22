import axios from "axios";

const BASE_URL = process.env.VUE_APP_BASE_URL;
const API_VERSION = process.env.VUE_APP_API_VERSION;
axios.defaults.withCredentials = true;

const ewmapi = {
    login(username, password) {
        const URL = BASE_URL+'/api/'+API_VERSION+'/auth/signin';
        const loginData = {"username" : username, "password" : password};
        let result = {"success":false, "reason":''};
        const returnPromise = new Promise((resolve) =>
        {
            axios.post(URL, loginData).then(response => {
                localStorage.setItem("token", response.data.token);
                localStorage.setItem("userName", response.data.username);
                localStorage.setItem("userId", response.data.id);
                result.success = true;
                result.reason = response.data.token;
                resolve(result);
            }).catch(error => {
                result.reason = error;
                resolve(result);
            });
        });

        return returnPromise;
    },
    isAuthorized() {
        return localStorage.getItem("token") != null;
    },
    getCurrentUserName() {
        return localStorage.getItem("userName");
    },
    isEventInitiator(eventId, username) {
        const URL = BASE_URL+'/api/'+API_VERSION +'/users/' + username + '/events/' + eventId + '/initiator/';

        let result = {"success":false, "reason":'', data:'', status: ''};

        if (localStorage.getItem("token") == null) {
            result.reason("Not authorized");
            return new Promise(resolve => {
                resolve(result);
            });
        }
        return this.get(URL, result);
    },
    logout() {
        const URL = BASE_URL+'/api/'+API_VERSION+'/auth/signout';
        let result = {"success":false, "reason":''};
        return new Promise(resolve => {
            axios.post(URL).then(response => {
                localStorage.removeItem("token");
                result.success = true;
                result.reason = response.data;
                resolve(result)
            })
        })
    },
    events() {
        const URL = BASE_URL+'/api/'+API_VERSION+'/events/all';
        let result = {"success":false, "reason":'', data:''};

        if (localStorage.getItem("token") == null) {
            result.reason("Not authorized");
            return new Promise(resolve => {
                resolve(result);
            });
        }

        return new Promise(resolve => {
            const headers = {'Authorization': 'Bearer '+ localStorage.getItem("token")};
            axios.get(URL, {headers:headers}).then(response => {
                result.success = true;
                result.data = response.data;
                resolve(result);
            }).catch(error => {
                result.reason = error;
                resolve(result);
            });
        });
    },
    createEvent(eventData, userName) {
        console.log(eventData)
        console.log(userName)
        const URL = BASE_URL+'/api/'+API_VERSION+'/users/' + userName + '/events/add';
        let result = {"success":false, "reason":'', data:'', status: ''};
        let data = eventData;

        if (localStorage.getItem("token") == null) {
            result.reason("Not authorized");
            return new Promise(resolve => {
                resolve(result);
            });
        }

        return this.post(URL, data, result);
    },
    getEvents() {
        const URL = BASE_URL+'/api/'+API_VERSION+'/events/all2';

        let result = {"success":false, "reason":'', data:'', status: ''};

        if (localStorage.getItem("token") == null) {
            result.reason("Not authorized");
            return new Promise(resolve => {
                resolve(result);
            });
        }

        return new Promise(resolve => {
            const headers = {'Authorization': 'Bearer '+ localStorage.getItem("token")};
            axios.get(URL, {headers:headers}).then(response => {
                result.success = true;
                result.data = response.data;
                resolve(result);
            }).catch(error => {
                result.reason = error;
                resolve(result);
            });
        });
    },
    getCategories() {
        const URL = BASE_URL+'/api/'+API_VERSION+'/categories';

        let result = {"success":false, "reason":'', data:'', status: ''};

        if (localStorage.getItem("token") == null) {
            result.reason("Not authorized");
            return new Promise(resolve => {
                resolve(result);
            });
        }

        return new Promise(resolve => {
            const headers = {'Authorization': 'Bearer '+ localStorage.getItem("token")};
            axios.get(URL, {headers:headers}).then(response => {
                result.success = true;
                result.data = response.data;
                resolve(result);
            }).catch(error => {
                result.reason = error;
                resolve(result);
            });
        });
    },
    getEvent(id) {
        const URL = BASE_URL+'/api/'+API_VERSION+'/events/'+id;

        let result = {"success":false, "reason":'', data:'', status: ''};

        if (localStorage.getItem("token") == null) {
            result.reason("Not authorized");
            return new Promise(resolve => {
                resolve(result);
            });
        }

        return this.get(URL, result);
    },
    get(url, result) {
        return new Promise(resolve => {
            const headers = {'Authorization': 'Bearer '+ localStorage.getItem("token")};
            axios.get(url, {headers:headers}).then(response => {
                result.success = true;
                result.data = response.data;
                resolve(result);
            }).catch(error => {
                console.log(error);
                result.reason = error;
                result.status = error.response ? error.response.status : '';
                resolve(result);
            });
        });
    },
    post(url, data, result) {
        return new Promise(resolve => {
            const headers = {'Authorization': 'Bearer '+ localStorage.getItem("token")};
            axios.post(url, data, {headers:headers}).then(response => {
                result.success = true;
                result.data = response.data;
                resolve(result);
            }).catch(error => {
                result.reason = error;
                result.status = error.response.status;
                resolve(result);
            });
        });
    }
};

export default {
    install: (app) => {
        app.config.globalProperties.$ewmapi = ewmapi;
    }
}