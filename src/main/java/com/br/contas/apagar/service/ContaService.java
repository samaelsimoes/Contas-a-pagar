package com.br.contas.apagar.service;

import com.br.contas.apagar.domain.Conta;
import com.br.contas.apagar.repository.ContaRepository;
import com.br.contas.apagar.util.DateUtils;
import com.br.contas.apagar.util.ValidationUtils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;

    public Conta save(Conta conta) {
        validateConta(conta);

        return contaRepository.save(conta);
    }

    public Conta update(Long id, Conta conta) {
        Conta existing = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        validateConta(conta);

        existing.setDescricao(conta.getDescricao());
        existing.setDataVencimento(conta.getDataVencimento());
        existing.setDataPagamento(conta.getDataPagamento());
        existing.setValor(conta.getValor());
        existing.setSituacao(conta.getSituacao());

        return contaRepository.save(existing);
    }

    public Page<Conta> getAllWithFilters(LocalDate startDate, LocalDate endDate, String descricao, Pageable pageable) {
        System.out.println("Descricao: " + descricao);

        if (descricao == null || descricao.isEmpty()) {
            descricao = "";
        }
        return contaRepository.findByFilters(startDate, endDate, descricao, pageable);
    }

    public Conta getById(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    public BigDecimal getTotalPaidByPeriod(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);

        if (parsedStartDate.isAfter(parsedEndDate)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }

        return contaRepository.getTotalPaidByPeriod(parsedStartDate, parsedEndDate);
    }

    public Conta updateSituacao(Long id, String situacao) {
        Conta existing = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        existing.setSituacao(situacao);
        return contaRepository.save(existing);
    }

    public void importCsv(MultipartFile file) {
        try {
            List<Conta> contas = parseCsv(file);
            contaRepository.saveAll(contas);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo CSV", e);
        }
    }

    private List<Conta> parseCsv(MultipartFile file) throws IOException {
        List<Conta> contas = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length != 5) {
                    throw new IllegalArgumentException("Formato de CSV inválido. Cada linha deve conter 5 campos.");
                }

                Conta conta = new Conta();
                try {
                    conta.setDataVencimento(LocalDate.parse(line[0]));
                    conta.setDataPagamento(LocalDate.parse(line[1]));
                    conta.setValor(new BigDecimal(line[2]));
                    conta.setDescricao(line[3]);
                    conta.setSituacao(line[4]);

                    validateConta(conta);
                    contas.add(conta);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Erro ao processar linha: " + String.join(",", line), e);
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return contas;
    }

    private void validateConta(Conta conta) {
        if (conta.getValor() == null || conta.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O campo 'Valor' é obrigatório e deve ser maior que zero.");
        }

        if (conta.getDataVencimento() == null) {
            throw new IllegalArgumentException("O campo 'Data de Vencimento' é obrigatório.");
        }

        if (conta.getDescricao() == null || conta.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("O campo 'Descrição' é obrigatório.");
        }

        if (conta.getSituacao() == null || conta.getSituacao().isEmpty()) {
            throw new IllegalArgumentException("O campo 'Situação' é obrigatório.");
        }

        if (!ValidationUtils.isValidAmount(conta.getValor())) {
            throw new IllegalArgumentException("Valor inválido.");
        }

        if (!DateUtils.isDateInRange(conta.getDataVencimento(), LocalDate.now(), LocalDate.now().plusMonths(1))) {
            throw new IllegalArgumentException("Data de vencimento fora do intervalo permitido.");
        }
    }
}
