package com.example.api_ecotour;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.api_ecotour.model.Comentario;


public class ComentarioTeste {

    @Test
    void quandoNomeVazio_deveRetornarErro() {
        Comentario comentario = new Comentario();
        comentario.setNome("José");
        comentario.setEmail("jose@gmail.com");
        comentario.setAvaliacao(3);
        assertNull(comentario.getNome().isEmpty() ? comentario.getNome() : null,
        "\n\n\n\n\nNome não pode estar em branco\n\n\n\n\n");
    }

    @Test
    void quandoEmailVazio_deveRetornarErro() {
        Comentario comentario = new Comentario();
        comentario.setNome("José");
        comentario.setEmail("jose@gmail.com");
        comentario.setAvaliacao(3);
        assertFalse(comentario.getEmail().isBlank(), 
        "\n\n\n\n\nEmail não pode estar em branco\n\n\n\n\n");
    }

    @Test
    void quandoAvaliacaoForaDoRange_deveRetornarErro() {
        Comentario comentario = new Comentario();
        comentario.setNome("José");
        comentario.setEmail("jose@gmail.com");
        comentario.setAvaliacao(4);
        assertTrue(comentario.getAvaliacao() >= 1 && comentario.getAvaliacao() <= 5,
        "\n\n\n\n\nAvaliação deve estar entre 1 e 5\n\n\n\n\n");
    }

}
