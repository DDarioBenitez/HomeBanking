const { createApp } = Vue;


const loans = createApp({
    data() {
        return {
            loans: [],
            paymentOption: [],
            accounts: [],
            client: [],
            clientLoans: [],
            numberAccount: "",
            amount: "",
            maxAmount: "",
            idLoanSelect: "",
            paymentSelect: "",
        }
    },
    created() {
        this.getLoans()
        this.getAccounts()
    },
    methods: {
        getLoans() {
            axios.get("http://localhost:8080/api/loans")
                .then(respone => {
                    this.loans = respone.data
                })
                .catch(err => console.log(err))
        },
        getAccounts() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.client = response.data
                    this.accounts = this.client.accounts
                    this.clientLoans = this.client.loans
                })
                .catch(err => console.log(err))
        },
        applyLoan() {
            axios.post("http://localhost:8080/api/loans", { "id": this.idLoanSelect, "numberAccount": this.numberAccount, "amount": this.amount, "payment": this.paymentSelect })
                .then(respone => {
                    console.log(respone.data);
                    Swal.fire('Saved!', '', 'success', 5000)
                })
                .catch(err => {
                    console.error(err);
                    Swal.fire('There was an error, try again', '', 'info')
                })
        },
        getPayment() {
            console.log(this.idLoanSelect);
            if (this.idLoanSelect == 0) {
                this.paymentOption = ""
            } else {
                this.paymentOption = [...this.loans.find(loan => loan.id === this.idLoanSelect).payment]
                this.getMaxAmount()
            }
            console.log(this.paymentOption);
        },
        getMaxAmount() {
            this.maxAmount = this.loans.find(loan => loan.id === this.idLoanSelect).maxAmount
            console.log(this.maxAmount);
        },
        formatAmount(maxAmount) {
            let reset = new Intl.NumberFormat('en-En', { style: 'currency', currency: 'USD' })
            let amountFormat = reset.format(maxAmount)
            return amountFormat
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
                        this.applyLoan()
                    }
                    else {
                        Swal.fire('Cancel', ' ', 'info')
                    }
                })
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

loans.mount("#app")
