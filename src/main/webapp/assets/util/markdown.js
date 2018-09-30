$.extend({
	markdownUtil:function(){
		
	}
})

/**将markdown转为html*/
$.markdownUtil.prototype.toHtml=function(content){
	  var converter = new showdown.Converter(); //初始化转换器
	  var htmlcontent  = converter.makeHtml(content); //将MarkDown转为html格式的内容
   return htmlcontent;
}