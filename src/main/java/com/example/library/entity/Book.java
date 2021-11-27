package com.example.library.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String title;
    @Column(columnDefinition = "text", nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer publishDate;

    @ManyToMany(mappedBy = "books")
    private Set<User> users = new HashSet<>();
}
