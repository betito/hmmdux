package hmmdux.method;
import java.util.Vector;

public class hmmSeqEstados {
	private Vector estados = null;
	private Vector termos = null;
	
	public hmmSeqEstados(){
		this.estados = new Vector(10);
		this.termos = new Vector(10);
	}
	
	public hmmSeqEstados(int taminicial){
		this.estados = new Vector(taminicial);
		this.termos = new Vector(taminicial);
	}
	
	
	public void putEstado(String e, String o){
		this.estados.add(e);
		this.termos.add(this.estados.size()-1, o);
	}
	
	public String getEstadoAt(int pos){
		return (String) this.estados.elementAt(pos);
	}
	
	public String getTermoAt(int pos){
		return (String) this.termos.elementAt(pos);
	}
	
	public Vector getEstados(){
		return this.estados;
	}
	
	public void printEstados(){
		for (int i = 0; i < this.estados.size(); i++) {			
			System.out.print("["+getEstadoAt(i)+"]");
		}
		System.out.println();
	}
	
}
