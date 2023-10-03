const { createApp } = Vue;


const transactions = createApp({
    data() {
        return {
            client: {},
            transactions: [],
            account: [],
            searchParam: "",
            startDate: '',
            endDate: '',
            change: false,
        }
    },
    created() {
        this.loadClient()
        this.loadTransactions()
    },
    methods: {
        loadClient() {
            axios.get("https://homebanking-production-0510.up.railway.app/api/clients/current")
                .then(data => {
                    this.client = data.data
                    console.log(this.client);
                    console.log(this.client.accounts.find(acc => acc.number == "VIN001"));
                })
                .catch(error => console.log("ERROR"))
        },
        loadTransactions() {
            let accountSelect = location.search;
            console.log(accountSelect);
            let accountIdSearch = new URLSearchParams(accountSelect)
            console.log(accountIdSearch);
            let accountId = accountIdSearch.get('id')
            console.log(accountId);
            axios.get("https://homebanking-production-0510.up.railway.app/api/accounts/" + accountId)
                .then(response => {
                    this.account = response.data
                    this.transactions = [...response.data.transactions];
                    this.transactions = this.transactions.sort((a, b) => b.date - a.date);
                    console.log(this.transactions.sort((a, b) => b.id - a.id));
                    console.log(response.data);

                    console.log(this.account);
                })
                .catch(error => console.log(error))
        },
        logout() {
            axios.post("https://homebanking-production-0510.up.railway.app/api/logout")
                .then(response => {
                    window.location.href = "https://homebanking-production-0510.up.railway.app/web/pages/public/login.html"
                })
                .catch(err => console.log(err))
        },
        changeV() {
            this.change = true
            console.log(this.change);
        },
        formatBalance(acc) {
            let reset = new Intl.NumberFormat('en-En', { style: 'currency', currency: 'USD' })
            let balanceFormat = reset.format(acc.balance)
            return balanceFormat
        },
        downloadPDF() {
            // Formatea las fechas en el formato esperado
            const sDate = `${this.startDate}T00:00:00`;
            const eDate = `${this.endDate}T00:00:00`;

            // Realiza la solicitud HTTP al backend para descargar el PDF
            axios.get(`https://homebanking-production-0510.up.railway.app/api/transactions_PDF`, {
                params: {
                    initDate: sDate,
                    finDate: eDate,
                    numberAccount: this.account.number
                },
                responseType: 'blob' // Indicar que la respuesta es de tipo binario
            })
                .then(response => {
                    // Crear una URL a partir del contenido binario del PDF
                    const blob = new Blob([response.data], { type: 'application/pdf' });
                    const url = window.URL.createObjectURL(blob);

                    // Crear un enlace de descarga
                    const a = document.createElement('a');
                    a.style.display = 'none';
                    a.href = url;
                    a.download = 'transactions.pdf';

                    // Agregar el enlace al documento y simular un clic para iniciar la descarga
                    document.body.appendChild(a);
                    a.click();

                    // Liberar la URL creada
                    window.URL.revokeObjectURL(url);

                    // Mostrar un mensaje de éxito
                    Swal.fire({
                        icon: 'success',
                        title: 'Download Complete',
                        showConfirmButton: false,
                    });
                })
                .catch(err => {
                    console.log(err);
                    // Mostrar un mensaje de error
                    Swal.fire({
                        icon: 'error',
                        text: `${err.response.data}`,
                        showConfirmButton: false,
                    });
                });
        }

    },
    computed: {
        prueba() {
            console.log(this.change);
            console.log(this.startDate);
            console.log(`${this.endDate}T00:00:00`)
        }
    }
})

transactions.mount("#app")
