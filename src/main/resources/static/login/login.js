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
            var param = {
                username:vm.userName,
                password:vm.password,
            };
            $.post(getHost() + "login", param)
               /* .done(function (data) {
                    if(data)
                })*/

        },
        init: function () {

        }


    }
})