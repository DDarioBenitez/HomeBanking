const { createApp } = Vue;


const loans = createApp({
    data() {
        return {
            loans: [],
            paymentOption: [],
            accounts: [],
            client: [],
            clientLoans: [],
            percentage: [],
            paymentWhitPercentage: {},
            numberAccount: "",
            amount: "",
            maxAmount: "",
            idLoanSelect: "",
            paymentSelect: "",
        }
    },
    created() {
        this.getAccounts()
    },
    methods: {
        getLoans() {
            axios.get("http://localhost:8080/api/loans")
                .then(respone => {
                    this.loans = this.getLoansNot(respone.data)
                    console.log(this.loans);
                })
                .catch(err => console.log(err))
        },
        getAccounts() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.client = response.data
                    this.accounts = this.client.accounts
                    this.clientLoans = this.client.loans
                    this.getLoans()
                    console.log(this.client.loans)
                })
                .catch(err => console.log(err))
        },
        getLoansNot(loans) {
            let aux = loans.filter(loan => !this.clientLoans.some(loanA => loanA.name === loan.name));
            return aux;
        },
        getPayment() {
            console.log(this.idLoanSelect);
            if (this.idLoanSelect == 0) {
                this.paymentOption = ""
            } else {
                this.paymentOption = [...this.loans.find(loan => loan.id === this.idLoanSelect).payment]
                this.percentage = [... this.loans.find(loan => loan.id === this.idLoanSelect).percentage]
                this.getMaxAmount()
            }
            console.log(this.paymentSelect);
        },
        getMaxAmount() {
            this.maxAmount = this.loans.find(loan => loan.id === this.idLoanSelect).maxAmount
            console.log(this.maxAmount);
        },
        getPercentage() {
            this.paymentWhitPercentage = [...this.paymentOption].reduce((result, current, index) => {
                result[current] = this.percentage[index];
                return result;
            }, {});
            console.log(this.paymentWhitPercentage[this.paymentSelect] * parseInt(this.amount));
            console.log(typeof parseInt(this.amount));
        },
        formatAmount(maxAmount) {
            let reset = new Intl.NumberFormat('en-En', { style: 'currency', currency: 'USD' })
            let amountFormat = reset.format(maxAmount)
            return amountFormat
        },
        totalAmount(amount) {
            let aux = "El total a paga es " + (parseInt(amount) + (this.paymentWhitPercentage[this.paymentSelect] * parseInt(amount) / 100))
            return aux
        },
        showPAymentAmount(amount, payment) {
            console.log(typeof payment);
            let aux = "El monto de la cuota es de: " + ((parseInt(amount) + (this.paymentWhitPercentage[this.paymentSelect] * parseInt(amount) / 100)) / payment).toFixed(2)
            return aux
        },
        alert() {
            Swal.fire({
                icon: 'question',
                title: 'Are you sure apply for loan?',
                text: `${this.totalAmount(this.amount)}.\n ${this.showPAymentAmount(this.amount, this.paymentSelect)}`,
                confirmButtonText: 'Yes',
                showCloseButton: true,
                showCancelButton: true,
                cancelButtonText: 'No'
            }).then(response => {
                console.log(response);
                if (response.isConfirmed) {
                    axios.post("http://localhost:8080/api/loans", { "id": this.idLoanSelect, "numberAccount": this.numberAccount, "amount": this.amount, "payment": this.paymentSelect })
                        .then(respone => {
                            console.log(respone.data);
                            Swal.fire({
                                icon: 'success',
                                text: 'You have aplly for new loan',
                                showConfirmButton: false,
                            });
                            setTimeout(() => {
                                window.location.reload()
                            }, 1000)
                        })
                        .catch(err => {
                            console.error(err);
                            Swal.fire({
                                icon: 'error',
                                text: 'A problem occurred',
                                showConfirmButton: false,
                            });
                            setTimeout(() => {
                                // Puedes agregar aquí cualquier acción adicional si es necesario
                            }, 1000)
                        })
                }
                else {
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
            axios.post("http://localhost:8080/api/logout")
                .then(response => {
                    window.location.href = "http://localhost:8080/web/pages/public/login.html"
                })
                .catch(err => console.log(err))
        }
    }
})

loans.mount("#app")
