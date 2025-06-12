package com.example.api_ecotour.repository;

import com.example.api_ecotour.model.Midia;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, UUID> {
    List<Midia> findByComentarioId(UUID comentarioId);

    @Modifying
    @Transactional
    void deleteByComentarioId(UUID comentarioId);

    @Query("SELECT COUNT(m) FROM Midia m WHERE m.comentario.id = :comentarioId")
    Long countByComentarioId(@Param("comentarioId") UUID comentarioId);
}
