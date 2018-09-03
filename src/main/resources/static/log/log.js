var vm = new Vue({
    el: '#app',
    data: {
        logList: [],         //分页数据
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
                var startTime;
                var endTime;
                if ($('#startTime').val() === "") {
                    startTime = "";
                } else {
                    startTime = $('#startTime').val();
                }
                if ($('#endTime').val() === "") {
                    endTime = "";
                } else {
                    endTime = $('#endTime').val();
                }
                var searchValue = $('#searchValue').val();

                var search = {
                    startTime: startTime,
                    endTime: endTime,
                    searchValue: searchValue,
                    pageIndex: pageIndex,
                    pagesize: vm.pagesize
                };
                //ajax获得分页数据
                $.post(getHost() + "api/logs/getLogPage", search)
                    .done(function (data) {
                        if (data.result === true) {
                            vm.logList = data.data.rows;
                            if (vm.logList.length === 0) {
                                alert("查询结果为空，请检查SQL")
                            }else {
                                vm.pageCurrent = pageIndex;
                                vm.pageCount = data.data.totalPage;
                                vm.totalCount = data.data.totalCount;
                                vm.logList.forEach(function (item) {
                                    if (item.updateTime) {
                                        item.updateTime = moment(item.updateTime).format("YYYY-MM-DD HH:mm:ss")
                                    }
                                })
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
                    vm.fDisabled = true;
                    vm.lDisabled = true;
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
        searchButton: function (pageIndex) {

        },
        init: function () {
            //初始化页面
            $(function () {//获取一周前的时间
                var startTime = moment().subtract('days', 6).format('YYYY-MM-DD HH:mm:ss');
                $('#datetimepicker1').datetimepicker({
                    defaultDate: startTime,
                    format: "YYYY-MM-DD HH:mm:ss",
                });
                $('#datetimepicker2').datetimepicker({
                    defaultDate: new Date(),
                    format: "YYYY-MM-DD HH:mm:ss",
                });
                vm.showPage(vm.pageCurrent, null, true);
            });
        }
    }
})
