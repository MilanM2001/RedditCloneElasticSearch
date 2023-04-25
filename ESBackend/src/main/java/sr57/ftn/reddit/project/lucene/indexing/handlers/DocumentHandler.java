package sr57.ftn.reddit.project.lucene.indexing.handlers;

import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

import java.io.File;

public abstract class DocumentHandler {

    public abstract ElasticCommunity getIndexUnitCommunity(File file);
    public abstract ElasticPost getIndexUnitPost(File file);
    public abstract String getText(File file);
}
