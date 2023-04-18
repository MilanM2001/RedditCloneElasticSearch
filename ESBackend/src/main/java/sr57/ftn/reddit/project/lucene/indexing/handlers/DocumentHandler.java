package sr57.ftn.reddit.project.lucene.indexing.handlers;

import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;

import java.io.File;

public abstract class DocumentHandler {

    public abstract ElasticCommunity getIndexUnit(File file);
    public abstract String getText(File file);
}
