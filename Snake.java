package com.ningzhao.snakes;

import java.util.*;
import java.util.concurrent.Callable;

public class Snake implements Callable<Object>{
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	public static final int LEFT = 4;
	public static final int SLEEPTIME = 5;
	
	private AiAlgorithm algorithm;
	private PlayGround playGround;
	
	public List<Point> body; // snake body link list
	
	int direction;
	int myId;
	
	public Snake(PlayGround playGround, int x, int y, int direction, int myId) {
		this.myId = myId;
		this.direction = direction;
		this.playGround = playGround;
		body = new LinkedList<>();
		algorithm = new AiAlgorithm();
		Point p = new Point(x, y, myId);
		
		while (!initBody(p)) {
			Random rand = new Random();
			int newx = rand.nextInt(PlayGround.WIDTH);
			int newy = rand.nextInt(PlayGround.HEIGHT);
			
			this.direction = rand.nextInt(4) + 1;
			p = new Point(newx, newy, myId);
		}
	}
	
	private boolean initBody(Point p) {
		int state = playGround.getPoint(p, myId);
		while (true) {
			if (state == 100) {
				body.add(0, p);
				break;
			}
			else if (state == myId) {
				body.add(0, p);
				break;
			}
			else if (state == 101) {
				body.add(0, p);
				p = newHead();
				body.remove(0);
			}
		}
		
		algorithm.calculateDir();
		p = newHead();
		state = playGround.getPoint(p, myId);
		
		int countFalse = 0;
		
		while (true) {
			if (state == 100) {
				body.add(0, p);
				break;
			}
			else if (state == myId) {
				body.add(0, p);
				break;
			}
			else if (state == 101) {
				direction = direction % 4 + 1;
				countFalse++;
				if (countFalse == 4) {
					playGround.releasePoint(body.get(0), myId);
					body.remove(0);
					break;
				}
			}
		}
		if (body.size() == 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private class AiAlgorithm{
		
		public void calculateDir() {
			Point tempFood = playGround.getFood();
			
			Point tempHead = body.get(0);
			
			int oldDir = direction;
			
			if (tempFood.getX() < tempHead.getX()) {
				direction = LEFT;
			}
			else if (tempFood.getX() > tempHead.getX()) {
				direction = RIGHT;
			}
			else {
				if (tempFood.getY() < tempHead.getY()) {
					direction = UP;
				}
				else if (tempFood.getY() > tempHead.getY()) {
					direction = DOWN;
				}
			}
			
			if (oldDir + 2 == direction || oldDir - 2 == direction) {
				direction = direction % 4 + 1;
			}
		}
	}

	@Override
	public Object call() {
		while (!Thread.interrupted()) {
			algorithm.calculateDir();
			int countFalse = 0;
			while (true) {
				Point p = newHead();
				int state = playGround.getPoint(p, myId);
				
				if (state == 100) {
					body.add(0, p);
					break;
				}
				else if (state == myId) {
					int last = body.size() - 1;
					playGround.releasePoint(body.get(last), myId);
					body.remove(last);
					body.add(0, p);
					break;
				}
				else if (state == 101) {
					direction = direction % 4 + 1;
					countFalse++;
				}
				
				if (countFalse == 4) {
					Collections.reverse(body);
					break;
				}
			}
			
			try {
				Thread.sleep(SLEEPTIME);
			} catch (InterruptedException e) {
				return new Object();
			}
		}
		return new Object();
	}
	
	private Point newHead()
	{
		int x = body.get(0).getX();
		int y = body.get(0).getY();
		
		switch(direction) {
		case UP:
			if (y > 0) {
				y = y - 1;
			}
			else {
				y = 99;
			}
			break;
		
		case DOWN:
			y = y % 99 + 1;
			break;
		case LEFT:
			if (x > 0) {
				x = x - 1;
			}
			else {
				x = 99;
			}
			break;
		case RIGHT:
			x = x % 99 + 1;
			break;
		}

		return new Point(x, y, myId);
	}
}
