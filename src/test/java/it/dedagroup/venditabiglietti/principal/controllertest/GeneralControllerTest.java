package it.dedagroup.venditabiglietti.principal.controllertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.dedagroup.venditabiglietti.principal.VenditaBigliettiPrincipalApplication;
import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiUtenteDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.LoginDTORequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@ContextConfiguration(classes = VenditaBigliettiPrincipalApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
public class GeneralControllerTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    @Test @Order(1)
    void registrazioneUtenteOk() throws Exception {
        AggiungiUtenteDTORequest auDTO = new AggiungiUtenteDTORequest("Luca", "Grigi", "grigiLuca@gmail.com", "grLuca__85", "3445551010", LocalDate.of(1985, 3, 31));
        String json = mapper.writeValueAsString(auDTO);
        System.out.println(json);
        mvc.perform(MockMvcRequestBuilders.post("/all/registrazioneCliente")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();
    }

    @Test
    void registrazioneUtenteSenzaBody() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/all/registrazioneCliente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void registrazioneUtenteEmailGiàEsistente() throws Exception {
        AggiungiUtenteDTORequest auDTO = new AggiungiUtenteDTORequest("Paolo", "Pacello", "pacello@gmail.com", "pacello98__!", "3489901451", LocalDate.of(2000, 1, 3));
        String json = mapper.writeValueAsString(auDTO);
        mvc.perform(MockMvcRequestBuilders.post("/all/registrazioneCliente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void registrazioneUtenteTelefonoGiàEsistente() throws Exception {
        AggiungiUtenteDTORequest auDTO = new AggiungiUtenteDTORequest("Paolo", "Pacello", "pacelloPaolo@gmail.com", "pacello98__!", "3489901451", LocalDate.of(2000, 1, 3));
        String json = mapper.writeValueAsString(auDTO);
        mvc.perform(MockMvcRequestBuilders.post("/all/registrazioneCliente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void registrazioneUtentePasswordNonConforme() throws Exception {
        AggiungiUtenteDTORequest auDTO = new AggiungiUtenteDTORequest("Claudio", "Iosca", "clIosca@gmail.com", "clios", "3477411981", LocalDate.of(2003, 5, 13));
        String json = mapper.writeValueAsString(auDTO);
        mvc.perform(MockMvcRequestBuilders.post("/all/registrazioneCliente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void loginOk() throws Exception{
        LoginDTORequest loginDTO = new LoginDTORequest();
        loginDTO.setEmail("neri@gmail.com");
        loginDTO.setPassword("neriLuca2311__!");
        String json = mapper.writeValueAsString(loginDTO);
        mvc.perform(MockMvcRequestBuilders.post("/all/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();
    }

    @Test
    void loginFail() throws Exception{
        LoginDTORequest loginDTO = new LoginDTORequest();
        loginDTO.setEmail("neri@gmail.com");
        loginDTO.setPassword("neriLuca23__");
        String json = mapper.writeValueAsString(loginDTO);
        mvc.perform(MockMvcRequestBuilders.post("/all/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    void loginSenzaBody() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/all/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

}
