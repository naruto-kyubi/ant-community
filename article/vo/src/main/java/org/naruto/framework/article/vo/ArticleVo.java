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

    private int commentCount = 0;

    private int viewCount = 0;

    private int starCount= 0;

    private int likeCount = 0;

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
