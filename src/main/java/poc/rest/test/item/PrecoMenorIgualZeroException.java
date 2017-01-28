package poc.rest.test.item;

public class PrecoMenorIgualZeroException extends Exception {
	
	public PrecoMenorIgualZeroException() { super(); }
	public PrecoMenorIgualZeroException(String message) { super(message); }
	public PrecoMenorIgualZeroException(String message, Throwable cause) { super(message, cause); }
	public PrecoMenorIgualZeroException(Throwable cause) { super(cause); }
}
