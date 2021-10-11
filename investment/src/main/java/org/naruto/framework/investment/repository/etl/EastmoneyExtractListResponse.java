package org.naruto.framework.investment.repository.etl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY,getterVisibility= JsonAutoDetect.Visibility.NONE)
public class EastmoneyExtractListResponse {

    public String Datas;
    private int PageIndex;
    private int TotalCount;
    private int ErrCode;
    private int PageSize;
}
