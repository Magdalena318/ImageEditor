/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_project;

import java.awt.Point;

/**
 *
 * @author root
 */
public class Edge {
    Point start;
    Point end;
    int y_min;
    int y_max;
    int cur_x;
    
    Edge(Point _start, Point _end){
        start = _start;
        end = _end;
        if(start.y > end.y){
            y_max = start.y;
            y_min = end.y;
        } else{
            y_min = start.y;
            y_max = end.y;
        }
    }
    
    @Override 
    public String toString(){
        return "Y_min: " + y_min + "Y_max " + y_max + "Start: x:" + start.x + " Y:" + start.y + "end: x:" + end.x + " Y:" + end.y + "cur_x: " + cur_x;
    }
}
