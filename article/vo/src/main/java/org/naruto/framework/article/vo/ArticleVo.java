package org.naruto.framework.article.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.naruto.framework.article.domain.Tag;
import org.naruto.framework.user.domain.User;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {

    private String id;

    private String title;

    private String cover;

    private String content;

    private String contentHtml;

    private long commentCount = 0L;

    private long viewCount = 0L;

    private long starCount= 0L;

    private long likeCount = 0L;

    private boolean deleted;

    private boolean recommend;

    private String userId;

    private User owner;

    private Date lastCommentAt;

    private String catalogId;

    private Date createdAt;

    private Date updatedAt;

    private List<Tag> tags;

    private String _type;

    public ArticleVo(String id){ this.id = id;}
}
