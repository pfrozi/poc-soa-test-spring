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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import poc.rest.test.MongoDBConfig;

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
    public void naoDeveriaIncluirItemComPrecoMenorIgualZero() {
    	
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
   
    
    @Test
    public void deveriaRetornarItemComPrecoAlterado() throws Exception {
    	
    	ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	
    	try {
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	ItemRepository itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	
        	this.mockMvc.perform(post("/item/alterarPreco")
            		.param("codigo", item1.getCodigo())
            		.param("preco", "5.05"))
            		.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.descricao").value("Aspirina D"))
                    .andExpect(jsonPath("$.preco").value(5.05));
        	
        	itemRepository.delete(item1);
        	
		} finally {
			contexto.close();
		}
    	
        
    }

    @Test
    public void naoDeveriaAlterarItemComPrecoMenorIgualZero() {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	
    	try {
    		ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    		
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	ItemRepository itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	
        	this.mockMvc.perform(post("/item/alterarPreco")
            		.param("codigo", item1.getCodigo())
            		.param("preco", "0.00"));
        	
        	itemRepository.delete(item1);
        	
        	Assert.fail("Exceção PrecoMenorIgualZeroException é esperada.");
        	
		} catch (Exception e) {
    		Assert.assertSame(PrecoMenorIgualZeroException.class, e.getCause().getClass());
    		
		}finally {
			contexto.close();
		}
    }
    
    @Test
    public void naoDeveriaAlterarItemInexistente() {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	
    	try {
    		ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    		
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	ItemRepository itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	
        	this.mockMvc.perform(post("/item/alterarPreco")
            		.param("codigo", item1.getCodigo()+"teste")
            		.param("preco", "10.00"));
        	
        	itemRepository.delete(item1);
        	
        	Assert.fail("Exceção ItemNaoExisteException é esperada.");
        	
		} catch (Exception e) {
    		Assert.assertSame(ItemNaoExisteException.class, e.getCause().getClass());
    		
		}finally {
			contexto.close();
		}
    }

}
