package com.br.contas.apagar.repository;


import com.br.contas.apagar.domain.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContaRepositoryImpl implements ContaRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Conta> findByFilters(LocalDate startDate, LocalDate endDate, String descricao, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Conta> cq = cb.createQuery(Conta.class);
        Root<Conta> contaRoot = cq.from(Conta.class);

        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(contaRoot.get("dataVencimento"), startDate));
        }

        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(contaRoot.get("dataVencimento"), endDate));
        }

        if (descricao != null && !descricao.isEmpty()) {
            predicates.add(cb.like(cb.lower(contaRoot.get("descricao")), "%" + descricao.toLowerCase() + "%"));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        cq.orderBy(cb.asc(contaRoot.get("dataVencimento")), cb.asc(contaRoot.get("descricao")));

        TypedQuery<Conta> query = entityManager.createQuery(cq);
        long total = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Conta> result = query.getResultList();

        return new PageImpl<>(result, pageable, total);
    }
}