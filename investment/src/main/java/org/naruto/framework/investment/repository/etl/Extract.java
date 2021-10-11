package org.naruto.framework.investment.repository.etl;

public interface Extract {
    public void extractList(ExtractRequest extractRequest);
    public void extractSingleObject(ExtractRequest extractRequest);
}
