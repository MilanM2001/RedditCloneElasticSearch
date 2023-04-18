package sr57.ftn.reddit.project.elasticservice;

import org.elasticsearch.index.query.QueryBuilder;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.SimpleQueryEs;
import sr57.ftn.reddit.project.lucene.search.QueryBuilderCustom;
import sr57.ftn.reddit.project.util.SearchType;

public class SearchQueryGenerator {

    public static QueryBuilder createMatchQueryBuilder(SimpleQueryEs simpleQueryEs) {
        if(simpleQueryEs.getValue().startsWith("\"") && simpleQueryEs.getValue().endsWith("\"")) {
            return QueryBuilderCustom.buildQuery(SearchType.PHRASE, simpleQueryEs.getField(), simpleQueryEs.getValue());
        } else {
            return QueryBuilderCustom.buildQuery(SearchType.MATCH, simpleQueryEs.getField(), simpleQueryEs.getValue());
        }
    }

    public static QueryBuilder createRangeQueryBuilder(SimpleQueryEs simpleQueryEs) {
        return QueryBuilderCustom.buildQuery(SearchType.RANGE, simpleQueryEs.getField(), simpleQueryEs.getValue());
    }
}
