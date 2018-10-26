var vm = new Vue({
    el: '#app',
    data: {
        userName:"",
        password:""
    },
    created: function () {
        this.init();
    },
    methods: {
        loginButton: function () {
            axios.post('/login', {
                username: vm.userName,
                password: vm.password
            })
                .then(function (response) {
                    if(response.headers.authorization){
                        localStorage.setItem("token",response.headers.authorization);
                        window.location.href = "/index.html";
                    }
                });
        },

        visitorButton: function () {
            localStorage.setItem("token","");
            window.location.href = "/index.html";
        }

    }
})