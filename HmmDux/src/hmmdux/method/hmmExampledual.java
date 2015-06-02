package hmmdux.method;

import hmmdux.auxclass.auxFunctions;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class hmmExampledual {

	String Estadao = "";
	String canonicmeta = "";
	String meta = "";
	String dado = "";
	String dadoorig = "";
	String metasimbolo = "";
	String metapath = "";
	String primeiro = "";
	public Vector postermo = null;
	public Vector posestado = null;
	
	public hmmExampledual(String par){
		
		postermo = new Vector(10);
		posestado = new Vector(10);
		//SUITE=<D:03,M:suites>
		String temp[] = par.split("=");
		this.Estadao = new String(temp[0]);
		
		// pegar o D:
		Pattern p1 = Pattern.compile("<D:(.*)>");
		Pattern p2 = Pattern.compile("<M:(.*)>");
		Pattern p3 = Pattern.compile("<D:(.*),M:(.*)>");
		Pattern p4 = Pattern.compile("<M:(.*),D:(.*)>");
		
		String texto = temp[1];

		Matcher m = p3.matcher(texto);
		if(m.matches()){
			this.dadoorig = m.group(1);
			this.dado = auxFunctions.fixNumberWordValue(dadoorig);
			this.meta = m.group(2);
			this.metapath = auxFunctions.fixNumberWordValueMetaPath(this.dadoorig) + " " + "M";
			this.metasimbolo = this.dado + " " + this.meta;
			this.primeiro = this.dado;
			
			String t[] = this.dado.split("\\s+");
			for(int c = 0; c < t.length; c++){
				postermo.add(t[c]);
				if(auxFunctions.isNumberValue(t[c])){
					posestado.add("_N_");
				}else{
					posestado.add("_W_");
				}
			}
			String x[] = this.meta.split("\\s+");
			for(int c = 0; c < x.length; c++){
				postermo.add(x[c]);
				posestado.add("M");
			}
			//this.metasimbolo = auxFunctions.fixNumberValue(dadoorig) + " " + "M";
		}else{
			m = p4.matcher(texto);
			if(m.matches()){
				this.dadoorig = m.group(2);
				this.dado = auxFunctions.fixNumberWordValue(dadoorig);
				this.meta = m.group(1);
				this.metapath = "M" + " " + auxFunctions.fixNumberWordValueMetaPath(this.dadoorig);
				this.metasimbolo = this.meta + " " + this.dado;
				this.primeiro = this.meta;
				
				String x[] = this.meta.split("\\s+");
				for(int c = 0; c < x.length; c++){
					postermo.add(x[c]);
					posestado.add("M");
				}

				String t[] = this.dado.split("\\s+");
				for(int c = 0; c < t.length; c++){
					postermo.add(t[c]);
					if(auxFunctions.isNumberValue(t[c])){
						posestado.add("_N_");
					}else{
						posestado.add("_W_");
					}
				}
			}else{
				m = p2.matcher(texto);
				if(m.matches()){
					this.meta = m.group(1);
					this.metapath = "M";
					this.metasimbolo = this.meta;
					this.primeiro = this.meta;

					String t[] = this.meta.split("\\s+");
					for(int c = 0; c < t.length; c++){
						postermo.add(t[c]);
						posestado.add("M");
					}
					
				}else{
					m = p1.matcher(texto);
					if(m.matches()){
						this.dadoorig = m.group(1);
						this.dado = auxFunctions.fixNumberWordValue(dadoorig);
						this.metapath = auxFunctions.fixNumberWordValueMetaPath(this.dadoorig);
						this.metasimbolo = this.dado;
						this.primeiro = this.dado;

						String t[] = this.dado.split("\\s+");
						for(int c = 0; c < t.length; c++){
							postermo.add(t[c]);
							if(auxFunctions.isNumberValue(t[c])){
								posestado.add("_N_");
							}else{
								posestado.add("_W_");
							}
						}
						
					}
				}
			}
		}	
	}
	
	/**
	 * @return Returns the canonicEstadao.
	 */
	public String getEstadao() {
		return Estadao;
	}
	/**
	 * @param canonicEstadao The canonicEstadao to set.
	 */
	public void setEstadao(String canonicEstadao) {
		this.Estadao = canonicEstadao;
	}
	/**
	 * @return Returns the canonicEstado.
	 */
	public String getCanonicmeta() {
		return canonicmeta;
	}
	/**
	 * @param canonicEstado The canonicEstado to set.
	 */
	public void setCanonicMeta(String canonicm) {
		this.canonicmeta = canonicm;
	}
	/**
	 * @return Returns the dado.
	 */
	public String getDado() {
		return dado;
	}
	/**
	 * @param dado The dado to set.
	 */
	public void setDado(String dado) {
		this.dado = dado;
	}
	/**
	 * @return Returns the dadoorig.
	 */
	public String getDadoorig() {
		return dadoorig;
	}
	/**
	 * @param dadoorig The dadoorig to set.
	 */
	public void setDadoorig(String dadoorig) {
		this.dadoorig = dadoorig;
	}
	/**
	 * @return Returns the meta.
	 */
	public String getMeta() {
		return meta;
	}
	/**
	 * @param meta The meta to set.
	 */
	public void setMeta(String meta) {
		this.meta = meta;
	}
	
	public String getTudo(){
		String saida = "";
		
		saida += "Estadao : " + this.getEstadao();
		saida += "\nMeta : " + this.getMeta();
		saida += "\nDado(RX) : " + this.getDado();
		saida += "\nDado : " + this.getDadoorig();
		saida += "\nMetaSimbolo : " + this.getMetasimbolo();
		
		return saida;
		
		
	}
	
	/**
	 * @return Returns the metasimbolo.
	 */
	public String getMetasimbolo() {
		return metasimbolo;
	}
	/**
	 * @param metasimbolo The metasimbolo to set.
	 */
	public void setMetasimbolo(String metasimbolo) {
		this.metasimbolo = metasimbolo;
	}
	
	/**
	 * @param metapath The metapath to set.
	 */
	public String getMetapath() {
		return metapath;
	}
	
	/**
	 * @param canonicmeta The canonicmeta to set.
	 */
	public void setCanonicmeta(String canonicmeta) {
		this.canonicmeta = canonicmeta;
	}
	
	public int gettotalmetasimbolos(){
		return this.getMetasimbolo().split(" ").length;
		
	}

	public String getPrimeiro() {
		return primeiro;
	}

	public void setPrimeiro(String primeiro) {
		this.primeiro = primeiro;
	}
	
	
}
