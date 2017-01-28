package poc.rest.pedido;

import java.util.List;

import org.springframework.data.annotation.Id;
import poc.rest.item.ItemModel;

public class PedidoModel {
	
	@Id
	private String          codigo;
	private List<ItemModel> itens;
	private Boolean         fechado;
	
	public PedidoModel(List<ItemModel> itens){
		
		this.itens   = itens;
		this.fechado = false;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public List<ItemModel> getItens() {
		return this.itens;
	}

	public Boolean getFechado() {
		return this.fechado;
	}
	
	public void fechar(){
		
		this.fechado = true;
	}
}
