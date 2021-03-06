package com.mygdx.game;


import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.util.Random;

import javax.swing.JOptionPane;

import org.omg.CORBA.portable.ValueOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ABPScreen implements Screen{
	
	private static Executor game;	 
	private static int posicaoAux;
	private OrthographicCamera camera;
	private Viewport port;
	private ABPHud hud;
	static int aux = 0, count = 0, valorRem;
	static int[] existe = new int[20];
	public static boolean negativo = false, elementoExiste = false, completo = false;
	
	//Atributos relacionados a constru��o gr�fica da arvore
	static ABP arvore;
	static Texture quadValido;
	static Texture quadDireita1;
	static Texture quadEsquerda1;
	static Texture quadDireita2;
	static Texture quadEsquerda2;
	static Texture quadDireita3;
	static Texture quadEsquerda3;
	static Texture quadDireita4;
	static Texture quadEsquerda4;
	static Texture raiz;
	static Texture ponteiro;
	private static int raizAux, indice, x, y, insereAux[];
	private static NoABP no;
	private static NoABP nos[];
	public static boolean exit;
	
	//Atributos relacionados a constru��o a fonte
	static BitmapFont font[];
	static BitmapFont font2;
	static int cont;
	static int menos[];
	static String pesquisa;
	/*
	 * Todos os textures precisam ser construidos
	 * apenas, e somente apenas, no construtor
	 */
	public ABPScreen(Executor game){		
		indice = 0;
		menos = new int [20];
		cont = 0;
		nos = new NoABP[20];
		exit = false;
		
		insereAux = new int[20];
		posicaoAux = 0;
		quadValido = new Texture("coisa/BlocoArvoreRaiz.png");
		
		for(int i = 0; i < existe.length; i++){
			existe[i] = -1;
		}
		
		//Precisa arrumar as setas e a raiz
		quadDireita1 = new Texture("coisa/BlocoArvoreNv1Direita.png");
		quadEsquerda1 = new Texture("coisa/BlocoArvoreNv1Esquerda.png");
		quadDireita2 = new Texture("coisa/BlocoArvoreNv2Direita.png");
		quadEsquerda2 = new Texture("coisa/BlocoArvoreNv2Esquerda.png");
		quadDireita3 = new Texture("coisa/BlocoArvoreNv3Direita.png");
		quadEsquerda3 = new Texture("coisa/BlocoArvoreNv3Esquerda.png");
		quadDireita4 = new Texture("coisa/BlocoArvoreNv4Direita.png");
		quadEsquerda4 = new Texture("coisa/BlocoArvoreNv4Esquerda.png");
		raiz = new Texture("coisa/PonteiroCabe�a.png");
		ponteiro = new Texture("coisa/Raiz.png");
		FileHandle caminho = new FileHandle("coisa/font.ttf");
		
		  FreeTypeFontGenerator generator = new FreeTypeFontGenerator(caminho);
		  FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		  parameter.size = 20;		  
		  font = new BitmapFont[21];
		  for(int i = 0; i <= 20; i++) {				 
		  font[i] = generator.generateFont(parameter);	
		  font[i].setColor(Color.valueOf("b7b7b7"));		  
		  }
		  generator.dispose();
		  
		  FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(caminho);
		  FreeTypeFontParameter parameter2 = new FreeTypeFontParameter();
		  parameter2.size = 20;		  
		  font2 = new BitmapFont();					 
		  font2 = generator2.generateFont(parameter2);	
		  font2.setColor(Color.valueOf("b7b7b7"));		  
		  generator2.dispose();
		  
		arvore = new ABP(quadValido, quadDireita1, quadEsquerda1, quadDireita2, quadEsquerda2, quadDireita3, quadEsquerda3, quadDireita4, quadEsquerda4);
		camera = new OrthographicCamera();
		port = new FitViewport(Executor.V_WIDTH, Executor.V_HEIGHT, camera);
		this.game = game;
		hud = new ABPHud(game.balde, game);	
		
		camera.zoom = (float) 1.5;
	
	}

	
	public void show() {
		
		
	}

	
	public void render(float delta) {
		moveCamera(delta);
				
		Gdx.gl.glClearColor(64/255.0f, 102/255.0f, 128/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.balde.setProjectionMatrix(camera.combined);
		game.balde.begin();
	
		
		if(exit) {
			
			this.dispose();
			
		}
		game.balde.draw(ponteiro, -170, 270);
		if(!arvore.vazia()) {
			
			arvore.raiz().setX(-65);
			arvore.raiz().setY(150);
			
			game.balde.draw(arvore.raiz().getQuad(), arvore.raiz().getX(), arvore.raiz().getY());
			
			font[0].draw(game.balde, String.valueOf(arvore.raiz().getConteudo()), arvore.raiz().getX() + 50,	arvore.raiz().getY() + 70);
			
		}
		for(int i = 1; i <= arvore.tamanho(); i++) {
			
			
				try {
					
				if(nos[i].getDirecao() ==  1)	{//Direita
						
					
					game.balde.draw(arvore.busca(insereAux[i]).getQuad(), (arvore.busca(insereAux[i]).getPai().getX() +150 * contaD(arvore.busca(insereAux[i]).getProfundidade(), i) + menos[i]) - contaNv(arvore.busca(insereAux[i]).getProfundidade()),
							arvore.busca(insereAux[i]).getPai().getY() -150 );
					arvore.busca(insereAux[i]).setX(arvore.busca(insereAux[i]).getPai().getX() +150 * contaD(arvore.busca(insereAux[i]).getProfundidade(), i) + menos[i] );
				
					arvore.busca(insereAux[i]).setY(arvore.busca(insereAux[i]).getPai().getY() -150 );
					
					
					font[i].draw(game.balde, String.valueOf(arvore.busca(insereAux[i]).getConteudo()), 
							arvore.busca(insereAux[i]).getX() + 50,	arvore.busca(insereAux[i]).getY() + 70);
					indice = 1;	
				}
				else if(nos[i].getDirecao() == 2) {//Esquerda
					
					game.balde.draw(arvore.busca(insereAux[i]).getQuad(), arvore.busca(insereAux[i]).getPai().getX() -150 * contaD(arvore.busca(insereAux[i]).getProfundidade(), i) - menos[i],
							arvore.busca(insereAux[i]).getPai().getY() -150 );
						arvore.busca(insereAux[i]).setX(arvore.busca(insereAux[i]).getPai().getX() -150 * contaD(arvore.busca(insereAux[i]).getProfundidade(), i) - menos[i]);
					
					arvore.busca(insereAux[i]).setY(arvore.busca(insereAux[i]).getPai().getY() -150 );
					
					
					font[i].draw(game.balde, String.valueOf(arvore.busca(insereAux[i]).getConteudo()),
							arvore.busca(insereAux[i]).getX() + 50, arvore.busca(insereAux[i]).getY() + 70);
					indice = 2;	
				}
			}catch(Exception e) {
				
				} 
		}
		game.balde.end();
		hud.stage.act(delta);
		hud.stage.draw();
	}
	
	private int contaD(int prof, int i) {
		
		if(prof == 0) {
			return 0;
		}
		if(prof == 1) {
			return 4;
		}
		if(prof == 2) {
			return 2;
		}
		if(prof == 3) {
			return 1;
		}
		if(prof == 4) {
			menos[i] = 50;
			return 1;			
		}
		
		else {
			return 1;
		}
		
	}
	
private int contaNv(int prof) {
		
		if(prof == 0) {
			return 0;
		}
		if(prof == 1) {
			return 598;
		}
		if(prof == 2) {
			return 300;
		}
		if(prof == 3) {
			return 150;
		}
		if(prof == 4) {
			
			return 200;			
		}
		
		else {
			return 150;
		}
		
	}

	
	private void moveCamera(float dt) {
		setmoveCamera(dt);
		
		camera.update();
		
	}


	private void setmoveCamera(float dt) {
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			   camera.zoom += 0.02;
			   if(camera.zoom > 2.5)camera.zoom = (float) 2.5;
			  }
			  if (Gdx.input.isKeyPressed(Keys.Q)) {
			   camera.zoom -= 0.02;   
			   if(camera.zoom < 0.30000037)camera.zoom = (float) 0.30000037;
			  }
			  
			  
			  if(Gdx.input.isKeyPressed(Keys.LEFT) ) {
			   camera.position.x -= 1000 * dt;   
			   if(camera.position.x < -1000.67914)camera.position.x = (float) -616.67914;
			  }
			  else if(Gdx.input.isKeyPressed(Keys.RIGHT) ){
			   camera.position.x += 1000 * dt;   
			   if(camera.position.x > 1000.621)camera.position.x = (float) 6326.621;
			  }
			  else if(Gdx.input.isKeyPressed(Keys.UP) ){
			   camera.position.y += 1000 * dt;   
			   if(camera.position.y > 1000.0886)camera.position.y = (float) 2267.0886;
			  }
			  else if(Gdx.input.isKeyPressed(Keys.DOWN) ){
			   camera.position.y -= 1000 * dt;    
			   if(camera.position.y < -1000.99722)camera.position.y = (float) -183.99722;
			  }
		
	}


	public void resize(int width, int height) {
		port.update(width, height);
		
	}

	
	public void pause() {
		
		
	}

	
	public void resume() {
		
	}

	
	public void hide() {
		
		
	}

	
	public void dispose() {
		quadDireita1.dispose();
		quadEsquerda1.dispose();
		quadDireita2.dispose();
		quadEsquerda2.dispose();
		raiz.dispose();
		quadValido.dispose();
			
		
	}
	
	public static void sair() {
		exit = true;		
	}
	
	public static void insereTela(String valor) {
		try {
			tiraPesquisa();
			if(count == existe.length){
				completo = true;
				throw new Exception();
			}
			int valorConvertido = Integer.parseInt(valor);//Gera uam exce��o caso o valor do conte�do n�o for um inteiro
				if(valorConvertido < 0) {
					negativo = true;
					throw new Exception();
				}
				if(existeP(valorConvertido)){
					throw new Exception();
				}
				
				existe[count] = valorConvertido;
				arvore.insere(valorConvertido);
				nos[cont] = arvore.ultimoNo;
		
				insereAux[cont] = valorConvertido;
				count++;
				cont++;
		}
		catch(NumberFormatException nf){
			JOptionPane.showMessageDialog(null, "Conte�do composto apenas por n�meros!", 
					  "Error", ERROR_MESSAGE);
		}
		catch(Exception e){
			if((negativo == false) && (completo == false)) {
				JOptionPane.showMessageDialog(null, "Elemento J� existe na estrutura!", 
						  "Error", ERROR_MESSAGE);
			}
			if(completo){
				completo = false;
				JOptionPane.showMessageDialog(null, "�rvore Completa!", 
						  "Error", ERROR_MESSAGE);
			}
			if(negativo) {
				negativo = false;
				JOptionPane.showMessageDialog(null, "Insira um valor maior que Zero!", 
						  "Error", ERROR_MESSAGE);
			}
		}	
	}
	


	public static void Pesquisa(String text) {
		pesquisa = text;
		try {			
			int valorConvertido = Integer.parseInt(text);
			for(int i = 0; i < existe.length; i++) {
				if(existe[i] == valorConvertido) {
					elementoExiste = true;
					break;
				}
			}
			
			arvore.buscaPesq(Integer.parseInt(text));
			
			for (int i = 0; i <= 20; i++) {
				font[i].setColor(Color.valueOf("b7b7b7"));
			}
			
			for (int i = 0; i < arvore.cont; i++) {
				for (int j = 0; j <= arvore.tamanho(); j++) {
					if (arvore.busca(arvore.tent[i]).getConteudo() == arvore.busca(insereAux[j]).getConteudo()) {
						font[j].setColor(Color.valueOf("7fff00"));
						Thread.sleep(1000);
						font[j].setColor(Color.valueOf("b7b7b7"));
						break;
					}
				}
			}
			
			if(valorConvertido < 0) throw new Exception();
			
			if(elementoExiste == false) {
				throw new Exception();
			}
			
			elementoExiste = false;
			
			for (int i = 0; i <= arvore.tamanho(); i++) {
				if (pesquisa.equals(String.valueOf(arvore.busca(insereAux[i]).getConteudo()))) {
					font[i].setColor(Color.valueOf("7fff00"));
					break;
				}
			}
			
		}
		catch(NumberFormatException nf){
			JOptionPane.showMessageDialog(null, "A estrutura apenas possui n�meros!", 
					  "Error", ERROR_MESSAGE);
		} 
		
		catch (Exception e) {
			elementoExiste = false;
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel achar o valor inserido!", 
					  "Error", ERROR_MESSAGE);
		}
		
		arvore.cont = 0;
		for (int i = 0; i < 20; i++) {
			arvore.tent[i] = 0;
		}
	}
	
	//Esse M�todo ser� iniciado a cada a��o da arvore de pesquisa bin�ria, zerando a marca��o da pesquisa
	public static void tiraPesquisa(){
		try{
			for(int i = 1; i <= arvore.tamanho(); i++){				
				font[i - 1].setColor(Color.valueOf("b7b7b7"));
					
			}
			pesquisa = null;
			}catch(Exception g){				
			}
	}
	
	/*
	 * M�todo que trata exce��o, apenas aceita a entrada de n�meros entre 1 e 20
	 */
	public static boolean isNumber(String text) throws Exception {
		int number = Integer.parseInt(text);
		if((number < 1) || (number > 20)){
			return false;
		}
			return true;
	}
	
	public static boolean existeP(int valor){
		for(int i = 0; i < existe.length; i++){
			if(existe[i] == valor){
				return true;
			}
		}
		return false;
	}
	
}
