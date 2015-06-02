package hmmdux.main;
import hmmdux.auxclass.auxFunctions;
import hmmdux.auxclass.auxVit;
import hmmdux.method.Indice;
import hmmdux.method.Viterbi3;
import hmmdux.method.hmmExampledual;
import hmmdux.method.hmmTok;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/*
 * Created on Dec 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author ros
 *
 */
public class Hmm2LevellMain {
	
		
	public static void main(String[] args) {
		
		StringBuffer base = new StringBuffer();
		
		BufferedReader bf = null;
		try {
			// Open an input stream
			
			
			
			bf = new BufferedReader(new FileReader(args[0]));

			while(bf.ready()){
				base.append(bf.readLine().toUpperCase());
				//base.append("#fim::fim");
				base.append("\n");
			}
		}catch (IOException e) {
				System.err.println("Erro na abertura do arquivo de TREINO -> +"+args[0]+"...");
				System.exit(-1);
		}
		
		bf = null;
		
		String linha[] = base.toString().split("\n");
		base = null;
		
		
		Hashtable estadosouter = new Hashtable(100);
		Hashtable cestadosouter = new Hashtable(100);
		Hashtable tamcaminhoexemplo = new Hashtable(100);
		Hashtable estadosoutersimbolos = new Hashtable(500);
		Hashtable simbolos = new Hashtable(500);
		Hashtable estadosPatterns = new Hashtable(100);
		Hashtable estadosinner = new Hashtable(100); // mudei aqui de 10 para 50
		
		estadosinner.put("START", new Indice(0));
		estadosinner.put("M", new Indice(1));
		estadosinner.put("_N_", new Indice(2));
		estadosinner.put("_W_", new Indice(3));
		estadosinner.put("END", new Indice(4));
		
		
		estadosouter.put("START", new Indice(0));
		
		//Este parser inicial ? para saber a quantidade de estados
		int idestados = 1, idsimb = 0;
		Vector dadoshmmex = new Vector(100);
		int contest = 1, contsimbolos = 1;
		
		
		simbolos.put("UNK", new Indice(0));	
		for(int c = 0; c < linha.length; c++){
			
			String conjPares[] = hmmTok.getPares(linha[c], ";");
			Vector dados = new Vector(conjPares.length);
			//System.out.println("\n**************************");
			
			
			for(int r = 0; r < conjPares.length; r++){
				hmmExampledual hmmex = hmmTok.getSimbolosEstadoDual(conjPares[r]);
				dadoshmmex.add(hmmex);
				
				String est = hmmex.getEstadao();
				
				
					if(cestadosouter.containsKey(est)){
						int ind = Indice.getIndice((Indice) estadosouter.get(est));
						ind++;
						cestadosouter.put(est, new Indice(ind));
						
						if(!(simbolos.containsKey(hmmex.getMeta())) && !(hmmex.getMeta().equals(""))){
							simbolos.put(hmmex.getMeta(), new Indice(contsimbolos)); // esse Indice ja eh para dentro da matrix inner do estdao est
							contsimbolos++;
						}
						
						String k[] = hmmex.getDado().split("\\s+");
						for(int x = 0; x < k.length; x++){
							//System.out.print("Sim : " + k[x]);
							if(!(simbolos.containsKey(k[x])) && !(k[x].equals(""))){
								simbolos.put(k[x], new Indice(contsimbolos));
								contsimbolos++;
								//System.out.print(" +");
							}
							
							//System.out.println();
						}
						
						
					}else{
						cestadosouter.put(est, new Indice(1));
						estadosouter.put(est, new Indice(contest));
						contest++;
						//simbolos de Est
						
						if(!(hmmex.getMeta().equals(""))){
							simbolos.put(hmmex.getMeta(), new Indice(contsimbolos)); // esse Indice ja eh para dentro da matrix inner do estdao est
							contsimbolos++;
						}
						
						String k[] = hmmex.getDado().split("\\s+");
						for(int x = 0; x < k.length; x++){
							//System.out.print("Sim : " + k[x]);
							if(!(simbolos.containsKey(k[x])) && !(k[x].equals(""))){
								simbolos.put(k[x], new Indice(contsimbolos));
								contsimbolos++;
								//System.out.print(" +");
							}
							//System.out.println();
						}
						
						//System.out.println(""+hmmex.getMeta() + "("+temp.size()+")");
						
					}
				
				//System.out.println(""+hmmex.getTudo() +"\nTotal: " +hmmex.gettotalmetasimbolos()+"\n------------------------");
			}
			dadoshmmex.add(null);
		}
		
		simbolos.put("_N_", new Indice(contsimbolos++));
		simbolos.put("_W_", new Indice(contsimbolos++));

		
		estadosouter.put("END", new Indice(contest));
		
		
		/*
		for(Enumeration x = simbolos.keys(); x.hasMoreElements();){
			String estx = (String) x.nextElement();
			int xp = Indice.getIndice(simbolos, estx);
			
			System.out.println(estx + " : " + Indice.getIndice(simbolos, estx));
		}
	*/
		
		// a matrix outer de estados ? do tamanaho dos estados outer ja conhecidos
		// agora vai ter que contar os simbolos de hmminner
		
		int cestouter = estadosouter.size();
		int matrixA[][] = new int[cestouter][cestouter];
		int matrixB[][][] = new int[cestouter][estadosinner.size()][simbolos.size() + 1];
		int matrixAinner[][][] = new int[cestouter][estadosinner.size()][estadosinner.size()];
		double dmatrixA[][] = new double[cestouter][cestouter];
		double dmatrixB[][][] = new double[cestouter][estadosinner.size()][simbolos.size() + 1];
		double dmatrixAinner[][][] = new double[cestouter][estadosinner.size()][estadosinner.size()];
		
		double consmata = 0.1;
		double consmatb = 0.001;
		auxFunctions.inicializarMatrix(matrixA, cestouter, cestouter);
		auxFunctions.inicializarMatrix(matrixB, cestouter, estadosinner.size(), simbolos.size() + 1, 0);
		auxFunctions.inicializarMatrix(matrixAinner, cestouter, estadosinner.size(), estadosinner.size(), 0);
		auxFunctions.inicializarMatrix(dmatrixA, cestouter, cestouter, 0.0);
		auxFunctions.inicializarMatrix(dmatrixB, cestouter, estadosinner.size(), simbolos.size() + 1, 0.0);
		auxFunctions.inicializarMatrix(dmatrixAinner, cestouter, estadosinner.size(), estadosinner.size(), 0.0);
		
		int lin = Indice.getIndice(estadosouter, "START");
		for (int c = 0; c < dadoshmmex.size(); c++){
			
			
			if(dadoshmmex.elementAt(c) != null){
				hmmExampledual hd = (hmmExampledual) dadoshmmex.elementAt(c);
				
				int col = Indice.getIndice(estadosouter, hd.getEstadao());
				matrixA[lin][col]++;
				lin = col;
				
			}else{

				//System.out.println("null");
				matrixA[lin][Indice.getIndice(estadosouter, "END")]++;;
				lin = Indice.getIndice(estadosouter, "START");
			}

		}

		// contar a as transicoes dos estados internos
		for (int c = 0; c < dadoshmmex.size(); c++){
			
			if(dadoshmmex.elementAt(c) != null){
				hmmExampledual hd = (hmmExampledual) dadoshmmex.elementAt(c);
				
				String pt[] = hd.getMetapath().split(" ");
				//System.out.println(hd.getDadoorig());
				//System.out.println(hd.getEstadao());
				//auxFunctions.printVetor(pt);
				
				int posouter = Indice.getIndice(estadosouter, hd.getEstadao());
				lin = 0;
				//auxFunctions.printMatrix(matrixAinner[Indice.getIndice(estadosouter, hd.getEstadao())]);
				for(int b = 0; b < pt.length; b++){
					
					int col = Indice.getIndice(estadosinner, pt[b]);
					
					//System.out.println("[" +posouter+","+lin+","+col+"]");
					matrixAinner[posouter][lin][col]++;
					lin = col;
					
				}
				matrixAinner[posouter][lin][Indice.getIndice(estadosinner, "END")]++;
				matrixAinner[posouter][Indice.getIndice(estadosinner, "END")][Indice.getIndice(estadosinner, "END")]++;
				//auxFunctions.printMatrixCompleto(estadosinner, estadosinner, matrixAinner[Indice.getIndice(estadosouter, hd.getEstadao())]);
			}
		}

		
		// contar apenas os simbolos M _N_ ou _W_
		int antestadao = 0;
		int pos = 0;
		boolean primeiro = true;
		lin = Indice.getIndice(estadosouter, "START");
		for (int c = 0; c < dadoshmmex.size(); c++){
			
			if(dadoshmmex.elementAt(c) != null){
				hmmExampledual hd = (hmmExampledual) dadoshmmex.elementAt(c);

				
				int z = Indice.getIndice(estadosouter, hd.getEstadao());
				
				// parte nova
				Vector termos = hd.postermo;
				Vector innerest = hd.posestado;

				if(!primeiro){
					//System.out.println("deu ");
					pos = Indice.getIndice(simbolos, (String) termos.elementAt(0));
					matrixB[antestadao][Indice.getIndice(estadosinner, "END")][pos]++;
				}
				
				
				for(int i = 0; i < termos.size(); i++){
					//System.out.println(termos.elementAt(i) + " - " + innerest.elementAt(i));
					String tmpest = (String)innerest.elementAt(i);
					String tmpsimb = (String)termos.elementAt(i);
					matrixB[z][Indice.getIndice(estadosinner, tmpest)][Indice.getIndice(simbolos, tmpsimb)]++;
				}
				
				antestadao = z;
				primeiro = false;
				// ate aqui a parte nova
				

			}else{
				primeiro = true;
			}
			
		}

		// calcular o consmata e consmatb
		// consmatb
		double constmatam[] = new double[estadosouter.size()];
		double constmatbm[][] = new double[estadosouter.size()][estadosinner.size()];
		auxFunctions.inicializarMatrix(constmatam, estadosouter.size(), 0);
		auxFunctions.inicializarMatrix(constmatbm, estadosouter.size(), estadosinner.size());
		
		double valmaxA = 0;
		double valmaxB = 0;
		double constA = 100;
		double constB = 1000;
		for(int z = 0; z < estadosouter.size();z++){
			for(int x = 0; x < estadosinner.size(); x++){
				for (int y = 0; y < simbolos.size(); y++) {
					if(matrixB[z][x][y] > 0){
						constmatbm[z][x]++;
						
						if(constmatbm[z][x] > valmaxB){
							valmaxB = constmatbm[z][x]; 
						}
					}     
				}
			}
		}
		
		for(int z = 0; z < estadosouter.size();z++){
			for(int x = 0; x < estadosinner.size(); x++){
				constmatbm[z][x] = (1/(constB*(valmaxB - constmatbm[z][x] + 1)));
				//if((x != Indice.getIndice(estadosinner,"M"))&& (x != Indice.getIndice(estadosinner,"_N_")) ){
				if((x != Indice.getIndice(estadosinner,"_N_")) ){
					dmatrixB[z][x][0]=constmatbm[z][x];
				}
				
				for (int y = 0; y < simbolos.size(); y++) {
					//if((x == Indice.getIndice(estadosinner,"M") || (x == Indice.getIndice(estadosinner,"_N_")))){
					if(x == Indice.getIndice(estadosinner,"END")){
						dmatrixB[z][x][y]=constmatbm[z][x];
					}

				}
				
				for (int y = 0; y < estadosinner.size(); y++) {
					if((x == Indice.getIndice(estadosinner,"START") || y == Indice.getIndice(estadosinner,"END")) && (x != y) && (x!= Indice.getIndice(estadosinner,"END")) && (y!= Indice.getIndice(estadosinner,"START"))){
						dmatrixAinner[z][x][y]=0.000000001;
					}
				}
			}
		}
		
		auxFunctions.printMatrixCompleto(estadosouter, estadosinner, constmatbm);
		
		for(int z = 0; z < estadosouter.size();z++){
			for(int x = 0; x < estadosouter.size(); x++){
				if(matrixA[z][x] > 0){
					constmatam[x]++;
					
					if(constmatam[x] > valmaxA){
						valmaxA = constmatam[x]; 
					}
				}  
			}
		}
		
		for(int z = 0; z < estadosouter.size();z++){
			constmatam[z] = (1/(constA*(valmaxA - constmatam[z]+ 1)));
		}
		
		for(int z = 0; z < estadosouter.size();z++){
			for (int x = 0; x < estadosouter.size(); x++) {
				dmatrixA[z][x] = constmatam[x];
			}
		}

		// fim do calculos das constantes
		
		// calcular as probabilidades da matrizA e colocar em dmatrizA
		for(int x = 0; x < estadosouter.size(); x++){
			int acum = 0;
			for(int y = 0; y < estadosouter.size(); y++){
				acum += matrixA[x][y];
			}
			
			for(int y = 0; y < estadosouter.size(); y++){
				if(matrixA[x][y] != 0  && acum!=0){
					//dmatrixA[x][y] = ((double) matrixA[x][y])/ (double)acum;
					//dmatrixA[x][y] = Math.abs((((double) matrixA[x][y])/(double)acum) - consmata);
					dmatrixA[x][y] = Math.abs((((double) matrixA[x][y])/(double)acum) - constmatam[y]); // modelo filipe

				}
			}	
		}
		
		for(Enumeration en = estadosouter.keys(); en.hasMoreElements();){
			String j = (String) en.nextElement();
			System.out.println(j + " = " + constmatam[Indice.getIndice(estadosouter, j)]);
		}

		
		//auxFunctions.inicializarMatrix(dmatrixA, cestouter, cestouter, 1);
		//calcular as probabilidades da matrizB e colocar em dmatrizB
		/*// borkar
		for(int z = 0; z < estadosouter.size(); z++){
			for(int x = 0; x < estadosinner.size(); x++){
				int acum = 0;
				for(int y = 0; y < simbolos.size(); y++){
					acum += matrixB[z][x][y];
				}
				
				for(int y = 0; y < simbolos.size(); y++){
					if(matrixB[z][x][y] != 0 && acum!=0){
						dmatrixB[z][x][y] = Math.abs((((double) matrixB[z][x][y])/(double)acum) - consmatb);
						//dmatrixB[z][x][y] = Math.abs((((double) matrixB[z][x][y])/(double)acum));
					}					
				}
				
				dmatrixB[z][x][0] = 0.001;
			}
		}*/
		int maxfreq[] = new int[estadosinner.size()];
		
		for(int z = 0; z < estadosouter.size(); z++){
			auxFunctions.inicializarMatrix(maxfreq, maxfreq.length, 0);
			for(int y = 0; y < simbolos.size(); y++){
				int acum = 0;
				for(int x = 0; x < estadosinner.size(); x++){
					
					for(int p = 0; p < estadosouter.size(); p++){
						acum += matrixB[p][x][y];
					}
					if(matrixB[z][x][y] > maxfreq[x]){
						maxfreq[x] = matrixB[z][x][y];
					}
				}
								
				for(int x = 0; x < estadosinner.size(); x++){
					if(y == Indice.getIndice(simbolos,"_N_") && x == Indice.getIndice(estadosinner,"_N_")){
						dmatrixB[z][x][y] = 1.0;
					}
					else{
						if(matrixB[z][x][y] != 0 && acum!=0 && maxfreq[x] != 0){
							dmatrixB[z][x][y] = ((double) matrixB[z][x][y])/(double)acum;
							dmatrixB[z][x][y] *= ((double) matrixB[z][x][y])/(double)maxfreq[x];
							//dmatrixB[z][x][y] = Math.abs((double) dmatrixB[z][x][y] - consmatb); // modelo filipe 1
							dmatrixB[z][x][y] = Math.abs((double) dmatrixB[z][x][y] - constmatbm[z][x]); // modelo filipe 2
							
						}
					}
				}
			}
		}
		
		
		//calcular as probabilidades da matrizAinner e colocar em dmatrizAinner
		for(int z = 0; z < estadosouter.size(); z++){
			for(int x = 0; x < estadosinner.size(); x++){
				int acum = 0;
				for(int y = 0; y < estadosinner.size(); y++){
					acum += matrixAinner[z][x][y];
				}
				
				for(int y = 0; y < estadosinner.size(); y++){
					if(matrixAinner[z][x][y] != 0  && acum!=0){
						
						if(y == Indice.getIndice(estadosinner,"END")){
							dmatrixAinner[z][x][y] = ((double) matrixAinner[z][x][y])/(double)acum;
						}
						else{
							dmatrixAinner[z][x][y] =((double) matrixAinner[z][x][y])/(double)acum;
							//dmatrixAinner[z][x][y] = 1.0; //teste
						}
					}
				}
			}
		}
		
		//**************************   
		StringBuffer baseteste = new StringBuffer();
		
		BufferedReader bft = null;
		try {
			// Open an input stream
			bft = new BufferedReader(new FileReader(args[1]));

			while(bft.ready()){
				baseteste.append(bft.readLine().toUpperCase());
				//base.append("#fim::fim");
				baseteste.append("\n");
			}
		}catch (IOException e) {
				System.err.println("Erro na abertura do arquivo de TESTE -> +"+args[1]+"...");
				System.exit(-1);
		}
		
		bft = null;

		
		String entrada[] = baseteste.toString().split("\n");
		for(int k = 0; k < entrada.length; k++){

			System.out.println("\n***********************************************\nTeste num. = " + k);
			
			//String t = "412 m² const. 2 pav. rever. p/ 2 casas, 3 sls., 8 qtts. 4 stes., 7 banhs. 2 aquec. à gás, 2 copas, 2 coz., 2 lavand., 2 gar. 4 carros, 2 sacadas, var. esquina acesso 2 ruas, pisc. 6 mÂ² c/ aquec., sauna vapor, ducha, quintal 250 m² árv. frut. Pode transf. 2 casas indep. TEL: 3342-0241/ 3342-0403/ 9946-1099 WEBER NUNES IMÓVEIS CRECI 27756 Tel. 3342-0403";
			//String t = "412 m² const. 2 pav. rever. p/ 2 casas, 3 sls., 8 qtts. 4 stes., 7 banhs. 2 aquec. à gás, 2 copas, 2 coz., 2 lavand., 2 gar. 4 carros, 2 sacadas, var. esquina acesso 2 ruas, pisc. 6 mÂ² c/ aquec., sauna vapor, ducha, quintal 250 m² árv. frut. Pode transf. 2 casas indep. TEL: 3342-0241/ 3342-0403/ 9946-1099 WEBER NUNES IMÓVEIS CRECI 27756 Tel. 3342-0403";
			//String t = "Tel. 3342-0403";
			//String t = "Bela casa, duplex, acabto. de 1º, vista panorâmica da cidade. Térreo: gar 2 lojão c/ ± 100 m². Andar sup.: sl. 2 amb., 2 qts., 2 stes. (1 c/ hidro), banh. soc., copa, coz., á. serv., aquecimento solar. + casa fundos c/ sl., 2 qts., banh., coz., á. serv. R$ 120.000,00. Troca p/ apto no Centro do Ano Bom H C IMOVEIS CRECI 27 Tel. 3323-8851/ 9997-8493";
			//String t = "serv.,";
			//String t = "Casa em condomínio fechado, c/2 qtos., 2 banhs., coz., área de serviço c churrasq. e ducha, 1 vaga de garagem. Bairro de fácil acesso ao centro, ônibus urbano, comércio local atuante. R$ 42.000,00. http://www.ademirferreiraimoveis.com.br ADEMIR FERREIRA IMOVEIS CRECI 13775 Tel. 3323-5119 9997-3571";
			//t = auxFunctions.tirarAcentos(t);
			
			//System.out.println("Saida : " + t);
			
			//String t = "Casa em condomínio fechado, c/ 2 qtos., 2 banhs., coz., área de serviço c/ churrasq. e ducha, 1 vaga de garagem. Bairro de fácil acesso ao centro, ônibus urbano, comércio local atuante. R$ 42.000,00. http://www.ademirferreiraimoveis.com.br ADEMIR FERREIRA IMOVEIS CRECI 13775 Tel. 3323-5119/ 9997-3571";
			//String t = "e ducha, 1 vaga ";
			//String t = "Casa em condomínio fechado, c/ 2 qtos";
			//String t = "Andar superior: 3 qtos. (ste. c/ varanda";
			//String t = "excelente casa, tipo duplex, sendo térreo c/ gar. (2), sl., banh. soc., copa, coz., área serv., dep. completa empregada, churrasq., ducha, varanda. Andar superior: 3 qtos. (ste. c/ varanda e closet). Área constr. 160 m². R$ 120.000,00. H C IMOVEIS CRECI 27 Tel. 3323-8851/ 9997-8493";
			//String t = "excelente casa";
			//String t = "Exc. casa, tipo duplex sendo 1º pav. c/ sl., banh. soc., copa/ coz., qto. despejo, lavanderia, churrasq., garagem. 2º pav. 4 qtos., banh. 3º pav. terraço. R$ 47.000,00. Aceita automóvel e cavalo mecânico como parte do pagamento. Não vende pela CEF. H C IMOVEIS CRECI 27 Tel. 3323-8851/ 9997-8493";
			//String t = "Albo Chiesse. Casa duplex, total de 897m² terreno. 1º pav.: 2 sls., lavabo, coz. c/desp., banh., escritório, área serv., dep. empr. 2º pav.: 4 qts. 1 ste. c/banheira, 2 banhs. 1 c/banheira, sl. íntima. gar. 2 carros, jardim. Ac. apto. menor na neg. R$ 250.000,00. http://www.ademirferreiraimoveis.com.br ADEMIR FERREIRA IMOVEIS CRECI-13775 Tel. 3323-5119/9997-3571";
			//String t = "gar. 2 carros, jardim. Ac. apto. menor na neg. R$ 250.000,00. http://www.ademirferreiraimoveis.com.br ADEMIR FERREIRA IMOVEIS CRECI-13775 Tel. 3323-5119/9997-3571";
			//String t = "Casa no Condomínio Garatucaia. R$ 250.000,00. Aceita-se proposta. SHIMAR IMOVEIS CRECI-18590 Tel. 3346-3180";
			//String t = "R$ 250.000,00. Aceita-se proposta. SHIMAR IMOVEIS CRECI-18590 Tel. 3346-3180";
			//String t = "ALPES DE sobrado, 03 suites, 2 sala azul, gar 02 vagas, F. (19) 3876-0021.";
			//String t = "ALPES DE VINHEDO sobrado, 03 suites, sacada, sala 2 amb, gar 02 carros, (19) 3876-0021.";
			//String t = "Cond SANTA FE casa 3 dorms, 1 st sl coz wc pisc R$ 85.000,00 F. (19) 3123123";
			//String t = "Cond SAO JOAQUIM 3 sts sala 3 ambientes cozinha piscina sauna churrasqueira R$ 620 Mil (19) 3876-2236";
			//String t = "2 QTOS SL COZ BANH ÁREA SERV QUINTAL C ÓTIMO ACABAMENTO  E C FINANC CAIXA PODENDO UTILIZAR O FGTS ASSIM ASS IMOBILIARIA 123 TEL 1233";
			//String t = "C ÓTIMO ACABAMENTO E C FINANC CAIXA PODENDO UTILIZAR O FGTS ASSIM ASS IMOBILIARIA 123 TEL 1233";
			//String t = "03 stes, sala";
			//String t = "ALPES DE VINHEDO sobrado, 03 suites, sacada";
			//String t = "sacada";
			//String t = "12 12345";
			
			String estteste = "FONE";
			
			String t = auxFunctions.tirarAcentos(entrada[k]);
			//String t = auxFunctions.tirarAcentos("çaça ÇaÇa çAÇa á Á í Í");
			//t = auxFunctions.tirarAcentos(t);
			
			String obsorig[] = t.split("\\s+");
			String obs[] = auxFunctions.canonicText(t);
			
			for(int h = 0; h < obs.length; h++){
				if(obs[h].equals("")){
					obs[h] = obsorig[h];
				}
			}
			
			//System.out.println("obs : " + obs.length + " obs orig : " + obsorig.length);
			//auxFunctions.printVetor(obsorig);
			auxFunctions.printVetor(obs);
			//auxFunctions.printMatrixCompleto(estadosinner, simbolos, dmatrixB[Indice.getIndice(estadosouter, estteste)]);
			//auxFunctions.printMatrixCompleto(estadosinner, estadosinner, dmatrixAinner[Indice.getIndice(estadosouter, estteste)]);
			
			//auxVit av = Viterbi3.getViterbiDualLevel2(obsorig, obs, estadosinner, simbolos, dmatrixAinner[Indice.getIndice(estadosouter, estteste)], dmatrixB[Indice.getIndice(estadosouter, estteste)]);
			auxVit av = Viterbi3.getViterbiPathOuter(obsorig, obs, estadosouter, estadosinner, simbolos, dmatrixA, dmatrixAinner, dmatrixB);
			
			System.out.println(av.getVprob());
			//auxFunctions.printVetddor(av.getEstadosValidos());
			//System.out.println(av.getSimbout());
			String ext[] = av.getEstadosValidos();
			String inte[] = av.getSimbout().split("\n");
			for(int g = 0; g < ext.length; g++){
				if(g < inte.length){
					System.out.println("["+ext[g]+"] :: " + inte[g]);
				}
			}
			
			//auxFunctions.printMatrixCompleto(estadosouter, estadosouter, matrixA);
			//auxFunctions.printMatrixCompleto(estadosouter, estadosouter, dmatrixA);
			//auxFunctions.printMatrixCompleto(estadosinner, estadosinner, dmatrixAinner[Indice.getIndice(estadosouter, estteste)]);
			//auxFunctions.printMatrixCompleto(estadosinner, simbolos, dmatrixB[Indice.getIndice(estadosouter, estteste)]);
			//auxFunctions.printMatrixCompleto(estadosinner, simbolos, dmatrixB[Indice.getIndice(estadosouter, "EXTRA")]);
		}

		
	} // main

}

