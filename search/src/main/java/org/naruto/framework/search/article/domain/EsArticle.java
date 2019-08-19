package org.naruto.framework.search.article.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.naruto.framework.article.domain.Tag;
import org.naruto.framework.user.domain.User;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
@Document(indexName = "naruto", type = "article")
public class EsArticle {

    @Id
    private String id;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String cover;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String content;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word", analyzer = "ik_smart")
    private String contentHtml;

    @Field(type = FieldType.Long)
    private long commentCount = 0;

    @Field(type = FieldType.Long)
    private long viewCount = 0;

    @Field(type = FieldType.Long)
    private long starCount= 0;

    @Field(type = FieldType.Long)
    private long likeCount = 0;

    @Field(type = FieldType.Boolean)
    private boolean recommend;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(ignoreFields = {"owner"})
    private User owner;

    @Field(type = FieldType.Keyword)
    private String catalogId;

    @Field(type = FieldType.Date)
    private Date lastCommentAt;

    @Field(type = FieldType.Date)
    private Date createdAt;

    @Field(type = FieldType.Date)
    private Date updatedAt;

    @Field(type=FieldType.Nested)
    private List<Tag> tags;

}
