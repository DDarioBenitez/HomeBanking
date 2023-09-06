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
            axios.get("http://localhost:8080/api/clients/current")
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
            axios.post("http://localhost:8080/api/transactions", `amount=${this.amount.replace(",", ".")}&description=${this.description}&originAccount=${this.numberOrginAcc}&destinyAccount=${this.numberDestinyAcc}`)
                .then(response => {
                    let aux = Swal.fire({
                        title: 'Saved!',
                        icon: 'success',

                    }).then(response => {
                        window.location.reload()
                    })
                    aux()
                })
                .catch(err => {
                    console.error(err)
                    Swale.fire('There was an error, try again', ' ', 'info')
                })
        },
        alert() {
            Swal.fire({
                icon: 'question',
                title: 'Are you sure make of transaction?',
                confirmButtonText: 'Yes',
                showCloseButton: true,
                showCancelButton: true,
                cancelButtonText: 'No'
            })
                .then(response => {
                    if (response.isConfirmed) {
                        this.sendTransfer()
                    } else {
                        Swale.fire('Transfer cancel', ' ', 'info')
                    }
                })
                .catch(err => {
                    Swal.fire('Transfer cancel', ' ', 'info')
                })
        },
        logout() {
            axios.post("http://localhost:8080/api/logout")
                .then(response => {
                    window.location.href = "http://localhost:8080/web/pages/public/login.html"
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