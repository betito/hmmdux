package hmmdux.auxclass;

import hmmdux.method.vPath;

/*
 * Created on Jan 19, 2006
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
public class auxVit {

	
	/**
	 * Serve como se fosse a vari?vel T do texto da wikipedia
	 */
	
	private double prob = 0.0;
	private double vprob = 0.0;
	private vPath estat = null;
	private String simbstr = "";
	private auxVit axin = null;
	private int acuminc = 0;
	
	public auxVit(){
		prob = 0.0;
		vprob = 0.0;
		estat = new vPath();
		
	}
	
	public auxVit(double p, String e, double vp){
		prob = p;
		vprob = vp;
		estat = new vPath();
		estat.putEstado(e);
	}
	
	
	public auxVit(double p, vPath e, double vp){
		prob = p;
		vprob = vp;
		this.estat = new vPath(10);
		for(int t = 0; t < e.getNumEstados(); t++){
			this.estat.putEstado((String) e.getEstadoAt(t));
		}
	}
	
	public auxVit(double p, vPath e, double vp, auxVit in){
		prob = p;
		vprob = vp;
		this.estat = new vPath(10);
		for(int t = 0; t < e.getNumEstados(); t++){
			this.estat.putEstado((String) e.getEstadoAt(t));
		}
		this.axin = in;
	}
	
	
	/**
	 * @return Returns the estate.
	 */
	public vPath getEstat() {
		return this.estat;
	}
	/**
	 * @return Returns the prob.
	 */
	public double getProb() {
		return prob;
	}
	/**
	 * @param prob The prob to set.
	 */
	public void setProb(double prob) {
		this.prob = prob;
	}
	/**
	 * @return Returns the vprob.
	 */
	public double getVprob() {
		return vprob;
	}
	/**
	 * @param vprob The vprob to set.
	 */
	public void setVprob(double vprob) {
		this.vprob = vprob;
	}

	public void setArgs(double prob, vPath vp, double vprob) {
		this.prob = prob;
		this.estat.limpar();
		if(vp != null)
		for(int r = 0; r < vp.getNumEstados(); r++){
			this.estat.putEstado((String) vp.getEstadoAt(r));
		}else{
			this.estat.putEstado("NADA");			
		}
 
		this.vprob = vprob;
	}
	
	public void setArgs(double prob, vPath vp, double vprob, auxVit in, int h) {
		this.prob = prob;
		this.estat.limpar();
		if(vp != null)
		for(int r = 0; r < vp.getNumEstados(); r++){
			this.estat.putEstado((String) vp.getEstadoAt(r));
		}else{
			this.estat.putEstado("NADA");			
		}
		this.estat.estadosinternos = vp.estadosinternos + "\n";
		this.vprob = vprob;
		this.axin = in;
		this.acuminc = h;
	}

	public void setArgs(double prob, vPath vp, double vprob, String v) {
		this.prob = prob;
		this.estat.limpar();
		if(vp != null)
		for(int r = 0; r < vp.getNumEstados(); r++){
			this.estat.putEstado((String) vp.getEstadoAt(r));
		}else{
			this.estat.putEstado("NADA");			
		}
 
		this.vprob = vprob;
		this.simbstr = v;
	}
	
	
	public String getEstatToString(){
		return this.estat.getEstadosToString();
	}
	
	public String getLastEstatToString(){
		return (String) this.estat.getLastEstado();
	}
	
	public String[] getEstadosValidos(){
		return this.estat.getEstadosValidos();
	}
	
	public String getEstadosValidostoString(){
		String tmp[] = this.estat.getEstadosValidos();
		StringBuffer saida = new StringBuffer();
		for(int a = 0; a < tmp.length; a++){
			saida.append(tmp[a] + " ");
		}
		
		return saida.toString(); 
	}
	
	
	public int getNumEstValidos(){
		return this.estat.tamanhoAceitavel();
	}
	
	public int getNumObsConsumidas(){
		return this.simbstr.split("\\s+").length;
	}

	public String getSimbout() {
		return simbstr;
	}

	public void setSimbstr(String simstr) {
		this.simbstr = simstr;
	}

	public auxVit getAxin() {
		return axin;
	}

	public void setAxin(auxVit axin) {
		this.axin = axin;
	}
	
	public int getIncremento() {
		if(this.axin == null){
			return 0;
		}
		return this.axin.getNumEstValidos();
	}

	public int getAcuminc() {
		return acuminc;
	}

	public void setAcuminc(int acuminc) {
		this.acuminc = acuminc;
	}
	
}
