package org.naruto.framework.article.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.naruto.framework.article.domain.Star;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StarVo {
    private Star star;
    private Long starCount;
}
