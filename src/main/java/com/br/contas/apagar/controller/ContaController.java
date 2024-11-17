package com.br.contas.apagar.controller;

import com.br.contas.apagar.domain.Conta;
import com.br.contas.apagar.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@Tag(name = "Contas a Pagar", description = "Operações relacionadas às contas a pagar")
public class ContaController {

    private final ContaService contaService;
    private static final Logger log = LoggerFactory.getLogger(ContaController.class);

    @PostMapping
    @Operation(summary = "Criar uma nova conta a pagar", description = "Cria uma nova conta no sistema")
    public ResponseEntity<Conta> create(@RequestBody Conta conta) {
        Conta savedConta = contaService.save(conta);
        return ResponseEntity.status(201).body(savedConta); // Retorna 201 Created
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma conta existente", description = "Atualiza os detalhes de uma conta a pagar")
    public ResponseEntity<Conta> update(
            @PathVariable Long id,
            @RequestBody Conta conta) {
        Conta updatedConta = contaService.update(id, conta);
        return updatedConta != null ? ResponseEntity.ok(updatedConta) : ResponseEntity.notFound().build(); // Retorna 404 caso não encontre a conta
    }

    @GetMapping
    @Operation(summary = "Obter a lista de contas a pagar", description = "Obtém uma lista paginada de contas com filtros por data de vencimento e descrição")
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
        Conta conta = contaService.getById(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build(); // Retorna 404 caso não encontre a conta
    }

    @GetMapping("/total")
    @Operation(summary = "Obter o valor total pago por período", description = "Obtém o valor total pago no período informado")
    public ResponseEntity<BigDecimal> getTotalPaid(
            @Parameter(description = "Data de início do filtro")
            @RequestParam String startDate,

            @Parameter(description = "Data de fim do filtro")
            @RequestParam String endDate) {

        try {
            BigDecimal totalPaid = contaService.getTotalPaidByPeriod(startDate, endDate);
            return ResponseEntity.ok(totalPaid);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(BigDecimal.ZERO); // Retorna 500 em caso de erro
        }
    }

    @PatchMapping("/{id}/situacao")
    @Operation(summary = "Alterar situação da conta", description = "Altera a situação de uma conta (ex: de 'Pendente' para 'Pago')")
    public ResponseEntity<Conta> updateSituacao(
            @PathVariable Long id,
            @RequestBody String situacao) {
        Conta updatedConta = contaService.updateSituacao(id, situacao);
        return updatedConta != null ? ResponseEntity.ok(updatedConta) : ResponseEntity.notFound().build(); // Retorna 404 caso não encontre a conta
    }

        @PostMapping("/importar")
        @Operation(summary = "Importar contas a pagar via arquivo CSV", description = "Recebe um arquivo CSV e importa as contas a pagar")
        public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
            log.info("Iniciando importação do arquivo CSV: {}", file.getOriginalFilename());

            if (file.isEmpty()) {
                log.warn("Arquivo vazio recebido para importação.");
                return ResponseEntity.badRequest().body("Arquivo CSV não fornecido ou vazio.");
            }

            try {
                contaService.importCsv(file);
                log.info("Importação do arquivo CSV {} concluída com sucesso", file.getOriginalFilename());
                return ResponseEntity.ok("Contas importadas com sucesso.");
            } catch (Exception e) {
                log.error("Erro inesperado ao processar o arquivo CSV: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao processar arquivo CSV: " + e.getMessage());
            }
        }
}
