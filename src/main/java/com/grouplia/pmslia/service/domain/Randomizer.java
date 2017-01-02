package com.grouplia.pmslia.service.domain;

import java.util.Arrays;
import java.util.Random;

import org.springframework.util.Assert;

public class Randomizer {

	private Random random = new Random(System.currentTimeMillis());

	private int basketSize;
	private int elementCount;

	public Randomizer(int _basketSize, int _elementCount) {
		Assert.isTrue(_basketSize <= _elementCount);
		basketSize = _basketSize;
		elementCount = _elementCount;
	}

	public int[] nextBasket() {
		int[] keys = new int[basketSize];
		int i = 0;
		while (i < keys.length) {
			int key = random.nextInt(elementCount);
			boolean found = false;
			int j = i - 1;
			while (j >= 0 && !found) {
				found = (keys[j] == key);
				j--;
			}
			if (!found) {
				keys[i] = key;
				i++;
			}
		}
		Arrays.sort(keys);
		return keys;
	}

}
