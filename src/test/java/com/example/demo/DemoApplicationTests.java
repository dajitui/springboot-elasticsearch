package com.example.demo;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	TestRepository testRepository;

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	@Test
	public void contextLoads() {
		test t=new test();
		t.setId(3);
		t.setName("大鸡腿哦哦哦");
		testRepository.save(t);
	}

	@Test
	public void a(){
		Iterable<test> iterable=testRepository.findAll();
		for(test t:iterable){
			System.out.println(t.getId()+" "+t.getName());
		}
	}

	@Test
	public void b(){
		NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
		queryBuilder.withIndices("test1");
		queryBuilder.withTypes("test2");
		String keyword="大家";
		queryBuilder.withHighlightFields(new HighlightBuilder.Field("name").preTags("<font style='color:red;'>").postTags("</font>"));
		queryBuilder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name",keyword)));

		List<test> list=elasticsearchTemplate.query(queryBuilder.build(), new ResultsExtractor<List<test>>() {
			@Override
			public List<test> extract(SearchResponse searchResponse) {
				List<test> list=new ArrayList<test>();

				for(SearchHit hit:searchResponse.getHits()){
					test t=new test();
					t.setId(Integer.parseInt(hit.getId()));
					t.setName(hit.getHighlightFields().get("name").fragments()[0].toString());
					list.add(t);
				}
				return list;
			}
		});

		for(test t:list){
			System.out.println(t.toString());
		}


	}


}

