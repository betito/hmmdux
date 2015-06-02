package hmmdux.auxclass;
import hmmdux.method.Indice;
import hmmdux.method.Levenshtein;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created on Jan 10, 2006
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
public class auxFunctions {

	
	public static String tirarAcentos(String t){
		char mascC[] = {'Á','É','Í','Ó','Ú','À','È','Ì','Ò','Ú','Â','Ê','Î','Ô','Û','Ã','Õ','Ç','²','³'}; 
		char mascS[] = {'A','E','I','O','U','A','E','I','O','U','A','E','I','O','U','A','O','C','2','3'};
		StringBuffer s = new StringBuffer();
		String k[] = t.toUpperCase().split("");
		
		for (int i = 0; i < k.length; i++) {
			for(int j = 0; j < mascC.length; j++){
				if(k[i].equals(String.valueOf(mascC[j]))){
					k[i] = String.valueOf(mascS[j]);
				}				
			}

		}
		
		for (int i = 0; i < k.length; i++) {
			s.append(k[i]);
		}		
		
		return s.toString();
	}
	
	public static void printMatrixCompleto(Hashtable e, Hashtable s, int m[][]){

		for(Enumeration enj = s.keys(); enj.hasMoreElements();){
			String c = (String) enj.nextElement();
			System.out.print("\t" + c);
		}
		System.out.println("");
		for(Enumeration eni = e.keys(); eni.hasMoreElements();){
			String et = (String) eni.nextElement();
			int ei = Indice.getIndice(e, et);
			System.out.print(et + "\t");
			for(Enumeration enj = s.keys(); enj.hasMoreElements();){
				String c = (String) enj.nextElement();
				int ej = Indice.getIndice(s, c);
				//System.out.print("\t"+matrixA[ei][ej]);
				System.out.print(m[ei][ej] + "\t");
			}
			System.out.println();
		}

	}
	
	public static void printMatrixCompleto(Hashtable e, Hashtable s, double m[][]){

		for(Enumeration enj = s.keys(); enj.hasMoreElements();){
			String c = (String) enj.nextElement();
			System.out.print("\t" + c);
		}
		System.out.println("\t");
		for(Enumeration eni = e.keys(); eni.hasMoreElements();){
			String et = (String) eni.nextElement();
			int ei = Indice.getIndice(e, et);
			System.out.print(et + " ");
			for(Enumeration enj = s.keys(); enj.hasMoreElements();){
				String c = (String) enj.nextElement();
				int ej = Indice.getIndice(s, c);
				//System.out.print("\t"+matrixA[ei][ej]);
				System.out.print("\t"+m[ei][ej]);
			}
			System.out.println();
		}

	}
	
	public static String[] getTermos(String f){
		String t = f;
		String aux[] = t.split("\\s+");
		return aux;
	}
	

	public static Vector getComb(String f[], Hashtable md){
		Vector v = new Vector(5);
		String x = "";
		boolean ok = true;
		int p = auxFunctions.getMetaPos(f, md);
		
		if(p==0){
			x = "";
			
			for(int i = 0; i < f.length; i++){
				x += f[i] + " ";
				v.add(x);
			}
			//System.out.println(" if ");
		}else{
			x = f[p];
			for(int i = p; i > 0; i--){
				x = f[p - i] + " " + x;
				//if(md.containsKey(f[i]) && ok){
					v.add(x);
					//ok = false;
				//}
			}
			
			x = f[p] + " ";
			//System.out.println(" else " + x);
			for(int i = p+1; i < f.length; i++){
				x += f[i] + " ";
				v.add(x);
			}
		}
		
		return v;
	}
	
	public static Vector getCombPos(String f[], String o[],Hashtable md, int ini){
		Vector v = new Vector(5);
		String x = "";
		boolean ok = true;
		int p = auxFunctions.getMetaPos(f, md);
		//System.out.println("INI:" + ini + " P: "+p + "Len: " + f.length);
		if(p==0){
			x = "";
			
			for(int i = 0; i < f.length; i++){
				x += o[ini+i] + " ";
				v.add(x);
				//System.out.println("if "+x);
			}
			//System.out.println(" if ");
		}else{
			x = o[p+ini];
			for(int i = (p+ini); i > ini; i--){
				x = o[i-p] + " " + x;
				v.add(x);
				//System.out.println("else 1. "+x);
			}
			
			x = o[p+ini] + " ";
			//System.out.println(" else " + x);
			for(int i = ini+p+1; i < f.length+ini; i++){
				x += o[i] + " ";
				v.add(x);
				//System.out.println("else 2. "+x);
			}
		}
		
		return v;
	}
	
	public static Vector getPosInc(String f[], Hashtable md, int ini){
		Vector v = new Vector(5);
		String x = "";
		boolean ok = true;
		int p = auxFunctions.getMetaPos(f, md);
		
		if(p==0){
			x = "";
			
			for(int i = 0; i < f.length; i++){
				v.add(new Integer(i+ini));
			}
			//System.out.println(" if ");
		}else{
			//x = o[p+ini];
			for(int i = (p+ini)-1; i > 0; i--){
				//if(md.containsKey(f[i]) && ok){
				v.add(new Integer(ini + p));
					//ok = false;
				//}
			}
			
			//x = f[p+ini] + " ";
			//System.out.println(" else " + x);
			for(int i = p+ini+1; i < (p+ini+f.length); i++){
				v.add(new Integer(i));
			}
		}
		
		return v;
	}
	
	
	
	public static String getFrag(String f[]){
		String aux = "";
		for(int i = 0;i < f.length; i++){
			aux += f[i] + " ";
		}
		return aux;
	}
	
	public static int getMetaPos(String obs[], Hashtable meta){
		int c = 0;
		
		for(int i = 0;i < obs.length; i++){
			
			if(meta.containsKey(obs[i])){
				c = i;
			}			
		}
		
		return c;
		
	}
	
	
	public static String[] getTermosUntilNextMeta(int j, String obs[], Hashtable meta){
		Vector v = new Vector(10);
		boolean s = true;
		int c = 0;
		
		for(int i = j; s && (i < obs.length); i++){
			
			if(meta.containsKey(obs[i])){
				c++;
			}
			if(c==2){
				s = false;
			}else{
				v.add(obs[i]);
			}
			
		}
		
		String saida[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			saida[i] = (String) v.elementAt(i);
		}
		
		return saida;
		
	}

	public static String[] getTermosUntilNextMeta(int j, String obs[]){
		Vector v = new Vector(10);
		
		for(int i = j;i < obs.length; i++){
			v.add(obs[i]);
		}
		
		String saida[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			saida[i] = (String) v.elementAt(i);
		}
		
		return saida;
		
	}

	public static String[] getTermosInRange(int j, int k, String obs[]){
		Vector v = new Vector(10);
		
		for(int i = j;i < k && i < obs.length; i++){
			v.add(obs[i]);
		}
		
		String saida[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			saida[i] = (String) v.elementAt(i);
		}
		
		return saida;
		
	}
	
	public static boolean hasOneMeta (String f, Hashtable simb){
		String fv[] = f.split(" ");
		int c = 0;
		
		for(int a = 0; a < fv.length; a++){
			if(simb.containsKey(fv)){
				c++;
			}
		}
		
		if(c == 1){
			return true;
		}
		
		return false;
		
	}
	
	public static String fixNumberValue(String ent){
		String aux[] = ent.split(" ");
		String p = ".*\\d+.*";
		String saida = "";
		
		for(int g = 0; g < aux.length; g++){
			if(aux[g].matches(p)){
				saida += Const.NumValue;
			}else{
				saida += aux[g];
			}
			
			if(g < aux.length - 1){
				saida += " ";
			}
		}
		
		//System.out.println(" " + aux+"");
		return saida;
	}
	
	public static String fixNumberWordValue(String ent){
		String aux[] = ent.split(" ");
		String p = ".*\\d+.*";
		String w = ".*\\w+.*";
		String saida = "";
		
		for(int g = 0; g < aux.length; g++){
			if(aux[g].matches(p)){
				saida += Const.NumValue;
			}else{
				//saida += Const.WordValue;
				saida += aux[g].toUpperCase();
			}
			
			if(g < aux.length - 1){
				saida += " ";
			}
		}
		
		//System.out.println(" " + aux+"");
		return saida;
	}

	public static String fixNumberWordValueMetaPath(String ent){
		String aux[] = ent.split(" ");
		String p = ".*\\d+.*";
		String w = ".*\\w+.*";
		String saida = "";
		
		for(int g = 0; g < aux.length; g++){
			if(aux[g].matches(p)){
				saida += Const.NumValue;
			}else{
				saida += Const.WordValue;
			}
			
			if(g < aux.length - 1){
				saida += " ";
			}
		}
		
		//System.out.println(" " + aux+"");
		return saida;
	}
	

	
	public static String[] cleanData(String texto, Hashtable simb){
		String obs[] = auxFunctions.retirarStopWords(texto);
		
		Vector v = new Vector(obs.length);
		
		for(int i = 0; i < obs.length; i++){
			String x = auxFunctions.canonicValue(obs[i]);
			
			if(simb.containsKey(x) || auxFunctions.isNumberValue(x)){
				v.add(x);
			}
		}
		
		String s[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			s[i] = (String) v.elementAt(i);
		}
		
		return s;
	}

	public static String[] canonicText(String texto){
		String obs[] = texto.split("\\s+");
		
		Vector v = new Vector(obs.length);
		
		for(int i = 0; i < obs.length; i++){
			String x = auxFunctions.canonicValue(obs[i]);
			
			if(auxFunctions.isNumberValue(x)){
				v.add(Const.NumValue);
			}else{
				v.add(x.toUpperCase());
			}
		}
		
		String s[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			s[i] = (String) v.elementAt(i);
		}
		
		return s;
	}
	
	public static String[] canonicText(String texto[]){
		
		Vector v = new Vector(texto.length);
		
		for(int i = 0; i < texto.length; i++){
			String x = auxFunctions.canonicValue(texto[i]);
			
			if(auxFunctions.isNumberValue(x)){
				v.add(Const.NumValue);
			}else{
				v.add(x.toUpperCase());
			}
		}
		
		String s[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			s[i] = (String) v.elementAt(i);
		}
		
		return s;
	}
	
	public static String[] copyData(String texto, Hashtable simb){
		String obs[] = auxFunctions.retirarStopWords(texto);
		
		Vector v = new Vector(obs.length);
		
		for(int i = 0; i < obs.length; i++){
			String x = auxFunctions.canonicValueNoCase(obs[i]);
			
			if(simb.containsKey(x) || auxFunctions.isNumberValue(x)){
				v.add(obs[i]);
			}
		}
		
		String s[] = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			s[i] = (String) v.elementAt(i);
		}
		
		return s;
	}
	
	public static boolean semiEstadoEhValido(String st){
		if(!(st.equals(""))){
			int r = Integer.parseInt(st);
			
			switch (r){
				case 1: 	return true;
				case 2: return true;
			}
		}
		
		return false;
	}
	
	
	public static boolean checarPadraoESimbolo(String o, String est, Hashtable pat){
		boolean aceito = false;

		//System.out.println(" O :: " + o + " Est :: " + est);
		
		if(auxFunctions.estadoEhValido(est)){
			if(Pattern.matches((String) pat.get(est), o)){
				aceito = true;
			}
		}

		return aceito;
	}
	
	
	public static boolean isNumberValue(String n){
		boolean estado = false;
		String numvaluepat = "\\d+"; // ocorrencia de um valor numerico em qualquer parte de n
		Pattern p = Pattern.compile(numvaluepat);
		Matcher Casar = p.matcher(n);
		
		if(Casar.find() || n.equals(Const.NumValue)){
			estado = true;
		}
		
		return estado;
	}

	public static String canonicValue(String n){
		String aux = n; 
		String delim = "[\\s-.,;:><!@#%&*'/''('')''{''}'\\[\\]]"; // ocorrencia de um valor numerico em qualquer parte de n
		Pattern p = Pattern.compile(delim);
		Matcher Casar = p.matcher(n);
		
		aux = Casar.replaceAll("");
		
		return aux.toUpperCase();
	}
	
	public static String canonicValueNoCase(String n){
		String aux = n; 
		String delim = "[ -.,;:><!@#%&*'/''('')''{''}'\\[\\]]"; // ocorrencia de um valor numerico em qualquer parte de n
		Pattern p = Pattern.compile(delim);
		Matcher Casar = p.matcher(n);
		
		aux = Casar.replaceAll("");
		
		return aux;
	}
	
	public static String getPatternSimbol(String n){
		String aux = "";
		String num = ".*\\d+.*";
		String letras = "[a-zA-Z]+";
		String palavra = "\\w+";
		String tudo = ".*";
			
		if(Pattern.matches(num, n)){
			aux = num;
		}else
			if(Pattern.matches(letras, n)){
				aux = letras;
			}else
				if(Pattern.matches(palavra, n)){
					aux = palavra;
				}else
					if(Pattern.matches(tudo, n)){
						aux = tudo;
					}
		
		
		return aux;
	}
	
	public static String getPattern(String n){
		String aux = "";
		String num = ".*\\d+.*";
		String letras = "[a-zA-Z]+";
		String palavra = "\\w+";
		String tudo = ".*";
		
		StringTokenizer st = new StringTokenizer(n);
		
		
		while(st.hasMoreTokens()){
			String tmp = st.nextToken();
			
			if(Pattern.matches(num, tmp)){
				aux += num;
			}else if(Pattern.matches(letras, tmp)){
					aux += letras;
					}else if(Pattern.matches(palavra, tmp)){
							aux += palavra;
							}else if(Pattern.matches(tudo, tmp)){
								aux += tudo;
							}
					
		}
		
		return aux;
	}

	public static double mediaDestePadrao(double matB[][], Hashtable simb, int state, String output){
		double ret = 0;
		int total = 0;
		String p = auxFunctions.getPattern(output);
		
		for(Enumeration en = simb.keys(); en.hasMoreElements();){
			String s = (String) en.nextElement();
			if(Pattern.matches(p, s)){
				ret = matB[state][Indice.getIndice(simb, s)];
				total++;
			}
		}
		
		
		return (ret/total);
	}
	
	public static void inicializarMatrix(int m[], int ex, int l){
		for(int eixo = 0; eixo < ex; eixo++){
				m[eixo] = l;
		}
	}
	
	public static void inicializarMatrix(double m[], int ex, double l){
		for(int eixo = 0; eixo < ex; eixo++){
				m[eixo] = l;
		}
	}
	
	public static void inicializarMatrix(int m[][][], int ex, int l, int c, int v){
		for(int eixo = 0; eixo < ex; eixo++){
			for(int lin = 0; lin < l; lin++){
				for(int col = 0; col < c; col++){
					m[eixo][lin][col] = v;
				}
			}						
		}
	}
	
	public static void inicializarMatrix(double m[][][], int ex, int l, int c, double v){
		for(int eixo = 0; eixo < ex; eixo++){
			for(int lin = 0; lin < l; lin++){
				for(int col = 0; col < c; col++){
					m[eixo][lin][col] = v;
				}
			}						
		}
	}
	
	public static void inicializarMatrix(double m[][][], int ex, int l, int c, int v){
		for(int eixo = 0; eixo < ex; eixo++){
			for(int lin = 0; lin < l; lin++){
				for(int col = 0; col < c; col++){
					m[eixo][lin][col] = v;
				}
			}						
		}
	}
	
	public static void inicializarMatrix(int m[][], int l, int c){
		for(int lin = 0; lin < l; lin++){
			for(int col = 0; col < c; col++){
				m[lin][col] = 0;
			}
		}						
	}
	
	public static void inicializarMatrix(double m[][][], int z, int l, int c){
		for(int ze = 0; ze < z; ze++){
			for(int lin = 0; lin < l; lin++){
				for(int col = 0; col < c; col++){
					m[ze][lin][col] = 0;
				}
			}
		}
	}
	
	public static void inicializarMatrix(double m[][], int l, int c){
		for(int lin = 0; lin < l; lin++){
			for(int col = 0; col < c; col++){
				m[lin][col] = 0.0;
			}						
		}
	}
	
	public static void inicializarMatrix(double m[][], int l, int c, double v){
		for(int lin = 0; lin < l; lin++){
			for(int col = 0; col < c; col++){
				m[lin][col] = v;
			}						
		}
	}
	
	public static void printMatrix(int m[][]){
		for(int lin = 0; lin < m.length; lin++){
			for(int col = 0; col < m[lin].length; col++){
				System.out.print("["+m[lin][col]+"]");
			}				
			System.out.println("");		
		}
	}
	
	public static void printMatrix(double m[][]){
		for(int lin = 0; lin < m.length; lin++){
			for(int col = 0; col < m[lin].length; col++){
				System.out.print("["+m[lin][col]+"]");
			}				
			System.out.println("");		
		}
	}
	
	public static void printHashKeys(Hashtable est){
		
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String tmpest = (String) en.nextElement();
			System.out.print("["+tmpest+"]");
		}
		
		System.out.println("");
	}
	
	public static void printHashtable(Hashtable est){
		
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String tmpest = (String) en.nextElement();
			System.out.println("{"+tmpest+"}["+est.get(tmpest)+"]");
		}
	}

	
	public static void printVetor(double v[]){
		for(int lin = 0; lin < v.length; lin++){
				System.out.print("["+v[lin]+"]");
		}
	}

	public static void printVetor(String v[], int i, int j){
		for(int lin = i; lin < j; lin++){
				System.out.print("["+v[lin]+"]");
		}
	}
	
	public static void printVetor(int v[]){
		for(int lin = 0; lin < v.length; lin++){
				System.out.print("["+v[lin]+"]");
		}
	}

	public static void printVetor(String v[]){
		for(int lin = 0; lin < v.length; lin++){
				System.out.print("["+v[lin]+"]");
		}
		System.out.println("");
	}

	
	
	public static double[] getLinha(double m[][], int linha){
		return m[linha];
	}
	

	public static boolean estadoEhValido(String est){
		boolean sn = false;
		
		//if(!(est.equalsIgnoreCase("start")) && !(est.equalsIgnoreCase("FIM"))){
		if(!(est.equalsIgnoreCase("start"))){
			sn = true;
		}
		
		return sn;
	}
	
	// checar a prioridade do padrao e atribuir ao estado
	public static void inferirPrioridadeDePadrao(Hashtable pat, String est, String simb){
		String p = auxFunctions.getPatternSimbol(simb);
		//System.out.println("Inicio: {"+simb+"} " + "est: "+ est + "["+p+"]");
		
		if(pat.containsKey(est)){
			String atual = (String) pat.get(est);
			
			if((p.equals("[a-zA-Z]+")) && (atual.equals("\\d+"))){
				pat.put(est, "\\w+");
				//System.out.println(" d1: "+ simb + "{"+est+"}");
				
			}else{
				if((p.equals("\\d+")) && (atual.equals("[a-zA-Z]+"))){
					pat.put(est, "\\w+");
					//System.out.println(" d2: "+ simb + "{"+est+"}");
					
				}
			}
			//System.out.println(" nada ...");
		}else{
			pat.put(est, p);
			//System.out.println(" d3: "+ simb + " est : " + est + " : " + p);
		}
	}
	
	
	public static Hashtable detectarPadrao(Hashtable est, Hashtable simb, double matsimb[][]){
		Hashtable pat = new Hashtable(est.size());
		
		for(Enumeration enest = est.keys(); enest.hasMoreElements();){
			String strest = (String) enest.nextElement();
			
			for(Enumeration ensimb = simb.keys(); ensimb.hasMoreElements();){
				String strsimb = (String) ensimb.nextElement();
				
				if(matsimb[Indice.getIndice(est, strest)][Indice.getIndice(simb, strsimb)] != 0){
					auxFunctions.inferirPrioridadeDePadrao(pat, strest, strsimb);
				}
			}
		}
			
		return pat;
	}
	
	
	public static String getRaiz(String s){
		Hashtable vogais = new Hashtable(5);
		vogais.put("a", " ");
		vogais.put("e", " ");
		vogais.put("i", " ");
		vogais.put("o", " ");
		vogais.put("u", " ");
		String aux = s.toLowerCase();
		StringBuffer o = new StringBuffer();
		
		for(int c = 0; c < aux.length(); c++){
			String t = (String) aux.substring(c,c+1);
			if(!(vogais.containsKey(t))){
				o.append(t);
			}
		}
		
		return o.toString();
		
	}
	
	public static boolean DELev(String alvo, String qq, double prec){
		
		double res = 0.0;
		int distedicao = Levenshtein.distance(alvo, qq);
		
		int maiorstr = 0;
		if(alvo.length() < qq.length()){
			maiorstr = qq.length();
		}else{
			maiorstr = alvo.length();
		}
		
		res = (100*distedicao)/maiorstr;
		res /= 100;
		res = 1 - res;

//		System.out.print("distedicao = " + distedicao + " res :: " + res);
		
		if(res < prec){
			return false;
		}

		return true;
	}
	
	public static String checarTokenUsandoDE(String o, Hashtable simb, double prec){
		boolean sair = false;
		String S = null;
		for(Enumeration en = simb.keys(); en.hasMoreElements() && !sair;){
			String e = (String) en.nextElement();
			
			if(auxFunctions.DELev(e, o, prec)){
				S = e;
				sair = true;
			}
		}		
		
		return S; 
	}

	
	public static String[] retirarStopWords(String obs){
		String w[] = {"com", "em", "no", "na", "nos", "nas", "e", "da", "das", "do", "dos", "de", "o", "a", "os", "as", "c /", "p", "para"};
		Vector auxo = new Vector(1);
		String o[] = null;
		String input[] = obs.toUpperCase().split(" ");
		
		Hashtable sw = new Hashtable(30);
		
		for(int c = 0; c < w.length; c++){
			sw.put(w[c].toUpperCase(), "");
		}
		
		for(int c = 0; c < input.length; c++){
		
			if(!sw.containsKey(input[c])){
				auxo.add(input[c]);
			}
		}
		
		o = new String[auxo.size()];
		for(int c = 0; c < auxo.size(); c++){
			o[c] = (String) auxo.elementAt(c);
		}

		return o;
	}
	
	public static String parseInput(String ent){
		//String S = auxFunctions.fixNumberValue(ent);
		String S = ent;
		String dlm[] = {"/", ":\\(", "\\$"};
		String dlmok[] = {" / ", ": \\(", "_ "};
		
		for(int f = 0 ; f < dlm.length; f++){

			Pattern p = Pattern.compile(dlm[f]);
			Matcher Casar = p.matcher(S);
			S = Casar.replaceAll(dlmok[f]);
			
		}
		
		Pattern p = Pattern.compile("\\s+");
		Matcher m = p.matcher(S);
		S = m.replaceAll(" ");
		
		return S.toUpperCase();
		
	}

	public static String parseInputOriginal(String ent){
		//String S = auxFunctions.fixNumberValue(ent);
		String S = ent;
		String dlm[] = {"/", ":\\(", "\\$"};
		String dlmok[] = {" / ", ": \\(", "_ "};
		
		for(int f = 0 ; f < dlm.length; f++){

			Pattern p = Pattern.compile(dlm[f]);
			Matcher Casar = p.matcher(S);
			S = Casar.replaceAll(dlmok[f]);
			
		}
		
		Pattern p = Pattern.compile("\\s+");
		Matcher m = p.matcher(S);
		S = m.replaceAll(" ");
		
		return S;
		
	}
	
	
	public static String parseInput(String ent[]){
		//String S = auxFunctions.fixNumberValue(ent);
		String S[] = ent;
		String St = "";
		String dlm[] = {"/", ":\\(", "\\$"};
		String dlmok[] = {" / ", ": \\(", "_ "};
		
		for(int g = 0; g < S.length; g++){
			for(int f = 0 ; f < dlm.length; f++){
				
				Pattern p = Pattern.compile(dlm[f]);
				Matcher Casar = p.matcher(S[g]);
				S[g] = Casar.replaceAll(dlmok[f]);
				
			}
			
			Pattern p = Pattern.compile("\\s+");
			Matcher m = p.matcher(S[g]);
			S[g] = m.replaceAll(" ");
			St = S[g].toUpperCase();
		}
		
		return St;
		
	}
	
	
}
