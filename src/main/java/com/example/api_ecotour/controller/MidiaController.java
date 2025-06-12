package com.example.api_ecotour.controller;

import com.example.api_ecotour.DTOs.MidiaResponseDTO;
import com.example.api_ecotour.service.MidiaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/midias")
@CrossOrigin(origins = "*")
public class MidiaController {

    @Autowired
    private MidiaService midiaService;

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<MidiaResponseDTO> buscarPorId(@PathVariable UUID id) {
        try {
            MidiaResponseDTO midia = midiaService.buscarPorId(id);
            return ResponseEntity.ok(midia);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // READ BY COMENTARIO ID
    @GetMapping("/comentario/{comentarioId}")
    public ResponseEntity<List<MidiaResponseDTO>> buscarPorComentarioId(@PathVariable UUID comentarioId) {
        List<MidiaResponseDTO> midias = midiaService.buscarPorComentarioId(comentarioId);
        return ResponseEntity.ok(midias);
    }

    // GET IMAGE AS BYTES
    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> obterImagem(@PathVariable UUID id) {
        try {
            byte[] imageData = midiaService.obterImagemBytes(id);
            String tipoMidia = midiaService.obterTipoMidia(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(tipoMidia));
            headers.setContentLength(imageData.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMidia(@PathVariable UUID id) {
        try {
            midiaService.deletarMidia(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}