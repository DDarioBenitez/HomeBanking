const body = document.getElementById("body")
const foot = document.getElementById("foot")
const head = document.getElementById("head")
const img = document.getElementById("logoNav")
const swith = document.getElementById("switch")

swith.addEventListener("change", e => {
    console.log(e.target.checked);
    if (true) {
        body.classList.toggle("dark")
        foot.classList.toggle("darkColor")
        head.classList.toggle("darkColor")
        body.setAttribute("data-bs-theme", (e.target.checked == true ? "dark" : "ligth"))
        img.setAttribute("src", (e.target.checked == true ? "../images/logo-blanco.png" : "../images/logo-azul.png"))
    }
})