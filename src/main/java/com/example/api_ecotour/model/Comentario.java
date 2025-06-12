package com.example.api_ecotour.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "comentario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String email;

    private Integer avaliacao;

    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Midia> midia;
}
