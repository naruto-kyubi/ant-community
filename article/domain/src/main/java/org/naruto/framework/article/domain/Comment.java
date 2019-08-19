package org.naruto.framework.article.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.naruto.framework.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString

public class Comment {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
//    private String id;
    private String id;

    @NotBlank(message = "The content cannot be blank ")
    @Column(columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "longtext")
    private String contentHtml;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private int replayCount;

    private int likeCount;

    private boolean blocked;

    private boolean deleted;

    private boolean recommend;


    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    private String articleId;

    private String replyId;

    @JsonBackReference
    @ManyToOne(cascade= CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @OrderBy("createdAt asc")
    private List<Comment> children = new ArrayList<>();

    public Comment(String id){this.id = id;}

}
