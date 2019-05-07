package com.autu.search.lucene;

import org.apache.lucene.analysis.Analyzer;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月10日 下午4:17:25
*/
public class IKAnalyzer extends Analyzer {

    private boolean useSmart = false;

    public IKAnalyzer() {
        this(false);
    }

    public IKAnalyzer(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

    public boolean isUseSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        IKTokenize tk = new IKTokenize(this.useSmart);
        return new TokenStreamComponents(tk);
    }

}