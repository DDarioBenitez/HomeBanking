const { createApp } = Vue;

const cards = createApp({
    data() {
        return {
            client: {},
            cards: [],
            createCard: false,
            date: new Date().toISOString(),
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
            console.log(card.truDate);
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
            Swal.fire({
                title: 'Are you sure?',
                html: `
                <select class="me-2" v-model="type" name="type" id="type">
                  <option value="">Type</option>
                  <option value="CCREDIT">CREDIT</option>
                  <option value="CDEBIT">DEBIT</option>
                </select>
                <select v-model="color" name="color" id="color">
                  <option value="">Color</option>
                  <option value="GOLD">GOLD</option>
                  <option value="SILVER">SILVER</option>
                  <option value="TITANIUM">TITANIUM</option>
                </select>
              `,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.value) {
                    // Acceder a los valores seleccionados en las variables type y color
                    const typeSelect = document.getElementById("type");
                    const colorSelect = document.getElementById("color");
                    const type = typeSelect.value;
                    const color = colorSelect.value;
                    axios.post('http://localhost:8080/api/clients/current/cards', `type=${type}&color=${color}`)
                        .then(response => {
                            window.location.reload();
                        })
                        .catch(err => {
                            console.log(err);
                            Swal.fire({
                                icon: 'error',
                                text: `${err.response.data}`,
                                showConfirmButton: false,
                            })
                            setTimeout(() => { }, 1000)
                        })
                } else {
                    Swal.fire({
                        icon: 'info',
                        text: 'Operation cancelled',
                        showConfirmButton: false,
                        timer: 1000
                    });
                }
            });
        }

        ,
        deleteCard(card) {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete("http://localhost:8080/api/clients/current/cards", {
                        params: {
                            active: false,
                            numberCard: card.number
                        }
                    })
                        .then(response => {
                            window.location.reload();
                        })
                        .catch(err => {
                            console.log(err);
                            Swal.fire({
                                icon: 'error',
                                text: `${err.response.data}`,
                                showConfirmButton: false,
                            });
                            setTimeout(() => { }, 1000);
                        });
                } else {
                    Swal.fire({
                        icon: 'info',
                        text: 'Operation cancelled',
                        showConfirmButton: false,
                        timer: 1000
                    });
                }
            })
                .catch(err => console.log(err));
        }
    }
})
cards.mount("#app")