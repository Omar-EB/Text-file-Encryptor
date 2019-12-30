public class Test{
	public static void main(String[] args){
		
		String message = "A message.";
		
		
		String encryptedMessage = Encryption.caesarCipher(message,2) ;
		System.out.println(encryptedMessage);
		
		
		String oldMessage = Encryption.caesarDicipher(encryptedMessage,2) ;
		System.out.println(oldMessage);
		
		
		String newMessage = Encryption.viginereCipher(oldMessage,"key") ;
		System.out.println(newMessage);
		
		
		message = Encryption.viginereDicipher(newMessage,"key") ;
		System.out.println(message);
		
		
	}
}
