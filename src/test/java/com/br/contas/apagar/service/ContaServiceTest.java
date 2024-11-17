package com.br.contas.apagar.service;

import com.br.contas.apagar.domain.Conta;
import com.br.contas.apagar.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @Test
    public void testSaveValidConta() {
        Conta conta = new Conta();
        conta.setDescricao("Conta Teste");
        conta.setValor(BigDecimal.valueOf(100));
        conta.setDataVencimento(LocalDate.now());

        Mockito.when(contaRepository.save(Mockito.any(Conta.class))).thenReturn(conta);

        Conta saved = contaService.save(conta);

        assertNotNull(saved);
        assertEquals("Conta Teste", saved.getDescricao());

        Mockito.verify(contaRepository, Mockito.times(1)).save(conta);
    }
}
