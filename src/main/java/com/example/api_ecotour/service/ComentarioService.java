package com.example.api_ecotour.service;

import com.example.api_ecotour.DTOs.ComentarioRequestDTO;
import com.example.api_ecotour.DTOs.ComentarioResponseDTO;
import com.example.api_ecotour.DTOs.MidiaResponseDTO;
import com.example.api_ecotour.model.Comentario;
import com.example.api_ecotour.model.Midia;
import com.example.api_ecotour.repository.ComentarioRepository;
import com.example.api_ecotour.repository.MidiaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private MidiaRepository midiaRepository;

    // CREATE
    public ComentarioResponseDTO criarComentario(ComentarioRequestDTO requestDTO) {
        Comentario comentario = new Comentario();
        comentario.setNome(requestDTO.nome());
        comentario.setEmail(requestDTO.email());
        comentario.setAvaliacao(requestDTO.avaliacao());
        comentario.setComment(requestDTO.comment());

        Comentario savedComentario = comentarioRepository.save(comentario);

        // Processar mídias se existirem
        if (requestDTO.midia() != null && !requestDTO.midia().isEmpty()) {
            List<Midia> midias = new ArrayList<>();
            for (MultipartFile file : requestDTO.midia()) {
                if (!file.isEmpty()) {
                    Midia midia = new Midia();
                    midia.setNome(file.getOriginalFilename());
                    midia.setType(file.getContentType());

                    try {
                        midia.setImageData(file.getBytes());
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Erro ao processar arquivo: " + e.getMessage());
                    }

                    midia.setComentario(savedComentario);
                    midias.add(midia);
                }
            }
            if (!midias.isEmpty()) {
                midiaRepository.saveAll(midias);
                savedComentario.setMidia(midias);
            }
        }

        return convertToResponseDTO(savedComentario);
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> listarTodos() {
        List<Comentario> comentarios = comentarioRepository.findAll();
        return comentarios.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // READ BY ID
    @Transactional(readOnly = true)
    public ComentarioResponseDTO buscarPorId(UUID id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comentário não encontrado com ID: " + id));
        return convertToResponseDTO(comentario);
    }

    // UPDATE
    public ComentarioResponseDTO atualizarComentario(UUID id, ComentarioRequestDTO requestDTO) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comentário não encontrado com ID: " + id));

        // Atualizar campos básicos
        comentario.setNome(requestDTO.nome());
        comentario.setEmail(requestDTO.email());
        comentario.setAvaliacao(requestDTO.avaliacao());
        comentario.setComment(requestDTO.comment());

        // Remover mídias existentes se novas mídias foram fornecidas
        if (requestDTO.midia() != null) {
            // Deletar mídias antigas
            midiaRepository.deleteByComentarioId(id);
            comentario.getMidia().clear();

            // Adicionar novas mídias
            if (!requestDTO.midia().isEmpty()) {
                List<Midia> novasMidias = new ArrayList<>();
                for (MultipartFile file : requestDTO.midia()) {
                    if (!file.isEmpty()) {
                        Midia midia = new Midia();
                        midia.setNome(file.getOriginalFilename());
                        midia.setType(file.getContentType());

                        try {
                            midia.setImageData(file.getBytes());
                        } catch (IOException e) {
                            throw new IllegalArgumentException("Erro ao processar arquivo: " + e.getMessage());
                        }

                        midia.setComentario(comentario);
                        novasMidias.add(midia);
                    }
                }
                if (!novasMidias.isEmpty()) {
                    midiaRepository.saveAll(novasMidias);
                    comentario.setMidia(novasMidias);
                }
            }
        }

        Comentario updatedComentario = comentarioRepository.save(comentario);
        return convertToResponseDTO(updatedComentario);
    }

    // DELETE
    public void deletarComentario(UUID id) {
        if (!comentarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Comentário não encontrado com ID: " + id);
        }
        comentarioRepository.deleteById(id);
    }

    // GET IMAGE BYTES BY MIDIA ID
    @Transactional(readOnly = true)
    public byte[] obterImagemBytes(UUID midiaId) {
        Midia midia = midiaRepository.findById(midiaId)
                .orElseThrow(() -> new EntityNotFoundException("Mídia não encontrada com ID: " + midiaId));
        return midia.getImageData();
    }

    // GET MIDIA TYPE BY MIDIA ID
    @Transactional(readOnly = true)
    public String obterTipoMidia(UUID midiaId) {
        Midia midia = midiaRepository.findById(midiaId)
                .orElseThrow(() -> new EntityNotFoundException("Mídia não encontrada com ID: " + midiaId));
        return midia.getType();
    }

    // Conversão para DTO
    private ComentarioResponseDTO convertToResponseDTO(Comentario comentario) {
        List<MidiaResponseDTO> midiaResponseDTOs = null;

        if (comentario.getMidia() != null && !comentario.getMidia().isEmpty()) {
            midiaResponseDTOs = comentario.getMidia().stream()
                    .map(this::convertMidiaToResponseDTO)
                    .collect(Collectors.toList());
        }

        return new ComentarioResponseDTO(
                comentario.getId(),
                comentario.getNome(),
                comentario.getEmail(),
                comentario.getAvaliacao(),
                comentario.getComment(),
                midiaResponseDTOs);
    }

    private MidiaResponseDTO convertMidiaToResponseDTO(Midia midia) {
        // Gerar URL da imagem através do endpoint do comentário
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/comentarios/midia/{midiaId}/imagem")
                .buildAndExpand(midia.getId())
                .toUriString();

        return new MidiaResponseDTO(
                midia.getId(),
                midia.getNome(),
                midia.getType(),
                imageUrl);
    }
}