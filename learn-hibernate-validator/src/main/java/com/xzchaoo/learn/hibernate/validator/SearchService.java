package com.xzchaoo.learn.hibernate.validator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SearchService {

	@NotNull
	public SearchResponse search(
		@NotNull SearchRequest searchRequest,
		@Min(1) @Max(4) int threads
	) {
		return new SearchResponse();
	}
}
