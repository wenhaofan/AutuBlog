package com.autu.common.keys;
 
 
import com.jfinal.kit.StrKit;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Const;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;
 
/**
 * KeysDirective
 */
public class KeysDirective extends Directive {
	
	static final String KEYS_DIRECTIVE="_KEYS_DIRECTIVE";
	
	private String id;
	
	public void setExprList(ExprList exprList) {
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #keys directive can not be blank", location);
		}
		if (exprList.length() > 1) {
			throw new ParseException("Only one parameter allowed for #keys directive", location);
		}
		Expr expr = exprList.getExpr(0);
		if (expr instanceof Const && ((Const)expr).isStr()) {
		} else {
			throw new ParseException("The parameter of #keys directive must be String", location);
		}
		
		this.id = ((Const)expr).getStr();
	}
	
	
	public void exec(Env env, Scope scope, Writer writer) {
		String beforeKey=(String)scope.get(KeysDirective.KEYS_DIRECTIVE);
 
		String key = StrKit.isBlank(beforeKey) ? id : beforeKey + "." + id;
		scope.set(KEYS_DIRECTIVE, key);
	
		stat.exec(env, scope, writer);
	
	}
	
	public boolean hasEnd() {
		return true;
	}
}