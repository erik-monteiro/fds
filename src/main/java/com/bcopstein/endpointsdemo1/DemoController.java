package com.bcopstein.endpointsdemo1;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/biblioteca")
public class DemoController
{
    private AcervoJDBCImpl acervo;

    public DemoController(AcervoJDBCImpl acervo) {
        this.acervo = acervo;
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    public String getSaudacao() { return "Bem vindo a biblioteca!"; }

    @GetMapping("/livros")
    @CrossOrigin(origins = "*")
    public List<Livro> getLivros() {
        return acervo.getAll();
    }

    // Solucao da dinâmica
    @GetMapping("/titulos")
    @CrossOrigin(origins = "*")
    public List<String> getTitulos() {
        return acervo.getTitulos();
    }

    @GetMapping("/autores")
    @CrossOrigin(origins = "*")
    public List<String> getAutores() {
        return acervo.getAutores();
    }

    // Recebendo uma Qwery String
    // Devolve os livros de um determinado autor
    @GetMapping("/livrosautor")
    @CrossOrigin(origins = "*")
    public List<Livro> getLivrosDoAutor(@RequestParam(value = "autor") String autor) {
        return acervo.getLivrosDoAutor(autor);
    }

    /* 
    // Recebendo Path Parameters
    // Retorna o livro que tem determinado título
    @GetMapping("/livrotitulo/{titulo}")
    @CrossOrigin(origins = "*")
    public Livro getLivroTitulo(@PathVariable("titulo") String titulo) {
        return livros.stream()
               .filter(livro->livro.titulo().equals(titulo))
               .findFirst()
               .orElse(null);   
    }
    */

    // Recebendo Path Parameters - versão usando ResponseEntity
    // Retorna o livro que tem determinado título
    @GetMapping("/livrotitulo/{titulo}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Livro> getLivroTitulo(@PathVariable("titulo") String titulo) {
        return acervo.getLivroTitulo(titulo);
    }


    // Recebendo parâmetros no corpo da mensagem (POST)
    // Cadastra um livro novo
    @PostMapping("/novolivro")
    @CrossOrigin(origins = "*")
    public boolean cadastraLivroNovo(@RequestBody final Livro livro) {
        if (acervo.cadastraLivroNovo(livro)) { return true; }
        return false;
    }

    @GetMapping("/removelivro/{codigo}")
    @CrossOrigin(origins = "*")
    public boolean removeLivro(@PathVariable(value = "codigo") int codigo) {
        if (acervo.removeLivro(codigo)) { return true; }
        return false;
    }

    @GetMapping("/livros/{autor}")
    @CrossOrigin(origins = "*")
    public int getNumeroDeObrasDoAutor(@PathVariable(value = "autor") String autor) {
        return acervo.getNumeroDeObrasDoAutor(autor);
    }

    @GetMapping("/livros/{autor}/{ano}")
    @CrossOrigin(origins = "*")
    public int getNumeroDeObrasDoAutorRecentes(@PathVariable(value = "autor") String autor, @PathVariable(value = "ano") int ano) {
        return acervo.getNumeroDeObrasDoAutorRecentes(autor, ano);
    }

    @GetMapping("/mediaObrasPorAutor")
    @CrossOrigin(origins = "*")
    public Map<String, Double> calcularMediaObrasPorAutor() {
        return acervo.calcularMediaObrasPorAutor();
    }

}