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
public class myColor {

    public int R;
    public int G;
    public int B;
    /**
     *
     */
    public Point[] neighbors;
    
    myColor(int r, int g, int b){
        this.R = r;
        this.G = g;
        this.B = b;
    }
    
    public void add_neighbor(Point n){
        int len = 0;
        if(neighbors != null){
            len = neighbors.length;
            Point[] tmp = new Point[len];
        
            for(int i = 0; i < len; i++){
                tmp[i] = neighbors[i];
            }
            
            neighbors = new Point[len + 1];
        
            for(int i = 0; i < len; i++){
                neighbors[i] = tmp[i];
            }
        } else{                   
            neighbors = new Point[len + 1];
        }
        
        
        neighbors[len] = n;
    }
    
    public void clean_neighbors(){
        neighbors = null;
    }
    
}
