package org.naruto.framework.article.service;

import lombok.Data;
import org.naruto.framework.core.repository.SearchRequest;

@Data
public class ArticleSearchRequest extends SearchRequest{

     private String status;
     private String catalogId;

}
