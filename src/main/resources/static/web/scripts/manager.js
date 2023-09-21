const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            clients: [],
            firstName: "",
            lastName: "",
            email: "",
            json: "",
            nameLoan: "",
            maxAmountLoan: "",
            paymentLoan: [],
            percentageLoan: [],
            loans: [],
        }
    },
    created() {
        this.loadData()
        this.loadLoans()
    },
    methods: {
        sendLoan() {
            let paymentLoan = this.paymentLoan.split(",").map(Number);
            let percentageLoan = this.percentageLoan.split(",").map(Number);
            if (paymentLoan.length != percentageLoan.length) {
                Swal.fire({
                    icon: 'error',
                    text: `The amount of installments does not match the amount of interest`,
                    showConfirmButton: false,
                });
            } else {
                Swal.fire({
                    icon: 'question',
                    title: 'Are you sure you want to create this Loan?',
                    confirmButtonText: 'Yes',
                    showCloseButton: true,
                    showCancelButton: true,
                    cancelButtonText: 'No'
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post("http://localhost:8080/api/admin/loans", { "name": this.nameLoan, "maxAmount": this.maxAmountLoan, "payment": paymentLoan, "percentage": percentageLoan })
                            .then(data => {
                                Swal.fire({
                                    icon: 'success',
                                    text: 'New Loan Created',
                                    showConfirmButton: false,
                                });
                                setTimeout(() => {
                                    window.location.reload()
                                }, 1000)
                            })
                            .catch((error) => {
                                console.log(error);
                                Swal.fire({
                                    icon: 'error',
                                    text: 'An error occurred try again',
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
            }

        },
        loadLoans() {
            axios.get("http://localhost:8080/api/loans")
                .then(data => {
                    this.loans = data.data
                })
                .catch(error => console.log(error))
        },
        loadData() {
            axios.get("http://localhost:8080/api/clients")
                .then(data => {
                    console.log(data);
                    this.clients = data.data
                    console.log(this.clients);
                    this.json = data.data;
                })
                .catch(error => console.log(error))
        },
        send() {
            axios.post("http://localhost:8080/clients", {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email
            })
                .then(data => this.loadData())
                .catch(error => console.log(error))
        },
        logout() {
            axios.post("http://localhost:8080/api/logout")
                .then(response => {
                    window.location.href = "http://localhost:8080/web/pages/public/login.html"
                })
                .catch(err => console.log(err))
        },
        prueba() {
            let aux = this.paymentLoan.split(",").map(Number)
            console.log(aux);
            console.log([...this.percentageLoan]);
        },
        formatBalance(number) {
            let reset = new Intl.NumberFormat('en-En', { style: 'currency', currency: 'USD' })
            let balanceFormat = reset.format(number)
            return balanceFormat
        }
    },
    computed: {
        pruebas() {
            console.log(this.nameLoan);
            console.log(this.paymentLoan);
            console.log(this.percentageLoan);
            console.log(this.idLoan);
            console.log(this.maxAmountLoan);
        }
    }
})
app.mount("#app")