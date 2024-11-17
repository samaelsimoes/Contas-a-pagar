package com.br.contas.apagar.repository;

import com.br.contas.apagar.domain.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface ContaRepositoryCustom {
    Page<Conta> findByFilters(LocalDate startDate, LocalDate endDate, String descricao, Pageable pageable);
}