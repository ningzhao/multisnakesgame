package com.ningzhao.snakes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class CreateUI extends JFrame{
	static final int NODESIZE = 6;
	
	final int WIDTH = NODESIZE * PlayGround.WIDTH;
	final int HEIGHT = NODESIZE * PlayGround.HEIGHT;
	
	BufferedImage offersetImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
	
	PlayGround playGround;
	
	Color colors[];
	
	public CreateUI(PlayGround pg, int colorNums) {
		playGround = pg;
		colors = new Color[colorNums];
		Random rand = new Random();
		
		for (int i = 0; i < colorNums; i++) {
			int r = rand.nextInt(255);
			int g = rand.nextInt(255);
			int b = rand.nextInt(255);
			colors[i] = new Color(r, g, b);
		}

		this.setTitle("Crazy Snakes");
		this.setBounds(100, 30, WIDTH + 15, HEIGHT + 30);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) offersetImage.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, WIDTH, HEIGHT);
		drawGround(g2d, playGround);
		g.drawImage(offersetImage, 7, 25, null);
		
	}
	
	public void drawGround(Graphics2D g2d, PlayGround playGround) {
		List<Point> occupiedList = new LinkedList<>();
		
		synchronized (playGround.occupiedList) {
			occupiedList.addAll(playGround.occupiedList);
		}
		
		for (int i = 0; i < occupiedList.size(); i++) {
			Point p = occupiedList.get(i);
			g2d.setColor(colors[p.getId() % 100]);
			g2d.drawRect(p.getX() * NODESIZE, p.getY() * NODESIZE, NODESIZE, NODESIZE);
			if (p.myId == 100) {
				g2d.setColor(Color.BLACK);
				g2d.fillRect(p.getX() * NODESIZE, p.getY() * NODESIZE, NODESIZE, NODESIZE);
			}
		}
	}
}
