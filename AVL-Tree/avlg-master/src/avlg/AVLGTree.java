package avlg;

import avlg.exceptions.UnimplementedMethodException;
import avlg.exceptions.EmptyTreeException;
import avlg.exceptions.InvalidBalanceException;

/** <p>{@link AVLGTree}  is a class representing an <a href="https://en.wikipedia.org/wiki/AVL_tree">AVL Tree</a> with
 * a relaxed balance condition. Its constructor receives a strictly  positive parameter which controls the <b>maximum</b>
 * imbalance allowed on any subtree of the tree which it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3.</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations. On the other hand, increasing
 *  the balance parameter also means that we will be making <b>insertions</b> faster.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see EmptyTreeException
 * @see InvalidBalanceException
 * @see StudentTests
 */
public class AVLGTree<T extends Comparable<T>> {

    /* ********************************************************* *
     * Write any private data elements or private methods here...*
     * ********************************************************* */
	
	private int size = 0; 
	private Node root; 
	int imbalance; 
	
	private class Node { 
		
		T value; 
		Node right; 
		Node left; 
		int height; 
		
//		public Node() { 
//			right = null; 
//			left = null; 
//			value = null; 
//			height = 0; 
//		}
//		
		public Node(T valueIn) { 
			value = valueIn; 
			right = null; 
			left = null; 
			height = 0; 
		}
	}



    /* ******************************************************** *
     * ************************ PUBLIC METHODS **************** *
     * ******************************************************** */

    /**
     * The class constructor provides the tree with the maximum imbalance allowed.
     * @param maxImbalance The maximum imbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if maxImbalance is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
    	
    	if (maxImbalance < 1) { 
    		throw new InvalidBalanceException("invalid imbalance"); 
    	}
    	
    	imbalance = maxImbalance; 
    	
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }
    
    private int getHeight(Node nodeIn) { 
    	//get method to avoid null pointer exceptions 
    	
    	if (nodeIn == null) { 
    		return -1; 
    	}
    	
    	return nodeIn.height; 
    }
    
    private Node lRotate(Node nodeIn) { 
    	
    	Node right = nodeIn.right; 
    	Node rightLTree = right.left; 
    	
    	right.left = nodeIn; 
    	nodeIn.right = rightLTree; 
    	
    	int nodeHeight = getHeight(nodeIn.left) > getHeight(nodeIn.right) ? getHeight(nodeIn.left) : getHeight(nodeIn.right); 
    	nodeIn.height = nodeHeight + 1; 
    	
    	int rightHeight = getHeight(right.left) > getHeight(right.right) ? getHeight(right.left) : getHeight(right.right); 
    	right.height = rightHeight + 1; 
    	
    	return right; 
    	
    }
    
    private Node rRotate(Node nodeIn) { 
    	
    	Node left = nodeIn.left; 
    	Node leftRTree = left.right; 
    	
    	left.right = nodeIn; 
    	nodeIn.left = leftRTree;
    	
    	int nodeHeight = getHeight(nodeIn.left) > getHeight(nodeIn.right) ? getHeight(nodeIn.left) : getHeight(nodeIn.right); 
    	nodeIn.height = nodeHeight + 1; 
    	
    	int leftHeight = getHeight(left.left) > getHeight(left.right) ? getHeight(left.left) : getHeight(left.right);  
    	left.height = leftHeight + 1; 
    	
    	return left; 
    }
    
    public int balanceCalc(Node nodeIn) { 
    	
    	if (nodeIn.left == null && nodeIn.right == null) { 
    		return 0; 
    	} else if (nodeIn.left == null) { 
    		return (-1 - nodeIn.right.height); 
    	} else if (nodeIn.right == null) { 
    		return (nodeIn.left.height + 1); 
    	} else { 
    		return nodeIn.left.height - nodeIn.right.height; 
    	}
    }
    
    private Node insertHelper(Node curr, T keyIn) { 
    	
    	if (curr == null) { 
    		return new Node(keyIn); 
    	}
    	
    	if (keyIn.compareTo(curr.value) < 0) { 
    		curr.left = insertHelper(curr.left, keyIn);
    
    	} else { 
    		curr.right = insertHelper(curr.right, keyIn); 
    	}
    	
    	int height = getHeight(curr.left) > getHeight(curr.right) ? getHeight(curr.left) : getHeight(curr.right); 
    	curr.height = height + 1; 
    	
    	if (balanceCalc(curr) > imbalance) { 
    		
    		if (keyIn.compareTo(curr.left.value) < 0) { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr = rRotate(curr); 
    				root = curr; 
    			} else { 
    				curr = rRotate(curr); 
    			}
    			
    		} else { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr.left = lRotate(curr.left); 
        			curr = rRotate(curr); 
        			root = curr; 
    			} else { 
    				curr.left = lRotate(curr.left); 
        			curr = rRotate(curr); 
    			}
    		}
    		
    	} else if (balanceCalc(curr) < -imbalance) { 
    		
    		if (keyIn.compareTo(curr.right.value) > 0) { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr = lRotate(curr); 
    				root = curr; 
    			} else { 
    				curr = lRotate(curr);
    			}
    
    			
    		} else { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr.right = rRotate(curr.right); 
        			curr = lRotate(curr); 
        			root = curr; 
    			} else { 
    				curr.right = rRotate(curr.right); 
        			curr = lRotate(curr);
    			}
    			
    		}
    	}
    	
    	
    	return curr; 
    }
    		
    	
// 
    /**
     * Insert key in the tree. You will <b>not</b> be tested on
     * duplicates! This means that in a deletion test, any key that has been
     * inserted and subsequently deleted should <b>not</b> be found in the tree!
     * s
     * @param key The key to insert in the tree.
     */
    public void insert(T key) {
    	
    	size++; 
    	
    	if (root == null) { 
    		root = new Node (key); 
    	} else { 
    		insertHelper(root, key); 
    	}
    	
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }
    
    private Node deleteHelper(Node curr, T key) { 
    	
    	if (curr == null) { 
    		return curr; 
    	}
    	
    	if (key.compareTo(curr.value) < 0) { 
    		curr.left = deleteHelper (curr.left , key); 
    	} else if (key.compareTo(curr.value) > 0) { 
    		curr.right = deleteHelper(curr.right, key); 
    	} else { 
    		

    		if ((curr.left == null || curr.right == null)) { 
    			Node delete; 
    			
    			if (curr.left == null) {  
    				delete = curr.right; 
    			} else { 
    				delete = curr.left; 
    			}
    			
    			if (delete == null) { 
    				//
    				curr = null; 
    			} else { 
    				
    				if (curr.value.compareTo(root.value) == 0) { 
    					curr = delete; 
    					root = curr; 
    				} else { 
    					curr = delete; 
    				}
    			}
    		} else { 
    			
    			Node inOrder = curr.right; 
    			
    			while (inOrder.left != null) { 
    				inOrder = inOrder.left; 
    			} 
    			
    			curr.value = inOrder.value; 
    			
    			curr.right = deleteHelper(curr.right, inOrder.value); 
    			
    		}
    	}
    	
    	if (curr == null) { 
    		return curr; 
    	}
    	
    	int height = getHeight(curr.left) > getHeight(curr.right) ? getHeight(curr.left) : getHeight(curr.right); 
    	curr.height = height + 1; 
    	
    	if (balanceCalc(curr) > imbalance) { 
    		
    		if (balanceCalc(curr.left) >= 0 ) { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr = rRotate(curr); 
    				root = curr; 
    			} else { 
    				curr = rRotate(curr); 
    			}
    			
    		} else { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr.left = lRotate(curr.left); 
        			curr = rRotate(curr); 
        			root = curr; 
    			} else { 
    				curr.left = lRotate(curr.left); 
        			curr = rRotate(curr); 
    			}
    		}
    		
    	} else if (balanceCalc(curr) < -imbalance) { 
    		
    		if (balanceCalc(curr.right) <= 0 ) { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr = lRotate(curr); 
    				root = curr; 
    			} else { 
    				curr = lRotate(curr);
    			}
    
    			
    		} else { 
    			
    			if (curr.value.compareTo(root.value) == 0) { 
    				curr.right = rRotate(curr.right); 
        			curr = lRotate(curr); 
        			root = curr; 
    			} else { 
    				curr.right = rRotate(curr.right); 
        			curr = lRotate(curr);
    			}
    			
    		}
    	}
    	
    	
    	return curr; 
    	
    	
    }

    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or {@code null} if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T delete(T key) throws EmptyTreeException {
    	
    	if (root == null) { 
    		throw new EmptyTreeException("empty tree"); 
    	} else if (search(key) == null) { 
    		return null; 
    	} else { 
    		T return_val = search(key); 
    		size--; 
    		root = deleteHelper(root, key); 
    		return return_val; 
    	}
    	
//    	throw new UnimplementedMethodException();
    }

    /**
     * <p>Search for key in the tree. Return a reference to it if it's in there,
     * or {@code null} otherwise.</p>
     * @param key The key to search for.
     * @return key if key is in the tree, or {@code null} otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
    	
    	if (root == null) { 
    		throw new EmptyTreeException("empty tree"); 
    	}
    	
    	Node curr = root; 
    	
    	while (curr != null) { 
    		
    		if (curr.value.compareTo(key) == 0) { 
    			return key; 
    		} else if (curr.value.compareTo(key) > 0) { 
    			curr = curr.left; 
    		} else { 
    			curr = curr.right; 
    		}
    		
    	}
    	
    	return null; 
    	
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * Retrieves the maximum imbalance parameter.
     * @return The maximum imbalance parameter provided as a constructor parameter.
     */
    public int getMaxImbalance(){
    	
    	return imbalance; 
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }


    /**
     * <p>Return the height of the tree. The height of the tree is defined as the length of the
     * longest path between the root and the leaf level. By definition of path length, a
     * stub tree has a height of 0, and we define an empty tree to have a height of -1.</p>
     * @return The height of the tree. If the tree is empty, returns -1.
     */
    public int getHeight() {
    	
    	if (root == null) { 
    		return -1; 
    	}
    	
    	return root.height; 
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return {@code true} if the tree is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
    	
    	return (root == null); 
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * Return the key at the tree's root node.
     * @return The key at the tree's root node.
     * @throws  EmptyTreeException if the tree is empty.
     */
    public T getRoot() throws EmptyTreeException{
    	
    	if (root == null) { 
    		throw new EmptyTreeException("empty tree"); 
    	}
    	
    	return root.value; 
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the Binary Search Tree property,
     * {@code false} otherwise.
     */
    
    
    public boolean isBstHelper(Node nodeIn) { 
    	
    	if (nodeIn == null) { 
    		return true; 
    	} else if (nodeIn.left != null && nodeIn.value.compareTo(nodeIn.left.value) < 0) { 
    		return false; 
    	} else if (nodeIn.right != null && nodeIn.value.compareTo(nodeIn.right.value) > 0) { 
    		return false; 
    	} else { 
    		return (isBstHelper(nodeIn.left) && true && isBstHelper(nodeIn.right)); 
    	}
    	
    }
    
    
    public boolean isBST() {
    	
    	return isBstHelper(root); 
    	
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }


    
    
    private boolean balancedHelper(Node nodeIn) { 
    
    	
    	if (nodeIn == null) { 
    		return true; 
    	}
    	else if (balanceCalc(nodeIn) > imbalance || balanceCalc(nodeIn) < -imbalance) { 
    		return false; 
    	} else { 
    		return (balancedHelper(nodeIn.left) && true && balancedHelper(nodeIn.right)); 
    	}
    	
    	
    }
    
    
    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the balance requirements of an AVLG tree, {@code false}
     * otherwise.
     */
    public boolean isAVLGBalanced() {
    	
    	return (isBST() && balancedHelper(root)); 
    	
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * <p>Empties the AVL-G Tree of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
    	
    	root = null; 
    	size = 0; 
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }


    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
    	
    	return size; 
//        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }
}
