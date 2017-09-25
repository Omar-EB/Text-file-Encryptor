public class Test{
	public static void main(String[] args){
		
		String message = "Scishow is the best 123-4!";
		
		
		String encryptedMessage = Encryption.caesarCipher(message,2) ;
		System.out.println(encryptedMessage);
		
		
		String oldMessage = Encryption.caesarDicipher(encryptedMessage,2) ;
		System.out.println(oldMessage);
		
		
		String newMessage = Encryption.viginereCipher(oldMessage,"omar") ;
		System.out.println(newMessage);
		
		
		message = Encryption.viginereDicipher(newMessage,"omar") ;
		System.out.println(message);
		
		
	}
}