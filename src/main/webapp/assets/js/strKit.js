function isNull(str){
	var b=( str==null||str.length==0||str==undefined);
	return b;
}

function notNull(str){
	return !isNull(str);
}