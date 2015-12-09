package com.jiuyi.doctor.es;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.stereotype.Service;

@Service("SearchService")
public class SearchService {

	public void handleSearch(String input) {
		NodeBuilder nodeBuilder = new NodeBuilder();
		Node node = nodeBuilder.clusterName("elasticsearch").node();
		Client client = node.client();
		SearchRequest sr = new SearchRequest();
		ActionFuture<SearchResponse> result = client.search(sr);
		float score = result.actionGet().getHits().getMaxScore();
		System.out.println(score);
		node.close();
	}

}
