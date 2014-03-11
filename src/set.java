//Made to learn array list. Can just replace with new Arraylist() where its used

class set {
	private Object[] elements;
	private int size=0;
	
	public set(int maxsize) throws Exception{
		if (maxsize>=0){
			elements=new Object[maxsize];
		}
		else{
			throw new Exception("set size cannot be negative");
		}
	}
	public set(){
		elements=new Object[100];
	}
	
	public void add(Object a){
		if(!this.contains(a)){
			if(size < elements.length){
				elements[size]=a;
				size++;
			}
			else{
				Object[] carry = new Object[size+1];
				for(int i=0;i<elements.length;i++){
					carry[i]=elements[i];
				}
				carry[size]=a;
				elements=carry;
				size++;
			}
		}
	}
	
	public boolean contains(Object key){
		int i = 0;
		boolean found=false;
		while(i < size && !found){
			if(elements[i].equals(key)){
				found=true;
			}
			i++;
		}
		return found;
	}
	
	public String toString(){
		String value = "Set[ \n";
		for(int i=0; i<size;i++){
				value+=elements[i].toString();
		}
		value+=" ]";
		return value;
	}
	
	public int size(){
		return size;
	}
	
	public boolean equals(Object o){
		if(o instanceof set && ((set) o).size()==size){
			int i = 0;
			while(i<size){
				if(!((set) o).contains(elements[i])){
					return false;
				}
				i++;
			}
		}
		return true;
	}
	
	public Object get(int index) throws Exception{
		if(index >= 0 && index < size){
			return elements[index];
		}
		else
			throw new Exception("Array index out of bounds");
	}
	
	public void remove(int index) throws Exception{
		if(index >= 0 && index < size){
			for(int i = index;i < size;i++){
					elements[i]=elements[i+1];
				}
				elements[size]=null;
				size--;
		}
		else if(index == size){
			elements[size]=null;
		}
		else
			throw new Exception("Array index out of bounds");	
	}
	
	public void remove(Object key) throws Exception{
		if(this.contains(key)){
			for(int i = 0;i<size;i++){
				if(elements[i].equals(key)){
					this.remove(i);
				}
			}
		}
	}
	
	public set union(set other) throws Exception{//elements that are the same
		set newset = new set(size + other.size);
		for(int i=0;i<size;i++){
			newset.add(this.elements[i]);
		}
		for(int i=0; i<other.size;i++){
			newset.add(other.get(i));
		}
		return newset;
	}
	
	public set intersection(set other) throws Exception{//elements that are different
		int minsize;
		if(size>other.size)
			minsize=size;
		else
			minsize=other.size;
		set newset = new set(minsize);
		for(int i=0;i<other.size;i++){
			if(this.contains(other.get(i))){
				newset.add(other.get(i));
			}
		}
		return newset;
	}
	
	public set copy() throws Exception{
		set newset=new set(size);
		for(int i=0;i<size;i++){
			newset.add(elements[i]);
		}
		return newset;
	}
	
}
