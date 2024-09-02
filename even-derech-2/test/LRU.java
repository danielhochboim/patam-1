package test;

import java.util.HashMap;

public class LRU implements CacheReplacementPolicy{
    // implementing a node in a linked list
    private class Node{
        String word;
        Node prev, next;
        Node(String word){
            this.word = word;
            this.prev = null;
            this.next = null;
        }
    }
    // there is a linked list of words and hash map to get every node in the linked list in O(1)
    private HashMap<String,Node> map;
    private Node head, tail;

    public LRU(){
        this.map = new HashMap<>();
        this.head = null;
        this.tail = null;
    }
    private void addToFront(Node node){
        if(head == null){
            head = node;
            tail = node;
        }
        else{
        node.next = head;
        head.prev = node;
        head = node;
        }
    }
    private void removeFirst(){
        map.remove(head.word);
        Node temp = head;
        head.next.prev = null;
        head = head.next;
        temp.next = null;
    }
    private void removeLast(){
        if(tail == null){
            return;
        }
        if(head == tail){
            map.remove(tail.word);
            head = null;
            tail = null;
            return;
        }
        map.remove(tail.word);
        Node temp = tail;
        tail = tail.prev;
        tail.next = null;
        temp.prev = null;

    }
    private void removeNode(Node node){
        map.remove(node.word);
        if(node == tail){
            removeLast();
        }
        else if(node == head){
            removeFirst();
        }
        else{
        node.next.prev = node.prev;
        node.prev.next = node.next;
        }
        node.next = null;
        node.prev = null;
    }
    private void moveToFront(Node node){
        removeNode(node);
        addToFront(node);
    }

    @Override
    public void add(String word) {
        if(this.map.containsKey(word)){
            moveToFront(map.get(word));
        }
        else{
            Node node = new Node(word);
            addToFront(node);
            map.put(word, node);
        }
        
    }
    @Override
    public String remove() {
        if(tail == null){
            return null;
        }
        Node temp = tail;
        removeLast();
        return temp.word;
    }

}
