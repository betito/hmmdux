package hmmdux.method;
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
public class hmmTok {

	
	public static String[] getPares(String linha, String delim){
		
		String dados[] = linha.split(delim);
		
		return dados;
	}
	
	public static hmmExample getSimbolosEstado(String linha){
		hmmExample hmmex = null;
		hmmex = new hmmExample(linha.toUpperCase());
		
		return hmmex;
	}
	
	public static hmmExampledual getSimbolosEstadoDual(String linha){
		hmmExampledual hmmex = null;
		hmmex = new hmmExampledual(linha.toUpperCase());
		
		return hmmex;
	}
	

}
