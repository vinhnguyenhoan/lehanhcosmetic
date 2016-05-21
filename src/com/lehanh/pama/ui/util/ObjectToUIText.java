package com.lehanh.pama.ui.util;

public interface ObjectToUIText<T, Id> {

	String showUI(T object);
	
	Id getIdForUI(T object);
}
