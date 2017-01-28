package poc.rest.pedido;

public class PedidoNaoExisteException extends Exception {
	
	public PedidoNaoExisteException() { super(); }
	public PedidoNaoExisteException(String message) { super(message); }
	public PedidoNaoExisteException(String message, Throwable cause) { super(message, cause); }
	public PedidoNaoExisteException(Throwable cause) { super(cause); }
	
}
