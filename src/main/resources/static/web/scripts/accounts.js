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
            axios.get("http://localhost:8080/api/clients/current")
                .then(data => {
                    this.client = data.data
                    console.log(this.client);
                    this.accounts = data.data.accounts
                    this.loans = data.data.loans
                    console.log(this.loans);
                })
                .catch(error => console.log("ERROR"))
        },
        logout() {
            axios.post("http://localhost:8080/api/logout")
                .then(response => {
                    window.location.href = "http://localhost:8080/web/pages/public/login.html"
                })
                .catch(err => console.log(err))
        }
    }
})

cards.mount("#app")