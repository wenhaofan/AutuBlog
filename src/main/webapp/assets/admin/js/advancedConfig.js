
layui.define(['jquery','fl', 'form', 'laytpl'], function (exports) {

    const advancedConfig = {
        bind: function () {
            const that = this;
            $("body").on("click", ".privacy", function () {
                that.showPrivacy(this);
            })
            $("body").on("click", ".mupdate", function () {
                that.renderMupdate(this);
            })
            $("body").on("click", ".bupdate", function () {
                that.renderBupdate(this);
            })

            $("body").on("click", ".madd", function () {
                that.renderMEdit({});
            })
            $("body").on("click", ".badd", function () {
                that.renderBEdit({});
            })

            $("body").on("click", ".mdelete", function () {
                that.mdelete(that);
            })

            $("body").on("click", ".bdelete", function () {
                that.bdelete(this);
            })

            layui.fl = layui.fl;
            form = layui.form;


            form.render();



            form.on("submit(editMconfig)", function (data) {
                that.medit(data.field);
            })
            form.on("submit(editBconfig)", function (data) {
                that.bedit(data.field);
            })

            form.on("submit(pushLinks)", function (data) {
                that.pushLinks(data.field);
            })
        },
        load: function () {
            table = layui.table;
            layui.fl.renderTable({
                elem: '#metaweblog'
                , url: '/admin/api/metaConfig/mList'
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    { field: 'id', sort: true, width: 60, title: "id" }
                    , { field: 'website', title: "网站", width: 100 }
                    , { field: 'url', title: "接口路径" }
                    , { field: 'userName', title: "账号", width: 100 }
                    , { templet: '#tpl-mprivacy', title: "密码",  }
                    , { templet: '#tpl-moperation', title: "操作", width: 150 }
                ]]
            });

            layui.fl.renderTable({
                elem: '#baiduseo'
                , url: '/admin/api/baiduConfig/bList'
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    { field: 'id', sort: true, width: 60, title: "id" }
                    , { field: 'site', title: "网站" }
                    , { templet: '#tpl-bprivacy', title: "token", width: 200 }
                    , { templet: '#tpl-boperation', title: "操作", width: 150 }
                ]]
            });

        }, pjaxLoad: function () {
            this.load();
        },
        mdelete: function (that) {
            deleteMid = $(that).data("id");
            layui.fl.alertConfirm({
                title: "是否确认删除？", then: function () {
                    layui.fl.ajax({
                        url: "/admin/api/metaConfig/mconfigDelete/" + deleteMid,
                        success: function (data) {
                            layui.fl.alertOkAndReload();
                        }
                    })
                }
            })
        },

        bdelete: function (that) {
            deleteBid = $(that).data("id");
            layui.fl.alertConfirm({
                title: "是否确认删除？", then: function () {
                    layui.fl.ajax({
                        url: "/admin/api/baiduConfig/bconfigDelete/" + deleteBid,
                        success: function (data) {
                            layui.fl.alertOkAndReload();
                        }
                    })

                }
            });
        },
        renderBupdate: function (obj) {
        	const that=this;
            layui.fl.ajax({
                url: "/admin/api/baiduConfig/bget/" + $(obj).data("id"),
                dataType: 'json',
                success: function (data) {
                	that.renderBEdit(data.config);
                }
            })
        },
        renderMupdate: function (taht) {
        	const that=this;
            layui.fl.ajax({
                url: "/admin/api/metaConfig/mget/" + $(taht).data("id"),
                dataType: 'json',
                success: function (data) {
                    that.renderMEdit(data.config);
                }
            })
        },
        /**
         * 显示隐藏的密码
         * @param that
         * @returns
         */
        showPrivacy: function (that) {
            var content = $(that).data("content");
            var text = $(that).text();
            $(that).text(content);
            $(that).data("content", text);
        },
        renderMEdit: function (data) {
            var content = template("tpl-medit", data);
            var width = 520;
            if ($(window).width() < 768) {
                width = 350;
            }
            layerIndex = layer.open({
                title: "新增",
                area: [width + 'px', '350px'],
                type: 1,
                content: content //这里content是一个普通的String
            });
        },
        renderBEdit: function (data) {
            var content = template("tpl-bedit", data);
            var width = 520;
            if ($(window).width() < 768) {
                width = 350;
            }
            layerIndex = layer.open({
                title: "新增",
                area: [width + 'px', '230px'],
                type: 1,
                content: content //这里content是一个普通的String
            });
        },

        medit: function (data) {
            layui.fl.ajax({
                url: "/admin/api/metaConfig/mconfigEdit",
                data: data,
                type: "post",
                success: function (data) {
                    layui.fl.alertOkAndReload();
                }
            })
        },
        bedit: function (data) {
            layui.fl.ajax({
                url: "/admin/api/baiduConfig/bconfigEdit",
                data: data,
                type: "post",
                success: function (data) {
                    layui.fl.alertOkAndReload();
                }
            })
        },
        pushLinks: function (data) {
            layui.fl.ajax({
                data: data,
                url: "/admin/api/baiduConfig/pushBaiduLinks",
                type: "post",
                success: function (data) {
                    layui.fl.alertOk({ title: "提交成功！" });
                }
            })
        }
    };

    advancedConfig.bind();
    advancedConfig.load();
    exports("advancedConfig", advancedConfig);
});


