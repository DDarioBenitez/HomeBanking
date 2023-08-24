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
                    if (this.userName.includes("admin")) {
                        window.location.href = "http://localhost:8080/web/pages/admin/manager.html"
                    } else {
                        window.location.href = "http://localhost:8080/web/pages/client/accounts.html"
                    }
                })
                .catch(error => {
                    window.alert("Password or User Name incorrect, try again.")
                })
        }
    }
})
login.mount("#app")