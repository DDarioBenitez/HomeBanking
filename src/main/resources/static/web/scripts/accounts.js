const { createApp } = Vue;

const cards = createApp({
    data() {
        return {
            client: [],
            accounts: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/1")
                .then(data => {
                    this.client = data.data
                    console.log(this.client);
                    console.log(this.theme);
                    this.accounts = data.data.accounts
                })
                .catch(error => console.log("ERROR"))
        },
    }
})

cards.mount("#app")