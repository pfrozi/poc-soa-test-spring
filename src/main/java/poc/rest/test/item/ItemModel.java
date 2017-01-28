package poc.rest.test.item;
import org.springframework.data.annotation.Id;

public class ItemModel {
	
	@Id
	private Long       codigo;
	private String     descricao;
	private Double     preco;
	
	public ItemModel(){}
	
	public ItemModel(String descricao, Double preco){
		
		this.descricao  = descricao;
		this.preco 		= preco;
	}

	public Long getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Double getPreco() {
		return preco;
	}
	
	/*
	 @Override
    public String toString() {
        return String.format(
                "Item[codigo=%s, descricao='%s', preco='%s']",
                codigo, descricao, preco.toString());
    }
	 * */
	
}
