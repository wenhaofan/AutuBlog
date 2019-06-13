
layui.define([
    'jquery',
    'layer',
    'fl',
    'contextMenu',
    'clipboard',
    'upload'
], function (exports) {

    const disk = {
        /**
 * 不同类型的文件对应不同的封面
 */
        thumbImg: {
            img: ["png", "jpg"], apk: "apk", code: ["java", "js", "css", "python", "php", "vue", "class"], miss: null, exe: "exe", folder: "folder", html: "html"
            , music: "music", pdf: "pdf", txt: "txt", video: "video", word: "word", zip: "zip"
        },
        bind: function () {
            const that=this;
            const contextMenu=layui.contextMenu;
         
             $("body").on("click",".folder-goback a",function () {
                that.goBackFolder();
            })

            $("body").on("click",".disk-create-folder",function () {
                that.createFolderItem();
            })


            var clipboard = new ClipboardJS('.copy-btn');

            clipboard.on('success', function (e) {
                $(".copy-link-hint").text("链接已复制到您的剪切板,如未生效请手动复制！");
            });

            $(".folder-nav").on("click", "a", function () {
            	that.changeFolder(this);
            })


            $("body").on("click", ".confirm-rename", function () {
                that.confirmRename(this);
            })

            $("body").on("click", ".cancel-create-folder", function () {
                $(this).parent().parent().remove();
            })

            $("body").on("click", ".cancel-rename", function () {
                $(this).parent().parent().prev().show();
                $(this).parent().parent().remove();
            })

            $(".disk-content").on("click", ".confirm-create-folder", function () {
                var folderName = $(this).prev().val();
                that.createFolder({ name: folderName });
                $(this).parent().parent().remove();
            })
            $(".disk-content").on("click", ".disk-folder", function () {
                var id = $(this).data("id");
                var name = $(this).find(".file-name").text();
                that.clickFolder(id, name);
            })

            contextMenu.on(".disk-file", [
                {
                    events: {
                        "click": function (element) {
                            var id = $(element).data("id");
                            that.download(id);
                        }
                    }, text: "下载"
                }, {
                    events: {
                        "click": function (element) {
                        	 that.rename(element);
                        }
                    }, text: "重命名"
                }, {
                    events: {
                        "click": function (element) {
                        	 that.removeFile($(element).data("id"));
                        }
                    }, text: "删除"
                }, {
                    events: {
                        "click": function (element) {
                            var id = $(element).data("id");
                            that.share(id);
                        }
                    }, text: "分享"
                }
            ]);


            contextMenu.on(".disk-main", [
                {
                    events: {
                        "click": function (element) {
                            goBackFolder();
                        }
                    }, text: "返回"
                    , className: "menuGoBack",
                    condition: function () {
                        return currentFolderId != 0;
                    }
                },
                {
                    events: {
                        "click": function (element) {
                            createFolderItem();
                        }
                    }, text: "新建文件夹"
                }, {
                    events: {
                        "click": function (element) {
                            $(".disk-upload-button").trigger("click");
                        }
                    }, text: "上传"
                }, {
                    events: {
                        "click": function (element) {
                            window.location.reload();
                        }
                    }, text: "刷新"
                }
            ]);

            contextMenu.on(".disk-folder", [
                {
                    events: {
                        "click": function (element) {
                            $(element).trigger("click");
                        }
                    }, text: "打开"
                }, {
                    events: {
                        "click": function (element) {
                            rename(element);
                        }
                    }, text: "重命名"
                }, {
                    events: {
                        "click": function (element) {
                            removeFile($(element).data("id"));
                        }
                    }, text: "删除"
                }
            ]);
        }, 
        
        load: function () {
        	
        	const that=this;
            this.listDiskItem({ parentId: currentFolderId });

            if (currentFolderId != 0) {
                queryFolderNav(currentFolderId);
            }
            
            
            // 执行实例
            var uploadInst = layui.upload.render({
                elem: '.disk-upload-button' // 绑定元素
                ,url: '/admin/api/disk/upload' // 上传接口
                ,data:{ "parentId": currentFolderId }
            	,accept:'file'
            	,field:'upfile'
                ,done: function(data){
                	
                	if(data.state=="ok"){
                		layer.msg("上传成功");
                		that.addFileItem(data.disk);
                	}else{
                		layer.msg(data.msg);
                	}
                }
                ,error: function(){
                	layer.msg('系统繁忙，请稍后再试');
                }
            });

        },
        pjaxLoad:function(){
        	this.load();
        },
        clickFolder: function (folderId, folderName) {
            this.changeMenuNav(folderId, folderName);
            this.listDiskItem({ parentId: folderId });
        },

        share: function (id) {
        	const that=this;
            layui.layer.open({
                type: 1,
                area: ['320px', '170px'],
                title: "分享链接",
                content: $("#tpl-copy").html(),
                success: function () {
                    that.getDiskFileUrl(id);
                    $(".copy-btn").trigger("click");
                }
            });
        },

        copyLink: function (text) {
            if (window.clipboardData) { // Internet Explorer
                window.clipboardData.setData("Text", text);
                return false;
            }
        },
        /**
         * 查询文件夹下的文件夹和文件
         * @param folderId
         * @returns
         */
        queryFolderNav: function (folderId) {
            $.ajax({
                url: "/admin/api/disk/listFolderChain/" + folderId,
                dataType: "json",
                success: function (data) {
                    if (fl.isOk(data)) {
                        var diskList = data.diskList;
                        for (var i = diskList.length - 1; i >= 0; i--) {
                            changeMenuNav(diskList[i].id, diskList[i].name);
                        }
                    }
                }
            })
        }
        ,
        changeMenuNav: function (folderId, folderName) {
        	const that=this;
            if ($(".folder-goback").hasClass("hide")) {
            	that.renderMenuNav(folderId);
            } else {
            	that.convertMenuNav();
            }
            $(".folder-nav").append("<span data-id='" + folderId + "'>" + folderName + "</span>");
        }
        ,
        convertMenuNav: function () {
            //获取最后一个节点的名字和文件夹id
            var $lastA = $(".folder-nav span:last");
            if ($lastA.length > 0) {
                var preId = $lastA.data("id");
                var preName = $lastA.text();
                var menuItem = template("tpl-folder-nav-item", { name: preName, id: preId });
                var $menuItem = $(menuItem);
                $lastA.replaceWith($menuItem);
            }
        }
        ,
        renderMenuNav: function (folderId) {
            $(".folder-goback").removeClass("hide")
            $(".folder-goback").data("id", folderId);
            $(".folder-nav").removeClass("hide");
            $(".folder-nav").append("<span style='color: #c5d8f3;padding: 0 5px;line-height: 16px;'>&gt;</span>");
            $(".folder-all-info").addClass("hide");
        },
        goBackFolder: function () {
            var $lastA = $(".folder-nav a:last");
            $lastA.trigger("click");
        },

        deleteNext: function (folderId) {
            var $obj = $(".folder-nav a[data-id='" + folderId + "']");
            if ($obj.length == 0) {
                return;
            }

            var $nextObj = $obj.next();

            while ($nextObj.length > 0) {
                $nextObj.remove();
                $nextObj = $obj.next();
            }
        },

        convertToSpan: function (obj) {
            var folderId = $(obj).data("id");
            var folderName = $(obj).text();
            var $span = $("<span data-id='" + folderId + "'>" + folderName + "</span>");
            $(obj).replaceWith($span);
        },

        addEmptyInfo: function () {
            $(".disk-content").append('<div style="background: url(/assets/admin/images/disk/empty.png) no-repeat scroll center 0 transparent;padding-top: 146px;width: 488px;position: absolute;left: 50%;top: 50%;text-align: center;margin: -40px 0 0 -244px;"><p style="    display: inline-block;text-align: center;width: 300px;">暂无文件</p></div>')
        },

        /**
         * 查询渲染
         * @param query
         * @returns
         */
       listDiskItem: function (query) {
            $(".disk-content").empty();
            if (!query) {
                query = {};
            }
            currentFolderId = query.parentId;
           const  that=this;
            $.ajax({
                url: "/admin/api/disk/list/",
                data: query,
                dataType: "json",
                success: function (data) {
                    var stateObject = {};

                    var currentUrl = window.location.href;
                    var strArr = currentUrl.split("?");
                    if (strArr.length != 0) {
                        currentUrl = strArr[0];
                    }
                    currentUrl += "?p=" + currentFolderId;
                    history.pushState(stateObject, document.title, currentUrl);
                    if (data.state == "ok") {
                        var folderNum = 0;
                        $.each(data.list, function () {
                            if (this.type == "folder") {
                                that.addFileItem(this);
                                folderNum++;
                            }
                        })
                        $(".disk-folder-num").text(folderNum);
                        var fileNum = 0;
                        $.each(data.list, function () {
                            if (this.type != "folder") {
                                that.addFileItem(this);
                                fileNum++;
                            }
                        })
                        $(".disk-file-num").text(fileNum);

                        if ((folderNum + fileNum) == 0) {
                            that.addEmptyInfo();
                        }
                    } else {
                        console.log("erro----加载失败");
                    }
                }
            })
        },

        /**
         * 向页面添加一个文件或文件夹
         * @param paras
         * @returns
         */
        addFileItem: function (paras) {

            var type = this.getType(paras.type);
            paras.type = type;

            var $diskItem = $(template("tpl-disk-item", paras));
            if (paras.sort != null && paras.sort == "first") {
                var $temp = $(".disk-content .disk-item:first-child");
                if ($temp.length == 0) {
                    $(".disk-content").append($diskItem);
                } else {
                    $temp.before($diskItem);
                }
            } else {
                var $temp = $(".disk-content .disk-item:last-child").after();
                if ($temp.length == 0) {
                    $(".disk-content").append($diskItem);
                } else {
                    $temp.after($diskItem);
                }
            }
        }
        ,
        /**
         * 创建文件夹dom
         * @returns
         */
        createFolderItem: function () {
            var $folder = $(template("tpl-folder-add-item", {}));
            var $firstDiskItem = $(".disk-content .disk-item:first-child");
            if ($firstDiskItem.length == 0) {
                $(".disk-content").append($folder);
            } else {
                $firstDiskItem.before($folder);
            }
        },
        /**
         * 创建文件夹
         * @param paras
         * @returns
         */
        createFolder: function (paras) {
            that = this;
            paras.parentId = currentFolderId;
            $.ajax({
                url: "/admin/api/disk/createFolder",
                data: paras,
                dataType: "json",
                success: function (data) {
                    if (data.state == "ok") {
                        data.disk.sort = "first";
                        that.addFileItem(data.disk);
                    } else {
                        console.log("erro----文件创建失败！");
                    }
                }
            })
        },


        getType: function (type) {
        	
            const thumbImg = this.thumbImg;
            
            const that=this;
            
            for (key in thumbImg) {
                if (key == type) {
                    return key;
                }
                if (that.isArray(thumbImg[key])) {
                    for (j = 0, len = thumbImg[key].length; j < len; j++) {
                        if (thumbImg[key][j] == type) {
                            return key;
                        }
                    }

                }
            }
            return "miss";
        },


        /**
         * 判断是否是数组
         * @param obj
         * @returns
         */
        isArray: function (obj) {
            return Object.prototype.toString.call(obj) === '[object Array]';
        }
        ,
        isJsonObj: function (str) {
            if (typeof (str) == "object" && Object.prototype.toString.call(str).toLowerCase() == "[object object]" && !str.length) {
                return true;
            }
            return false;
        },

        /**
         * 下载文件
         * @param id
         * @returns
         */
        download: function (id) {
            layui.fl.ajax({
                url: "/admin/api/disk/get/" + id,
                dataType: "json",
                success: function (data) {
                    var $eleForm = $("<form method='get'></form>");
                    $eleForm.attr("action", data.disk.url);
                    $(document.body).append($eleForm);
                    //提交表单，实现下载
                    $eleForm.submit();
                }
            })
        },
        /**
         * 获取文件的下载链接
         * @param id
         * @returns
         */
        getDiskFileUrl: function (id) {
            var fileUrl;
            const fl = layui.fl;
            fl.ajax({
                url: "/admin/api/disk/get/" + id,
                dataType: "json",
                async: false,
                success: function (data) {
                    fileUrl = data.disk.url;
                    $(".copy-link").val(fileUrl);
                }
            })
            return fileUrl;
        }
        ,


        removeFile: function (id) {
            const fl = layui.fl;
            fl.alertConfirm({
                title: "是否确认删除？", text: "删除了就找不回来啦!", then: function () {
                    $.ajax({
                        url: "/admin/api/disk/remove/" + id,
                        dataType: "json",
                        success: function (data) {
                            if (fl.isOk(data)) {
                                $("[data-id='" + id + "']").remove();
                            }
                        }
                    })
                }
            })

        },

        rename: function (element) {
            var $element = $(element);
            var $fileNameElement = $element.find(".file-name");

            var id = $element.data("id");
            var style = $element.find(".file-thumb").attr("style");
            var name = $fileNameElement.find("h3").text();

            var $newElement = $(template("tpl-disk-item-rename", { name: name, id: id, style: style }));
            $fileNameElement.parent().hide();
            $fileNameElement.parent().after($newElement);
        }
        ,
        confirmRename: function (element) {
            const that = this;
            var name = $(element).prev().val();
            var id = $(element).parent().data("id");

            $.ajax({
                url: "/admin/api/disk/update",
                data: { id: id, name: name },
                dataType: "json",
                success: function (data) {
                    if (fl.isOk(data)) {
                        var type = that.getType(data.disk.type);
                        data.disk.type = type;
                        var $afterElement = $(template("tpl-disk-item", data.disk));
                        $(element).parent().parent().replaceWith($afterElement);
                    }
                }
            })
        },

        /**
         * 切换目录
         * @param obj
         * @returns
         */
        changeFolder: function (obj) {
            var id = $(obj).data("id");
            const that = this;
            that.deleteNext(id);
            if (id == 0) {
                $(".folder-nav").addClass("hide");
                $(".folder-goback").addClass("hide");
                $(".folder-all-info").removeClass("hide");
            } else {
                that.convertToSpan(obj);
            }
            that.listDiskItem({ parentId: id });
        }


    };

    disk.bind();
    disk.load();
    exports("disk", disk);
});

