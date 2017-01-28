package poc.rest.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poc.rest.MongoDBConfig;


//import java.rest.test.item.ItemModel;

@RestController
public class ItemController {

    @GetMapping("/item")
    public ItemModel item(@RequestParam(value="codigo") String codigo) {
    	
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
    		@RequestParam(value="preco")     double preco) throws PrecoMenorIgualZeroException {
    	
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
    
    @PostMapping("/item/alterarPreco")
    public ItemModel itemAlterarPreco(
    		@RequestParam(value="codigo") String codigo, 
    		@RequestParam(value="preco")  double preco) throws PrecoMenorIgualZeroException, ItemNaoExisteException {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	
    	try {
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	ItemRepository itemRepository = contexto.getBean(ItemRepository.class);
        	
        	ItemModel itemSolicitado = itemRepository.findByCodigo(codigo);
        	
        	if(itemSolicitado==null){
        		throw new ItemNaoExisteException("O item solicitado não existe.");
        	}
        	
        	itemSolicitado.setPreco(preco);
        	
            return itemRepository.save(itemSolicitado);
            
		} finally {
			contexto.close();
		}
    	
    }
    
    @GetMapping("/item/buscar")
    public List<ItemModel> itemBuscar(
    		@RequestParam(value="descricao") String descricao) {
    	
    	List<ItemModel> lista = new ArrayList<ItemModel>();
    	
    	// caso o usuario tenha enviado menos de 3 caracteres
    	if(descricao.length()<=3){
    		return  (lista);	
    	}
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	
    	try {
    		contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	ItemRepository itemRepository = contexto.getBean(ItemRepository.class);
        	String regexPredicado = String.format("/^%s/", descricao);
        	
        	return (itemRepository.findByDescricaoRegex(regexPredicado));
            
		} finally {
			contexto.close();
		}
        
    }
    
    /*
    
    
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