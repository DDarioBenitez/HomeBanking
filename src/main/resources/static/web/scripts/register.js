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
            console.log("hola");
            axios.post("/api/clients", `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
                .then(response => {
                    window.alert("Sucess")
                    window.location.href = "http://localhost:8080/web/pages/public/login.html"
                })
                .catch(err => {
                    window.alert("Incomplete data or email already in use, try again")
                })
        }
    }
})
register.mount("#app")