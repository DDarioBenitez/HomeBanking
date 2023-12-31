const { createApp } = Vue

const transfer = createApp({
    data() {
        return {
            client: [],
            accounts: {},
            originAcc: "",
            amount: "",
            description: "",
            numberOrginAcc: "",
            numberDestinyAcc: "",
            showAcc: false,
            showInpNumber: false,
        }
    },
    created() {
        console.log(this.originAcc);
        this.numberDestinyAcc
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("https://homebanking-production-0510.up.railway.app/api/clients/current")
                .then(response => {
                    this.client = response.data
                    this.accounts = this.client.accounts
                    console.log(this.client, this.accounts);
                })
        },
        formatBalance(acc) {
            let reset = new Intl.NumberFormat('en-En', { style: 'currency', currency: 'USD' })
            let balanceFormat = reset.format(acc.balance)
            return balanceFormat
        },
        sendTransfer() {
            this.amount = this.amount.toString().replace(",", ".")
            console.log(this.amount);
            Swal.fire({
                icon: 'question',
                title: 'Are you sure make of transaction?',
                confirmButtonText: 'Yes',
                showCloseButton: true,
                showCancelButton: true,
                cancelButtonText: 'No'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("https://homebanking-production-0510.up.railway.app/api/transactions", `amount=${this.amount}&description=${this.description}&originAccount=${this.numberOrginAcc}&destinyAccount=${this.numberDestinyAcc}`)
                        .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'You have requested a new transaction',
                                showConfirmButton: false,
                            });
                            setTimeout(() => {
                                window.location.reload()
                            }, 1000)
                        }).catch((error) => {
                            console.log(error);
                            Swal.fire({
                                icon: 'error',
                                text: 'A problem occurred',
                                showConfirmButton: false,
                            });
                            setTimeout(() => {
                                // Puedes agregar aquí cualquier acción adicional si es necesario
                            }, 1000)
                        })
                } else {
                    Swal.fire({
                        icon: 'info',
                        text: 'Operation cancelled',
                        showConfirmButton: false,
                        timer: 1000
                    });
                }
            })
        },
        logout() {
            axios.post("https://homebanking-production-0510.up.railway.app/api/logout")
                .then(response => {
                    window.location.href = "https://homebanking-production-0510.up.railway.app/web/pages/public/login.html"
                })
                .catch(err => console.log(err))
        },
    },
    computed: {
        showMyAcc() {
            console.log(this.originAcc);
            if (this.originAcc == "my") {
                this.showAcc = true
            } else {
                this.showAcc = false
            }
        },
        showOtherAcc() {
            if (this.originAcc == "other") {
                this.showInpNumber = true
            } else {
                this.showInpNumber = false
            }
        }
    }
})

transfer.mount("#app")
