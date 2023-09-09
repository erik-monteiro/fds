package com.bcopstein.endpointsdemo1;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface IAcervoRepository 
{
    public List<Livro> getAll();
    public List<String> getTitulos();
    public List<String> getAutores();
    public List<Livro> getLivrosDoAutor(@RequestParam(value = "autor") String autor);
    public ResponseEntity<Livro> getLivroTitulo(@PathVariable("titulo") String titulo);
    public boolean cadastraLivroNovo(@RequestBody final Livro livro);
    public boolean removeLivro(int codigo);
    public int getNumeroDeObrasDoAutor(@RequestParam(value = "autor") String autor);
    public int getNumeroDeObrasDoAutorRecentes(@RequestParam(value = "autor") String autor, @RequestParam(value = "ano") int ano);
    public Map<String, Double> calcularMediaObrasPorAutor();
}
