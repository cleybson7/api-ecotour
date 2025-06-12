package com.example.api_ecotour.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.api_ecotour.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, UUID>{
    List<Comentario> findAllByOrderByCreatedAtDesc();

    List<Comentario> findByAvaliacaoOrderByCreatedAtDesc(Integer avaliacao);

    List<Comentario> findByNomeContainingIgnoreCaseOrderByCreatedAtDesc(String nome);

    @Query("SELECT c FROM Comentario c WHERE c.avaliacao >= :minAvaliacao ORDER BY c.createdAt DESC")
    List<Comentario> findByAvaliacaoMinima(@Param("minAvaliacao") Integer minAvaliacao);

    @Query("SELECT AVG(c.avaliacao) FROM Comentario c")
    Double findAvaliacaoMedia();

    Long countByAvaliacao(Integer avaliacao);
}
