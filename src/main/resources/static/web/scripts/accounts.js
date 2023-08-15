const { createApp } = Vue;

const cards = createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
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
                    this.accounts = data.data.accounts
                    this.loans = data.data.loans
                    console.log(this.loans);
                })
                .catch(error => console.log("ERROR"))
        },
    }
})

cards.mount("#app")