const { createApp } = Vue;

const cards = createApp({
    data() {
        return {
            client: {},
            cards: []
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
                    this.cards = data.data.cards;
                    console.log(this.cards);
                })
                .catch(error => console.log("ERROR"))
        }
    }
})
cards.mount("#app")