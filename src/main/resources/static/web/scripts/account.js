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
            axios.get("http://localhost:8080/api/clients/1")
                .then(data => {
                    this.client = data.data
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
                    console.log(this.transactions.sort((a, b) => b.id - a.id));
                    console.log(typeof this.transactions);
                })
                .catch(error => console.log("ERROR"))
        }
    }
})

transactions.mount("#app")