const { createApp } = Vue

const register = createApp({
    data() {
        return {
            firstName: "",
            lastName: "",
            email: "",
            password: ""
        }
    },
    created() {
        console.log(this.firstName);
    },
    methods: {
        register() {
            axios.post("/api/clients", `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
                .then(response => {
                    Swal.fire({
                        icon: 'success',
                        text: 'Sucess Register',
                        showConfirmButton: false,
                    })
                    setTimeout(() => {
                        window.location.href = "http://localhost:8080/web/pages/public/login.html"
                    }, 2000)
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        text: 'Incomplete data or email already in use, try again',
                        showConfirmButton: false,
                    })
                })
            console.log("hola");
        }
    }
})
register.mount("#app")