package com.esand.crip.controller;

import com.esand.crip.service.CripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/crip")
@RequiredArgsConstructor
public class CripController {

    private final CripService cripService;

    @GetMapping
    public ResponseEntity<String> gerarChave() {
        return ResponseEntity.status(HttpStatus.CREATED).body(cripService.gerarChave());
    }

    @PostMapping
    public ResponseEntity<Object> processar(@RequestHeader("operacao") String operacao,
                                            @RequestHeader("chave") String chave,
                                            @RequestBody Object senha) {
        return ResponseEntity.ok(cripService.processar(senha, chave, operacao));
    }

    @PostMapping("/list")
    public ResponseEntity<List<String>> processarLista(@RequestHeader("operacao") String operacao,
                                                       @RequestHeader("chave") String chave,
                                                       @RequestHeader("senha") List<String> senhas) {
        return ResponseEntity.ok(cripService.processarLista(chave, operacao, senhas));
    }
}
