package fr.dz.sherizi.service.share;

public class SharedData<T> {

	// Datas to share
	private T data;

	/**
	 * Constructor
	 */
	public SharedData(T data) {
		this.data = data;
	}

	/*
	 * GETTERS & SETTERS
	 */

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
