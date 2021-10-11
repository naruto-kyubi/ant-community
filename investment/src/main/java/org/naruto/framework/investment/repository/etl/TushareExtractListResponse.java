package org.naruto.framework.investment.repository.etl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TushareExtractListResponse {
    private String request_id;
    private String code;
    private String msg;
    private Object data;
}
