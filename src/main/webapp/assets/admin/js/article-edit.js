var mditor, htmlEditor;

var markdownUtil=new $.markdownUtil();
var attach_url = $('#attach_url').val();

Dropzone.autoDiscover = false;

var article={
	isChange:false,
	init:function(){
		this.initArticleInfo();
		this.autoSaveCache();
	},getArticleId:function(){
		return $("input[name='id']").val();
	},editArticle:function(paras){
	   fl.ajax({
		   url:"/admin/api/article/edit",
		   data:paras.fdata,
		   async:false,
		   success:function(data){
			    articleCache.removeCurrentCache();
				paras.success(data);
		   }
	   })
	},getContent:function (){
		var fmtType= $('#fmtType').val();
		var content = fmtType == 'markdown' ? mditor.value : htmlEditor.summernote('code');
		if(fmtType == 'markdown'){
		    content=markdownUtil.toHtml(content);
		}
		return content;
	},setTags:function(tags){
		if(!(tags&&tags.length>0)){
			return;
		}
		$.each(tags.split(","),function(index,item){
			$('#tags').addTag(item);
		})
	},setCategorys:function(categorys){
		$.each(categorys,function(index,item){
			$("#multiple-sel option[value='"+item+"']").attr("selected",true)
		})
	},saveArticleCache:function(){
		try {
			if(!this.isChange){
				return false;
			}
			this.isChange=false;
			
			var cacheInfo={};
			cacheInfo.content=article.getContent();
			cacheInfo.title=$("#articleForm input[name='title']").val();
			cacheInfo.tags=$("#tags").val();
			cacheInfo.categorys=$("#multiple-sel").select2("val");
			cacheInfo.identify=$("#articleForm input[name='identify']").val();
			cacheInfo.thumbImg=$("#articleForm input[name='thumbImg']").val();
			var jsonStr=JSON.stringify(cacheInfo); 
			
			if(getPlainText(cacheInfo.content)||cacheInfo.title||cacheInfo.tags
					||cacheInfo.categorys.length>0||cacheInfo.identify){
				articleCache.saveCache(article.getArticleId(),jsonStr); 
				console.log("自动保存成功！");
				$(".hint-msg").text("自动保存成功！["+new Date()+"]");
			}
		} catch (e) {
			console.log(e);
			console.log("自动保存失败！");
		}
	},autoSaveCache:function(){
		setInterval(() => {
			this.saveArticleCache();
		}, articleCache.constant.autoTime);
	},initArticleInfo:function(){
		var cacheData=articleCache.getCurrentCache();
		
		if(!cacheData){
			tagInit();
			$('#html-container .note-editable').empty().html($("#tpl-content").html());
			return;
		}
		if(cacheData.content){
			$('#html-container .note-editable').empty().html(cacheData.content);
		}
		if(cacheData.identify){
			$("#articleForm input[name='identify']").val(cacheData.identify);
		}
		if(cacheData.title){
			$("#articleForm input[name='title']").val(cacheData.title);
		}
		if(cacheData.thumbImg){
			$("#articleForm input[name='thumbImg']").val(cacheData.thumbImg);
			var $thumbdropzone= $('.dropzone');
			$thumbdropzone.css('background-image', 'url('+ cacheData.thumbImg +')');
			$thumbdropzone.css('background-size', 'cover');
             $('.dz-image').hide();
		}
 
		if(cacheData.tags){
			this.setTags(cacheData.tags);
		}
		if(cacheData.categorys){
			this.setCategorys(cacheData.categorys);
		}
		
		$(".hint-msg").text("本地存储读取成功！");
	}
	
}

var articleCache={
	constant:{
		cacheKey: 'article-cache-',
		autoTime:10000
	},getCurrentCache:function(id){
		return this.getCache(article.getArticleId());
	},getCache:function(id){
		var cache=localStorage.getItem(this.constant.cacheKey+id);
		if(!cache){
			return cache;
		}
		return  eval('(' + cache + ')');;
	},saveCache:function(id,data){
		this.removeCache(this.constant.cacheKey+id);
		localStorage.setItem(this.constant.cacheKey+id, data );	
	},removeCache:function(id){
		localStorage.removeItem(this.constant.cacheKey+id);
	},removeCurrentCache:function(){
		this.removeCache(article.getArticleId());
	} 
}

function getPlainText(content){
   content = content.replace(/<\/?[^>]*>/g,''); //去除HTML tag
   content = content.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
            //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
   content=content.replace(/&nbsp;/ig,'');//去掉&nbsp;
   content=content.replace(/\s/g,''); //将空格去掉
   return content;
}


 
 
function setSelectedTag(data){
    var tags=$("#tags").val();
    if(notNull(tags)){
    	var tagArr=tags.split(",");
    	$.each(tagArr,function(index,item){
    		data.push({name:"tag["+index+"].mname",value:item});
    		data.push({name:"tag["+index+"].type",value:"tag"});
    	})
    }
}
function setSelectedCategory(data){
	var categoryIds=$("#multiple-sel").select2("val");
	var categoryVal;
	for(var i=0,size=categoryIds.length;i<size;i++){
		categoryVal=$("option[value='"+categoryIds[i]+"']").text();
		data.push({name:"category["+i+"].mname",value:categoryVal});
		data.push({name:"category["+i+"].type",value:"category"});
	}
	return data;
}

 
/**
 * 保存文章
 * @returns
 */
function save(state){
	var content = article.getContent();
    var title = $('#articleForm input[name=title]').val();
    if (title == '') {
    	fl.alertWarn('标题不能为空');
        return;
    }
  
    if (content.length<=0) {
    	fl.alertWarn('请输入文章内容');
        return;
    }
    $("#content-editor").val(content);
    
    if(state!=undefined){
    	$('input[name="state"]').val(state);
    }
    
    var fdata= $("#articleForm").serializeArray();
    
    setSelectedTag(fdata);
    setSelectedCategory(fdata);
   
    article.editArticle({fdata:fdata,success:function(data){
    	  var time="["+new Date()+"]";
    	  $(".hint-msg").text((data.article.state==0?"草稿保存成功！":"发布成功！")+time);
    	  $("input[name='id']").val(data.article.id);
    	  var host=window.location.host;
    	  var currentUrl=window.location.protocol+"//"+host+"/admin/article/edit/"+data.article.id;
    	  
    	  if(window.location.href!=currentUrl){
    		  history.pushState({}, document.title, currentUrl);
    	  } 
    }});
}


$(document).ready(function () {
 
	
	//保存草稿
	$("#draft").click(function(){
		 save(0);
	});
	//保存并发布
	$("#subArticle").click(function(){
		save(1);
	})
    mditor = window.mditor = Mditor.fromTextarea(document.getElementById('md-editor'));
    // 富文本编辑器
    htmlEditor = $('.summernote').summernote({
        lang: 'zh-CN',
        height: 340,
        placeholder: '',
        //上传图片的接口
        callbacks:{
            onImageUpload: function(files) {
            	 var uploadUtil=new $.uploadUtil();
                 uploadUtil.setUploadServerUrl("/admin/api/upload/article");
                 uploadUtil.uploadFile({
                	file:files[0],
                	success:function(data){
            		   if(fl.isOk(data)){
                      	 var url =data.info.url;
                          console.log('url =>' + url);
                          htmlEditor.summernote('insertImage', url);
                      } else {
                          fl.alertError(result.msg || '图片上传失败');
                      }
                	}
                });
               
            }
        }
    });

	$('.modal').on('shown.bs.modal', function (e) {  
        // 关键代码，如没将modal设置为 block，则$modala_dialog.height() 为零  
        $(this).css('display', 'block');  
       var windowHeight= $(window).height();
       var modalHeight2=$('.modal').height();
        var modalHeight= windowHeight/ 2 - modalHeight2 / 2;  
        $(this).find('.modal-dialog').css({  
            'margin-top': modalHeight  
        });  
    });  
   
  
	 var fmtType = $('#fmtType').val();
	    // 富文本编辑器
	    if (fmtType != 'markdown') {
	        var this_ = $('#switch-btn');
	        mditor.value = '';
	        $('#md-container').hide();
	        $('#html-container').show();
	        this_.text('切换为Markdown编辑器');
	        this_.attr('type', 'texteditor');
	        $("#md-container > div > div.head > ul > i.item.fa.fa-columns.active.control").trigger("click");
	    } else {
	        var this_ = $('#switch-btn');
	        $('#html-container').hide();
	        $('#md-container').show();
	        $('#fmtType').val('markdown');
	        this_.attr('type', 'markdown');
	        this_.text('切换为富文本编辑器');
	        htmlEditor.summernote("code", "");
	    }
    /*
     * 切换编辑器
     * */
    $('#switch-btn').click(function () {
        var type = $('#fmtType').val();
        var this_ = $(this);
        if (type == 'markdown') {
            // 切换为富文本编辑器
            if($('#md-container .markdown-body').html().length > 0){
                $('#html-container .note-editable').empty().html($('#md-container .markdown-body').html());
                $('#html-container .note-placeholder').hide();

            }
            mditor.value = '';
            $('#md-container').hide();
            $('#html-container').show();
            this_.text('切换为Markdown编辑器');
            $('#fmtType').val('html');
        } else {
            // 切换为markdown编辑器
            if($('#html-container .note-editable').html().length > 0){
                mditor.value = '';
                mditor.value = toMarkdown($('#html-container .note-editable').html());
            }
            $('#html-container').hide();
            $('#md-container').show();
            $('#fmtType').val('markdown');
            this_.text('切换为富文本编辑器');
            htmlEditor.summernote("code", "");
        }
    });
  
    $(".toggle").each(function(){
    	var on=$(this).attr("on")!="false";
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
        paramName:"upfile",
        filesizeBase:1024,//定义字节算法 默认1000
        maxFilesize: '10', //MB
        fallback:function(){
            fl.alertError('暂不支持您的浏览器上传!');
        },
        acceptedFiles: 'image/*',
        dictFileTooBig:'您的文件超过10MB!',
        dictInvalidInputType:'不支持您上传的类型',
        init: function() {
            this.on('success', function (files, result) {
                if(fl.isOk(result)){
                    var url =result.info.url;
                    thumbdropzone.css('background-image', 'url('+ url +')');
                    thumbdropzone.css('background-size', 'cover');
                    $('.dz-image').hide();
                    $('#thumbImg').val(url);
                    article.saveArticleCache();
                }
            });
            this.on('error', function (a, errorMessage, result) {
                if(!result.success && result.msg){
                    tale.alertError(result.msg || '缩略图上传失败');
                }
            });
        }
    });
});

$(function(){
	article.init();
	 //监听seletc2 
	 $("body").on("change","#articleForm #multiple-sel",function(e){
		 article.isChange=true;
	 })
	//避免pjax重复加载js导致事件重复绑定
	if (typeof (adminArticleIsBind) != "undefined") {
	    return;
	}   
	adminArticleIsBind=true;
	
	//监听input变化
	$("body").on("input propertychange","#articleForm input",function(event){
	     article.isChange=true;
	});
	//监听summernote编辑
	 $('body').on('DOMNodeInserted',".note-editable",function(){
		 article.isChange=true;
	 })
	 //监听mditor编辑
	 $('body').on('input propertychange',".editor textarea",function(){
		 article.isChange=true;
	 })
	
	 
	 $('#tags').tagsInput({
        width: '100%',
        height: '35px',
        defaultText: '请输入文章标签',
        onRemoveTag:function(){
        	article.isChange=true;
        },onChange:function(){
        	article.isChange=true;
        },onAddTag:function(){
        	article.isChange=true;
        }
    });
	 
	if($(window).width()<=768){
	    $(".fa-columns").trigger("click");
	    isHideShowHtml=true;
	}
	 $("#multiple-sel").select2({
	        width: '100%'
	 });	
	 
   
 
})

function allow_comment(obj) {
    var this_ = $(obj);
    var on = this_.attr('on');
    if (on == 'true') {
        this_.attr('on', 'false');
        $('#allowComment').val('false');
    }else {
        this_.attr('on', 'true');
        $('#allowComment').val('true');
    }
}
function isTop(obj){
	var this_ = $(obj);
    var on = this_.attr('on');
    if (on == 'true') {
        this_.attr('on', 'false');
        $('#isTop').val('false');
    }else {
        this_.attr('on', 'true');
        $('#isTop').val('true');
    }
}

function allow_ping(obj) {
    var this_ = $(obj);
    var on = this_.attr('on');
    if (on == 'true') {
        this_.attr('on', 'false');
        $('#allowPing').val('false');
    } else {
        this_.attr('on', 'true');
        $('#allowPing').val('true');
    }
}


function allow_feed(obj) {
    var this_ = $(obj);
    var on = this_.attr('on');
    if (on == 'true') {
        this_.attr('on', 'false');
        $('#allowFeed').val('false');
    } else {
        this_.attr('on', 'true');
        $('#allowFeed').val('true');
    }
}

function add_thumbimg(obj) {
    var this_ = $(obj);
    var on = this_.attr('on');
    console.log(on);
    if (on == 'true') {
        this_.attr('on', 'false');
        $('#dropzone-container').addClass('hide');
        $('#thumbImg').val('');
    } else {
        this_.attr('on', 'true');
        $('#dropzone-container').removeClass('hide');
        $('#dropzone-container').show();
    }
}
