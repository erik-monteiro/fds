package com.bcopstein.endpointsdemo1;

import java.util.LinkedList;
import java.util.List;

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
public class DemoController{
    private List<Livro> livros;

    public DemoController(){
        livros = new LinkedList<>();

        livros.add(new Livro(10,"Introdução ao Java","Huguinho Pato",2022));
        livros.add(new Livro(20,"Introdução ao Spring-Boot","Zezinho Pato",2020));
        livros.add(new Livro(15,"Principios SOLID","Luizinho Pato",2023));
        livros.add(new Livro(17,"Padroes de Projeto","Lala Pato",2019));
    }

    @GetMapping("/")
    @CrossOrigin(origins = "*")
    public String getSaudacao() {
        return "Bem vindo as biblioteca central!";
    }

    @GetMapping("/livros")
    @CrossOrigin(origins = "*")
    public List<Livro> getLivros() {
        return livros;
    }

    // Solucao da dinâmica
    @GetMapping("/titulos")
    @CrossOrigin(origins = "*")
    public List<String> getTitulos() {
        return livros.stream()
               .map(livro->livro.titulo())
               .toList();
    }

    @GetMapping("/autores")
    @CrossOrigin(origins = "*")
    public List<String> getAutores() {
        return livros.stream()
               .map(livro->livro.autor())
               .toList();
    }

    // Recebendo uma Qwery String
    // Devolve os livros de um determinado autor
    @GetMapping("/livrosautor")
    @CrossOrigin(origins = "*")
    public List<Livro> getLivrosDoAutor(@RequestParam(value = "autor") String autor) {
        return livros.stream()
               .filter(livro->livro.autor().equals(autor))
               .toList();
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
        Livro resp = livros.stream()
               .filter(livro->livro.titulo().equals(titulo))
               .findFirst()
               .orElse(null);   
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
    }


    // Recebendo parâmetros no corpo da mensagem (POST)
    // Cadastra um livro novo
    @PostMapping("/novolivro")
    @CrossOrigin(origins = "*")
    public boolean cadastraLivroNovo(@RequestBody final Livro livro) {
        livros.add(livro);
        return true;
    }
}