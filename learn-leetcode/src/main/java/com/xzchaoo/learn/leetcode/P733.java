package com.xzchaoo.learn.leetcode;

import java.util.LinkedList;

/**
 * created by xzchaoo at 2017/11/26
 *
 * @author xzchaoo
 */
public class P733 {
	private static final int[] dx = {0, 0, 1, -1};
	private static final int[] dy = {1, -1, 0, 0};

	public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
		int n = image.length;
		int m = image[0].length;
		int[][] map = image.clone();
		LinkedList<int[]> q = new LinkedList<>();
		boolean[][] marked = new boolean[image.length][image[0].length];
		marked[sr][sc] = true;
		q.add(new int[]{sr, sc});
		while (!q.isEmpty()) {
			int[] pos = q.pop();
			map[pos[0]][pos[1]] = newColor;
			for (int i = 0; i < 4; ++i) {
				int xx = pos[0] + dx[i];
				int yy = pos[1] + dy[i];
				if (xx >= 0 && xx < n && yy >= 0 && yy < m && !marked[xx][yy]) {
					marked[xx][yy] = true;
					q.push(new int[]{xx, yy});
				}
			}
		}
		return map;
	}
}
