const { createApp } = Vue;

const cards = createApp({
    data() {
        return {
            client: {},
            cards: [],
            createCard: false,
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
                    this.cards = data.data.cards;
                    console.log(this.cards);
                })
                .catch(error => console.log("ERROR"))
        },
        setNumber(card) {
            return card.number;
        },
        setColor(card) {
            if (card.color == "TITANIUM") {
                return "tarjet-titanium"
            } else if (card.color == "GOLD") {
                return "tarjet-gold"
            } else {
                return "tarjet-silver"
            }
        },
        logout() {
            axios.post("http://localhost:8080/api/logout")
                .then(response => {
                    window.location.href = "http://localhost:8080/web/pages/public/login.html"
                })
                .catch(err => console.log(err))
        },
        createdCard() {
            window.location.href = "http://localhost:8080/web/pages/client/create-cards.html"
        }
    }
})
cards.mount("#app")