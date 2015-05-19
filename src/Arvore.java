package minimax;

import java.util.ArrayList;

public class Arvore {
	
	/** ATRIBUTOS */
	
	private String[] velha = {"?","?","?","?","?","?","?","?","?"};
	private String tipo = "max";
	private int custoDeEscolha = 0;
    public ArrayList<Arvore> filhos = new ArrayList<Arvore>();
    
    /** POLIMORFISMO */
    
    public void setVelha(String[] velha){ this.velha = velha.clone(); }
    public String[] getVelha(){ return this.velha; }
    
    public void setTipo(String tipo){ this.tipo = tipo; }
    public String getTipo(){ return this.tipo; }
    
    public void setCustoDeEscolha(int custoDeEscolha){ this.custoDeEscolha = custoDeEscolha; }
    public int getCustoDeEscolha(){ return this.custoDeEscolha;  }
    
    public ArrayList<Arvore> getFilhos(){ return this.filhos; }
    
    /** MÉTODOS CONSTRUTORES */
    
    public Arvore(){}
    public Arvore(String[] velha, String tipo){
        this.velha = velha.clone();
        this.tipo = tipo;
    }
    
    /** MÉTODO QUE GERA A ÁRVORE */
    
    public void criaFilhos(){
    	// 'O' venceu..
        if (vencedor(getVelha(),"O")){ 
        	setCustoDeEscolha(-1);
        // 'X' venceu..
        } else if (vencedor(getVelha(),"X")){ 
        	setCustoDeEscolha(1);
        // Ainda sem um vencedor.
        } else { 
        	String[] novaVelha = velha.clone();
            Arvore filho = null;
            // Gera os próximos filhos.
        	for (int i=0; i<novaVelha.length; i++){
        		// Encontrou uma posição vazia, inclui para gerar um novo filho.
	        	if (novaVelha[i].equals("?")){ 
	        		//- Usuário.
	        		if (getTipo().equals("max")){
	        			novaVelha[i] = "O";
	        			filho = new Arvore(novaVelha, "min");
	        		//- CPU.
	        		} else {
	        			novaVelha[i] = "X";
	        			filho = new Arvore(novaVelha, "max");
	        		}
	                filhos.add(filho);
	                filho.criaFilhos();
	        	}
	        	novaVelha = velha.clone();
	        }
        }
    }
    
    /** MÉTODO DE VERIFICAÇÃO DE VITÓRIA OU DERROTA */
    
    private boolean vencedor(String[] velha, String jogador){
        if (velha[0].equals(jogador) && velha[0].equals(velha[3]) && velha[3].equals(velha[6]))
        	return true;
        else if (velha[1].equals(jogador) && velha[1].equals(velha[4]) && velha[4].equals(velha[7]))
        	return true;
        else if (velha[2].equals(jogador) && velha[2].equals(velha[5]) && velha[5].equals(velha[8]))
        	return true;
        else if (velha[0].equals(jogador) && velha[0].equals(velha[4]) && velha[4].equals(velha[8]))
        	return true;
        else if (velha[2].equals(jogador) && velha[2].equals(velha[4]) && velha[4].equals(velha[6]))
        	return true;
        else if (velha[0].equals(jogador) && velha[0].equals(velha[1]) && velha[1].equals(velha[2]))
        	return true;
        else if (velha[3].equals(jogador) && velha[3].equals(velha[4]) && velha[4].equals(velha[5]))
        	return true;
        else if (velha[6].equals(jogador) && velha[6].equals(velha[7]) && velha[7].equals(velha[8]))
        	return true;
        return false;
    }
}
