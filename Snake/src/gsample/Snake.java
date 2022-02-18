package gsample;
import javax.swing.*;
//Nos permite interactuar con las teclas y ventanas 
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//Para el punto en el plano 
import java.awt.Point;
public class Snake extends JFrame {
	
	/* ----------------------------------------------------------- VARIABLES GLOBALES ----------------------------------------------------------- */
	//	TAMAÑO DE VENTANA
	int widht = 640;
	int height = 480;
	
	Point snake;
	Point food;
	
	boolean gameOver = false;
	
	ArrayList<Point> lista = new ArrayList<Point>();
	
	// TAMAÑO DEL PUNTO
	int widhtPoint = 10;
	int heightPoint = 10;
	
	
	ImagenSnake imagenSnake;
	
	long frecuencia = 50;
	
	int direccion = KeyEvent.VK_LEFT;
		
	public static void main(String[] args) throws Exception {		
		Snake s = new Snake();
	}
	
	/* ----------------------------------------------------------- CLASE SNAKE ----------------------------------------------------------- */

	public Snake() {
		setTitle("Snake");
		setSize(widht,height);
		
		/* PARA POSICIONAR LA VENTANA DEL JUEGO EN EL CENTRO DE LA PANTALLA  */
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//TOMA LAS DIMENSIONES DE LA PANTALLA DE LA COMPU
		this.setLocation(dim.width/2-widht/2, dim.height/2-height/2);//CENTRA LA VENTANA

		//PARA CERRAR LA APLICACIÓN
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Teclas teclas = new Teclas();
		this.addKeyListener(teclas);
		
		
		snake = new Point(widht/2,height/2);
		
		imagenSnake = new ImagenSnake();
		this.getContentPane().add(imagenSnake);
		
		setVisible(true);
		
		startGame();
		
		Momento momento = new Momento();
		Thread trid = new Thread(momento);
		trid.start();
	}
	
	/* ----------------------------------------------------------- METODOS ----------------------------------------------------------- */
	
	public void actualizar() {
		//PARA VOLVER DIBUJAR LOS GRÁFICOS
		imagenSnake.repaint();
		
		lista.add(0,new Point(snake.x,snake.y));
		lista.remove(lista.size()-1);
		
		for (int i=1; i<lista.size();i++) {
			Point p = lista.get(i);
			if(snake.x == p.x && snake.y == p.y) {
				gameOver = true;
			}
		}
		// si se cumple esta condición, indica que esta dentro de la zona del evento 
		if(snake.x > (food.x - 10) && (snake.x < (food.x + 10)) && snake.y > (food.y - 10) && (snake.y < (food.y + 10)) ) {
			lista.add(0,new Point(snake.x, snake.y));//CADA VEZ QUE COMA, SE AGREGA A LA LISTA UN NUEVO PUNTO EN LA POSICIÓN CERO
			
			crearComida();
		}
	}
	
	//FORMA DE LA SERPIENTE 
	public class ImagenSnake extends JPanel{
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(new Color(0,0,255));

			for (int i = 0; i < lista.size();i++) {
				Point p = (Point)lista.get(i);
				g.fillOval(p.x, p.y, widhtPoint, widhtPoint);
			}
			
			g.setColor(new Color(255,0,0));
			g.fillOval(food.x, food.y, widhtPoint, widhtPoint);
			
			if (gameOver) {
				g.drawString("Game OVER", 200, 320);
			}
		}
	}
	
	//PARA MOVER LA SERPIENTE
	public class Teclas extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}else if (e.getKeyCode() == KeyEvent.VK_UP) {
				if(direccion != KeyEvent.VK_DOWN) {
					direccion = KeyEvent.VK_UP;
				}
			}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(direccion != KeyEvent.VK_UP) {
					direccion = KeyEvent.VK_DOWN;
				}
			}else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(direccion != KeyEvent.VK_RIGHT) {
					direccion = KeyEvent.VK_LEFT;
				}
			}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if(direccion != KeyEvent.VK_LEFT) {
					direccion = KeyEvent.VK_RIGHT;
				}
			}
		}
	}
	
	//EL JUEGO SE VA A REFRESCAR CADA 20 SEGUNDOS 
	public class Momento extends Thread{
		long last = 0;
		public void run() {
			while (true) {
				if ((java.lang.System.currentTimeMillis() - last ) > frecuencia) {
					
					if(!gameOver) {
						if(direccion == KeyEvent.VK_UP) {
							snake.y = snake.y - heightPoint;
							
							if(snake.y > height) {
								snake.y = 0;
							}
							
							if(snake.y < 0) {
								snake.y = height - heightPoint;
							}
							
							
						}else if (direccion == KeyEvent.VK_DOWN) {
							snake.y = snake.y + heightPoint;
							
							if(snake.y > height) {
								snake.y = 0;
								
							}if (snake.y < 0 ) {
								snake.y = height - heightPoint;
							}
							
						}else if (direccion == KeyEvent.VK_RIGHT) {
							snake.x = snake.x + widhtPoint;
							
							if(snake.x > widht) {
								snake.x = 0;
								
							}if (snake.x < 0 ) {
								snake.x = widht - widhtPoint;
							}
						}else if (direccion == KeyEvent.VK_LEFT) {
							snake.x = snake.x - widhtPoint;
							
							if(snake.x > widht) {
								snake.x = 0;
								
							}if (snake.x < 0 ) {
								snake.x = widht + widhtPoint;
							}
						}
					}
					
					
					actualizar();
					last = java.lang.System.currentTimeMillis();
					
				}
			}
		}
	}
	
	public void startGame() {
		food = new Point(200,200);
		snake = new Point(widht/2, height/2);
		
		lista  = new ArrayList<Point>();//Instanciar 
		lista.add(snake);
		
		crearComida();
	}
	
	//MÉTODO PARA CREAR LA COMIDA EN DIFERENTES PUNTOS DEL PLANO CARTESIANO
	public void crearComida() {
		Random rnd = new Random();
		food.x = rnd.nextInt(widht - 15);
		
		if((food.x%5) > 0 ) {
			food.x = food.x - (food.x%5);
		}
		
		if (food.x < 5) {
			food.x += 10;
		}
		
		food.y = rnd.nextInt(height);
		if((food.y %5) > 0 ) {
			food.y = food.y - (food.y %5);
		}
		
		if (food.y < 5) {
			food.y += 10;
		}
		
	}

}
