
const { createApp } = Vue;

const cards = createApp({
    data() {
        return {
            client: [],
            accounts: [],
            loans: [],
            typeAccount: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(data => {
                    console.log(data);
                    this.client = data.data
                    console.log(this.client);
                    this.accounts = Array.from(Object.values(this.client.accounts)).sort((a, b) => a.id - b.id)
                    this.loans = data.data.loans
                    console.log(this.loans);
                })
                .catch(error => console.log(error))
        },
        formatBalance(number) {
            let reset = new Intl.NumberFormat('en-En', { style: 'currency', currency: 'USD' })
            let balanceFormat = reset.format(number)
            return balanceFormat
        },
        logout() {
            axios.post("http://localhost:8080/api/logout")
                .then(response => {
                    window.location.href = "http://localhost:8080/web/pages/public/login.html"
                })
                .catch(err => console.log(err))
        },
        createdAccount() {
            Swal.fire({
                title: 'Are you sure?',
                text: "Are you sure you want to create a new account?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, create it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: 'Account Type',
                        input: 'select',
                        inputOptions: {
                            'current': 'Current',
                            'saving': 'Saving'
                        },
                        inputPlaceholder: 'Select account type',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: 'Ok',
                        preConfirm: (value) => {
                            // Aquí obtendrías el valor seleccionado y podrías hacer algo con él
                            this.typeAccount = value.toUpperCase();
                            console.log(this.typeAccount);
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            axios.post(`http://localhost:8080/api/clients/current/accounts?type=${this.typeAccount}`)
                                .then(response => {
                                    window.location.reload()
                                })
                                .catch(err => {
                                    console.log(err)
                                    Swal.fire({
                                        icon: 'error',
                                        text: `${err.response.data}`,
                                        showConfirmButton: false,
                                    })
                                })
                        }
                    })
                } else {
                    Swal.fire({
                        icon: 'info',
                        text: 'Operation cancelled',
                        showConfirmButton: false,
                        timer: 1000
                    })
                }
            })
        },
        deleteAccount(acc) {
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
                    axios.delete("http://localhost:8080/api/clients/current/accounts", {
                        params: {
                            numberAccount: acc.number,
                            active: false
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
                            setTimeout(() => {
                                // Puedes agregar aquí cualquier acción adicional si es necesario
                            }, 1000);
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
        },
        payLoan(loan) {
            let acc = null;
            Swal.fire({
                title: 'Are you sure?',
                text: `Are you sure you want to pay ${loan.name} loan, the amount is ${Math.ceil(loan.paymentAmount * 10) / 10}`,
                html: `
                <div>
                  <p>You won't be able to revert this!</p>
                  <label for="accountSelect">Select an account:</label>
                  <select id="accountSelect">
                    ${this.accounts.map(acc => `<option value="${acc.id}">${acc.number}</option>`).join('')}
                  </select>
                </div>`,
                confirmButtonText: 'Yes, pay it!',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                preConfirm: () => {
                    // Accede al valor seleccionado en el elemento <select>
                    const selectedAccount = document.getElementById('accountSelect').value;
                    acc = this.accounts.find(account => account.id == selectedAccount); // Encuentra la cuenta correspondiente
                    console.log(acc);
                    if (!acc) {
                        Swal.showValidationMessage('Please select an account');
                    }
                },
            }).then((result) => {
                if (result.isConfirmed) {
                    // Ahora puedes usar acc en la solicitud Axios
                    if (acc) {
                        let loanPaym = Math.ceil(loan.paymentAmount * 10) / 10
                        let loanN = loan.name
                        let numberAcc = acc.number
                        console.log(loan.name, acc.number, loanPaym);
                        axios.patch(`http://localhost:8080/api/loans/payment?loanName=${loanN}&numberAcc=${numberAcc}&amount=${loanPaym}`)
                            .then(response => {
                                Swal.fire(
                                    'Paid!',
                                    'Your loan has been paid.',
                                    'success'
                                )
                                setTimeout(() => {
                                    window.location.reload();
                                }, 1000);
                            }).catch(err => {
                                console.log(err);
                                Swal.fire({
                                    icon: 'error',
                                    text: `${err.response.data}`,
                                    showConfirmButton: false,
                                })
                                setTimeout(() => { }, 1000)
                            });
                    }
                } else {
                    Swal.fire({
                        icon: 'info',
                        text: 'Operation cancelled',
                        showConfirmButton: false,
                        timer: 1000
                    });
                }
            });
        },
        formatBalance(number) {
            let reset = new Intl.NumberFormat('en-En', { style: 'currency', currency: 'USD' })
            let balanceFormat = reset.format(number)
            return balanceFormat
        }

    }
})

cards.mount("#app")