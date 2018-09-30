/**
 *  fl全局函数对象   var fl = new $.fl();
 */
$.extend({
    fl: function () {
    },
    constant: function () { //常量池
        return {
            ///-------文件常量----------
            MAX_FILES: 10,//一次队列最大文件数
        }
    }
});



$.fl.prototype.isOk = function (data){
	var b= data&&data.state=="ok";
	if(!b&&(!data.isHint)){
		var msg=(data&&data.msg)||'出bug了！';
		this.alertError({title:msg});
	}
	return b;
}
/**
 * 成功弹框
 * @param options
 */
$.fl.prototype.alertOk = function (options) {
    options.title = options.title || '操作成功';
    options.text = options.text;
    options.showCancelButton = false;
    options.showCloseButton = false;
    options.type = 'success';
    this.alertBox(options);
};

/**
 * 弹出成功，并在500毫秒后刷新页面
 * @param text
 */
$.fl.prototype.alertOkAndReload = function (title) {
    this.alertOk({
        title: title, then: function () {
            setTimeout(function () {
                window.location.reload();
            }, 500);
        }
    });
};
/**
 * 警告弹框
 * @param options
 */
$.fl.prototype.alertWarn = function (options) {
    options = options.length ? {title: options} : ( options || {} );
    options.title = options.title || '警告信息';
    options.text = options.text;
    options.timer = 3000;
    options.type = 'warning';
    this.alertBox(options);
};

/**
 * 询问确认弹框，这里会传入then函数进来
 * @param options
 */
$.fl.prototype.alertConfirm = function (options) {
    options = options || {};
    options.title = options.title || '确定要删除吗？';
    options.text = options.text;
    options.showCancelButton = true;
    options.type = 'question';
    this.alertBox(options);
};

/**
 * 错误提示
 * @param options
 */
$.fl.prototype.alertError = function (options) {
    options = options.length ? {title: options} : ( options || {} );
    options.title = options.title || '错误信息';
    options.text = options.text;
    options.type = 'error';
    this.alertBox(options);
};


/**
 * 公共弹框
 * @param options
 */
$.fl.prototype.alertBox = function (options) {
    swal({
        title: options.title,
        text: options.text,
        type: options.type,
        timer: options.timer || 9999,
        showCloseButton: options.showCloseButton,
        showCancelButton: options.showCancelButton,
        showLoaderOnConfirm: options.showLoaderOnConfirm || false,
        confirmButtonColor: options.confirmButtonColor || '#3085d6',
        cancelButtonColor: options.cancelButtonColor || '#d33',
        confirmButtonText: options.confirmButtonText || '确定',
        cancelButtonText: options.cancelButtonText || '取消'
    }).then(function (e) {
        options.then && options.then(e);
    }).catch(swal.noop);
};

/**
 * 全局post函数
 *
 * @param options   参数
 */
$.fl.prototype.ajax = function (options) {
	NProgress.start();  
    var self = this;
    $.ajax({
        type: options.type||"post",
        url: options.url,
        data: options.data || {},
        async: options.async || false,
        dataType: options.dataType||"json",
        success: function (data) {
           data.isHint=options.isHint;
           if(self.isOk(data)){
        	   options.success(data);
           }else{
        	   if(options.fail){
        		   options.fail(data);
        	   } 
           }
           NProgress.done();
        },
        error: function (e) {
            console.log('异常', e);
            if(options.error){
            	options.error();
            }
            NProgress.done();
        }
    });
};


