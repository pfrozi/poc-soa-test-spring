package poc.rest.pedido;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poc.rest.MongoDBConfig;

@RestController
public class PedidoController {
	
	@GetMapping("/pedido")
    public PedidoModel pedido(@RequestParam(value="codigo") String codigo) {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	try {
    		
        	contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	PedidoRepository pedidoRepository = contexto.getBean(PedidoRepository.class);
        	
            return (pedidoRepository.findByCodigo(codigo));
            
		} finally {
			contexto.close();
		}
    }
	
	@PostMapping("/pedido/fechar")
    public PedidoModel pedidoFechar(@RequestParam(value="codigo") String codigo) throws PedidoNaoExisteException {
    	
    	AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
    	try {
    		
        	contexto.register(MongoDBConfig.class);
        	contexto.refresh();
        	
        	PedidoRepository pedidoRepository = contexto.getBean(PedidoRepository.class);
        	
        	PedidoModel pedidoSelecionado = pedidoRepository.findByCodigo(codigo);
        	if(pedidoSelecionado==null){
        		
        		throw new PedidoNaoExisteException("O pedido selecionado não existe.");
        	}
        	pedidoSelecionado.fechar();
        	pedidoSelecionado = pedidoRepository.save(pedidoSelecionado);
        	
            return (pedidoSelecionado);
            
		} finally {
			contexto.close();
		}
    }
}
