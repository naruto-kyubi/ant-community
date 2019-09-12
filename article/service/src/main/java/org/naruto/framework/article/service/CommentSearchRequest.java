package org.naruto.framework.article.service;

import lombok.Data;
import org.naruto.framework.core.repository.Pagination;

@Data
public class CommentSearchRequest {
    private String articleId;
    private Pagination pagination = new Pagination();
}
