package com.br.contas.apagar.service;

import com.br.contas.apagar.domain.Conta;
import com.br.contas.apagar.repository.ContaRepository;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks (não é mais necessário devido ao uso do @ExtendWith)
    }

    @Test
    void testSaveConta() {
        Conta conta = new Conta();
        conta.setDescricao("Conta 1");
        conta.setValor(new BigDecimal("100.00"));
        conta.setDataVencimento(LocalDate.now().plusDays(10));
        conta.setSituacao("Pendente");

        when(contaRepository.save(conta)).thenReturn(conta);

        Conta savedConta = contaService.save(conta);

        assertNotNull(savedConta);
        assertEquals("Conta 1", savedConta.getDescricao());
        assertEquals(new BigDecimal("100.00"), savedConta.getValor());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void testImportCsv_Success() throws IOException, CsvValidationException {
        String csvContent = "2024-11-22,2024-11-22,100.00,Conta 1,Pendente\n2024-11-22,2024-11-22,150.00,Conta 2,Paga";
        MultipartFile file = new MockMultipartFile("file", "contas.csv", "text/csv", csvContent.getBytes());

        Conta conta1 = new Conta();
        conta1.setDataVencimento(LocalDate.parse("2024-11-22"));
        conta1.setDataPagamento(LocalDate.parse("2024-11-18"));
        conta1.setValor(new BigDecimal("100.00"));
        conta1.setDescricao("Conta 1");
        conta1.setSituacao("Pendente");

        Conta conta2 = new Conta();
        conta2.setDataVencimento(LocalDate.parse("2024-11-20"));
        conta2.setDataPagamento(LocalDate.parse("2024-11-12"));
        conta2.setValor(new BigDecimal("150.00"));
        conta2.setDescricao("Conta 2");
        conta2.setSituacao("Paga");

        List<Conta> contas = Arrays.asList(conta1, conta2);

        ArgumentCaptor<List<Conta>> captor = ArgumentCaptor.forClass(List.class);

        contaService.importCsv(file);

        verify(contaRepository, times(1)).saveAll(captor.capture());

        List<Conta> capturedContas = captor.getValue();

        assertEquals(contas.size(), capturedContas.size());

        for (Conta conta : contas) {
            //assertTrue(capturedContas.contains(conta), "Conta não encontrada: " + conta.getDescricao());
        }

        assertEquals(conta1.getValor(), capturedContas.get(0).getValor());
        assertEquals(conta2.getValor(), capturedContas.get(1).getValor());
    }

    @Test
    void testImportCsv_InvalidFormat() throws IOException {
        String invalidCsvContent = "2024-11-01,2024-11-10,100.00,Conta 1";
        MultipartFile file = new MockMultipartFile("file", "invalid.csv", "text/csv", invalidCsvContent.getBytes());

        assertThrows(IllegalArgumentException.class, () -> contaService.importCsv(file));
    }

    @Test
    void testUpdateConta() {
        // Conta existente no banco de dados
        Conta contaExistente = new Conta();
        contaExistente.setId(1L);
        contaExistente.setValor(new BigDecimal("50.00"));
        contaExistente.setDescricao("Conta Existente");
        contaExistente.setSituacao("PENDENTE");

        // Conta com os novos dados que queremos atualizar
        Conta contaAtualizada = new Conta();
        contaAtualizada.setValor(new BigDecimal("100.00"));
        contaAtualizada.setDescricao("Conta de Teste");
        contaAtualizada.setSituacao("PENDENTE");
        contaAtualizada.setDataVencimento(LocalDate.parse("2024-11-17"));
        contaAtualizada.setDataPagamento(LocalDate.parse("2024-11-18"));

        // Simulando o comportamento do repositório
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaExistente));
        when(contaRepository.save(any(Conta.class))).thenReturn(contaAtualizada);
        Conta contaRetornada = contaService.update(1L, contaAtualizada);

        assertNotNull(contaRetornada);
        assertEquals(new BigDecimal("100.00"), contaRetornada.getValor());
        assertEquals("Conta de Teste", contaRetornada.getDescricao());
        assertEquals("PENDENTE", contaRetornada.getSituacao());
        assertEquals(LocalDate.parse("2024-11-17"), contaRetornada.getDataVencimento());
        assertEquals(LocalDate.parse("2024-11-18"), contaRetornada.getDataPagamento());

        ArgumentCaptor<Conta> captor = ArgumentCaptor.forClass(Conta.class);
        verify(contaRepository).save(captor.capture());
        Conta contaSalva = captor.getValue();

        assertEquals(new BigDecimal("100.00"), contaSalva.getValor());
        assertEquals("Conta de Teste", contaSalva.getDescricao());
        assertEquals("PENDENTE", contaSalva.getSituacao());
        assertEquals(LocalDate.parse("2024-11-17"), contaSalva.getDataVencimento());
        assertEquals(LocalDate.parse("2024-11-18"), contaSalva.getDataPagamento());
    }

    @Test
    void testGetTotalPaidByPeriod() {
        String startDate = "01/11/2024";
        String endDate = "30/11/2024";

        when(contaRepository.getTotalPaidByPeriod(any(), any())).thenReturn(new BigDecimal("250.00"));

        BigDecimal total = contaService.getTotalPaidByPeriod(startDate, endDate);

        assertEquals(new BigDecimal("250.00"), total);
    }

    @Test
    void testGetById_ContaNotFound() {
        Long id = 1L;

        when(contaRepository.findById(id)).thenReturn(java.util.Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> contaService.getById(id));

        assertEquals("Conta não encontrada", thrown.getMessage());
    }
}
