package com.example.api_ecotour;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.api_ecotour.model.Comentario;
import com.example.api_ecotour.model.Midia;
import com.example.api_ecotour.repository.ComentarioRepository;

@DataJpaTest
public class ComentarioRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Test
    void aoSalvarComentario_deveGerarIdETimestampAutomaticamente() {
        
        Comentario comentario = new Comentario();
        comentario.setNome("Ana");
        comentario.setEmail("ana@test.com");
        comentario.setAvaliacao(5);
        comentario.setComment("Excelente experiência!");

        //Tire a anotação @CreationTimestamp da classe Comentario
        Comentario comentarioSalvo = comentarioRepository.save(comentario);
        entityManager.flush();

        assertNotNull(comentarioSalvo.getId(),
        "\n\n\n\n\nID deve ser gerado automaticamente\n\n\n\n\n");

        assertNotNull(comentarioSalvo.getCreatedAt(),
        "\n\n\n\n\nCreatedAt deve ser gerado automaticamente\n\n\n\n\n");
    }
    
    @Test
    void aoSalvarMidia_devePersistirDadosBinarios() {

        Comentario comentario = new Comentario();
        comentario.setNome("Usuario Teste");
        comentario.setEmail("teste@email.com");
        comentario.setAvaliacao(5);
        comentario.setComment("Teste comentário");
        
        Comentario comentarioSalvo = entityManager.persistAndFlush(comentario);
        
        Midia midia = new Midia();

        //Mude o nome da foto
        midia.setNome("foto_teste.jpg");
        midia.setType("image/jpeg");

        byte[] dadosBinarios = {0x01, 0x02, 0x03, 0x04, 0x05};

        //Mude os dados binários para null
        midia.setImageData(dadosBinarios);
        midia.setComentario(comentarioSalvo);
        
        Midia midiaSalva = entityManager.persistAndFlush(midia);
        
        assertEquals("foto_teste.jpg", midiaSalva.getNome(), 
        "\n\n\n\n\nNome da midia deve ser persistido corretamente\n\n\n\n\n");

        assertEquals("image/jpeg", midiaSalva.getType(), 
        "\n\n\n\n\nTipo da midia deve ser persistido corretamente\n\n\n\n\n");

        assertArrayEquals(dadosBinarios, midiaSalva.getImageData(), 
            "\n\n\n\n\nDados binários devem ser persistidos corretamente\n\n\n\n\n");

        assertEquals(comentarioSalvo.getId(), midiaSalva.getComentario().getId());
    }
}
