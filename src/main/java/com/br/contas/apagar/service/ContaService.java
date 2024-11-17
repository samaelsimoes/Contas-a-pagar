package com.br.contas.apagar.service;

import com.br.contas.apagar.domain.Conta;
import com.br.contas.apagar.repository.ContaRepository;
import com.br.contas.apagar.util.DateUtils;
import com.br.contas.apagar.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;

    public Conta save(Conta conta) {

        if (!ValidationUtils.isValidAmount(conta.getValor())) {
            throw new IllegalArgumentException("Valor inválido.");
        }

        if (!DateUtils.isDateInRange(conta.getDataVencimento(), LocalDate.now(), LocalDate.now().plusMonths(1))) {
            throw new IllegalArgumentException("Data de vencimento fora do intervalo permitido.");
        }

        return contaRepository.save(conta);
    }

    public Conta update(Long id, Conta conta) {
        Conta existing = contaRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (!ValidationUtils.isValidAmount(conta.getValor())) {
            throw new IllegalArgumentException("Valor inválido.");
        }

        if (!DateUtils.isDateInRange(conta.getDataVencimento(), LocalDate.now(), LocalDate.now().plusMonths(1))) {
            throw new IllegalArgumentException("Data de vencimento fora do intervalo permitido.");
        }

        existing.setDescricao(conta.getDescricao());
        existing.setDataVencimento(conta.getDataVencimento());
        existing.setDataPagamento(conta.getDataPagamento());
        existing.setValor(conta.getValor());
        existing.setSituacao(conta.getSituacao());

        return contaRepository.save(existing);
    }

    public Page<Conta> getAllWithFilters(LocalDate startDate, LocalDate endDate, String descricao, Pageable pageable) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }

        return contaRepository.findByDataVencimentoBetweenAndDescricaoContaining(startDate, endDate, descricao, pageable);
    }

    public Conta getById(Long id) {
        return contaRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    public BigDecimal getTotalPaidByPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }

        return contaRepository.getTotalPaidByPeriod(startDate, endDate);
    }
}
