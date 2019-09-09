package org.naruto.framework.article.service;

import lombok.Data;
import org.naruto.framework.core.repository.Pagination;

@Data
public class ArticleSearchRequest{


     private String status;
     private String catalogId;

     private Pagination pagination = new Pagination();

}
