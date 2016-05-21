package com.lehanh.pama.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CommonUtils {
    public static final <K extends Comparable<K>, V> TreeMap<K, V> sortMapByKey(Map<K, V> mapToSort, boolean reversed) {
        if (mapToSort == null) {
            return null;
        }
        TreeMap<K, V> result = new TreeMap<K, V>();
        List<K> listKey = new LinkedList<K>(mapToSort.keySet());
        Collections.sort(listKey, new Comparator<K>() {

			@Override
			public int compare(K k1, K k2) {
				if (reversed) {
					return k2.compareTo(k1);
				} else {
					return k1.compareTo(k2);
				}
			}

		});
        for (K key : listKey) {
            result.put(key, mapToSort.get(key));
        }
        return result;
    }

    public static final <K> ArrayList<K> removeArrayDuplicate(K[] dataArray) {
        Set<K> dataSet = new HashSet<K>(Arrays.asList(dataArray));
        return new ArrayList<>(dataSet);
    }


    public static List<?> subList(final List<?> list, final int limit) {
        final AtomicInteger counter = new AtomicInteger();
        return list.stream()
                .filter(i -> counter.incrementAndGet() == limit || counter.get() < limit)
                .collect(Collectors.toList());
    }

}
