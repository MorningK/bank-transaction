package io.github.mk.bank.transaction.controller;

import io.github.mk.bank.transaction.controller.advice.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class TransactionControllerTest {
    @Autowired
    MockMvcTester mvcTester;

    @Test
    void transactions() {
        mvcTester.get().uri("/transactions/").accept(MediaType.APPLICATION_JSON).assertThat()
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON)
                .apply(document("transactions", responseFields(
                        fieldWithPath("code").description("status code"),
                        fieldWithPath("message").description("error message"),
                        subsectionWithPath("data").description("response data")
                )));
      }
}