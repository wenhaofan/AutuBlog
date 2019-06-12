layui.define([
    'jquery',
    'form',
    'formSelects',
    'inputTags',
    'editormd',
    'upload'
], function (exports) {

    const  editorSet=  {
            editorArr: [],
            currEditor: null,
            editorType: null,
            initSelector: "#content",
            turndownService: new TurndownService(),
            setEditor: function (key, editor) {
                this.editorArr[key] = editor;
            },
            getEditor: function (type) {
                return this.editorArr[type];
            },
            useEditor:function(type){
                this.currEditor=this.editorArr[type];
                return this.getCurrentEditor();
            },
            getCurrentEditor: function () {
                return this.currEditor;
            },
            init: function () {
                this.initUeditor();
                this.initEditorMd();
            }, initUeditor: function () {
                const that=this;
                let editor = {
                    ueditor: null,
                    getEditor: function () {
                        return this.ueditor;
                    },
                    setEditor:function(ue){
                        this.ueditor=ue;
                    },
                    init: function (content) {

                        const that=this;

                        const editorSelector='article-ueditor';

                        if(editorSet.getEditor("ueditor").getEditor()){
                            UE.delEditor(editorSelector);

                            

                            editorSet.getEditor("ueditor").setEditor(null);
                            $('#'+editorSelector+"-container").html(' 	<script id="article-ueditor" name="content" type="text/plain"></script>');
                        }

                        that.setEditor(UE.getEditor(editorSelector, {
                            initialFrameHeight: 400,
                            initialContent: content
                        }))

                        that.ueditor.ready(function() {
                            that.ueditor.execCommand('serverparam', {
                                'uploadType': 'article',
                                'ueditor':true
                            });
                        });

                        that.editorType = "html";
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

                        if(editorSet.getEditor("editormd").getEditor()){

                            const editormd=editorSet.getEditor("editormd").getEditor();
                            editormd.setMarkdown(content)

                            return;
                        } 

                        this.editormd = editormd("meditor", {
                            width: "100%",
                            height: 640,
                            markdown: content,
                            syncScrolling: "single",
                            path: '/assets/plugins/editor/lib/',
                              saveHTMLToTextarea : true,   
                                imageUpload : true,
                                imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                                imageUploadURL : "/admin/api/upload/editormd",
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
                } else if (currEditor.editormd) {
                    editorContentType = "mardown";
                    content = currEditor.editormd.getMarkdown();
                }

                if (type == "html") {
                    if (editorContentType == "html") {
                        return content;
                    } else {
                        return currEditor.editormd.getHTML();
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

    const articleEdit = {
       
        isChange: false,
        editorType: "",
        isInit: false,
        tagsContent:null,

        previewCover:function(){
           const thumbImg=  $("input[name='thumbImg']").val();
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                shadeClose: true,
                skin: 'yourclass',
                content: '<img src="'+thumbImg+'"/>'
            });
        },

        bind: function () {
            const form =  layui.form;
            const that=this;
            form.render();

            form.on('submit(publish)', function (data) {
                that.save(data.field, 1);
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


            //监听input变化
            $("body").on("input propertychange", "#articleForm input", function (event) {
                that.isChange = true;
            });

            //监听mditor编辑
            $('body').on('input propertychange', ".editor textarea", function () {
                that.isChange = true;
            })



            $("body").on("click", ".switch-editor", function () {
                that.confirmsSwicthEditor(this);
            })
 

        },
        pjaxLoad: function () {
             this.load();
        },

        load: function () {
            
            layui.formSelects.render();
            layui.form.render();
            const upload=layui.upload;

            const that=this;

            that.editorType = $('#contentType').val();

            editorSet.init();
            that.swicthEditorByType("ueditor");

            var inputTags = layui.inputTags;
            that.tagsContent=inputTags.render({
                elem:'#articleTags',//定义输入框input对象
                content: getInitTags(),//默认标签
        
                done: function(value){ //回车后的回调
                    console.log(value)
                }
            }).config.content; 

             
            //执行实例
            var uploadInst = upload.render({
                elem: '.article-upload-thumb' //绑定元素
                ,url: '/upload/' //上传接口
                ,done: function(res){
                //上传完毕回调
                }
                ,error: function(){
                //请求异常回调
                }
            });

             //保存草稿
            $("#draft").click(function () {
                that.save(0);
            });

            //保存并发布
            $("#subArticle").click(function () {
                that.save(1);
            })
 

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

        }, getArticleId: function () {
            return $("input[name='id']").val();
        }, editArticle: function (paras) {
            layui.fl.ajax({
                url: "/admin/api/article/edit",
                data: paras.fdata,
                async: false,
                success: function (data) {
                   
                    paras.success(data);
                }
            })
        },setCategorys: function (categorys) {
            $.each(categorys, function (index, item) {
                $("#multiple-sel option[value='" + item + "']").attr("selected", true)
            })
        }, initArticleInfo: function () {

        }, getSelectedTag: function () {
           
            var tagArr = this.tagsContent,data=new Array();
            $.each(tagArr, function (index, item) {
                data.push({ name: "tag[" + index + "].mname", value: item });
                data.push({ name: "tag[" + index + "].type", value: "tag" });
            })
          
            return data;
        }, getSelectedCategory: function () {
            const categoryNames=layui.formSelects.value('article-edit-catgeory-select', 'name'),data=new Array(); 
            for (var i = 0, size = categoryNames.length; i < size; i++) {
                categoryVal =  categoryNames[i];
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
            const fl=layui.fl;
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

            this.editArticle({
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
        }, getPlainText: function (content) {
            content = content.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
            content = content.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
            //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
            content = content.replace(/&nbsp;/ig, '');//去掉&nbsp;
            content = content.replace(/\s/g, ''); //将空格去掉
            return content;
        },
        confirmsSwicthEditorByType: function (type) {
            const that=this;
            let swicthEditorIndex = layer.confirm('切换编辑器可能会丢失部分样式，是否继续？', {
                btn: ['继续', '算了吧'] //按钮
            }, function () {

                that.swicthEditorByType(type);
                layer.close(swicthEditorIndex);

            }, function () {
                layer.close(swicthEditorIndex);
            });
        },

        swicthEditorByType: function (type) {
            const that=this;
            let content = editorSet.getContent(type == "ueditor" ? "html" : "markdown")
            let currEditor = editorSet.useEditor(type);
            currEditor.init(content);
            
            $("[data-editor]").removeClass("selected-editor-btn");
            $('[data-editor="' + type + '"').addClass("selected-editor-btn");

            if (type == "ueditor") {

                //切换为html编辑器
                $('#md-container').hide();
                $('#html-container').show();

                $('#contentType').val("html");

                that.editorType = "markdown";
                that.meditor = currEditor.getEditor();

            } else if (type == "editormd") {
                //切换为markdown编辑器

                $('#md-container').show();
                $('#html-container').hide();
                $('#contentType').val("markdown");
                that.editorType = "html";
                that.htmlEditor = currEditor.getEditor();

            }
        },

        confirmsSwicthEditor: function (obj) {
            this.confirmsSwicthEditorByType($(obj).data("editor"));
        }


    };
articleEdit.bind();
    articleEdit.load();
    exports("articleEdit", articleEdit);
});


var meditor, htmlEditor;

var attach_url = $('#attach_url').val();
 


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



