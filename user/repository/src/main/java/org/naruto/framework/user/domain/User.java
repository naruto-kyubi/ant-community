package org.naruto.framework.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class User implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @Column(length = 20,unique = true)
    private String nickname;

    @Column(length = 20,unique = true)
    private String mobile;


    @Column(length = 50)
    private String password;

    @JsonIgnore
    @Column(length = 50)
    private String passwordSalt;

    @Column(length = 50,unique = true)
    private String email;

    @Column(length = 255)
    private String avatar;

    @Column(length = 255)
    private String profile;

    @Embedded
    private Geographic geographic;

    private String country;

    @Column(length = 255)
    private String  address;

    @Transient
    private String captcha;

    private Long articleCount = 0L;

    private Long likeCount = 0L;

    private Long starCount= 0L;

    private Long fanCount =0L;

    private Long followCount =0L;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

//    @ManyToMany(fetch=FetchType.EAGER)
//    @JoinTable(name= "user_roles", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false) },inverseJoinColumns = { @JoinColumn(name = "role_id", nullable =false, updatable = false) })
//    private Set<Role> roles= new HashSet<>();

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public User(String id){
        this.id = id;
    }

}
