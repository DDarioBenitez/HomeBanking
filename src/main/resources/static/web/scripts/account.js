const { createApp } = Vue;


const transactions = createApp({
    data() {
        return {
            client: {},
            transactions: [],
            account: [],
            searchParam: ""
        }
    },
    created() {
        this.loadClient()
        this.loadTransactions()
    },
    methods: {
        loadClient() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(data => {
                    this.client = data.data
                    console.log(this.client);
                    console.log(this.client.accounts.find(acc => acc.number == "VIN001"));
                })
                .catch(error => console.log("ERROR"))
        },
        loadTransactions() {
            let accountSelect = location.search;
            console.log(accountSelect);
            let accountIdSearch = new URLSearchParams(accountSelect)
            console.log(accountIdSearch);
            let accountId = accountIdSearch.get('id')
            console.log(accountId);
            axios.get("http://localhost:8080/api/accounts/" + accountId)
                .then(response => {
                    this.account = response.data
                    this.transactions = [...response.data.transactions];
                    this.transactions = this.transactions.sort((a, b) => b.id - a.id);
                    console.log(this.transactions.sort((a, b) => b.id - a.id));
                    console.log(response.data);

                    console.log(this.account);
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

transactions.mount("#app")