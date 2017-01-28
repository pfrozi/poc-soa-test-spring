package poc.rest.pedido;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PedidoControllerTests {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void deveriaFecharPedido() throws Exception {

    	ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    	ItemModel item2 = new ItemModel("Paracetamol 500mg", 2.15);
    	
    	List<ItemModel> itensDoPedido = new ArrayList<ItemModel>();
    	
    	itensDoPedido.add(item1);
    	itensDoPedido.add(item2);
    	
    	PedidoModel pedido = new PedidoModel(itensDoPedido);
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	PedidoRepository pedidoRepository = null;
    	ItemRepository itemRepository   = null;
    	
    	try {
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	pedidoRepository = contexto.getBean(PedidoRepository.class);
        	itemRepository   = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	item2 = itemRepository.save(item2);
        	
            pedido = pedidoRepository.save(pedido); 
        	
        	this.mockMvc.perform(post("/pedido/fechar")
            		.param("codigo", pedido.getCodigo()))
            		.andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.fechado").value("true"));
        	
        	
		} finally {
			if(pedidoRepository!=null){
				pedidoRepository.delete(pedido);
			}
			if(itemRepository!=null){
				itemRepository.delete(item1);
				itemRepository.delete(item2);
			}
			
			contexto.close();
		}
    }
    
    @Test
    public void naoDeveriaFecharPedido() throws Exception {

    	ItemModel item1 = new ItemModel("Aspirina D", 5.00);
    	ItemModel item2 = new ItemModel("Paracetamol 500mg", 2.15);
    	
    	List<ItemModel> itensDoPedido = new ArrayList<ItemModel>();
    	
    	itensDoPedido.add(item1);
    	itensDoPedido.add(item2);
    	
    	PedidoModel pedido = new PedidoModel(itensDoPedido);
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	PedidoRepository pedidoRepository = null;
    	ItemRepository itemRepository   = null;
    	
    	try {
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	pedidoRepository = contexto.getBean(PedidoRepository.class);
        	itemRepository   = contexto.getBean(ItemRepository.class);
        	
        	item1 = itemRepository.save(item1);
        	item2 = itemRepository.save(item2);
        	
            pedido = pedidoRepository.save(pedido); 
        	
        	this.mockMvc.perform(post("/pedido/fechar")
            		.param("codigo", pedido.getCodigo()+"teste"));
        	
        	Assert.fail("Exceção PedidoNaoExisteException é esperada.");
        	
		} catch (Exception e) {
    		Assert.assertSame(PedidoNaoExisteException.class, e.getCause().getClass());
    		
		} finally {
			if(pedidoRepository!=null){
				pedidoRepository.delete(pedido);
			}
			if(itemRepository!=null){
				itemRepository.delete(item1);
				itemRepository.delete(item2);
			}
			
			contexto.close();
		}
    }
}
