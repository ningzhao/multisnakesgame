package com.ningzhao.snakes;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


public class PlayGround {
	static final int WIDTH = 100;
	static final int HEIGHT = 100;
	static final int FOOD = 100;//the index in ground, FOOD occupied ground[100].
	static final int FAIL = FOOD + 1;
	
	static volatile int countFood = 0;

	public List<Point> occupiedList;

	private AtomicInteger[][] ground;//0: free; 1: snake1 occupied; 2: snake2 occupied; 100: food occupied
	
	private volatile Point food;

	public PlayGround() {
		ground = new AtomicInteger[WIDTH][HEIGHT];
		food = new Point(WIDTH / 2, HEIGHT / 2, 100);
		occupiedList = Collections.synchronizedList(new LinkedList<>());
		occupiedList.add(food);
		
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				ground[j][i] = new AtomicInteger(0);
			}
		}
		ground[food.x][food.y].set(FOOD);
	}
	
	public Point getFood() {
		return food;
	}
	
	public synchronized boolean getFood(Point p) {
		if (p.getX() == food.getX() && p.getY() == food.getY()) {
			if (ground[p.getX()][p.getY()].compareAndSet(FOOD, p.myId)) {
				randomFood();
				countFood++;
				return true;
			}
		}
		return false;
	}
	
	public int getPoint(Point p, int update) {
		if (getFood(p)) {
			return FOOD;
		}
		else {
			if (ground[p.getX()][p.getY()].compareAndSet(0, update)) {
				occupiedList.add(p);
				return update;
			}
		}
		return FAIL;
	}
	
	public void releasePoint(Point p, int expect) {
		ground[p.getX()][p.getY()].compareAndSet(expect, 0);
		occupiedList.remove(p);
	}
	
	private void randomFood() {
		Random randomX = new Random();
		Random randomY = new Random();
		
		while (true) {
			int x = randomX.nextInt(WIDTH);
			int y = randomY.nextInt(HEIGHT);
			if (ground[x][y].compareAndSet(0, 100)) {
				occupiedList.remove(food);
				Point newFood = new Point(x, y, 100);
				food = newFood;
				occupiedList.add(0, food);
				break;
			}
		}
	}
}
