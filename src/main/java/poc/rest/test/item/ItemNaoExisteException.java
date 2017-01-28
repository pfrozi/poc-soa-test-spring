package poc.rest.test.item;

public class ItemNaoExisteException extends Exception {

	public ItemNaoExisteException() { super(); }
	public ItemNaoExisteException(String message) { super(message); }
	public ItemNaoExisteException(String message, Throwable cause) { super(message, cause); }
	public ItemNaoExisteException(Throwable cause) { super(cause); }
}
