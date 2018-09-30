package edu.self.bean;

public class PageScroller {
	private int first, last, min, max, current;
	
	public PageScroller(int itemsCount, int currentPage, int intemsPerPage, int linksCount) {
		int pagesCount = (int) Math.ceil((double) itemsCount / intemsPerPage);
		first = 1;
		last = pagesCount;
		min = Math.max(first + 1, currentPage - (linksCount / 2 - 1));
		max = Math.min(last - 1, min + linksCount - 3);
		if (max - min < linksCount - 3) {
			min = Math.max(first + 1, max - (linksCount - 3));
		}
		current = currentPage;
	}
	
	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getCurrent() {
		return current;
	}
}
