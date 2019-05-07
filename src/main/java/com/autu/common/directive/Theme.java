package com.autu.common.directive;

import java.io.IOException;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Const;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;

/** 
* @author 作者:范文皓
* @createDate 创建时间：2019年4月16日-下午1:18:10 
*/
public class Theme extends Directive {

	private String value;

	@Override
	public void setExprList(ExprList exprList) {
		// TODO Auto-generated method stub
		super.setExprList(exprList);
		
		if (exprList.length() == 0) {
			throw new ParseException("The parameter of #theme directive can not be blank", location);
		}
		if (exprList.length() > 1) {
			throw new ParseException("Only one parameter allowed for #theme directive", location);
		}
		Expr expr = exprList.getExpr(0);
		if (expr instanceof Const && ((Const)expr).isStr()) {
		} else {
			throw new ParseException("The parameter of #theme directive must be String", location);
		}
		
		this.value = ((Const)expr).getStr();
	}

	@Override
	public void exec(Env env, Scope scope, Writer writer) {
		// TODO Auto-generated method stub
		try {
			writer.write("/_view/templates/pinghsu/"+value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
}
