package poc.rest.test.item;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemModel,String> {

    public ItemModel findByCodigo(Long codigo);
    public List<ItemModel> findByDescricao(String descricao);
    public List<ItemModel> findByDescricaoRegex(String descricao);

}