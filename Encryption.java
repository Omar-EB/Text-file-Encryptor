//Author : Omar Elboraey
//Usage of the following code without the consent of Omar Elboraey is prohibited

import java.util.Map ;
import java.util.HashMap ;


public class Encryption{
	
	@SuppressWarnings("unchecked")
	
	private static String key ; // Encryption Key
	private static int shift ; // Shifting Integer
	private static Map <Integer, Character> characterMap ; // Character Maping Dictionnary.
	private static Map <Character, Integer> positionMap ; // Position Maping Dictionnary
	private static char[][] vrMatrix ; // Vigenère Square
	private static CipherList<Character> list ; // Character List (A->Z)
	
	
	private static boolean initialized = false ; // tester of the initializer method
	public static void initializer (){
		// Maps & List
		characterMap = new HashMap<Integer, Character>() ;
		positionMap = new HashMap<Character, Integer>() ;
		list = new CipherList<Character>() ;
		int position = 1 ;
		for(char letter = 'a'; letter <= 'z'; letter++, position++){
			characterMap.put(position,letter) ;
			positionMap.put(letter, position) ;
			list.add(letter) ;
		}
		
		// Square
		vrMatrix = new char[26][26] ;
		Iterator<Character> itr = list.cipheringIterator() ;
		
		for (int num = 0; num < 26; num++){
			int pos = 0 ;
			boolean finished = false ;
			char[] row = vrMatrix[num] ;
			char currentChar = itr.next() ;
			char aborter = currentChar ;
			row[pos]=currentChar ;
			while(!finished){
				pos++ ;
				currentChar = itr.next();
				if(currentChar==aborter){
					finished=true ;
				} else {
					row[pos] = currentChar ;
				}
				
			}
		}
		/*
			String s = "";
			for (int a=0; a<vrMatrix.length; a++){
				for (int b=0; b<vrMatrix.length; b++){
					s+=vrMatrix[a][b]+" " ;
				}
				s+=System.lineSeparator();
			}
			System.out.print(s);
		*/
		
	}
	
	// Mono-Alphabetic Encryption : Caesar Cipher
	public static String caesarCipher(String message,int shift){
		
		if (shift <= 0 || shift > 25){
			throw new UnsupportedOperationException("Invalid shifting integer provided: "+ shift) ;
		}
		if (message==null || message.equals("") ){
			throw new UnsupportedOperationException("Nothing to be encrypted.") ;
		}
		
		if (!initialized){
			initializer() ;
			initialized = true ;
		}
		String encryption = "" ;
		for (int marker = 0; marker < message.length(); marker++){
			char character = message.charAt(marker) ;
			char encryptedCharacter = '.' ;
			
			
			if(Character.isDigit(character)){
				int number = Character.getNumericValue(character) ;
				//Integer.parseInt(Character.toString(character)) ;
				int encryptedNumber ;
				if(number==9){
					encryptedNumber = 0 ;
				} else {
					encryptedNumber = number + 1 ; 
				}
				encryptedCharacter = (Integer.toString(encryptedNumber)).charAt(0) ;
				encryption += encryptedCharacter ;
			} else {
				boolean transition = false ;
				if (Character.isUpperCase(character)){
					character = Character.toLowerCase(character) ;
					transition=true ;
				}
				if (!positionMap.containsKey(character)){
					encryption += character ;
				} else {
					int position = positionMap.get(character) ;
					position = (position+shift)%26 ;
					
					if (position == 0){
						position=26 ;
					}
					encryptedCharacter = characterMap.get(position) ;
					if(transition){
						encryptedCharacter = Character.toUpperCase(encryptedCharacter) ;
					}
					encryption += encryptedCharacter ;
				}
				
			}
		}
		return encryption ;
	}
	
	
	// Corresponding Di-ciphering method.
	public static String caesarDicipher(String encryption,int shift){
		
		if (shift <= 0 || shift > 25){
			throw new UnsupportedOperationException("Invalid shifting integer provided: "+ shift+".") ;
		}
		if (encryption==null || encryption.equals("") ){
			throw new UnsupportedOperationException("Nothing to be decrypted.") ;
		}
		
		if (!initialized){
			initializer() ;
			initialized = true ;
		}
		
		String message = "" ;
		for (int marker = 0; marker < encryption.length(); marker++){
			char character = encryption.charAt(marker) ;
			char decryptedCharacter = '.' ; 
			if(Character.isDigit(character)){
				int number = Character.getNumericValue(character) ;
				//Integer.parseInt(Character.toString(character)) ;
				int decryptedNumber ;
				if(number==0){
					decryptedNumber = 9 ;
				} else {
					decryptedNumber = number - 1 ; 
				}
				decryptedCharacter = (Integer.toString(decryptedNumber)).charAt(0) ;
				message += decryptedCharacter ;
			} else {
				boolean transition = false ;
				if (Character.isUpperCase(character)){
					character = Character.toLowerCase(character) ;
					transition=true ;
				}
				if (!positionMap.containsKey(character)){
					message += character ;
				} else {
					int position = positionMap.get(character) ;
					position = position-shift ;
					if (position < 1){
						position=26+position ;
					}
					decryptedCharacter = characterMap.get(position) ;
					if(transition){
						decryptedCharacter = Character.toUpperCase(decryptedCharacter) ;
					}
					message += decryptedCharacter ;
				}
				
			}
		}
		return message ;
		
	}
	
	// Poly-Alphabetic Encryption : Vigenère Cipher
	public static String viginereCipher(String message, String key){
		if (message==null || message.equals("") ){
			throw new UnsupportedOperationException("Nothing to be encrypted.") ;
		}
		
		if (!initialized){
			initializer() ;
			initialized = true ;
		}
		
		if(key==null || key==""){
			throw new UnsupportedOperationException("Invalid Key :"+ key +".") ;
		}
		
		CipherList keyList = new CipherList() ;
		
		for (int k = 0; k < key.length(); k++){
			char verificator = key.charAt(k) ;
			if (Character.isUpperCase(verificator)){
				verificator = Character.toLowerCase(verificator) ; 
			}			
			if (!positionMap.containsKey(verificator)){
				throw new UnsupportedOperationException("The Key ("+key+") cannot contain spaces/numbers/special characters.") ;
			}
			keyList.add(verificator) ;
		}
		
		Iterator<Character> vrItr = keyList.cipheringIterator() ;
		String encryption = "" ;
		for (int marker = 0; marker < message.length(); marker++){
			char character = message.charAt(marker) ;
			char encryptedCharacter = '.' ;
			if(Character.isDigit(character)){
				int number = Character.getNumericValue(character) ;
				// Integer.parseInt(Character.toString(character)) ;
				int encryptedNumber ;
				if(number==9){
					encryptedNumber = 0 ;
				} else {
					encryptedNumber = number + 1 ; 
				}
				encryptedCharacter = (Integer.toString(encryptedNumber)).charAt(0) ;
				encryption += encryptedCharacter ;
			} else {
				boolean transition = false ;
				if (Character.isUpperCase(character)){
					character = Character.toLowerCase(character) ;
					transition=true ;
				}
				if (!positionMap.containsKey(character)){
					encryption += character ;
				} else {
					char matcher = vrItr.next() ;
					int X = positionMap.get(matcher)-1 ;
					int Y = positionMap.get(character)-1 ;
					encryptedCharacter = vrMatrix[Y][X] ;
					if(transition){
						encryptedCharacter = Character.toUpperCase(encryptedCharacter) ;
					}
					encryption += encryptedCharacter ;
				}
				
			}
		}
		
		return encryption ;
	}
	
	// Corresponding Di-ciphering method.
	public static String viginereDicipher(String encryption,String key){
		if (encryption==null || encryption.equals("") ){
			throw new UnsupportedOperationException("Nothing to be encrypted.") ;
		}
		
		if (!initialized){
			initializer() ;
			initialized = true ;
		}
		
		if(key==null || key==""){
			throw new UnsupportedOperationException("Invalid Key :"+ key +".") ;
		}
		
		CipherList keyList = new CipherList() ;
		
		for (int k = 0; k < key.length(); k++){
			char verificator = key.charAt(k) ;
			if (Character.isUpperCase(verificator)){
				verificator = Character.toLowerCase(verificator) ; 
			}			
			if (!positionMap.containsKey(verificator)){
				throw new UnsupportedOperationException("The Key ("+key+") cannot contain spaces/numbers/special characters.") ;
			}
			keyList.add(verificator) ;
		}
		
		Iterator<Character> vrItr = keyList.cipheringIterator() ;
		String message = "" ;
		for (int marker = 0; marker < encryption.length(); marker++){ 
			char character = encryption.charAt(marker) ;
			char decryptedCharacter = '.' ; 
			if(Character.isDigit(character)){
				int number = Character.getNumericValue(character) ;
				//Integer.parseInt(Character.toString(character)) ;
				int decryptedNumber ;
				if(number==0){
					decryptedNumber = 9 ;
				} else {
					decryptedNumber = number - 1 ; 
				}
				decryptedCharacter = (Integer.toString(decryptedNumber)).charAt(0) ;
				message += decryptedCharacter ;
			} else {
				boolean transition = false ;
				if (Character.isUpperCase(character)){
					character = Character.toLowerCase(character) ;
					transition=true ;
				}
				if (!positionMap.containsKey(character)){
					message += character ;
				} else {
					char matcher = vrItr.next() ;
					int X = positionMap.get(matcher)-1 ;
					char[] row = null ;
					boolean found = false ;
					for (int pos = 0; pos<26 && !found; pos++){
						row = vrMatrix[pos] ;
						if (row[X]==character){
							found = true ;
						}
					}
					decryptedCharacter = row[0] ;
					if(transition){
						decryptedCharacter = Character.toUpperCase(decryptedCharacter) ;
					}
					message += decryptedCharacter ;
				}
			
			}
		}
		return message ;
	}
}
