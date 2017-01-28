package poc.rest.item;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import poc.rest.MongoDBConfig;
import poc.rest.item.ItemModel;
import poc.rest.item.ItemNaoExisteException;
import poc.rest.item.ItemRepository;
import poc.rest.item.PrecoMenorIgualZeroException;

import com.jayway.jsonpath.*;

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
    	ItemRepository itemRepository = null;
    	
    	try {
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	
        	this.mockMvc.perform(post("/item/alterarPreco")
            		.param("codigo", item1.getCodigo())
            		.param("preco", "5.05"))
            		.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.descricao").value("Aspirina D"))
                    .andExpect(jsonPath("$.preco").value(5.05));
        	
        	
		} finally {
			
			if(itemRepository!=null){
				itemRepository.delete(item1);
			}
			contexto.close();
		}
    	
        
    }

    @Test
    public void naoDeveriaAlterarItemComPrecoMenorIgualZero() throws Exception {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	ItemRepository itemRepository = null;
    	
    	ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    	
    	try {
    		
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	
        	this.mockMvc.perform(post("/item/alterarPreco")
            		.param("codigo", item1.getCodigo())
            		.param("preco", "0.00"));
        	
        	Assert.fail("Exceção PrecoMenorIgualZeroException é esperada.");
        	
		} catch (Exception e) {
    		Assert.assertSame(PrecoMenorIgualZeroException.class, e.getCause().getClass());
    		
		}finally {
			if(itemRepository!=null){
				itemRepository.delete(item1);
			}
			contexto.close();
		}
    }
    
    @Test
    public void naoDeveriaAlterarItemInexistente() throws Exception{
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	ItemRepository itemRepository = null;
    	
    	ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    	
    	try {
    		
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	
        	this.mockMvc.perform(post("/item/alterarPreco")
            		.param("codigo", item1.getCodigo()+"teste")
            		.param("preco", "10.00"));
        	
        	
        	
        	Assert.fail("Exceção ItemNaoExisteException é esperada.");
        	
		} catch (Exception e) {
    		Assert.assertSame(ItemNaoExisteException.class, e.getCause().getClass());
    		
		}finally {
			if(itemRepository!=null){
				itemRepository.delete(item1);
			}
			contexto.close();
		}
    }

    
    @Test
    public void deveriaRetornarItensAutoComplete() throws Exception{
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	ItemRepository itemRepository = null;
    	
    	ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    	ItemModel item2 = new ItemModel("Aspirina X", 5.05);
    	ItemModel item3 = new ItemModel("Aspirina Z", 5.10);
    	
    	try {
    		
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	item2 = itemRepository.save(item2);
        	item3 = itemRepository.save(item3);
        	
			this.mockMvc.perform(get("/item/buscar")
            		.param("descricao", "Aspirina"))
            		.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$..[?(@.descricao=='%s')]","Aspirina D").isNotEmpty())
                    .andExpect(jsonPath("$..[?(@.descricao=='%s')]","Aspirina X").isNotEmpty())
                    .andExpect(jsonPath("$..[?(@.descricao=='%s')]","Aspirina Z").isNotEmpty())
                	.andExpect(jsonPath("$.*", Matchers.hasSize(3)));
		}finally {
			
			if(itemRepository!=null){
				itemRepository.delete(item1);
				itemRepository.delete(item2);
				itemRepository.delete(item3);
			}
			contexto.close();
		}
    }
	@Test
    public void naoDeveriaRetornarItensAutoComplete() throws Exception{
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	ItemRepository itemRepository = null;
    	
    	ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    	ItemModel item2 = new ItemModel("Aspirina X", 5.05);
    	ItemModel item3 = new ItemModel("Aspirina Z", 5.10);
    	
    	try {
    		
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	itemRepository = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	item2 = itemRepository.save(item2);
        	item3 = itemRepository.save(item3);
        	
			this.mockMvc.perform(get("/item/buscar")
            		.param("descricao", "As"))
            		.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.*").isEmpty());
		}finally {
			
			if(itemRepository!=null){
				itemRepository.delete(item1);
				itemRepository.delete(item2);
				itemRepository.delete(item3);
			}
			contexto.close();
		}
    }
}
