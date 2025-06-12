package com.example.api_ecotour.service;

import com.example.api_ecotour.DTOs.MidiaResponseDTO;
import com.example.api_ecotour.model.Midia;
import com.example.api_ecotour.repository.MidiaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MidiaService {

    @Autowired
    private MidiaRepository midiaRepository;

    // READ BY ID
    @Transactional(readOnly = true)
    public MidiaResponseDTO buscarPorId(UUID id) {
        Midia midia = midiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mídia não encontrada com ID: " + id));
        return convertToResponseDTO(midia);
    }

    // READ BY COMENTARIO ID
    @Transactional(readOnly = true)
    public List<MidiaResponseDTO> buscarPorComentarioId(UUID comentarioId) {
        List<Midia> midias = midiaRepository.findByComentarioId(comentarioId);
        return midias.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // DELETE
    public void deletarMidia(UUID id) {
        if (!midiaRepository.existsById(id)) {
            throw new EntityNotFoundException("Mídia não encontrada com ID: " + id);
        }
        midiaRepository.deleteById(id);
    }

    // GET IMAGE BYTES
    @Transactional(readOnly = true)
    public byte[] obterImagemBytes(UUID id) {
        Midia midia = midiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mídia não encontrada com ID: " + id));
        return midia.getImageData();
    }

    // GET MIDIA TYPE
    @Transactional(readOnly = true)
    public String obterTipoMidia(UUID id) {
        Midia midia = midiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mídia não encontrada com ID: " + id));
        return midia.getType();
    }

    // Conversão para DTO
    private MidiaResponseDTO convertToResponseDTO(Midia midia) {
        // Gerar URL da imagem ao invés de retornar base64
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/midias/{id}/imagem")
                .buildAndExpand(midia.getId())
                .toUriString();

        return new MidiaResponseDTO(
                midia.getId(),
                midia.getNome(),
                midia.getType(),
                imageUrl);
    }
}