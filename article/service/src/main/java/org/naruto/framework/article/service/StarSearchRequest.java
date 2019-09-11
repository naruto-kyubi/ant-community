package org.naruto.framework.article.service;

import lombok.Data;
import org.naruto.framework.core.repository.Pagination;

@Data
public class StarSearchRequest {
    private String userId;
    private Pagination pagination = new Pagination();
}
