package org.naruto.framework.user.service;

import lombok.Data;
import org.naruto.framework.core.repository.Pagination;

@Data
public class FanSearchRequest {
    private String followUserId;
    private String currentUserId;
    private Pagination pagination = new Pagination();
}
