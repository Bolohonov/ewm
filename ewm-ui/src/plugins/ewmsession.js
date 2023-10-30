const ewmsession = {
    save(ref) {
        const name = ref.$options.name;
        let data = ref.$data.session;
        localStorage.setItem(name, JSON.stringify(data));
    },
    load(ref) {
        const name = ref.$options.name;
        let data = null;
        try {
            data = JSON.parse(localStorage.getItem(name));
        } catch (e) {
            console.log("Cannot deserialize local storage JSON for: "+name+", deleting item");
            localStorage.removeItem(name);
        }
        return data;
    },
    test(o) {
        let data = {};
        for (let key in o.$data) {
            data[key] = o.$data[key];
        }
        console.log(data);
    }
};

export default {
    install: (app) => {
        app.config.globalProperties.$ewmsession = ewmsession;
    }
}