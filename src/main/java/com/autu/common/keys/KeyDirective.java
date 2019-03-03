package com.autu.common.keys;
 
 
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.Template;
import com.jfinal.template.expr.ast.Const;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;

/**
 * KeyDirective
 */
public class KeyDirective extends Directive {
	
	static final String KEY_DIRECTIVE="_KEY_DIRECTIVE";
	
	private String id;
	
	public void setExprList(ExprList exprList) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #key directive can not be blank", location);
		}
		if (exprList.length() > 1) {
			throw new ParseException("Only one parameter allowed for #key directive", location);
		}
		Expr expr = exprList.getExpr(0);
		if (expr instanceof Const && ((Const)expr).isStr()) {
		} else {
			throw new ParseException("The parameter of #key directive must be String", location);
		}
		
		this.id = ((Const)expr).getStr();
	}
	
	
	@SuppressWarnings("unchecked")
	public void exec(Env env, Scope scope, Writer writer) {
		String nameSpace = (String)scope.get(KeysDirective.KEYS_DIRECTIVE);
		String key = StrKit.isBlank(nameSpace) ? id : nameSpace + "." + id;
		Map<String, Template> keyTemplateMap = (Map<String, Template>)scope.get(KeyKit.KEY_TEMPLATE_MAP_KEY);
		if (keyTemplateMap.containsKey(key)) {
			throw new ParseException("Key already exists with key : " + key, location);
		}
		
		keyTemplateMap.put(key, new Template(env, stat));
	}
	
	public boolean hasEnd() {
		return true;
	}
}



