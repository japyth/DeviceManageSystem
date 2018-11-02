var vm = new Vue({
    el: '#app',
    data: {
        userName: "",
        password: "",
        modifyPwd: {}   //修改的用户信息
    },
    created: function () {

    },
    methods: {
        loginButton: function () {
            axios.post('/login', {
                username: vm.userName,
                password: vm.password
            })
                .then(function (response) {
                    if (response.headers.authorization) {
                        localStorage.setItem("token", response.headers.authorization);
                        window.location.href = "/index.html";
                    }
                });
        },

        visitorButton: function () {
            localStorage.setItem("token", "");
            window.location.href = "/index.html";
        },

        ChgPwdButton: function () {
            $('#changePwdModal').modal('show');
        },

        changePwd: function () {
            if (vm.modifyPwd.username && vm.modifyPwd.password && vm.modifyPwd.newPwd && vm.modifyPwd.newPwdSec) {
                if (vm.modifyPwd.newPwd === vm.modifyPwd.newPwdSec) {
                    $.post(getHost() + "api/pwd/changePassword", vm.modifyPwd)
                        .done(function (data) {
                            if (data.result === true) {
                                $('#changePwdModal').modal('hide');
                                vm.modifyPwd = {};
                                alert("修改密码成功")
                            }
                            else {
                                alert(data.errorMessage ? data.errorMessage : "请求异常");
                            }
                        })
                } else {
                    alert("两次密码输入不一致");
                }
            } else {
                alert("表单内容不能为空！");
            }
        }
    }
})