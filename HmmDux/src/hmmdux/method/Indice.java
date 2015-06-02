package hmmdux.method;
import java.util.Hashtable;

/*
 * Created on Jan 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author ros
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Indice {

	private int pos = 0;
	
	public Indice(){
		this.pos = 0;
	}
	
	public Indice(int p){
		this.pos = p;
	}
	
	public static int getIndice(Indice p){
		return p.getPos();
	}
	
	
	public static int getIndice(Hashtable h, String est){
		if(h.containsKey(est.toUpperCase())){
			return Indice.getIndice((Indice) h.get(est.toUpperCase()));
		}
		return 0; // esse zero eh o valor do UNK
	}
	
	
	
	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}
	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
}
