package poc.rest.test.item;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poc.rest.test.MongoDBConfig;


//import java.rest.test.item.ItemModel;

@RestController
public class ItemController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/item")
    public ItemModel item(@RequestParam(value="codigo") Long codigo) {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	try {
    		
        	contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	ItemRepository itemRepository = contexto.getBean(ItemRepository.class);
        	
            return (itemRepository.findByCodigo(codigo));
            
		} finally {
			contexto.close();
		}
}
    
    
    @PostMapping("/item/cadastrar")
    public ItemModel itemCadastrar(
    		@RequestParam(value="descricao") String descricao, 
    		@RequestParam(value="preco")     double preco) {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	
    	try {
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	ItemRepository itemRepository = contexto.getBean(ItemRepository.class);
        	
            return itemRepository.save(new ItemModel(descricao, preco));
            
		} finally {
			contexto.close();
		}
    	
    	
    	
    	
    }
    
    /*
    @PostMapping("/item/alterarPreco")
    public ItemModel itemAlterarPreco(
    		@RequestParam(value="descricao", defaultValue="test") String descricao, 
    		@RequestParam(value="preco", defaultValue="1.5") double preco) {
        return new ItemModel(counter.incrementAndGet(),
                            descricao,
                            preco);
    }
    
    @GetMapping("/item/buscar")
    public ItemModel itemAlterarPreco(
    		@RequestParam(value="descricao", defaultValue="test") String descricao, 
    		@RequestParam(value="preco", defaultValue="1.5") double preco) {
        return new ItemModel(counter.incrementAndGet(),
                            descricao,
                            preco);
    }
    
    @PostMapping("/item/fecharPedido")
    public ItemModel itemFecharPedido(
    		@RequestParam(value="descricao", defaultValue="test") String descricao, 
    		@RequestParam(value="preco", defaultValue="1.5") double preco) {
        return new ItemModel(counter.incrementAndGet(),
                            descricao,
                            preco);
    }
	*/
}