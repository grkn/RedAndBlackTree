import java.util.Arrays;
import java.util.List;



/**
 * 1 - Every node is either RED or BLACK
 * 2 - Every nil node is BLACK
 * 3 - Every RED node has two BLACK child nodes
 * 4 - Every path from node x down to a leaf node has the same number of Black nodes
 * 5 - The root node is always BLACK
 * 
 * VIOLATION : 
 * Case 1 : Uncle is Red. Red node has red child. Solution : Change Color of  parent, grandparent, uncle 
 * Case 2 : Uncle is Black. Child is inside only in little subtree and Red node has red node. Solution : Rotate right or left
 * Case 3 : Uncle is Black. Red node has red child. Solution : Rotate the grandparent(left or right) and swap the color of grandparent and parent
 * @author gilleez
 *
 * @param <T>
 */
public class RedAndBlackTree<T extends Comparable> implements BalancedTree<T>{

	Node<T> root = null;
	
	private static class Node<T>{
		T data;
		Node<T> leftChild = null;
		Node<T> rightChild = null;
		Node<T> parent;
		//RED : 1 BLACK : 0
		int color = 1;
		
		@Override
		public String toString() {
			return "Node : "+data;
		}
		
	}
	
	@Override
	public void insert(T data) {
		if(root == null){
			root = new Node<T>();
			root.data = data;
			colorMe(root);
		}else{
			Node<T> tmp = root;
			int flag = 0;
			while(true){
				if(tmp.data.compareTo(data) > 0 ){
					if(tmp.leftChild == null){
						flag = 0;
						break;
					}else
					tmp = tmp.leftChild;
				}else
				if(tmp.data.compareTo(data) < 0 ){
					if(tmp.rightChild == null){
						flag = 1;
						break;
					}else
					tmp = tmp.rightChild;
				}
			}
			Node<T> newNode = new Node<T>();
			newNode.data = data;
			newNode.parent = tmp;
			if(flag == 0)
				tmp.leftChild = newNode;
			else
				tmp.rightChild = newNode;
			do{
				balance(newNode);
				newNode = newNode.parent;
			}while(newNode.parent != null);
			
		}
	}
	
	private boolean isRed(Node<T> node){
		if(node == null)
			return false;
		if(node.color == 1)
			return true;
		else
			return false;
	}
	
	private Node<T> getUncle(Node<T> node){
		if(node.parent != null && node.parent.parent != null){
			if(node.parent.parent.rightChild != node.parent){
				return node.parent.parent.rightChild;
			}else
				return node.parent.parent.leftChild;
		}
		return null;
	}
	
	private boolean isRightChild(Node<T> newNode){
		if(newNode.parent.rightChild != null && newNode.parent.rightChild.equals(newNode))
			return true;
		else
			return false;
	}
	
	private void balance(Node<T> newNode) {
		
		if(isRed(newNode.parent) && isRed(newNode)){
			System.out.println("Inserted Node Data : "+newNode.data);
			Node<T> uncle = getUncle(newNode);
			boolean red = isRed(uncle);
			if(red){
				//recolor parent grandparent uncle
				colorMe(newNode.parent.parent);
				colorMe(newNode.parent);
				if(isRightChild(newNode.parent))
				colorMe(newNode.parent.parent.leftChild);
				else
				colorMe(newNode.parent.parent.rightChild);
				System.out.println("CASE 1");
			}else{
				if(isRightChild(newNode)){
					if(isRightChild(newNode.parent)){
						//rotate left case 3
						rotateGrandParentLeft(newNode.parent);
						System.out.println("CASE 3");
					}else{
						//rotate rightleft case 2
						rotateLeft(newNode.parent);
						rotateGrandParentLeft(newNode.parent);
						System.out.println("CASE 2");
					}
				}else{
					if(isRightChild(newNode.parent)){
						//rotate left right case 2
						rotateRight(newNode.parent);
						rotateGrandParentRight(newNode.parent);
						System.out.println("CASE 2");
					}else{
						//rotate right case 3
						rotateGrandParentRight(newNode.parent);
						System.out.println("CASE 3");
					}
				}
			}
			if(isRed(root))
				colorMe(root);
			display(root);
		}
		
	}

	private void rotateLeft(Node<T> parent) {
		Node<T> gParent = parent.parent;
		Node<T> newNode = parent.rightChild;
		gParent.leftChild = newNode;
		newNode.parent = gParent;
		newNode.leftChild = parent;
		parent.rightChild = null;
		parent.leftChild = null;
	}

	private void rotateGrandParentLeft(Node<T> parent) {
		Node<T> gParent = parent.parent;
		swapColor(gParent, parent);
		Node<T> transferChild = parent.leftChild;
		parent.leftChild = gParent;
		parent.parent = gParent.parent;
		if(gParent == root)
			root = parent;
		else{
			if(isRightChild(gParent)){
				gParent.parent.rightChild = parent;
			}else{
				gParent.parent.leftChild = parent;
			}
		}
		gParent.parent = parent;
		gParent.rightChild = transferChild;
		
	}
	
	private void rotateGrandParentRight(Node<T> parent) {
		Node<T> gParent = parent.parent;
		swapColor(gParent, parent);
		Node<T> transferChild = parent.rightChild;
		parent.rightChild = gParent;
		parent.parent = gParent.parent;
		if(root == gParent)
			root = parent;
		else{
			if(isRightChild(gParent)){
				gParent.parent.rightChild = parent;
			}else{
				gParent.parent.leftChild = parent;
			}
		}
		gParent.parent = parent;
		gParent.leftChild = transferChild;
		
	}
	
	private void swapColor(Node<T> node1,Node<T> node2){
		int color = node1.color;
		node1.color = node2.color;
		node2.color = color;
	}
	
	private void rotateRight(Node<T> parent) {
		Node<T> gParent = parent.parent;
		Node<T> newNode = parent.leftChild;
		gParent.rightChild = newNode;
		newNode.parent = gParent;
		newNode.rightChild = parent;
		parent.leftChild = null;
		parent.rightChild = null;
	}

	public void display(Node<T> tmp){
		if(tmp == null)
			return;
		else{
			
			System.out.print(tmp.data + " color : " + tmp.color);
			if(tmp.parent != null)
				System.out.println(" Parent : "+tmp.parent.data );
			else
				System.out.println("");
			display(tmp.leftChild);
			display(tmp.rightChild);
		}
	}

	private void colorMe(Node<T> tmp){
		if(tmp.color == 1)
			tmp.color = 0;
		else
			tmp.color = 1;
	}
	
	private void colorParent(Node<T> tmp){
		if(tmp.parent != null){
			tmp = tmp.parent;
			colorMe(tmp);
		}
	}
	
	private void colorGParent(Node<T> tmp){
		if(tmp.parent != null && tmp.parent.parent != null){
			tmp = tmp.parent.parent;
			colorMe(tmp);
		}
	}
	
	private void colorUncle(Node<T> tmp){
		if(tmp.parent != null && tmp.parent.parent != null){
			Node<T> gparent = tmp.parent.parent;
			if(gparent.leftChild.equals(tmp.parent)){
				tmp = gparent.rightChild;
			}else{
				tmp = gparent.leftChild;
			}
			colorMe(tmp);
		}
	}
	
	@Override
	public T get(T data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(T data) {
		// TODO Auto-generated method stub
		
	}

	
	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8);
		RedAndBlackTree<Integer> tree = new RedAndBlackTree<Integer>();
		for (Integer integer : list) {
			tree.insert(integer);
		}
		
	
	}
}
