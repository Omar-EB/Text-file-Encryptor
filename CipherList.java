import java.util.NoSuchElementException ;

public class CipherList <E> {
	@SuppressWarnings("unchecked")
	
	private Node<E> head ; 
	private Node<E> currentNode ;
	private int size ;
	
	public CipherList(){
		size = 0 ;
		head = null ;
	}
	
	private static class Node<T>{
		private T value ;
		private Node<T> next ;
		
		private Node(T value, Node<T> next){
			this.value=value ; 
			this.next=next ;
		}
		
	}
	
	private class CipherListIterator implements Iterator<E>{
		private Node<E> current ;
		private CipherListIterator(){
			current = null ;
		}
		
		public boolean hasNext(){
			return (head!=null) ;
		}
		public E next(){
			if(!hasNext()){
				throw new NoSuchElementException() ;
			}
			if(current==null || current.next==null){
				current=head ;
			} else {
				current = current.next ;
			}
			
			return current.value ;
		}
	}
	
	public Iterator<E> cipheringIterator() {
		return new CipherListIterator();
    }
	
	public void add(E value){
		if(currentNode == null){
			head = new Node<E>(value, null) ;
			currentNode = head ;
		} else {
			currentNode.next = new Node(value, null) ;
			currentNode = currentNode.next ;
		}
		
	}
	public int size(){
		return size ;
	}
	
}
