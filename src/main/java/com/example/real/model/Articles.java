package com.example.real.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "Articles")
@Data
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "articles",cascade = CascadeType.ALL)
    private List<Comments> comments = new LinkedList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Favorited> article = new LinkedList<>();

    @OneToMany(mappedBy = "articles",cascade = CascadeType.ALL)
    private List<Tags> tags = new LinkedList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private UserData author;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "body")
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "tagList")
    private ArrayList<String> tagList;

    @Column(name = "description")
    private String description;

    @Column(name = "favorited")
    private boolean favorited;

    @Column(name = "favoritesCount")
    private int favoritesCount;
}
