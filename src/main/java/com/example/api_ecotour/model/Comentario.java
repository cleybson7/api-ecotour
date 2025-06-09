package com.example.api_ecotour.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    
//    (message = "O nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    
    @NotNull(message = "A avaliação é obrigatória")
    @Min(value = 1, message = "A avaliação mínima é 1 estrela")
    @Max(value = 5, message = "A avaliação máxima é 5 estrelas")
    private Integer avaliacao;
    
    // Opção 1: Lista de Blobs para múltiplas mídias
    @ElementCollection
    @Column(name = "arquivo_blob")
    @Lob
    private List<Blob> medias = new ArrayList<>();
}
