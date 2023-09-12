const { createApp } = Vue

const login = createApp({
    data() {
        return {
            userName: "",
            password: "",
        }
    },
    created() {
        console.log(this.userName);
    },
    methods: {
        login() {
            axios.post("/api/login", `email=${this.userName}&password=${this.password}`)
                .then(response => {
                    Swal.fire({
                        icon: 'success',
                        text: 'Sucess Login',
                        showConfirmButton: false,
                    })
                    if (this.userName.includes("admin")) {
                        setTimeout(() => {
                            window.location.href = "http://localhost:8080/web/pages/admin/manager.html";
                        }, 2000);
                    } else {
                        setTimeout(() => {
                            window.location.href = "http://localhost:8080/web/pages/client/accounts.html";
                        }, 2000);
                    }
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        text: 'Password or User Name incorrect, try again.',
                        showConfirmButton: false,
                    })
                })
        }
    }
})
login.mount("#app")