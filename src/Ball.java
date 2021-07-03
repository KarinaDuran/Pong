import java.awt.*;
import java.util.Random;

/**
 * Esta classe representa a bola usada no jogo. A classe princial do jogo (Pong)
 * instancia um objeto deste tipo quando a execução é iniciada.
 */

public class Ball {

	/**
	 * Construtor da classe Ball. Observe que quem invoca o construtor desta classe
	 * define a velocidade da bola (em pixels por millisegundo), mas não define a
	 * direção deste movimento. A direção do movimento é determinada aleatóriamente
	 * pelo construtor.
	 * 
	 * @param cx     coordenada x da posição inicial da bola (centro do retangulo
	 *               que a representa).
	 * @param cy     coordenada y da posição inicial da bola (centro do retangulo
	 *               que a representa).
	 * @param width  largura do retangulo que representa a bola.
	 * @param height altura do retangulo que representa a bola.
	 * @param color  cor da bola.
	 * @param speed  velocidade da bola (em pixels por millisegundo).
	 * @param direcaoX variavel que determina a direcao da bola no eixo X
	 * @param direcaoY variavel que determina a direcao da bola no eixo Y
	 */

	private double cx;
	private double cy;
	private double width;
	private double height;
	private Color color;
	private double speed;
	private int direcaoX;
	private int direcaoY;

	public Ball(double cx, double cy, double width, double height, Color color, double speed) {
		this.cx = cx;
		this.cy = cy;
		this.width = width;
		this.height = height;
		this.color = color;
		this.speed = speed;

		// No eixo x, horizontal, se direcaoX = 1, quer dizer que a bola se movimenta
		// para a direita, se eh = -1 movimenta-se para a esquerda. No eixo y, se
		// direcaoY = 1, quer dizer que a bola movimenta-se para baixo, se = -1
		// movimenta-se para cima.

		// O gerador aleatório escolhe as direcoes iniciais do jogo
		Random gerador = new Random();
		if (gerador.nextInt(2) == 0) {
			direcaoX = 1;
		} else {
			direcaoX = -1;
		}
		if (gerador.nextInt(2) == 0) {
			direcaoY = 1;
		} else {
			direcaoY = -1;
		}
	}

	/**
	 * Método chamado sempre que a bola precisa ser (re)desenhada.
	 */

	public void draw() {

		GameLib.setColor(color);
		GameLib.fillRect(cx, cy, width, height);
	}

	/**
	 * Método chamado quando o estado (posição) da bola precisa ser atualizado.
	 * 
	 * @param delta quantidade de millisegundos que se passou entre o ciclo anterior
	 *              de atualização do jogo e o atual.
	 */

	public void update(long delta) {
		// atualiza os valores de cy e cx com base na sua direcao e velocidade
		// Como a velocidade eh multiplicada 2x dividimos por 2
		this.cy = cy + delta * direcaoY * speed / 2;
		this.cx = cx + delta * direcaoX * speed / 2;
	}

	/**
	 * Método chamado quando detecta-se uma colisão da bola com um jogador.
	 * 
	 * @param playerId uma string cujo conteúdo identifica um dos jogadores.
	 */

	public void onPlayerCollision(String playerId) {
		// Muda as direcoes horizontais
		if (playerId == "Player 1") {
			direcaoX = 1;
		} else {
			direcaoX = -1;
		}

	}

	/**
	 * Método chamado quando detecta-se uma colisão da bola com uma parede.
	 * 
	 * @param wallId uma string cujo conteúdo identifica uma das paredes da quadra.
	 */

	public void onWallCollision(String wallId) {
		// Dependendo da parede, muda as direcoes necessarias
		if (wallId == "Left") {
			this.direcaoX = 1;
		} else if (wallId == "Right") {
			this.direcaoX = -1;
		} else if (wallId == "Top") {
			this.direcaoY = 1;
		} else {
			this.direcaoY = -1;
		}

	}

	/**
	 * Método que verifica se houve colisão da bola com uma parede.
	 * 
	 * @param wall referência para uma instância de Wall contra a qual será
	 *             verificada a ocorrência de colisão da bola.
	 * @return um valor booleano que indica a ocorrência (true) ou não (false) de
	 *         colisão.
	 */

	public boolean checkCollision(Wall wall) {
		// Armazena as posicoes de todos os lados da bola
		double esquerdo, direito, cima, baixo;
		esquerdo = cx - width / 2;
		direito = cx + width / 2;
		cima = cy - height / 2;
		baixo = cy + height / 2;

		// Armazena as posicoes dos lados da parede
		double paredeEsquerda, paredeDireita, paredeCima, paredeBaixo;
		paredeEsquerda = wall.getCx() - wall.getWidth() / 2;
		paredeDireita = wall.getCx() + wall.getWidth() / 2;
		paredeCima = wall.getCy() - wall.getHeight() / 2;
		paredeBaixo = wall.getCy() + wall.getHeight() / 2;

		// Verifica se abola esta dentro dessas coordenadas
		if (baixo > paredeCima && esquerdo < paredeDireita && direito > paredeEsquerda && cima < paredeBaixo) {
			onWallCollision(wall.getId());
			return true;
		}

		return false;
	}

	/**
	 * Método que verifica se houve colisão da bola com um jogador.
	 * 
	 * @param player referência para uma instância de Player contra o qual será
	 *               verificada a ocorrência de colisão da bola.
	 * @return um valor booleano que indica a ocorrência (true) ou não (false) de
	 *         colisão.
	 */

	public boolean checkCollision(Player player) {
		// Armazena as posicoes das laterais da bola
		double cima, baixo, esquerda, direita;
		cima = cy - height / 2;
		baixo = cy + height / 2;
		esquerda = cx - width / 2;
		direita = cx + width / 2;
		// Armazena as posicoes das laterais do player
		double playerCima, playerBaixo, playerEsquerda, playerDireita;
		playerCima = player.getCy() - player.getHeight() / 2;
		playerBaixo = player.getCy() + player.getHeight() / 2;
		playerEsquerda = player.getCx() - player.getWidth() / 2;
		playerDireita = player.getCx() + player.getWidth() / 2;

		// Verifica se as laterais da bola estao dentro das coordeandas do player
		if (baixo > playerCima && cima < playerBaixo && direita > playerEsquerda && esquerda < playerDireita) {
			onPlayerCollision(player.getId());
			return true;
		}
		return false;
	}

	/**
	 * Método que devolve a coordenada x do centro do retângulo que representa a
	 * bola.
	 * 
	 * @return o valor double da coordenada x.
	 */

	public double getCx() {

		return cx;
	}

	/**
	 * Método que devolve a coordenada y do centro do retângulo que representa a
	 * bola.
	 * 
	 * @return o valor double da coordenada y.
	 */

	public double getCy() {

		return cy;
	}

	/**
	 * Método que devolve a velocidade da bola.
	 * 
	 * @return o valor double da velocidade.
	 * 
	 */

	public double getSpeed() {

		return speed;
	}

}
