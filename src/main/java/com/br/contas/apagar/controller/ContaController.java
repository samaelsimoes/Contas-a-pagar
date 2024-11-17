package com.br.contas.apagar.controller;

import com.br.contas.apagar.domain.Conta;
import com.br.contas.apagar.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@Tag(name = "Contas a Pagar", description = "Operações relacionadas às contas a pagar") // Descrição geral da API
public class ContaController {

    private final ContaService contaService;

    @PostMapping
    @Operation(summary = "Criar uma nova conta a pagar", description = "Cria uma nova conta no sistema")
    public ResponseEntity<Conta> create(@RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.save(conta));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma conta existente", description = "Atualiza os detalhes de uma conta a pagar")
    public ResponseEntity<Conta> update(
            @PathVariable Long id,
            @RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.update(id, conta));
    }

    @GetMapping
    @Operation(summary = "Obter a lista de contas a pagar",
            description = "Obtém uma lista paginada de contas com filtros por data de vencimento e descrição")
    public ResponseEntity<Page<Conta>> getAll(
            @Parameter(description = "Data de início do filtro de vencimento")
            @RequestParam(required = false) LocalDate startDate,

            @Parameter(description = "Data de fim do filtro de vencimento")
            @RequestParam(required = false) LocalDate endDate,

            @Parameter(description = "Descrição para filtragem das contas")
            @RequestParam(required = false) String descricao,

            Pageable pageable) {
        return ResponseEntity.ok(contaService.getAllWithFilters(startDate, endDate, descricao, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma conta por ID", description = "Obtém uma conta específica pelo ID")
    public ResponseEntity<Conta> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.getById(id));
    }

    @GetMapping("/total")
    @Operation(summary = "Obter o valor total pago por período",
            description = "Obtém o valor total pago no período informado")
    public ResponseEntity<BigDecimal> getTotalPaid(
            @Parameter(description = "Data de início do filtro")
            @RequestParam LocalDate startDate,

            @Parameter(description = "Data de fim do filtro")
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(contaService.getTotalPaidByPeriod(startDate, endDate));
    }
}
