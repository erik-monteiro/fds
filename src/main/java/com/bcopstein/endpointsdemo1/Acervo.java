package com.bcopstein.endpointsdemo1;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
class Acervo
{
    private List<Livro> livros;

    public Acervo(){
        livros = new LinkedList<>();

        livros.add(new Livro(10,"Introdução ao Java","Huguinho Pato",2022));
        livros.add(new Livro(20,"Introdução ao Spring-Boot","Zezinho Pato",2020));
        livros.add(new Livro(15,"Principios SOLID","Luizinho Pato",2023));
        livros.add(new Livro(17,"Padroes de Projeto","Lala Pato",2019));
    }


    public List<Livro> getAll() {
        return livros.stream().toList();
    }

    public List<String> getTitulos() {
        return livros.stream().map(livro -> livro.titulo()).toList();
    }

    public List<String> getAutores() {
        return livros.stream().map(livro -> livro.autor()).toList();
    }

    public List<Livro> getLivrosDoAutor(@RequestParam(value = "autor") String autor) {
        return livros.stream().filter(livro -> livro.autor().equals(autor)).toList();
    }

    public ResponseEntity<Livro> getLivroTitulo(@PathVariable("titulo") String titulo) {
        Livro resp = livros.stream().filter(livro -> livro.titulo()
                            .equals(titulo)).findFirst().orElse(null);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    public boolean cadastraLivroNovo(@RequestBody final Livro livro) {
        livros.add(livro);
        return true;
    }
}