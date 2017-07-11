package pp2017.team20.shared;



public class Door extends Elements {
	private boolean open;
	
	public Door(boolean open){
		this.open = open;	
	}
	
	public void setOpen(){
		open = true;
	}
	
	public void setClose(){
		open = false;
	}
	
	public boolean isOpen(){
		return open;
	}


}
