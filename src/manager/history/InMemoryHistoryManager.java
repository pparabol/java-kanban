package manager.history;

import model.Task;
import util.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> history;
    private final Map<Integer, Node<Task>> tasksMap;

    public InMemoryHistoryManager() {
        history = new CustomLinkedList<>();
        tasksMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            history.removeNode(tasksMap.get(task.getId()));
        }
        tasksMap.put(task.getId(), history.linkLast(task));
    }

    @Override
    public void remove(int id) {
        history.removeNode(tasksMap.get(id));
        tasksMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }


    private static class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        private Node<T> linkLast(T element) {
            final Node<T> newNode = new Node<>(element);
            if (this.head == null) {
                this.head = newNode;
                this.tail = this.head;
            } else {
                newNode.setPrev(this.tail);
                this.tail.setNext(newNode);
                this.tail = newNode;
            }
            size++;
            return newNode;
        }

        private List<T> getTasks() {
            List<T> result = new ArrayList<>(size);
            if (this.head != null) {
                Node<T> currentNode = this.head;
                while (currentNode != null) {
                    result.add(currentNode.getData());
                    currentNode = currentNode.getNext();
                }
            }
            return result;
        }

        private void removeNode(Node<T> node) {
            if (this.head == null || node == null) {
                System.out.println("Невозможно удалить узел");
                return;
            }
           if (node == this.head) {
                this.head = node.getNext();
            }
            if (node == this.tail) {
                this.tail = node.getPrev();
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            }
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            }
        }
    }
}
