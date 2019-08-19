package org.naruto.framework.article.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.naruto.framework.user.domain.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Article implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @Column(length=500)
    private String title;

    @Column(length=500)
    private String cover;

    @NotBlank(message = "The content cannot be blank ")
    @Column(columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "longtext")
    private String contentHtml;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private long commentCount = 0;

    private long viewCount = 0;

    private long starCount= 0;

    private long likeCount = 0;

    private boolean deleted;

    private boolean recommend;


    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="article_tags",joinColumns={@JoinColumn(name="article_id")}
            ,inverseJoinColumns={@JoinColumn(name="tag_id",nullable =false, updatable = false)})

    private List<Tag> tags;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "owner")
    @Lazy(false)
    private User owner;

    private Date lastCommentAt;

    private String catalogId;

    // draft publish
    private String status;

    // the article id which has been published, current record is the copy.
    private String publishedVersion;

    public Article(String id){ this.id = id;}

}
