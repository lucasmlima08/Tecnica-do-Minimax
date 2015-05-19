package minimax;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame implements ActionListener {
	
	/** ATRIBUTOS */
	
	private String[] velhaAtual = {"?","?","?","?","?","?","?","?","?"};
	private Arvore raizAtual;
	private boolean terminou = false;
	
	private String strJogador = "O";
	private String strCPU = "X";
	private int vitoria = 1;
	private int derrota = -1;
	
	private static final long serialVersionUID = 1L;
	private JButton[] botoes = new JButton[9];
	
	/** INTERFACE DO JOGO */
	
	public Main() {
		setTitle("Jogo da Velha");
		setSize(400,400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(3,3,5,5));
		for (int i=0; i<botoes.length; i++){
			botoes[i] = new JButton();
			botoes[i].addActionListener(this);
			add(botoes[i]);
		}
		gerarArvore();
		setVisible(true);
	}
	
	/** GERA A ÁRVORE INTEIRA */
	
	private void gerarArvore(){
		Arvore arvore = new Arvore();
        arvore.criaFilhos();
        arvore = incluirCustoDeEscolha(arvore);
        raizAtual = arvore;
	}
	
	/** BUSCA EM PROFUNDIDADE PARA SOMA DOS CUSTOS DE ESCOLHA */
	
	private Arvore incluirCustoDeEscolha(Arvore raiz){
		for (int i=0; i<raiz.getFilhos().size(); i++){
			incluirCustoDeEscolha(raiz.getFilhos().get(i));
			raiz.setCustoDeEscolha(raiz.getCustoDeEscolha()+raiz.getFilhos().get(i).getCustoDeEscolha());
		}
		return raiz;
	}
	
	/** MÉTODO DE IMPRESSÃO DO ARRAY NO FORMATO DE MATRIZ */
	
	private void imprimeMatriz(String[] velha){
        System.out.println(" | "+velha[0]+" | "+velha[1]+" | "+velha[2]+" | ");
        System.out.println(" | "+velha[3]+" | "+velha[4]+" | "+velha[5]+" | ");
        System.out.println(" | "+velha[6]+" | "+velha[7]+" | "+velha[8]+" | ");
		System.out.println();
    }
	
	/** MÉTODO DE JOGADA DO USUÁRIO */
	
	private void jogadaUsuario(int escolha){
		if (raizAtual.getFilhos().size() > 0 && botoes[escolha].getText().equals("")){
			botoes[escolha].setBackground(Color.GREEN);
			botoes[escolha].setText(strJogador);
			velhaAtual[escolha] = strJogador;
			// **********************************************************************************
			// Procura o nó raiz da jogada do usuário.
			for (int i=0; i<raizAtual.getFilhos().size(); i++)
				if (Arrays.equals(raizAtual.getFilhos().get(i).getVelha(), velhaAtual)){
					raizAtual = raizAtual.getFilhos().get(i);
					break;
				}
			System.out.println("----- Jogada do Usuário -----"); 
			imprimeMatriz(raizAtual.getVelha());
			// **********************************************************************************
			// Verifica se ganhou..
			if (raizAtual.getCustoDeEscolha() == derrota && raizAtual.getFilhos().size() == 0){
				terminou = true;
				System.out.println("### VITÓRIA DO USUÁRIO ###");
			} else if (raizAtual.getCustoDeEscolha() == 0 && raizAtual.getFilhos().size() == 0){
				terminou = true;
				System.out.println("### NINGUÉM VENCEU ###");
			}
		}
	}
	
	/** MÉTODO DE DECISÃO DA JOGADA DA CPU */
	
	private void jogadaCPU(){
		if (raizAtual.getFilhos().size() > 0 && !terminou){
			Arvore filhoVencedor = null, filhoSemDerrota = null, filhoDerrotado = null, maximizacao = null;
			boolean vencedor = false, perdedor = false;
			// **************************************************************************************
			// Percorre os filhos para encontrar a melhor escolha de jogada.
			int melhorCusto = raizAtual.getFilhos().get(0).getCustoDeEscolha();
			maximizacao = raizAtual.getFilhos().get(0);
			for (int i=0; i<raizAtual.getFilhos().size(); i++){
				Arvore filhoAux = raizAtual.getFilhos().get(i);
				// **********************************************************************************
				// Vai vencer..
				if ((filhoAux.getCustoDeEscolha() == vitoria)&&(filhoAux.getFilhos().size() == 0)){
					vencedor = true;
					filhoVencedor = filhoAux;
					break;
				}
				// ***********************************************************************************
				// Procura o maior valor (maximização).
				// Se o usuário passar a vez, a maximização passa a ser minimização (por causa do nível da árvore).
				if (raizAtual.getFilhos().get(i).getCustoDeEscolha() > melhorCusto){
					melhorCusto = raizAtual.getFilhos().get(i).getCustoDeEscolha();
					maximizacao = filhoAux;
				}
				// *************************************************************************************
		                // Verifica os filhos do filho atual, para saber se vai perder na próxima jogada do usuário.
		                boolean perdedorAux = false;
		                int nDerrotasAux = 0;
		                for(int j=0; j<filhoAux.getFilhos().size(); j++){
		                    if ((filhoAux.getFilhos().get(j).getCustoDeEscolha() == derrota)&&(filhoAux.getFilhos().get(j).getFilhos().size() == 0)){
		                        perdedor = true;
		                        perdedorAux = true;
		                        nDerrotasAux++;
		                    }
		                }
		                // Verifica o número de possíveis derrotas na próxima jogada.
		                // Isso porque mesmo que seja derrotado ele irá escolher o caminho com o menor número de derrotas.
		                if (nDerrotasAux < 2){
		                    filhoDerrotado = filhoAux;
		                }
				// Verifica se esté é um filho que não haverá derrotas na próxima jogada.
				if (!perdedorAux){
					filhoSemDerrota = filhoAux;
				}
			}
			// ******************************************************************************************
			// Condições de escolha da jogada da CPU.
			if (vencedor)
				raizAtual = filhoVencedor;
			else if (perdedor && filhoSemDerrota != null)
				raizAtual = filhoSemDerrota;
			else if (perdedor && filhoSemDerrota == null)
				raizAtual = filhoDerrotado;
			else
				raizAtual = maximizacao;
			// ******************************************************************************************
			// Após descobrir qual o melhor filho, verifica qual local a CPU vai jogar.
			for (int i=0; i<raizAtual.getVelha().length; i++){
				if (!velhaAtual[i].equals(raizAtual.getVelha()[i])){
					botoes[i].setBackground(Color.ORANGE);
					botoes[i].setText(strCPU);
					velhaAtual[i] = strCPU;
					break;
				}
			}
			// ******************************************************************************************
			// Informação no console.
			System.out.println("----- Jogada da CPU -----");
			imprimeMatriz(raizAtual.getVelha());
			if (raizAtual.getCustoDeEscolha() == vitoria && raizAtual.getFilhos().size() == 0){
				terminou = true;
				System.out.println("### VITÓRIA DA CPU ###");
			}
		}
	}
	
	/** EVENTOS */
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		for (int i=0; i<botoes.length; i++){
			if ((o.equals(botoes[i]) && (!terminou))){
				jogadaUsuario(i);
				jogadaCPU();
			}
		}
	}
	
	/** MAIN */
	
	public static void main(String[] args) {
		new Main();
	}
}
