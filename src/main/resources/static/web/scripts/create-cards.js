const { createApp } = Vue;

const newCard = createApp({
    data() {
        return {
            type: "",
            color: "",
            cards: []
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        createCard() {
            console.log(this.type);
            axios.post('http://localhost:8080/api/clients/current/cards', `type=${this.type}&color=${this.color}`)
                .then(response => {
                    window.alert(`${response.data}`)
                    window.location.href = "http://localhost:8080/web/pages/client/cards.html"
                })
                .catch(err => {
                    console.log(err);
                    if (this.cards.length >= 6) {
                        window.alert(`${err.response.data}+try again`)
                    } else {

                        window.alert(`Maximum number of cards reached`)
                    }
                })
        },
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(data => {
                    this.client = data.data
                    this.cards = data.data.cards;
                    console.log(this.cards);
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

newCard.mount("#app")