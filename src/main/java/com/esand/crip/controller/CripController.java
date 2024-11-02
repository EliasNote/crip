package com.esand.crip.controller;

import com.esand.crip.entity.Crip;
import com.esand.crip.service.CripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> processar(@RequestBody Crip crip) {
        return ResponseEntity.ok(cripService.processar(crip));
    }
}
