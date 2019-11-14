package com.ningzhao.snakes;

public class Point {
	
	public final int x;
	public final int y;
	public final int myId;
	
	public Point(int x, int y, int id) {
		this.x = x;
		this.y = y;
		myId = id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getId() {
		return myId;
	}
}
