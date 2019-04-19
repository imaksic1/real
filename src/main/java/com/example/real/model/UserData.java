package com.example.real.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "User_Data")
@Data
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    private List<Articles> article = new LinkedList<>();

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    private List<Comments> comments = new LinkedList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Favorited> favorited = new LinkedList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Following> user = new LinkedList<>();

    @OneToMany(mappedBy = "followingUser")
    private List<Following> followingUser = new LinkedList<>();

    @Column(name = "email")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private Date updatedAt;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "image")
    private String image;

    @Column(name = "token")
    @Size(max = 255)
    private String token;

    @Column(name = "following")
    private boolean following;
}
