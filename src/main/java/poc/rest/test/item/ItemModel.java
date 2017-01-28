package poc.rest.test.item;
import org.springframework.data.annotation.Id;

public class ItemModel  {
	
	@Id
	private String     codigo;
	private String     descricao;
	private Double     preco;
	
	public ItemModel(String descricao, Double preco) throws PrecoMenorIgualZeroException{
		
		setPreco(preco);
		this.descricao  = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Double getPreco() {
		return preco;
	}
	
	public void setPreco(Double preco) throws PrecoMenorIgualZeroException {
		if(preco<=0){
			throw new PrecoMenorIgualZeroException("Preco deve ser maior do que zero.");
		}
		this.preco= preco;
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
