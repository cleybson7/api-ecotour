package com.example.api_ecotour.controller;

import com.example.api_ecotour.DTOs.ComentarioRequestDTO;
import com.example.api_ecotour.DTOs.ComentarioResponseDTO;
import com.example.api_ecotour.service.ComentarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ComentarioResponseDTO> criarComentario(
            @Valid @RequestParam("nome") String nome,
            @Valid @RequestParam("email") String email,
            @Valid @RequestParam("avaliacao") Integer avaliacao,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "midia", required = false) List<MultipartFile> midia) {
        try {
            ComentarioRequestDTO requestDTO = new ComentarioRequestDTO(nome, email, avaliacao, comment, midia);
            ComentarioResponseDTO responseDTO = comentarioService.criarComentario(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listarTodos() {
        List<ComentarioResponseDTO> comentarios = comentarioService.listarTodos();
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        try {
            ComentarioResponseDTO comentario = comentarioService.buscarPorId(id);
            return ResponseEntity.ok(comentario);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/midia/{midiaId}/imagem")
    public ResponseEntity<byte[]> obterImagemDoComentario(@PathVariable UUID midiaId) {
        try {
            byte[] imageData = comentarioService.obterImagemBytes(midiaId);
            String tipoMidia = comentarioService.obterTipoMidia(midiaId);

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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ComentarioResponseDTO> atualizarComentario(
            @PathVariable UUID id,
            @Valid @RequestParam("nome") String nome,
            @Valid @RequestParam("email") String email,
            @Valid @RequestParam("avaliacao") Integer avaliacao,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "midia", required = false) List<MultipartFile> midia) {
        try {
            ComentarioRequestDTO requestDTO = new ComentarioRequestDTO(nome, email, avaliacao, comment, midia);
            ComentarioResponseDTO comentario = comentarioService.atualizarComentario(id, requestDTO);
            return ResponseEntity.ok(comentario);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable UUID id) {
        try {
            comentarioService.deletarComentario(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}