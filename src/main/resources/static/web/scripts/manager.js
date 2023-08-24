const { createApp } = Vue;

const clients = createApp({
    data() {
        return {
            clients: [],
            firstName: "",
            lastName: "",
            email: "",
            json: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients")
                .then(data => {
                    console.log(data);
                    this.clients = data.data
                    console.log(this.clients);
                    this.json = data.data;
                })
                .catch(error => console.log("ERROR"))
        },
        send() {
            axios.post("http://localhost:8080/clients", {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email
            })
                .then(data => this.loadData())
                .catch(error => console.log("ERROR"))
        }
    }
})
clients.mount("#clients")