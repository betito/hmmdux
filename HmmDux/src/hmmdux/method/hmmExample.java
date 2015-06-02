package hmmdux.method;


import hmmdux.auxclass.auxFunctions;
import hmmdux.auxclass.Const;
/*
 * Created on Jan 12, 2006
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
public class hmmExample {

	String canonicestado = null;
	String estado = null;
	String simbolos[] = null;
	
	public hmmExample(String par){
		
		String temp[] = par.split("::");
		estado = new String(temp[1].toUpperCase());
		canonicestado = new String(temp[1]);
		simbolos = temp[0].split(" ");
		
		this.capsAll();
		
	}
	
	private void capsAll(){
		this.estado = this.estado.toUpperCase();
		
		for(int t = 0; t < this.simbolos.length; t++){
			if(auxFunctions.isNumberValue(this.simbolos[t])){
			
				this.simbolos[t] = Const.NumValue;
			}else{
				this.simbolos[t] = this.simbolos[t].toUpperCase();
			}
		}
	}
	
	public String toString(){
		
		String t = "";
		for(int v = 0; v < simbolos.length; v++){
			t += "[" + simbolos[v] + "]";
		}
		return ("<EST:"+estado+",SBL:" + t +">");
	}
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstadoOriginal() {
		return estado.toUpperCase();
	}

	/**
	 * @return Returns the estado cononico.
	 */
	public String getEstado() {
		return this.canonicestado.toUpperCase();
	}
	
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the simbolos.
	 */
	public String[] getSimbolos() {
		return simbolos;
	}
	
	/**
	 * @return Returns the simbolo em pos
	 */
	public String getSimboloAt(int pos) {
		return auxFunctions.canonicValue(simbolos[pos]);
	}
	
	public String getSimbolo(){
		String st = null;
		
		for(int j = 0; j < this.getTotalSimbolos(); j++){
			st = auxFunctions.canonicValue(this.getSimboloAt(j));
		}
		
		return st;
	}
	
	public int getTotalSimbolos() {
		return simbolos.length;
	}

	/**
	 * @param simbolos The simbolos to set.
	 */
	public void setSimbolos(String[] simbolos) {
		this.simbolos = simbolos;
	}
}
