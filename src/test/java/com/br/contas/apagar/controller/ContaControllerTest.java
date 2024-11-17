package com.br.contas.apagar.controller;

import com.br.contas.apagar.domain.Conta;
import com.br.contas.apagar.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaControllerTest {

    @InjectMocks
    private ContaController contaController;

    @Mock
    private ContaService contaService;

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.now().plusDays(10));
        conta.setValor(new BigDecimal("100.00"));
        conta.setDescricao("Conta de teste");
        conta.setSituacao("Pendente");
    }

    @Test
    void testCreateContaSuccess() {
        when(contaService.save(any(Conta.class))).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.create(conta);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    public void testCreateContaFail() {
        Conta contaInvalida = new Conta();
        contaInvalida.setValor(BigDecimal.ZERO);
        contaInvalida.setDataVencimento(null);
        contaInvalida.setDescricao("");
        contaInvalida.setSituacao("");

        doThrow(new IllegalArgumentException("Erro ao salvar conta")).when(contaService).save(any(Conta.class));

        assertThrows(IllegalArgumentException.class, () -> {
            contaController.create(contaInvalida);
        });
    }

    @Test
    void testUpdateContaSuccess() {
        when(contaService.update(eq(1L), any(Conta.class))).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.update(1L, conta);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void testUpdateContaFailNotFound() {
        when(contaService.update(eq(1L), any(Conta.class))).thenReturn(null);

        ResponseEntity<Conta> response = contaController.update(1L, conta);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllContas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> contaPage = mock(Page.class);
        when(contaService.getAllWithFilters(any(), any(), any(), eq(pageable))).thenReturn(contaPage);

        ResponseEntity<Page<Conta>> response = contaController.getAll(null, null, null, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contaPage, response.getBody());
    }

    @Test
    void testGetByIdSuccess() {
        when(contaService.getById(1L)).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void testGetByIdFailNotFound() {
        when(contaService.getById(1L)).thenReturn(null);

        ResponseEntity<Conta> response = contaController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetTotalPaidSuccess() {
        when(contaService.getTotalPaidByPeriod(anyString(), anyString())).thenReturn(new BigDecimal("500.00"));

        ResponseEntity<BigDecimal> response = contaController.getTotalPaid("01/01/2024", "31/01/2024");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("500.00"), response.getBody());
    }

    @Test
    void testGetTotalPaidFail() {
        when(contaService.getTotalPaidByPeriod(anyString(), anyString())).thenThrow(new RuntimeException("Erro ao calcular total"));

        ResponseEntity<BigDecimal> response = contaController.getTotalPaid("01/01/2024", "31/01/2024");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(BigDecimal.ZERO, response.getBody());
    }

    @Test
    void testUpdateSituacaoSuccess() {
        when(contaService.updateSituacao(eq(1L), anyString())).thenReturn(conta);

        ResponseEntity<Conta> response = contaController.updateSituacao(1L, "Pago");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conta, response.getBody());
    }

    @Test
    void testUpdateSituacaoFailNotFound() {
        when(contaService.updateSituacao(eq(1L), anyString())).thenReturn(null);

        ResponseEntity<Conta> response = contaController.updateSituacao(1L, "Pago");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testImportCsvSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "data".getBytes());
        doNothing().when(contaService).importCsv(file);

        ResponseEntity<String> response = contaController.importCsv(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contas importadas com sucesso.", response.getBody());
    }

    @Test
    void testImportCsvFail() {
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", new byte[0]);

        ResponseEntity<String> response = contaController.importCsv(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Arquivo CSV não fornecido ou vazio.", response.getBody());
    }
}
