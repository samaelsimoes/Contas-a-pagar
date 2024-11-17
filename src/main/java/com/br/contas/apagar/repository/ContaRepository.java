package com.br.contas.apagar.repository;

import com.br.contas.apagar.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContaRepository extends JpaRepository<Conta, Long>, ContaRepositoryCustom  {
    Page<Conta> findByDataVencimentoBetweenAndDescricaoContainingIgnoreCase(
            LocalDate startDate, LocalDate endDate, String descricao, Pageable pageable);

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :startDate AND :endDate")
    BigDecimal getTotalPaidByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}