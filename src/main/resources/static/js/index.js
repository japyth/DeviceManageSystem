var vm = new Vue({
    el: '#app',
    data: {
        deviceList: [],         //table列表
        device: {},             //新增的设备
        deviceStatus: 0,       //设备状态
        deviceType:"设备类型",
        borrowDevice: {},         //借出设备
        modifyDevice: {},          //修改的设备
        deleteDevice: {},      //删除的设备
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
        showPageEnd: 20,
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
                var search = {
                    deviceStatus: vm.deviceStatus,
                    searchValue: searchValue,
                    deviceType: vm.deviceType,
                    pageIndex: pageIndex,
                    pagesize: vm.pagesize
                };
                //分页数据
                $.post(getHost() + "api/device/getAllDevice", search)
                    .done(function (data) {
                        if (data.result === true) {
                            vm.deviceList = data.data.rows;
                            if (vm.deviceList.length === 0) {
                                alert("查询结果为空，请检查SQL")
                            }else{
                                vm.pageCurrent = pageIndex;
                                vm.pageCount = data.data.totalPage;
                                vm.totalCount = data.data.totalCount;
                                vm.deviceList.forEach(function (item) {
                                    if (item.updateTime) {
                                        item.updateTime = moment(item.updateTime).format("YYYY-MM-DD HH:mm:ss")
                                    }
                                });
                            }
                        }
                        else {
                            alert(data.errorMessage ? data.errorMessage : "请求异常");
                        }
                    })
                //处理分页按钮的样式
                var buttons = $("#pager").find("span");
                for (var i = 0; i < buttons.length; i++) {
                    if(buttons.eq(i).html() === pageIndex.toString()){
                        buttons.eq(i).addClass("active");
                    } else {
                        buttons.eq(i).removeClass("active");
                    }
                }

                //如果当前页首页或者尾页，则上一页首页就不能点击，下一页尾页就不能点击
                if(vm.pageCount === 1){
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

        openAddDialog: function () {
            $('#addDeviceModal').modal('show')
        },

        addDevice: function () {
            if(vm.device.deviceName&&vm.device.deviceType&&vm.device.deviceType&&vm.device.serialNumber&&vm.device.owner){
                $.post(getHost() + "api/device/addDevice", vm.device)
                    .done(function (data) {
                        if (data.result === true) {
                            vm.showPage(1, null, true);
                            $('#addDeviceModal').modal('hide');
                            vm.device = {};     //清空内容
                        }
                        else {
                            alert(data.errorMessage ? data.errorMessage : "请求异常");
                        }
                    })
            } else {
                alert("表单内容（除备注外）不能为空！")
            }

        },

        borrowButton: function (theDevice) {
            vm.borrowDevice = JSON.parse(JSON.stringify(theDevice));
            $('#borrowDeviceModal').modal('show')
        },

        borrowDialogButton: function () {
            if (vm.borrowDevice.borrower) {
                $.post(getHost() + "api/device/borrowDevice", vm.borrowDevice)
                    .done(function (data) {
                        if (data.result === true) {
                            vm.showPage(1, null, true);
                            $('#borrowDeviceModal').modal('hide');
                            vm.borrowDevice = {};     //清空内容
                        }
                        else {
                            alert(data.errorMessage ? data.errorMessage : "请求异常");
                        }
                    })
            } else {
                alert("借出人员为空");
            }
        },

        modifyButton: function (theDevice) {
            vm.modifyDevice = JSON.parse(JSON.stringify(theDevice));
            $('#modifyDeviceModal').modal('show');
        },

        modifyDeviceButton: function () {
            if (vm.modifyDevice.owner) {
                $.post(getHost() + "api/device/modifyDevice", vm.modifyDevice)
                    .done(function (data) {
                        if (data.result === true) {
                            vm.showPage(1, null, true);
                            $('#modifyDeviceModal').modal('hide');
                            vm.modifyDevice = {};     //清空内容
                        }
                        else {
                            alert(data.errorMessage ? data.errorMessage : "请求异常");
                        }
                    })
            } else {
                alert("拥有者为空");
            }
        },
        deleteButton: function (theDevice) {
            vm.deleteDevice = JSON.parse(JSON.stringify(theDevice));
            $('#deleteDeviceModal').modal('show');
        },

        deleteDialogButton: function () {
            $.post(getHost() + "api/device/deleteDevice", vm.deleteDevice)
                .done(function (data) {
                    if (data.result === true) {
                        vm.showPage(1, null, true);
                        $('#deleteDeviceModal').modal('hide');
                        vm.deleteDevice = {};     //清空内容
                    }
                    else {
                        alert(data.errorMessage ? data.errorMessage : "请求异常");
                    }
                })
        },

        returnButton: function (theDevice) {
            $.post(getHost() + "api/device/returnDevice", theDevice)
                .done(function (data) {
                    if (data.result === true) {
                        vm.showPage(1, null, true);
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