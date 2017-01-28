package poc.rest.test.item;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTests {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void deveriaRetornarItemIncluidoNoMongo() throws Exception {

        this.mockMvc.perform(post("/item/cadastrar")
        		.param("descricao", "Item de teste")
        		.param("preco", "50.30"))
        		.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Item de teste"))
                .andExpect(jsonPath("$.preco").value(50.30));
    }

    @Test
    public void naoDeveriaRetornarItemIncluidoNoMongo() {
    	
    	try {
    		this.mockMvc.perform(post("/item/cadastrar")
            		.param("descricao", "Item de teste")
            		.param("preco", "0.0"));
    		Assert.fail("Exceção PrecoMenorIgualZeroException é esperada.");
		}
    	catch (Exception e) {
    		Assert.assertSame(PrecoMenorIgualZeroException.class, e.getCause().getClass());
    		
		}
    }

}
