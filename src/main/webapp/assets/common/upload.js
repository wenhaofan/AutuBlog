jQuery.extend({
	uploadUtil:function(){
	}
});

/**
 * 上传file form
 */
$.uploadUtil.prototype.uploadFileForm=function(paras){
	  if(this.uploadServerUrl==undefined){
			throw "upload.js->uploadServerUrl not defined"
	  }
	  paras.url=this.uploadServerUrl;
	  paras.method='POST';
	  paras.processData=false,        //告诉jQuery不要加工数据
	  paras.dataType='json',
	  paras.contentType=false,   
	
	  $.ajax(paras);

}
/**
 * 上传js file文件
 */
$.uploadUtil.prototype.uploadFile= function (paras) {
	var dataForm=new FormData();
	var file=paras.file;
	
	var resultFile;
	//如果是jquery对象 且类型为input file 则抛出异常
	if(this.isJquery(file)&&this.isFileInput(file)){
		resultFile=$(file)[0].files[0];
	}else{
		resultFile=file;
	}

	if(paras.data){
		$.each(paras.data, function(i) {
			dataForm.append(i,paras.data[i]);
		});
	}
	
	dataForm.append('upfile',resultFile);
	paras.data=dataForm;
	this.uploadFileForm(paras);
}
/**
 * 判断是否是file input
 */
$.uploadUtil.prototype.isFileInput=function(obj){
	var tagName=$(obj)[0].tagName;
	var type=$(obj).attr("type").toLowerCase();
	return tagName=="INPUT"&&type=="file";
}
/**
 * 判断是否是jquery对象
 */
$.uploadUtil.prototype.isJquery=function(obj){
	return obj instanceof jQuery;
}

$.uploadUtil.prototype.uploadServerUrl=undefined;
/**
 * 设置上传路径
 */
$.uploadUtil.prototype.setUploadServerUrl=function(uploadServerUrl){
	this.uploadServerUrl=uploadServerUrl;
}

/**
 * 获取上传路径
 */
$.uploadUtil.prototype.getUploadServerUrl=function(){
	return this.uploadServerUrl;
}