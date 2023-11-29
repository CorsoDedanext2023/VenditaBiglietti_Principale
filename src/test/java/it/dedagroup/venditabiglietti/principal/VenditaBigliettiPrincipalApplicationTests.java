package it.dedagroup.venditabiglietti.principal;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import it.dedagroup.venditabiglietti.principal.dto.request.AddEventoDTORequest;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;

@SpringBootTest
@ContextConfiguration(classes = VenditaBigliettiPrincipalApplication.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
class VenditaBigliettiPrincipalApplicationTests implements GeneralCallService{

	@Autowired
	MockMvc mvc;

	@Test
	@Order(2)
	public void testAddEventoConDati() throws Exception{
		//TODO capire perchè c'è uno username
		String json = convertToJson(new AddEventoDTORequest(LocalDate.now().toString(),LocalTime.now().toString(),"Concerto Gemitaiz",1,1));
		mvc.perform(MockMvcRequestBuilders.post("/venditore/evento/add")
				//la richiesta all'interno del body è un JSON
				.contentType(MediaType.APPLICATION_JSON)
				//è il JSON(il mio request)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
		 .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		 .andReturn();
	}

	@Test
	@Order(3)
	public void testDelete() throws Exception{
		mvc.perform(MockMvcRequestBuilders.post("/venditore/evento/cancella")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andDo(print());
	}
}
