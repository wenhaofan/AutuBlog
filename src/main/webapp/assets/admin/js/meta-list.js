function update(obj, type, id, name, pri) {
	$('input[name="id"]').val(id);
	$('input[name="mname"]').val(name);
	$('input[name="sort"]').val(pri);
	$('input[name="type"]').val(type);
	if (type == "category") {
		$("#submit-btn-tag").hide();
		$("#submit-btn-category").show();
		$("#submit-btn-category").text("修改分类");
	} else {
		$("#submit-btn-category").hide();
		$("#submit-btn-tag").show();
		$("#submit-btn-tag").text("修改标签");
	}
	$("#reset").show();
	var $ul = $(obj).parent().parent();
	$ul.hide();
}

function add(data) {
	fl.ajax({
		url : "/admin/api/meta/add",
		type : "post",
		data : data,
		dataType : "json",
		success : function(data) {
			fl.alertOkAndReload(data.text);
		}
	})
}

function doUpdate(data) {
	fl.ajax({
		url : "/admin/api/meta/update",
		data : data,
		dataType : "json",
		success : function(data) {
			fl.alertOkAndReload(data.text);
		}
	})
}

function remove(id) {
	fl.alertConfirm({
		title : '确认删除吗？',
		then : function() {
			fl.ajax({
				url : "/admin/api/meta/remove/" + id,
				dataType : "json",
				success : function(data) {
					fl.alertOkAndReload(data.text);
				}
			})
		}
	})
}

$(function() {
	
	//避免pjax重复加载js导致事件重复绑定
	if (typeof (adminMetaListIsBind) != "undefined") {
	    return;
	}   
	adminMetaListIsBind=true;
	
	$("a").hover(function() {
		return false;
	})
	$("#reset").click(
			function() {
				var type = $('input[name="type"]').val();
				$("#submit-btn-" + type).text(
						"添加" + (type == "category" ? "分类" : "标签"));
				$('input[name="id"]').val(0);
				$(this).hide();
			})
	$("body").on(
			'click',
			"a",
			function() {
				var left = $(this).position().left
						+ parseFloat($(this).css("marginLeft"));
				var top = $(this).position().top + $(this).height();
				$(this).next().css({
					opacity : 1,
					left : left,
					top : top,
					display : "block"
				})
				$(this).find("span").addClass("layui-nva-mored");
			})

	$(document).bind("click", function(e) {
		var $t = $(e.target).parent();
		var cl = $t.closest(".layui-nav-child").length
		var il = $t.closest(".layui-nav-item").length;
		if (cl == 0 && il == 0) {
			$(".layui-nav-child").each(function() {
				$(this).hide();
				$(this).prev().find("span").removeClass("layui-nva-mored");
			})
		}
	})
})

$( function() {

	var initCategory = {
		type:"category",
		callback : function(categorys) {
			var $metaBlock = $("#category-list");
			var $meta;
			$.each(categorys, function(index, item) {
				$meta = $(template('tpl-meta-list', {
					meta : item,
					type : "category"
				}));
				$metaBlock.append($meta);
			})
		}
	}

	metaUtils.listMeta(initCategory);

	var initTags = {
			type:"tag",
		callback : function(tags) {
			var $metaBlock = $("#tag-list");
			var $meta;
			$.each(tags, function(index, item) {
				$meta = $(template('tpl-meta-list', {
					meta : item,
					type : "tag"
				}));
				$metaBlock.append($meta);
			})
		}
	}
	metaUtils.listMeta( initTags);
})

layui.use('form', function() {
	var form = layui.form;
	// 监听提交
	form.on('submit(metaForm)', function(data) {
		var field = data.field;
		if (field.id) {
			doUpdate(field);
		} else {
			add(field);
		}
		return false;
	});
});

function changeType(type) {
	$("input[name='type']").val(type);
}