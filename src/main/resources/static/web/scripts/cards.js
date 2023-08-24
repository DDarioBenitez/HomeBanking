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
            axios.get("http://localhost:8080/api/clients")
                .then(data => {
                    this.client = data.data
                    this.cards = data.data.cards;
                    console.log(this.cards);
                })
                .catch(error => console.log("ERROR"))
        },
        setNumber(card) {
            const number = card.number;
            const aux = [];
            for (let i = 0; i < number.length; i += 4) {
                aux.push(number.substring(i, i + 4));
            }
            return aux.join(" ")
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
        }
    }
})
cards.mount("#app")