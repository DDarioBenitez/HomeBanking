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
                    window.location.reload()
                })
                .catch(err => {
                    console.error(err)
                    Swale.fire('Transfer cancel', ' ', 'info')
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
                        Swal.fire('Saved!', '', 'success', 5000)
                    } else {
                        Swale.fire('Transfer cancel', ' ', 'info')
                    }
                })
                .catch(err => {
                    Swal.fire('Transfer cancel', ' ', 'info')
                })
        }
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