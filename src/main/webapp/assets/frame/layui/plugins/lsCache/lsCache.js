(function(layui, window, factory) {
	if ((typeof exports === 'undefined' ? 'undefined' : _typeof(exports)) === 'object') {
		// 支持 CommonJS
		module.exports = factory();
	} else if (typeof define === 'function' && define.amd) {
		// 支持 AMD
		define(factory);
	} else if (window.layui && layui.define) {
		//layui加载
		layui.define(function(exports) {
			exports('lsCache', factory());
		});
	} else {
		window.formSelects = factory();
	}
})(typeof layui == 'undefined' ? null : layui, window, function() {

	var lsCache = function Common() {

	};

	lsCache.prototype.get = function(key) {
		var cache = localStorage.getItem(key);
		if (!cache) {
			return cache;
		}
		return eval('(' + cache + ')');
		;
	}

	lsCache.prototype.set = function(key, val) {

		this.remove(key);

		localStorage.setItem(key, JSON.stringify(val));

	}

	lsCache.prototype.remove = function(key) {
		localStorage.removeItem(key);
	}

	return new lsCache();
})