package poc.rest.test.item;

public class ItemControllerCadastrarException extends Exception {
	
	public enum ErrorTypes{
		CadastrarItemValorInvalido,
		CadastrarItemDescricaoInvalida
	}
	
	public ItemControllerCadastrarException() { super(); }
	public ItemControllerCadastrarException(String message) { super(message); }
	public ItemControllerCadastrarException(String message, Throwable cause) { super(message, cause); }
	public ItemControllerCadastrarException(Throwable cause) { super(cause); }
}
