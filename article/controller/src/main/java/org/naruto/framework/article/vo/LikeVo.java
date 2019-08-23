package org.naruto.framework.article.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.naruto.framework.article.domain.Like;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeVo {
    private Like like;
    private Long likeCount;
}
