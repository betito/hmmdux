package hmmdux.method;
import java.util.Vector;

/*
 * Created on Jan 18, 2006
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
public class vPath {
 
	/**
	 *  Armazenar os estados para retornar, onde os estdos sao apontados pelos indices
	 */
	private Vector estados = null;
	public String estadosinternos = ""; 
	
	
	public vPath(){
		this.estados = new Vector(10);
		this.estadosinternos = new String(""); 
	}
	
	public vPath(int numest){
		this.estados = new Vector(numest);
	}
	
	public Object getEstadoAt(int pos){
		return this.estados.elementAt(pos);
	}
	
	public Object getLastEstado(){
		return this.estados.elementAt(this.estados.size() - 1);
	}
	
	public Object getLastEstado2(){
		return this.estados.elementAt(this.estados.size() - 2);
	}
	
	public int getNumEstados(){
		return this.estados.size();
	}
	
	public void putEstado(String e){
		this.estados.add(e);
	}

	public void putEstado(String e, String inte){
		this.estados.add(e);
		this.estadosinternos += inte;
		//System.out.println(this.estadosinternos);
	}
	
	public void putEstado(vPath e){
		this.estados.clear();
		this.estadosinternos = "";
		for(int t = 0; t < e.getNumEstados(); t++){
			this.estados.add((String) e.getEstadoAt(t));
		}
		
		this.estadosinternos = e.estadosinternos;
	}

	
	public String getEstadosToString(){
		String s = "";
		
		for(int t = 0; t < this.estados.size(); t++){
			s += "["+ this.estados.elementAt(t) + "]";
		}
		
		return s;
		
	}
	
	
	
	
	public void limpar(){
		this.estados.clear();
	}
	
	
	public String[] getEstados(){
		String s[] = new String[this.estados.size()];
		
		for(int t = 0; t < this.estados.size(); t++){
			s[t] = (String) this.estados.elementAt(t);
		}
		
		return s;
			
	}
	
	public String[] getEstadosValidos(){
		String s[] = new String[this.estados.size()];
		int t = 0;
		for(t = 0; t < this.estados.size() && (!((String)this.estados.elementAt(t)).equals("END")); t++){
			s[t] = (String) this.estados.elementAt(t);
		}
		String v[] = new String[t];
		
		for (int i = 0; i < t; i++) {
			v[i] = s[i];
			
		}
		
		return v;
			
	}

	public int tamanho(){
		return this.estados.size();
	}
	
	public int tamanhoAceitavel(){
		return this.getEstadosValidos().length;
	}
	


	
}

