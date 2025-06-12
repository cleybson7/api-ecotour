package com.example.api_ecotour.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "midia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Midia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String type;
    
    @Lob
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id")
    @JsonIgnore
    private Comentario comentario;
}
