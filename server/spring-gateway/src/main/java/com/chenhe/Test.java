package com.chenhe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chenhe
 * @date 2020-03-21 23:44
 * @desc
 */
public class Test {
    public static void main(String[] args) {

        Node head = makeNode(19,6);
        Node first = head;
        Node second = head;

        int firstStep=0;
        int secondStep=0;
        int i=0;
        boolean flag = false;
        while (first.next != null) {
            first = first.next.next;
            second = second.next;
            firstStep++;
            firstStep++;
            secondStep++;

            if (first == second){
                System.out.println("firstStep = "+firstStep+",secondStep = "+secondStep+",total = "+(firstStep-secondStep -1 )+": 有环");
                if (flag)
                    break;
                flag=true;
                second=head;
                secondStep=0;
            }
        }

    }

    private static Node makeNode(int length,int xPoint){
        List<Node> list = new LinkedList<>();

        for (int i=1;i<=length; i++){
            list.add(new Node(i,null));
        }

        for (int i=0;i<length-1;i++){
            list.get(i).next=list.get(i+1);
        }
        list.get(length-1).next=list.get(xPoint-1);

        return list.get(0);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Node {
        Object value;
        Node next;
    }
}
