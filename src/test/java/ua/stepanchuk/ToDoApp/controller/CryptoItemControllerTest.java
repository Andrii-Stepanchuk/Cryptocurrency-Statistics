package ua.stepanchuk.ToDoApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.stepanchuk.ToDoApp.service.CryptoItemService;
import ua.stepanchuk.ToDoApp.service.CsvExportService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class CryptoItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoItemService cryptoItemService;

    @MockBean
    private CsvExportService csvExportService;

    @Test
    void getCryptoItemsByPagination() {
//        mockMvc.perform(get(""))
    }

    @Test
    void getCryptoItemWithMinPrice() {
    }

    @Test
    void getCryptoItemWithMaxPrice() {
    }

    @Test
    void exportIntoCSV() {
    }
}