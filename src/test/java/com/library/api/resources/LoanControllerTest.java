package com.library.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.DTOs.LoanDTO;
import com.library.api.domain.Book;
import com.library.api.domain.Loan;
import com.library.api.services.ILoanService;
import com.library.api.services.impl.BookService;
import com.library.api.services.impl.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoanController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoanControllerTest {

    static String BASE_URL = "/api/loan";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loanService;

    @Test
    @DisplayName("Deve realizar um empréstimo")
    public void createLoanTest() throws Exception {
        LoanDTO dto = LoanDTO.builder().isbn("12345").customer("Jon Doe").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given( bookService.findByIsbn(dto.getIsbn()) ).willReturn(Optional.of(Book.builder().id(1L).isbn("12345").build()));

        Loan loan = Loan.builder().id(1L).customer("Jon Doe").isbn("12345").book(Book.builder().id(1L).isbn("12345").build())
                .loanDate(LocalDate.now())
                .build();
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("1"));

    }
}
