package com.ningzhao.snakes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SnakeTest {
	
	public static void main(String[] args) throws ExecutionException {
		
		final int WIDTH = 100;
		final int HEIGHT = 100;
		
		int countFood = 0;
		final int maxFoodNum = 50000;
		int snakeNum = 10;
		
		PlayGround playGround = new PlayGround();
		
		ExecutorService snakeExecutor = Executors.newCachedThreadPool();
		
		List<Future<Object>> snakeFuture = new LinkedList<Future<Object>>();
		
		CreateUI ui = new CreateUI(playGround, snakeNum + 1);
		
		Random rand = new Random();
		
		for (int i = 0; i < snakeNum; i++) {
			int x = rand.nextInt(WIDTH);
			int y = rand.nextInt(HEIGHT);
			int d = rand.nextInt(4) + 1;

			Future<Object> future = snakeExecutor.submit(new Snake(playGround, x, y, d, i + 1));
			snakeFuture.add(future);
		}
		
		
		try {
			while (countFood <= maxFoodNum) {
				Thread.sleep(1);
				ui.repaint();
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < snakeNum; i++) {
			snakeFuture.get(i).cancel(true);
		}
		
		ui.setVisible(false);
		ui.dispose();
		System.exit(0);
	}
}
