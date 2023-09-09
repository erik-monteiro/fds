package com.bcopstein.endpointsdemo1;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Primary
class AcervoJDBCImpl implements IAcervoRepository
{
    private JdbcTemplate jdbc;

    @Autowired
    public AcervoJDBCImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public List<Livro> getAll() {
        List<Livro> resp = this.jdbc.query("SELECT * FROM livros", 
                                            (rs, rowNum) -> new Livro(rs.getInt("codigo"), rs.getString("titulo"),
                                                                      rs.getString("autor"), rs.getInt("ano"))
        );
        return resp;
    }

    public List<String> getTitulos() {
        List<String> titulos = this.jdbc.queryForList("SELECT titulo FROM livros", String.class);
        return titulos;
    }    

    public List<String> getAutores() {
        List<String> autores = this.jdbc.queryForList("SELECT autor FROM livros", String.class);
        return autores;
    }

    public List<Livro> getLivrosDoAutor(@RequestParam(value = "autor") String autor) {
        return this.jdbc.query(
            "SELECT * FROM livros WHERE autor = ?", 
            new PreparedStatementSetter() {
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setString(1, autor);
                }
            },
            (rs, rowNum) -> new Livro(rs.getInt("codigo"), rs.getString("titulo"),
                rs.getString("autor"), rs.getInt("ano"))
        );
    }

    // nao achei outra forma de fazer a nao ser essa deprecated
    public ResponseEntity<Livro> getLivroTitulo(@PathVariable("titulo") String titulo) {
        List<Livro> resp = this.jdbc.query(
            "SELECT * FROM livros WHERE titulo = ?", 
            new Object[]{titulo}, 
            (rs, rowNum) -> new Livro(rs.getInt("codigo"), rs.getString("titulo"),
                rs.getString("autor"), rs.getInt("ano"))
        );
    
        if (!resp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resp.get(0));
        } 
    
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    

    public boolean cadastraLivroNovo(@RequestBody final Livro livro) {
        this.jdbc.update("INSERT INTO livros (codigo, titulo, autor, ano) VALUES (?, ?, ?, ?)",
                          livro.codigo(), livro.titulo(), livro.autor(), livro.ano());
        return true;
    }

    public boolean removeLivro(int codigo) {
        String sql = "DELETE FROM livros WHERE codigo = " + codigo;
        this.jdbc.batchUpdate(sql);
        return true;
    }

    public int getNumeroDeObrasDoAutor(@RequestParam(value = "autor") String autor) {
        List<Livro> resp = this.jdbc.query(
            "SELECT * FROM livros WHERE autor = ?",
            new Object[]{autor}, 
            (rs, rowNum) -> new Livro(rs.getInt("codigo"), rs.getString("titulo"),
                rs.getString("autor"), rs.getInt("ano"))
        );
        return resp.size();
    }
    

    public int getNumeroDeObrasDoAutorRecentes(@PathVariable(value = "autor") String autor, @PathVariable(value = "ano") int ano) {
        List<Livro> resp = this.jdbc.query(
            "SELECT * FROM livros WHERE autor = ? AND ano > ?",
            new Object[]{autor, ano},
            (rs, rowNum) -> new Livro(rs.getInt("codigo"), rs.getString("titulo"),
                rs.getString("autor"), rs.getInt("ano"))
        );
        return resp.size();
    }

    public Map<String, Double> calcularMediaObrasPorAutor() {
        Map<String, Double> mediaPorAutor = new HashMap<>();
        
        Map<String, Integer> countPorAutor = this.jdbc.query("SELECT autor, COUNT(*) FROM livros GROUP BY autor", 
            (rs, rowNum) -> new AbstractMap.SimpleEntry<>(rs.getString("autor"), rs.getInt("COUNT(*)")))
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int totalLivros = countPorAutor.values().stream().mapToInt(Integer::intValue).sum();

        countPorAutor.forEach((autor, count) -> {
            double media = (double) count / totalLivros;
            mediaPorAutor.put(autor, media);
        });

        return mediaPorAutor;
    }
}