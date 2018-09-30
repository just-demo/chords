package edu.self.web.tags;

import java.util.Comparator;

enum FretComparator implements Comparator<Integer> {
	MIN(-1), MAX(1);

	private int target;

	FretComparator(int target) {
		this.target = target;
	}

	@Override
	public int compare(Integer o1, Integer o2) {
		if (o1 != null && o2 != null) {
			return o1.compareTo(o2);
		}
		return o1 != null ? target : (o2 != null ? -target : 0);
	}
}
