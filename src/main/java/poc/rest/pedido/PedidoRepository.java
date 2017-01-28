package poc.rest.pedido;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import poc.rest.pedido.PedidoModel;

public interface PedidoRepository extends MongoRepository<PedidoModel,String> {

    public PedidoModel findByCodigo(String codigo);
    
}
