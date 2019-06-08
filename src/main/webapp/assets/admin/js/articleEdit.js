define([
    'jquery',
    'layui'
], function ($, layui) {



    const articleEdit = {
        layui: layui,
        isChange: false,
        editorType: "",
        isInit: false,

        bind: function () {
            form = this.layui.form;
            form.render();

            form.on('submit(publish)', function (data) {
                article.save(data.field, 1);
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            });


            form.on('switch(isTop)', function (data) {
                $('input[name="isTop"]').val(data.elem.checked);
            });

            form.on('switch(allowComment)', function (data) {
                $('input[name="allowComment"]').val(data.elem.checked);
            });
            form.on('switch(isOriginal)', function (data) {
                $('input[name="isOriginal"]').val(data.elem.checked);

            });

            $('#tags').tagsInput({
                width: '100%',
                height: '26px',
                defaultText: '请输入文章标签',
                onRemoveTag: function () {
                    article.isChange = true;
                }, onChange: function () {
                    article.isChange = true;
                }, onAddTag: function () {
                    article.isChange = true;
                }
            });

            $("#multiple-sel").select2({
                width: '100%'
            });

            //监听seletc2 
            $("body").on("change", "#articleForm #multiple-sel", function (e) {
                article.isChange = true;
            })
 
            //监听input变化
            $("body").on("input propertychange", "#articleForm input", function (event) {
                article.isChange = true;
            });

            //监听mditor编辑
            $('body').on('input propertychange', ".editor textarea", function () {
                article.isChange = true;
            })



            $("body").on("click", ".switch-editor", function () {
                confirmsSwicthEditor(this);
            })

        },
        pjaxLoad: function () {

        },
        init: function () {
            this.initArticleInfo();
            //this.autoSaveCache();
            this.editorType = $('#contentType').val();

            editorSet.init();
            swicthEditorByType("ueditor");
        }, getArticleId: function () {
            return $("input[name='id']").val();
        }, editArticle: function (paras) {
            fl.ajax({
                url: "/admin/api/article/edit",
                data: paras.fdata,
                async: false,
                success: function (data) {
                    articleCache.removeCurrentCache();
                    paras.success(data);
                }
            })
        }, setTags: function (tags) {
            if (!(tags && tags.length > 0)) {
                return;
            }
            $.each(tags.split(","), function (index, item) {
                $('#tags').addTag(item);
            })
        }, setCategorys: function (categorys) {
            $.each(categorys, function (index, item) {
                $("#multiple-sel option[value='" + item + "']").attr("selected", true)
            })
        }, initArticleInfo: function () {
     
        }, getSelectedTag: function () {
            var data = [];
            var tags = $("#tags").val();
            if (fl.notBlank(tags)) {
                var tagArr = tags.split(",");
                $.each(tagArr, function (index, item) {
                    data.push({ name: "tag[" + index + "].mname", value: item });
                    data.push({ name: "tag[" + index + "].type", value: "tag" });
                })
            }
            return data;
        }, getSelectedCategory: function () {
            var data = [];
            var categoryIds = $("#multiple-sel").select2("val");
            var categoryVal;
            for (var i = 0, size = categoryIds.length; i < size; i++) {
                categoryVal = $("option[value='" + categoryIds[i] + "']").text();
                data.push({ name: "category[" + i + "].mname", value: categoryVal });
                data.push({ name: "category[" + i + "].type", value: "category" });
            }
            return data;
        },
        /**
                 * 保存文章
         * @returns
         */
        save: function (fdata, state) {

            fdata.content = editorSet.getContent();
            
            fdata.state = state;



            if (fl.isBlank(fdata.title)) {
                fl.alertWarn('标题不能为空');
                return;
            }

            if (fl.isBlank(fdata.content)) {
                fl.alertWarn('请输入文章内容');
                return;
            }

            if (fdata.state != undefined) {
                $('input[name="state"]').val(state);
            }

            fdata = fl.objectToArray(fdata);

            fdata = fl.mergeJson(fdata, this.getSelectedTag());
            fdata = fl.mergeJson(fdata, this.getSelectedCategory());

            article.editArticle({
                fdata: fdata, success: function (data) {
                    var time = "[" + new Date() + "]";
                    $(".hint-msg").text((data.article.state == 0 ? "草稿保存成功！" : "发布成功！") + time);
                    $("input[name='id']").val(data.article.id);
                    var host = window.location.host;
                    var currentUrl = window.location.protocol + "//" + host + "/admin/article/edit/" + data.article.id;

                    if (window.location.href != currentUrl) {
                        history.pushState({}, document.title, currentUrl);
                    }

                    layer.msg((data.article.state == 0 ? "草稿保存成功！" : "发布成功！"));
                }
            });
        }
    };

    return articleEdit;
});


var meditor, htmlEditor;

var attach_url = $('#attach_url').val();

Dropzone.autoDiscover = false;

editorSet = {
    editorArr: [],
    currEditor: null,
    editorType: null,
    initSelector: "#content",
    turndownService: new TurndownService(),
    setEditor: function (key, editor) {
        this.editorArr[key] = editor;
    },
    getEditor: function (type) {
        this.currEditor = this.editorArr[type];
        return this.getCurrentEditor();
    },
    getCurrentEditor: function () {
        return this.currEditor;
    },
    init: function () {
        this.initUeditor();
        this.initEditorMd();
    }, initUeditor: function () {
        let editor = {
            ueditor: null,
            getEditor: function () {
                return this.ueditor;
            },
            init: function (content) {
                this.ueditor = UE.getEditor('article-ueditor', {
                    initialFrameHeight: 400,
                    initialContent: content
                });
                editorSet.editorType = "html";
            }
        };
        this.setEditor("ueditor", editor);
    }, initEditorMd() {
        let editor = {
            editormd: null,
            getEditor: function () {
                return this.editormd;
            },
            init: function (content) {
                this.editormd = editormd("meditor", {
                    width: "100%",
                    height: 640,
                    markdown: content,
                    syncScrolling: "single",
                    path: '/assets/plugins/editor/lib/',
                });

                editorSet.editorType = "markdown";
            }
        };
        this.setEditor("editormd", editor);
    }, getDefaultContent: function () {
        return $(this.initSelector).html();
    },
    getContent: function (type, isDefault) {

        if (isDefault) {
            return this.getDefaultContent();
        }

        if (!type) {
            type = $("#contentType").val();
        }

        let currEditor = this.getCurrentEditor();

        if (!currEditor) {
            return this.getDefaultContent();
        }

        let editorContentType = "html";
        let content;
        if (currEditor.ueditor) {
            content = currEditor.ueditor.getContent();
        } else if (currEditor.meditor) {
            editorContentType = "mardown";
            content = currEditor.meditor.getMarkdown();
        }

        if (type == "html") {
            if (editorContentType == "html") {
                return content;
            } else {
                return currEditor.meditor.getHtml();
            }
        } else {
            if (editorContentType == "html") {
                return this.turndownService.turndown(content);
            } else {
                return content;
            }
        }

    }
};



function confirmsSwicthEditorByType(type) {
    let swicthEditorIndex = layer.confirm('切换编辑器可能会丢失部分样式，是否继续？', {
        btn: ['继续', '算了吧'] //按钮
    }, function () {

        swicthEditorByType(type);
        layer.close(swicthEditorIndex);

    }, function () {
        layer.close(swicthEditorIndex);
    });
}

function swicthEditorByType(type) {
    let content = editorSet.getContent(type == "ueditor" ? "html" : "markdown")
    let currEditor = editorSet.getEditor(type);
    currEditor.init(content);

    $("[data-editor]").removeClass("selected-editor-btn");
    $('[data-editor="' + type + '"').addClass("selected-editor-btn");

    if (type == "ueditor") {

        //切换为html编辑器
        $('#md-container').hide();
        $('#html-container').show();

        $('#contentType').val("html");

        article.editorType = "markdown";
        article.meditor = currEditor.getEditor();

    } else if (type == "editormd") {
        //切换为markdown编辑器

        $('#md-container').show();
        $('#html-container').hide();
        $('#contentType').val("markdown");
        article.editorType = "html";
        article.htmlEditor = currEditor.getEditor();

    }
}

function confirmsSwicthEditor(obj) {
    confirmsSwicthEditorByType($(obj).data("editor"));
}


var articleCache = {
    constant: {
        cacheKey: 'article-cache-',
        autoTime: 10000
    }, getCurrentCache: function (id) {
        return this.getCache(article.getArticleId());
    }, getCache: function (id) {
        var cache = localStorage.getItem(this.constant.cacheKey + id);
        if (!cache) {
            return cache;
        }
        return eval('(' + cache + ')');;
    }, saveCache: function (id, data) {
        this.removeCache(this.constant.cacheKey + id);
        localStorage.setItem(this.constant.cacheKey + id, data);
    }, removeCache: function (id) {
        localStorage.removeItem(this.constant.cacheKey + id);
    }, removeCurrentCache: function () {
        this.removeCache(article.getArticleId());
    }
}

function getPlainText(content) {
    content = content.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
    content = content.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
    //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
    content = content.replace(/&nbsp;/ig, '');//去掉&nbsp;
    content = content.replace(/\s/g, ''); //将空格去掉
    return content;
}

$(document).ready(function () {


    //保存草稿
    $("#draft").click(function () {
        save(0);
    });
    //保存并发布
    $("#subArticle").click(function () {
        save(1);
    })

    //mditor = window.mditor = Mditor.fromTextarea(document.getElementById('meditor'));

    $('.modal').on('shown.bs.modal', function (e) {
        // 关键代码，如没将modal设置为 block，则$modala_dialog.height() 为零  
        $(this).css('display', 'block');
        var windowHeight = $(window).height();
        var modalHeight2 = $('.modal').height();
        var modalHeight = windowHeight / 2 - modalHeight2 / 2;
        $(this).find('.modal-dialog').css({
            'margin-top': modalHeight
        });
    });

    /*
     * 切换编辑器
     * */
    $('#switch-btn').click(function () {
        article.changeEditor();
    });

    $(".toggle").each(function () {
        var on = $(this).attr("on") != "false";
        $(this).toggles({
            on: on,
            text: {
                on: '开启',
                off: '关闭'
            }
        });
    })

    var thumbdropzone = $('.dropzone');

    // 缩略图上传
    $("#dropzone").dropzone({
        url: "/admin/api/upload/thumb",
        paramName: "upfile",
        filesizeBase: 1024,//定义字节算法 默认1000
        maxFilesize: '10', //MB
        fallback: function () {
            fl.alertError('暂不支持您的浏览器上传!');
        },
        acceptedFiles: 'image/*',
        dictFileTooBig: '您的文件超过10MB!',
        dictInvalidInputType: '不支持您上传的类型',
        init: function () {
            this.on('success', function (files, result) {
                if (fl.isOk(result)) {
                    var url = result.info.url;
                    thumbdropzone.css('background-image', 'url(' + url + ')');
                    $('.dz-image').hide();
                    $('input[name="thumbImg"]').val(url);
                    article.saveArticleCache();
                }
            });
            this.on('error', function (a, errorMessage, result) {
                if (!result.success && result.msg) {
                    tale.alertError(result.msg || '缩略图上传失败');
                }
            });
        }
    });
});

$(function () {

    article.init();


})


