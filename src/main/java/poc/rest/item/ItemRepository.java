package poc.rest.item;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemModel,String> {

    public ItemModel       findByCodigo(String codigo);
    public List<ItemModel> findByDescricao(String descricao);
    public List<ItemModel> findByDescricaoLike(String descricao);
    public List<ItemModel> findByDescricaoRegex(String descricao);
    
    public List<ItemModel> findByDescricaoStartingWith(String descricao);

}