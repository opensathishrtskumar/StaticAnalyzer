package com.analyzer.results;

import com.analyzer.rules.NullArgumentRule;

public class NullPointerCheckResult implements Result{

	@Override
	public Class<NullArgumentRule> getRuleClass() {
		return NullArgumentRule.class;
	}

	@Override
	public Object getResult() {
		
		
		return null;
	}

}
