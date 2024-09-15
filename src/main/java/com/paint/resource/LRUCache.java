package com.paint.resource;

import java.util.HashMap;
import java.util.LinkedList;

public class LRUCache<T> { // LRUCache uses generics to account for undo/redo & tab state management
	private LinkedList<T> doublyLinkedList;
	private HashMap hashMap;

	public LRUCache() {
		this.doublyLinkedList = new LinkedList<T>();
		this.hashMap = new HashMap<>();
	}

	// Most recent item accessed is @ the head of DLL
	public void addItem(T item) {
		// Check length of DLL
		if (this.doublyLinkedList.isEmpty()) {
			// Initialize
			this.doublyLinkedList.addFirst(item);
		} else {
			// Add to end
			this.doublyLinkedList.addLast(item);
		}
	}

	public LinkedList<T> getDoublyLinkedList() {
		return this.doublyLinkedList;
	}

	public HashMap getHashMap() {
		return this.hashMap;
	}
}
