var vm = new Vue({
    el: '#app',
    data: {
        username: "visitor",
        showAdmin: false,
        showUser: false,
        userList: [],         //table列表
        user: {},             //新增的人员
        modifyUser: {},
        deleteUser: {},
        authorizeUser: {},
        searchValue: "",
        fDisabled: false,
        lDisabled: false,
        totalCount: 200,
        //分页数
        pageCount: 20,
        //当前页面
        pageCurrent: 1,
        //分页大小
        pagesize: 10,
        //显示分页按钮数
        showPages: 11,
        //开始显示的分页按钮
        showPagesStart: 1,
        //结束显示的分页按钮
        showPageEnd: 20
    },
    created: function () {
        this.init();
    },
    watch: {
        // 如果 `question` 发生改变，这个函数就会运行
        searchValue: function (newValue, oldValue) {
            this.init();
        }
    },
    methods: {
        showPage: function (pageIndex, $event, forceRefresh) {
            if (pageIndex > 0) {
                if (pageIndex > vm.pageCount) {
                    pageIndex = vm.pageCount;
                }
                //判断数据是否需要更新
                var currentPageCount = Math.ceil(vm.totalCount / vm.pagesize);
                if (currentPageCount !== vm.pageCount) {
                    pageIndex = 1;
                    vm.pageCount = currentPageCount;
                }
                else if (vm.pageCurrent === pageIndex && currentPageCount === vm.pageCount && typeof (forceRefresh) === "undefined") {
                    return;
                }

                //查询和分页参数
                var searchValue = $('#searchValue').val();
                //分页数据
                axios.post("/api/admin/getAllUser", {
                    searchValue: searchValue,
                    pageIndex: pageIndex,
                    pagesize: vm.pagesize
                }, {
                    headers: {
                        "Authorization": localStorage.getItem("token")
                    }
                })
                    .then(function (response) {
                        if (response.data.result === true) {
                            var data = response.data;
                            vm.userList = data.data.rows;
                            vm.username = data.username;
                            var roleList = data.roles;
                            if (roleList !== null) {
                                vm.showAuth(roleList);
                            }
                            if (vm.userList.length === 0) {
                                alert("查询结果为空，请检查SQL")
                            } else {
                                vm.pageCurrent = pageIndex;
                                vm.pageCount = data.data.totalPage;
                                vm.totalCount = data.data.totalCount;
                            }
                        }
                        else {
                            alert(data.errorMessage ? data.errorMessage : "请求异常");
                        }
                    })
                //处理分页按钮的样式
                var buttons = $("#pager").find("span");
                for (var i = 0; i < buttons.length; i++) {
                    if (buttons.eq(i).html() === pageIndex.toString()) {
                        buttons.eq(i).addClass("active");
                    } else {
                        buttons.eq(i).removeClass("active");
                    }
                }

                //如果当前页首页或者尾页，则上一页首页就不能点击，下一页尾页就不能点击
                if (vm.pageCount === 1) {
                    vm.lDisabled = true;
                    vm.fDisabled = true;
                } else if (pageIndex === 1) {
                    vm.fDisabled = true;
                    vm.lDisabled = false;
                } else if (pageIndex === vm.pageCount) {
                    vm.lDisabled = true;
                    vm.fDisabled = false;
                } else {
                    vm.fDisabled = false;
                    vm.lDisabled = false;
                }

                //计算分页按钮数据
                if (vm.pageCount > vm.showPages) {
                    if (pageIndex <= (vm.showPages - 1) / 2) {
                        vm.showPagesStart = 1;
                        vm.showPageEnd = vm.showPages - 1;
                    }
                    else if (pageIndex >= vm.pageCount - (vm.showPages - 3) / 2) {
                        vm.showPagesStart = vm.pageCount - vm.showPages + 2;
                        vm.showPageEnd = vm.pageCount;
                    }
                    else {
                        vm.showPagesStart = pageIndex - (vm.showPages - 3) / 2;
                        vm.showPageEnd = pageIndex + (vm.showPages - 3) / 2;
                    }
                }
            }
        },
        //判断用户具有哪些权限
        showAuth: function (roleList) {
            for (var i in roleList) {
                if (roleList[i] === "user") {
                    vm.showUser = true;
                }
                if (roleList[i] === "admin") {
                    vm.showAdmin = true;
                }
            }
        },

        openAddDialog: function () {
            $('#addUserModal').modal('show')
        },

        addUser: function () {
            if (vm.user.username) {
                axios.post("/api/admin/addUser", vm.user, {
                    headers: {
                        "Authorization": localStorage.getItem("token")
                    }
                })
                    .then(function (response) {
                        if (response.data.result === true) {
                            vm.showPage(1, null, true);
                            $('#addUserModal').modal('hide');
                            vm.user = {};     //清空内容
                        }
                        else {
                            alert(data.errorMessage ? data.errorMessage : "请求异常");
                        }
                    })
            } else {
                alert("用户姓名不能为空！")
            }

        },

        modifyButton: function (theUser) {
            vm.modifyUser = JSON.parse(JSON.stringify(theUser));
            //页面上显示的是拼接之后的权限
            var roles = vm.modifyUser.roles;
            if (roles == null) {
                vm.modifyUser.adminAuth = false;
                vm.modifyUser.userAuth = false;
            }
            if (roles.indexOf("超级管理员") >= 0) {
                vm.modifyUser.adminAuth = true;
            }
            if (roles.indexOf("设备管理员") >= 0) {
                vm.modifyUser.userAuth = true;
            }
            $('#modifyUserModal').modal('show');
        },

        modifyUserButton: function () {
            if (vm.modifyUser.username) {
                axios.post(getHost() + "api/admin/modifyUser", vm.modifyUser, {
                    headers: {
                        "Authorization": localStorage.getItem("token")
                    }
                })
                    .then(function (response) {
                        if (response.data.result === true) {
                            vm.showPage(1, null, true);
                            $('#modifyUserModal').modal('hide');
                            vm.modifyUser = {};     //清空内容
                        }
                        else {
                            alert(data.errorMessage ? data.errorMessage : "请求异常");
                        }
                    })
            } else {
                alert("用户姓名为空");
            }
        },
        deleteButton: function (theUser) {
            vm.deleteUser = JSON.parse(JSON.stringify(theUser));
            $('#deleteUserModal').modal('show');
        },

        deleteDialogButton: function () {
            axios.post(getHost() + "api/admin/deleteUser", vm.deleteUser, {
                headers: {
                    "Authorization": localStorage.getItem("token")
                }
            })
                .then(function (response) {
                    if (response.data.result === true) {
                        vm.showPage(1, null, true);
                        $('#deleteUserModal').modal('hide');
                        vm.deleteUser = {};     //清空内容
                    }
                    else {
                        alert(data.errorMessage ? data.errorMessage : "请求异常");
                    }
                })
        },

        init: function () {
            //初始化页面
            $(function () {
                vm.showPage(vm.pageCurrent, null, true);
            });
        }
    }
})