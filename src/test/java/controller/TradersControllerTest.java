package controller;

import Eco.TradeX.business.Interfaces.TraderServiceInterfaces.*;
import Eco.TradeX.business.exceptions.TraderExceptions;
import Eco.TradeX.controller.TradersController;
import Eco.TradeX.domain.Trader.TraderData;
import Eco.TradeX.domain.Trader.TraderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TradersControllerTest {
    @Mock
    private GetTradersMethodsUseCase getTradersMethodsUseCase;

    @Mock
    private CreateTraderUseCase createTraderUseCase;

    @Mock
    private EditTraderUseCase editTraderUseCase;

    @Mock
    private DeleteTraderUseCase deleteTraderUseCase;

    @Mock
    private GetTraderPaginatedUseCase getTraderPaginatedUseCase;

    @InjectMocks
    private TradersController tradersController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tradersController).build();
    }

    @Test
    void testGetTrader() throws Exception {
        Long id = 1L;
        TraderData traderData = TraderData.builder().build();
        traderData.setId(id);
        traderData.setUsername("test_user");
        traderData.setEmail("test@example.com");
        traderData.setStatus(TraderStatus.TRADER_BASIC);

        when(getTradersMethodsUseCase.getTraderById(id)).thenReturn(traderData);

        mockMvc.perform(get("/api/traders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value("test_user"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.status").value(TraderStatus.TRADER_BASIC.toString()));

        verify(getTradersMethodsUseCase, times(1)).getTraderById(id);
        verifyNoMoreInteractions(getTradersMethodsUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Add security role for testing
    void testGetTraders_AdminRole() throws Exception {
        List<TraderData> traderList = new ArrayList<>();
        traderList.add(TraderData.builder().build());
        traderList.add(TraderData.builder().build());

        when(getTradersMethodsUseCase.getAllTraders()).thenReturn(traderList);

        mockMvc.perform(get("/api/traders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.traders").isArray())
                .andExpect(jsonPath("$.traders.length()").value(2));

        verify(getTradersMethodsUseCase, times(1)).getAllTraders();
        verifyNoMoreInteractions(getTradersMethodsUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTraderPage_Success() throws Exception {
        int page = 1;
        int pageSize = 10;

        // Mock successful response from the use case
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<TraderData> traderPage = new PageImpl<>(List.of(TraderData.builder().build(), TraderData.builder().build()), pageable, 0);
        when(getTraderPaginatedUseCase.getTraderByPage(page, pageSize)).thenReturn(traderPage);

        mockMvc.perform(get("/api/traders/pages")
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(getTraderPaginatedUseCase, times(1)).getTraderByPage(page, pageSize);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearchTraders_Success() throws Exception {
        int page = 1;
        int pageSize = 10;
        String searchString = "test";

        // Mock successful response from the use case
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<TraderData> traderPage = new PageImpl<>(List.of(TraderData.builder().build(), TraderData.builder().build()), pageable, 0);
        when(getTraderPaginatedUseCase.getTraderByUniversalSearch(page, pageSize, searchString)).thenReturn(traderPage);

        mockMvc.perform(get("/api/traders/search")
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("searchString", searchString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        // Verify that the use case was called once
        verify(getTraderPaginatedUseCase, times(1)).getTraderByUniversalSearch(page, pageSize, searchString);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCountAllStatusTraders_Success() throws Exception {
        TraderStatus status = TraderStatus.TRADER_BASIC;

        // Mock the total count of traders for the given status
        long totalCount = 10L;
        when(getTradersMethodsUseCase.getCountOfTradersStatus(status)).thenReturn(totalCount);

        mockMvc.perform(get("/api/traders/countAllStatusTraders")
                        .param("status", status.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(totalCount));

        // Verify that the use case was called once with the correct status
        verify(getTradersMethodsUseCase, times(1)).getCountOfTradersStatus(status);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCountAllTraders_Success() throws Exception {
        long totalCount = 100L; // Mock total count of traders

        // Mock the total count of traders
        when(getTradersMethodsUseCase.getCountAllTraders()).thenReturn(totalCount);

        mockMvc.perform(get("/api/traders/countAllTraders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(totalCount));

        // Verify that the use case was called once
        verify(getTradersMethodsUseCase, times(1)).getCountAllTraders();
    }

}