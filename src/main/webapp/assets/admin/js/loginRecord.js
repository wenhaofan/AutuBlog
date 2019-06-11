layui.define([
    'table',
    'jquery',
    'f;'
], function (exports) {
    const loginRecord = {
        bind: function () {
            const fl = layui.fl;
            $("body").on("click", ".login-downline", function () {
                fl.ajax({
                    url: "/admin/api/loginRecord/downline/" + $(this).data("id"),
                    success: function (data) {
                        fl.alertOkAndReload("操作成功！");
                    }
                })
            })
        },
        load: function () {
            template.defaults.imports.currentSessionId = $("input[name='currentSessionId']").val();
            this.renderLoginRecordTable();
        }, renderLoginRecordTable: function (data) {
            layui.fl.renderTable({
                where: data ? data : {}
                , elem: '#loginRecordTable'
                , url: '/admin/api/loginRecord/page' //数据接口
                , page: true //开启分页
                , cols: [[ //表头
                    { field: 'id', title: 'ID', sort: true, fixed: 'left' }
                    , { field: 'gmtCreate', title: '登录时间' }
                    , { field: 'ip', title: 'IP' }
                    , { templet: '#tpl-device', title: '设备信息' },
                    { templet: '#tpl-operation', title: '操作' }
                ]]
            });
        }
    };

    loginRecord.bind();
    loginRecord.load();
    exports("loginRecord", loginRecord);
});




