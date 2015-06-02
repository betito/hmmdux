package hmmdux.method;

import hmmdux.auxclass.Const;
import hmmdux.auxclass.auxVit;
import hmmdux.auxclass.auxFunctions;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Pattern;

public class Viterbi3 {

	public static auxVit getViterbiPath(String obs[], Hashtable est, Hashtable simb, Hashtable pat, double matrixa[][], double matrixb[][]){
		auxVit vp = null;
		//1
		auxVit T[] = new  auxVit[est.size()];
				
		// gerar padrao para cada estado
		//Hashtable padraoestado = auxFunctions.detectarPadrao(est, simb, matrixb);
		//auxFunctions.printHashtable(padraoestado);
		
		
		//2
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String tmpest = (String) en.nextElement();
			T[Indice.getIndice(est, tmpest)] = new auxVit(matrixa[0][Indice.getIndice(est, tmpest)], tmpest, matrixa[0][Indice.getIndice(est, tmpest)]);
		}
				
		
		//3
		
		for(int i = 0; i < obs.length; i++){			
			String output = "";
			/*if(i == (obs.length)){
				output = "FIM";
			}else{
				output = auxFunctions.canonicValue(obs[i].toUpperCase());
			}*/
			output = auxFunctions.canonicValue(obs[i].toUpperCase());
			auxVit U[] = new auxVit[T.length];
			
			//System.out.print(":["+ output+"] ");
			//.1
			Vector listaestados = new Vector(est.size());
			if(i == (obs.length-1)){
				listaestados.add("FIM");
			}else{
				for(Enumeration en = est.keys(); en.hasMoreElements();){
					listaestados.add((String) en.nextElement());
				}
			}
			
			for(int r = 0; r < listaestados.size(); r++){
				String next_state = (String) listaestados.elementAt(r);
				
				//double total = 0;
				double total = 0;
				vPath argmax = new vPath(10);
				double valmax = 0;
				
				if(auxFunctions.estadoEhValido(next_state)){
	
					//.2
					for(Enumeration enj = est.keys(); enj.hasMoreElements();){
						String state = "";
						state = (String) enj.nextElement();
						
						if(auxFunctions.estadoEhValido(state)){
							
							double prob = 0, v_prob = 0, a = 0, b = 0, p = 0; // original
							vPath v_path = null;
							
								prob = T[Indice.getIndice(est, state)].getProb();
								v_prob = T[Indice.getIndice(est, state)].getVprob();
								v_path = T[Indice.getIndice(est, state)].getEstat();
							
								b = matrixb[Indice.getIndice(est, state)][Indice.getIndice(simb, output)];
								a = matrixa[Indice.getIndice(est, state)][Indice.getIndice(est, next_state)];
								p =  b * a;
							//}
							
								prob *= p;
								v_prob *= p;
								total += prob;
								
							if(v_prob > valmax){
								argmax.putEstado(v_path);
								argmax.putEstado(next_state);
								valmax = v_prob;
							}
							
						}
					}// fim .2
					
					
					U[Indice.getIndice(est, next_state)] = new auxVit();
					U[Indice.getIndice(est, next_state)].setArgs(total, argmax, valmax);
					//System.out.println(" V "+ valmax + " :: " + argmax.getEstadosToString());
					//System.out.println(" next :: " + next_state);
				}
			}// fim .1
			
			//for(int f = 1; f < T.length; f++){
			for(Enumeration en = est.keys(); en.hasMoreElements();){
				String st = (String) en.nextElement();
				if(auxFunctions.estadoEhValido(st)){
					T[Indice.getIndice(est, st)] = U[Indice.getIndice(est, st)];
					//System.out.println(""+ Indice.getIndice(est, st) +"] "+	T[Indice.getIndice(est, st)].getEstatToString());
				}
			}
			
		}// fim 3
		
		//4
		double total = 0;
		double valmax = 0;
		vPath argmax = null;
		/*
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String state = (String) en.nextElement();
			
			if(auxFunctions.estadoEhValido(state)){
				double prob = T[Indice.getIndice(est, state)].getProb();
				double v_prob = T[Indice.getIndice(est, state)].getVprob();
				vPath v_path = T[Indice.getIndice(est, state)].getEstat();
				
				total += prob;
				if(v_prob > valmax){
					argmax = v_path;
					valmax = v_prob;
				}
			}
		}
		*/
		double prob = T[Indice.getIndice(est, "FIM")].getProb();
		double v_prob = T[Indice.getIndice(est, "FIM")].getVprob();
		vPath v_path = T[Indice.getIndice(est,  "FIM")].getEstat();
		
		total += prob;
		if(v_prob > valmax){
			argmax = v_path;
			valmax = v_prob;
		}
		//5
		vp = new auxVit();
		vp.setArgs(total, argmax, valmax);
		
		return vp;
	}

	
	public static auxVit getViterbiDualLevel2(String obsorig[], String obs[], Hashtable est, Hashtable simb, double matrixa[][], double matrixb[][]){
		auxVit vp = null;
		//1
		auxVit T[] = new  auxVit[est.size()];

		// gerar padrao para cada estado
		//Hashtable padraoestado = auxFunctions.detectarPadrao(est, simb, matrixb);
		//auxFunctions.printHashtable(padraoestado);
		
		
		//2
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String tmpest = (String) en.nextElement();
			T[Indice.getIndice(est, tmpest)] = new auxVit(matrixa[0][Indice.getIndice(est, tmpest)], tmpest, matrixa[0][Indice.getIndice(est, tmpest)]);

		}
				
		
		//3
		boolean ok = true;
		int i;
		for(i = 0; (i < obs.length) && ok; i++){
			String output = "";

			output = auxFunctions.canonicValue(obs[i].toUpperCase());
			auxVit U[] = new auxVit[T.length];
			
			//System.out.print(":["+ output+"] ");
			//.1
			Vector listaestados = new Vector(est.size());
			/*if(i == (obs.length-1)){
				listaestados.add("END");
			}else{*/
				for(Enumeration en = est.keys(); en.hasMoreElements();){
					listaestados.add((String) en.nextElement());
				}
			//}
			
			for(int r = 0; r < listaestados.size(); r++){
				String next_state = (String) listaestados.elementAt(r);
				
				double total = 0;
				vPath argmax = new vPath(50);
				double valmax = 0;
				
				
				if(auxFunctions.estadoEhValido(next_state)){
	
					//.2
					for(Enumeration enj = est.keys(); enj.hasMoreElements();){
						String state = "";
						state = (String) enj.nextElement();
						
						double prob = 0, v_prob = 0, a = 0, b = 0, p = 0; // original
						if(auxFunctions.estadoEhValido(state)){
							
							
								vPath v_path = null;
							
								prob = T[Indice.getIndice(est, state)].getProb();
								v_prob = T[Indice.getIndice(est, state)].getVprob();
								v_path = T[Indice.getIndice(est, state)].getEstat();
								
								b = matrixb[Indice.getIndice(est, state)][Indice.getIndice(simb, output)];
								a = matrixa[Indice.getIndice(est, state)][Indice.getIndice(est, next_state)];
								p =  b * a;

							
								prob *= p;
								v_prob *= p;
								total += prob;
							
								
							if(v_prob > valmax){
								//ok = true;
								argmax.putEstado(v_path);
								//if( (i != (obs.length -1)) && !(state.equals("END") && next_state.equals("END")))
								{
									argmax.putEstado(next_state);
								}
								/*if((state.equals("END") && next_state.equals("END")))
								{
									ok = false;
								}*/
								valmax = v_prob;
								
								
							}
							
							
							
						}
						
						
					}// fim .2
					
					/*
					if(argmax.tamanho() > 1 && argmax.getLastEstado().equals("END") && argmax.getLastEstado2().equals("END")){
						ok = false;
						//System.out.println("Entrou *****************");
					}*/
					
					
					U[Indice.getIndice(est, next_state)] = new auxVit();
					U[Indice.getIndice(est, next_state)].setArgs(total, argmax, valmax);
					
					//bomba 2
					//System.out.println(" V "+ valmax + " :: " + argmax.getEstadosToString());
					//System.out.println(" next :: " + next_state);
					
				}
				
				//System.out.println("Estado : " + next_state + " out : " + output);
				
			}// fim .1
			
			double prob;
			double valmax=0;
			vPath estados = new vPath();
			
			
			//for(int f = 1; f < T.length; f++){
			for(Enumeration en = est.keys(); en.hasMoreElements();){
				String st = (String) en.nextElement();
				if(auxFunctions.estadoEhValido(st)){
					T[Indice.getIndice(est, st)] = U[Indice.getIndice(est, st)];
					
					//Verifica se a melhor opção é parar de consumir elementos
					prob = U[Indice.getIndice(est, st)].getProb();
					if(prob > valmax)
					{
						valmax=prob;
						estados = U[Indice.getIndice(est, st)].getEstat();
					}

					//System.out.println("["+ st +"] "+	T[Indice.getIndice(est, st)].getEstatToString() + " " + T[Indice.getIndice(est, st)].getVprob());
				}
			}
			
			if(estados.tamanho() > 1 && estados.getLastEstado().equals("END") && estados.getLastEstado2().equals("END"))
			{
				ok = false;
			}
			
			
		}// fim 3
		
		int obs_consumidas = i - 1;
		
		//4
		double total = 0;
		double valmax = 0;
		vPath argmax = null;
		String simbout = "";
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String state = (String) en.nextElement();
			
			if(auxFunctions.estadoEhValido(state)){
				
				//String state = "END";
				//System.out.println("["+ state +"] "+	T[Indice.getIndice(est, state)].getEstatToString() + " " + T[Indice.getIndice(est, state)].getVprob());
				
				double prob = T[Indice.getIndice(est, state)].getProb();
				double v_prob = T[Indice.getIndice(est, state)].getVprob();
				vPath v_path = T[Indice.getIndice(est, state)].getEstat();
				
				total += prob;
				
				if(v_prob > valmax){

					if(((obs_consumidas < (obs.length -1)) && (!(v_path.getLastEstado().equals("END")))) || (v_path.getEstadosValidos().length == 0)){
						continue;
					}
					
					argmax = v_path;
					valmax = v_prob;
					
					//pegar os termos de obs ateh chegar no primeiro END
					simbout = "";
					//for(int r = 0; r < argmax.getEstadosValidos().length && r < obs.length; r++){
					for(int r = 0; r < (argmax.getEstadosValidos().length) && r < obsorig.length; r++){
						simbout += obsorig[r];
						if(r < (argmax.getEstadosValidos().length-1)){
							simbout += " ";
						}
					}
					
				}
			}
		}
		
		//5
		vp = new auxVit();
		vp.setArgs(total, argmax, valmax, simbout);

		return vp;
	}

	
	
	public static auxVit getViterbiPathOuter(String obsorig[], String obs[], Hashtable est, Hashtable estinner, Hashtable simb, double matrixa[][], double matrixain[][][], double matrixb[][][]){
		auxVit vp = null;
		//1
		auxVit T[][] = new  auxVit[est.size()][obs.length+2];
		 
		
		// gerar padrao para cada estado
		//Hashtable padraoestado = auxFunctions.detectarPadrao(est, simb, matrixb);
		//auxFunctions.printHashtable(padraoestado);
		
		
		//2
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String tmpest = (String) en.nextElement();
			T[Indice.getIndice(est, tmpest)][0] = new auxVit(matrixa[0][Indice.getIndice(est, tmpest)], tmpest, matrixa[0][Indice.getIndice(est, tmpest)]);
			//Tin[Indice.getIndice(est, tmpest)] = "";
		}
				
		
		//3
		int incremento = 0;
		auxVit avin = null;
		boolean ok = true;
		//for(int i = 0; i < obs.length;){
		//auxVit P[] = new auxVit[T.length];
		
		for(int i = 0; i < obs.length; i++){

			ok = true;
			//auxVit U[] = new auxVit[T.length];
			//System.out.print(":["+ output+"] ");
			//.1
			Vector listaestados = new Vector(est.size());
			for(Enumeration enj = est.keys(); enj.hasMoreElements();){
				listaestados.add((String) enj.nextElement());
			}
			for(int r = 0; r < listaestados.size(); r++){
				
				String next_state = (String) listaestados.elementAt(r);
				
				//System.out.println("*********************");
				
				//double total = 0;
				double total = 0;
				vPath argmax = new vPath(30);
				double valmax = 0;


				//System.out.println("Next " + next_state);
				if(auxFunctions.estadoEhValido(next_state)){
					//.2
					int prox = 0;
					for(Enumeration enj = est.keys(); enj.hasMoreElements();){
						String state = "";
						state = (String) enj.nextElement();
						
						if(auxFunctions.estadoEhValido(state)){
							
							double prob = 0, v_prob = 0, a = 0, b = 0, p = 0; // original
							vPath v_path = null;
							
							if(T[Indice.getIndice(est, state)][i] == null){
								continue;
							}
								
							
							prob = T[Indice.getIndice(est, state)][i].getProb();
							v_prob = T[Indice.getIndice(est, state)][i].getVprob();
							v_path = T[Indice.getIndice(est, state)][i].getEstat();
							
							//System.out.println(v_pa);
							
							if(v_prob == 0){
								//System.out.println("******** VPROB NULA!" + v_path.getEstadosToString()+ " - " + v_prob + " OUT " + obs[i]);
								continue;
							}
							//b = matrixb[Indice.getIndice(est, state)][Indice.getIndice(simb, output)];
							double tempma[][] = matrixain[Indice.getIndice(est, state)];
							double tempmb[][] = matrixb[Indice.getIndice(est, state)];
							
							int h = T[Indice.getIndice(est, state)][i].getAcuminc();
							
							if(h >= obs.length){
								//ok = false;
								//System.out.println("******** H! " + v_path.getEstadosToString()+ " - " + v_prob);
								continue;
							}
							
							prox = 0;
							
							for(int z=0;;z++)
							{
								
								
								String obstmp[];
								String obstmporig[];
								
								if(z==0){
									obstmp = auxFunctions.getTermosUntilNextMeta(h, obs);
									obstmporig = auxFunctions.getTermosUntilNextMeta(h, obsorig);
								}else{
									obstmp = auxFunctions.getTermosInRange(h,h+z, obs);
									obstmporig = auxFunctions.getTermosInRange(h,h+z,obsorig);									
								}
								
								
								auxVit avint = Viterbi3.getViterbiDualLevel2(obstmporig, obstmp, estinner, simb, tempma, tempmb);
								b = avint.getVprob();
								a = matrixa[Indice.getIndice(est, state)][Indice.getIndice(est, next_state)];
								p =  b * a;
							
								// original, que faz  a multiplicacao entre estados
								prob *= p;
								v_prob *= p;
								total += prob;
								
								// para fazer somas
								/*prob += p;
								v_prob += p;
								total += prob;
								*/
								
								//System.out.println("Est : + " +state+ " p : " + prob + " vp : "+ v_prob + " vpath_: "+v_path.getEstadosToString());
							
								//prox = h + avint.getNumEstValidos();
								prox = h + avint.getNumObsConsumidas();
								
								if(T[Indice.getIndice(est, next_state)][prox] == null){
									argmax.putEstado(v_path);
									argmax.putEstado(next_state, "["+avint.getEstadosValidostoString()+"]" + "["+avint.getSimbout()+"]");
									T[Indice.getIndice(est, next_state)][prox] = new auxVit();
									T[Indice.getIndice(est, next_state)][prox].setArgs(total, argmax, v_prob, avint, prox);
								}
								else{
									if(v_prob > T[Indice.getIndice(est, next_state)][prox].getProb()){
										argmax.putEstado(v_path);
										argmax.putEstado(next_state, "["+avint.getEstadosValidostoString()+"]" + "["+avint.getSimbout()+"]");
										
										T[Indice.getIndice(est, next_state)][prox].setArgs(total, argmax, v_prob, avint, prox);
									}	
								}
								//aqui eh onde bomba
								if(T[Indice.getIndice(est, next_state)][prox].getProb() >0)
								{
									//System.out.println(argmax.getEstadosToString() + " :: "+ T[Indice.getIndice(est, next_state)][prox].getProb() + " PROX: "+prox+ " obs.length: " + obs.length );
								}
								
								if(z >= prox || (!(state.equals("EXTRA")))) break;
							}	
						}
						
					}// fim .2
					
					//System.out.println("Estado : "+next_state + " Prox " + prox);
					/*if(avin != null){
						System.out.println(" + " + avin.getEstadosValidostoString());
						System.out.println(" + " + avin.getSimbout());
					}*/
					//System.out.println(argmax.getEstadosToString());
					//System.out.println(argmax.estadosinternos);
					//U[Indice.getIndice(est, next_state)] = new auxVit();
					//U[Indice.getIndice(est, next_state)].setArgs(total, argmax, valmax, avin, prox);
					/*if(prox <= obs.length+1){
						T[Indice.getIndice(est, next_state)][prox] = new auxVit();
						T[Indice.getIndice(est, next_state)][prox].setArgs(total, argmax, valmax, avin, prox);
					}*/
					
					
					
					//System.out.println(" next :: " + next_state);
				}
				
			}// fim .1

			//System.out.println("*********************************");
			//for(int f = 1; f < T.length; f++){
			/*
			double probant = 0;
			String tmaior = "";
			for(Enumeration en = est.keys(); en.hasMoreElements();){
				String st = (String) en.nextElement();
				if(auxFunctions.estadoEhValido(st)){
					T[Indice.getIndice(est, st)] = U[Indice.getIndice(est, st)];
					double p = T[Indice.getIndice(est, st)].getVprob();
					int prox = T[Indice.getIndice(est, st)].getAcuminc();
					if(probant < p){
						probant = p;
						tmaior = st;
					}
					//System.out.println(""+ Indice.getIndice(est, st) +"] "+	T[Indice.getIndice(est, st)].getEstatToString() + " " + p);
				}
			}
			
			auxVit asd = T[Indice.getIndice(est, tmaior)][obs.length]; 
			if((asd.getAcuminc() >= obs.length) || (probant == 0)){
				ok = false;
			}
			*/
			
		}// fim 3
		
		//4
		double total = 0;
		double valmax = 0;
		vPath argmax = null;
		
		for(Enumeration en = est.keys(); en.hasMoreElements();){
			String state = (String) en.nextElement();
			
			if(auxFunctions.estadoEhValido(state)){
				
				if(T[Indice.getIndice(est, state)][obs.length]==null){
					continue;
				}
					
				double prob = T[Indice.getIndice(est, state)][obs.length].getProb();
				double v_prob = T[Indice.getIndice(est, state)][obs.length].getVprob();
				vPath v_path = T[Indice.getIndice(est, state)][obs.length].getEstat();
				
				total += prob;
				if(v_prob > valmax){
					argmax = v_path;
					valmax = v_prob;
					
				}
			}
		}
		
		//5
		
		/*vp = new auxVit();
		
		vp.setArgs(total, argmax, valmax, argmax.estadosinternos);
		*/
		vp = new auxVit();
		if((argmax != null) && (argmax.estadosinternos != null)){
			vp.setArgs(total, argmax, valmax, argmax.estadosinternos);
		}


		return vp;
		//vp.setArgs(total, argmax, valmax);

	}
	
	
	public static hmmSeqEstados getObservationStates(String obs[], Hashtable est, Hashtable simb, Hashtable pat, double matb[][]){
		hmmSeqEstados hmmse = new hmmSeqEstados(obs.length); 
	
		for(int i = 0;i < obs.length; i++){
			String o = obs[i];
			o = auxFunctions.canonicValue(o);
			String estsaida = "";
			
			//System.out.print("checando => "+o+" ... ");
			
			if(simb.containsKey(o)){
				// pegar a maor probabilidade de este simbolo pertencer a qual estado isoladamente
				double prob = 0;
				estsaida = "";
				for(Enumeration en = est.keys(); en.hasMoreElements();){
					String e = (String) en.nextElement();
					
					if(auxFunctions.estadoEhValido(e)){
						if(prob < matb[Indice.getIndice(est, e)][Indice.getIndice(simb, o)]){
							prob = matb[Indice.getIndice(est, e)][Indice.getIndice(simb, o)];
							estsaida = e;
						}
					}
				}
				
				//System.out.print("p1 estado de ["+o+"]::["+estsaida+"]");
				hmmse.putEstado(estsaida, o);
			}else{
				// checar se o padrao do simbolo eh aceitavel no estado
				estsaida = "";
				for(Enumeration en = pat.keys(); en.hasMoreElements();){
					String e = (String) en.nextElement();
					String p = (String) pat.get(e);
					
					if(auxFunctions.estadoEhValido(e)){
						//System.out.println("entrou " + p);
						if(Pattern.matches(p, o)){
							estsaida = e;
						}
					}
					
				}
				
				if(estsaida.equals("")){	
					// nao encaixa em nada
					estsaida = "3";
///					System.out.print("p3 estado de ["+o+"]::["+estsaida+"]");
					hmmse.putEstado(estsaida, o);					
				}else{
					//System.out.print("p2 estado de ["+o+"]::["+estsaida+"]");
					hmmse.putEstado(estsaida, o);
				}
			}
			//System.out.println("");
		}
		
		return hmmse;
	}
	
	
	public static hmmSeqEstados getSemiStates(String obs[], Hashtable est, Hashtable simb){
		hmmSeqEstados hmmse = new hmmSeqEstados(obs.length); 
		String e = "";
		
		for(int i = 0;i < obs.length; i++){
			e = "";			
			String o = obs[i];
			o = auxFunctions.canonicValue(o);
			String estsaida = "";
			//System.out.print("checando => "+o+" ... ");
			
			if(simb.containsKey(o) || auxFunctions.isNumberValue(o)){
				
				e = Const.semidado;
				//System.out.print(" (a) ");
				
			}else{
				String sim = auxFunctions.checarTokenUsandoDE(o, simb, Const.similar); 
				if( sim != null){
					
					e = Const.semidado;
					//System.out.print(" (b) " + sim + " ");
					
				}else{
					e = Const.naotem;
				}
			}
			
			//System.out.print(""+e);
			hmmse.putEstado(e, o);
			//System.out.println("");
		}
		
		hmmse.putEstado(Const.naotem, "END");
		return hmmse;
	}
	
	public static hmmSeqEstados getStatesMetaStates(String obs[], Hashtable est, Hashtable simb, Hashtable pat, double matrixb[][]){
		hmmSeqEstados hmmse = new hmmSeqEstados(obs.length); 
		String e = "";
		
		for(int i = 0;i < obs.length; i++){
			e = "";			
			String o = obs[i];
			o = auxFunctions.canonicValue(o);
			String estsaida = "";
			//System.out.print("checando => "+o+" ... ");
			
			if(simb.containsKey(o)){
				/*int t = simb.get(Indice.getIndice(o), simb);
				if(t.equals("1")){
					e = Const.meta;
				}else{
					e = Const.naotem;
				}
				*/
				
			}else{
				e = Const.naotem;
			}
			
			//System.out.print(""+e);
			hmmse.putEstado(e, o);
			//System.out.println("");
		}
	
		return hmmse;
	}
	
	
	
}
