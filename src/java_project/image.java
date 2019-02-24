/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_project;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author root
 */
public class image {
    int w;
    int h;
    int [][][] colors;
    int [][][] original;
    
    int[][] av_mask = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    int[][] gauss_mask = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    int[][] sharp_mask = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
    int[][] emboss_mask = {{-1, -1, 0}, {-1, 1, 1}, {0, 1, 1}};
    int[][] edge_mask = {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
    int[][] custom_mask;
    
    double av_divisor = 9;
    double gauss_divisor = 16;
    double sharp_divisor = 1;
    double emboss_divisor = 1;
    double edge_divisor = 1;
    double custom_divisor;
    
    double av_offset = 0;
    double gauss_offset = 0;
    double sharp_offset = 0;
    double emboss_offset = 0;
    double edge_offset = 127;
    double custom_offset;
    
    int c_row_num;
    int c_col_num;
    
    int[][] ordered_two = {{1, 3}, {4, 2}};
    int[][] ordered_three = {{3, 7, 4}, {6, 1, 9}, {2, 8, 5}};
    int[][] ordered_four =  {{1, 9, 3, 11}, {13, 5, 15, 7}, {4, 12, 2, 10}, {16, 8, 14, 5}};
    int[][] ordered_six = {{9, 25, 13, 11, 27, 15}, {21, 1, 33, 23, 3, 35}, {5, 29, 17, 7, 31, 19}, {12, 28, 16, 10, 26, 14}, {24, 4, 36, 22, 2, 34}, {8, 32, 20, 6, 30, 18}};
    
    image() {
        w = 862;
        h = 477;
        colors = new int[3][h][w];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                        colors[0][i][j] = 255;
                        colors[1][i][j] = 255;
                        colors[2][i][j] = 255;
            }
        }       
    }
    
    image(int width, int height) {
        w = width;
        h = height;
        colors = new int[3][h][w];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                        colors[0][i][j] = 255;
                        colors[1][i][j] = 255;
                        colors[2][i][j] = 255;
            }
        }       
    }
    
    image(File img) throws IOException{
        BufferedImage image = ImageIO.read(img);

        w = image.getWidth();
        h = image.getHeight();
        colors = new int[3][h][w];

        int[] dataBuffInt = image.getRGB(0, 0, w, h, null, 0, w); 

        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){

                        Color c = new Color(dataBuffInt[i*w+j]);

                        colors[0][i][j] = c.getRed();
                        colors[1][i][j] = c.getGreen();
                        colors[2][i][j] = c.getBlue();
            }
        }        
    }
    
    
    /*public ImageIcon getImage(){
        BufferedImage img;
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                        Color newColor = new Color(colors[0][i][j], colors[1][i][j], colors[2][i][j]);
                        img.setRGB(j, i, newColor.getRGB());
                }
        }
        ImageIcon out = new ImageIcon(img);
        return out;
    }*/
    
     public BufferedImage getBufferedImage(){
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Color newColor = new Color(colors[0][i][j], colors[1][i][j], colors[2][i][j]);
                image.setRGB(j, i, newColor.getRGB());
            }
        }
        return image;
    }
    
    public BufferedImage Resize(int width, int height){
        int new_w;
        int new_h;
        if(w <= width && h <= height){
            return this.getBufferedImage();
        } else {
            double w_coeff = (double)width/w;
            double h_coeff = (double)height/h;
            if(w_coeff < h_coeff){
                new_w = (int)(w * w_coeff);
                new_h = (int)(h * w_coeff);
            } else {
                new_h = (int)(h * h_coeff);
                new_w = (int)(w * h_coeff);
            }
        }
        BufferedImage bi = new BufferedImage(new_w, new_h, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(this.getBufferedImage(), 0, 0, new_w, new_h, null);
        g2d.dispose();
        return bi;
    }   
    
    void Clean(){
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                        colors[0][i][j] = 255;
                        colors[1][i][j] = 255;
                        colors[2][i][j] = 255;
            }
        }  
    }
    
    
    int Truncate(double value){
		if(value > 255){
			value = 255;		
		} else if(value < 0){
			value = 0;
		}
		return (int)(value);
    }
    
    public void putPixel(int y, int x, int R, int G, int B){
        this.colors[0][y][x] = R;
        this.colors[1][y][x] = G;
        this.colors[2][y][x] = B;
    }
    
    void display_kernel(String mode){
        int[][] kernel = new int[3][3];
        switch(mode){
            case "average": 
                kernel = av_mask;
                break;
            case "gauss":
                kernel = gauss_mask;
                break;
            case "sharp":
                kernel = sharp_mask;
                break;
            case "emboss":
                kernel = emboss_mask;
                break;
            default: 
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        kernel[i][j] = 0;
                    }
                }
                break;               
        }
        
    }
    
    
    public void grayscale(){
        int value;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                value = (int) ((colors[0][i][j] + colors[1][i][j] + colors[2][i][j]) / 3);
                putPixel(i, j, value, value, value);
            }
        }
    }
    
    public void original(){
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                putPixel(i, j, original[0][i][j], original[1][i][j], original[2][i][j]);
            }
        } 
    }
    
    public void negation(){
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                putPixel(i, j, (int)(255 - colors[0][i][j]), (int)(255 - colors[1][i][j]), (int)(255 - colors[2][i][j]));
            }
        } 
    }
    public void contrast(double contrast){
        double f = (259 * (contrast + 255))/(255 * (259 - contrast));
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                putPixel(i, j, Truncate((colors[0][i][j] - 128)  * f + 128), Truncate((colors[1][i][j] - 128)  * f + 128), Truncate((colors[2][i][j] - 128)  * f + 128));
            }
        } 
    }
    
    public void brightness(double brightness){
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                    colors[0][i][j] = Truncate(brightness + colors[0][i][j]);
                    colors[1][i][j] = Truncate(brightness + colors[1][i][j]);
                    colors[2][i][j] = Truncate(brightness + colors[2][i][j]);
            }
        } 
    }
    
    public void gamma_correction(double gamma){
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                    double in_red = (double)colors[0][i][j]/255;
                    double in_green = (double)colors[1][i][j]/255;
                    double in_blue = (double)colors[2][i][j]/255;
                    colors[0][i][j] = Truncate(Math.pow(in_red, (gamma)) * 255);
                    colors[1][i][j] = Truncate(Math.pow(in_green, (gamma)) * 255);
                    colors[2][i][j] = Truncate(Math.pow(in_blue, (gamma)) * 255);
            }
        } 
    }
    
    public void average_threshold(){
        //this.grayscale();
        double sum = 0;
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                sum += colors[0][i][j];
            }
        }
        double threshold = sum / (w * h);
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                if(colors[0][i][j] >= threshold){
                    colors[0][i][j] = 255;
                    colors[1][i][j] = 255;
                    colors[2][i][j] = 255;
                } else {
                    colors[0][i][j] = 0;
                    colors[1][i][j] = 0;
                    colors[2][i][j] = 0;
                }                
            }
        } 
    }
    
    public void random_threshold(int k){
        int threshold;
        Random rand = new Random();
        
        int[] levels = new int[k];
        for(int i = 0; i < k; i++){
            levels[i] = i * (255 / (k - 1));
        } 
        
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                int upperbound, lowerbound;
                for(int count = 0; count < k - 1; count++){
                    if(colors[0][i][j] >= levels[count] && colors[0][i][j] <= levels[count + 1]){
                        lowerbound = levels[count];
                        upperbound = levels[count + 1];
                        
                        threshold = rand.nextInt(upperbound - lowerbound);
                        
                        if(colors[0][i][j] >= lowerbound + threshold){
                            colors[0][i][j] = upperbound;
                            colors[1][i][j] = upperbound;
                            colors[2][i][j] = upperbound;
                        } else {
                            colors[0][i][j] = lowerbound;
                            colors[1][i][j] = lowerbound;
                            colors[2][i][j] = lowerbound;
                        }    
                        
                    }
                }               
            }
        } 
    }
    
    public void ordered_threshold(int k, int n){
        double threshold;
        int[][] matrix;
        
        switch(n){
            case 2:
                matrix = ordered_two;
                break;
            case 3:
                matrix = ordered_three;
                break;
            case 4: 
                matrix = ordered_four;
                break;
            case 6:
                matrix = ordered_six;
                break;
            default:
                return;
        }
    
        int[] levels = new int[k];
        for(int i = 0; i < k; i++){
            levels[i] = i * (255 / (k - 1));
        } 
        
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                int upperbound, lowerbound;
                for(int count = 0; count < k - 1; count++){
                    if(colors[0][i][j] >= levels[count] && colors[0][i][j] <= levels[count + 1]){
                        lowerbound = levels[count];
                        upperbound = levels[count + 1];
                        
                        double t = matrix[i%n][j%n]/(Math.pow(n,2) + 1);
                        threshold = lowerbound * (1 - t) + upperbound * t;
                        
                        if(colors[0][i][j] >= threshold){
                            putPixel(i, j, upperbound, upperbound, upperbound);
                        } else {
                            putPixel(i, j, lowerbound, lowerbound, lowerbound);
                        }    
                        
                    }
                }               
            }
        } 
    }
    
    public void k_means(int k){
        Random rand = new Random();
        
        myColor[] palette = new myColor[k];
        
        for(int i = 0; i < k; i++){
            int width = rand.nextInt(w);
            int height = rand.nextInt(h);
            
            palette[i] = new myColor(colors[0][height][width], colors[1][height][width], colors[2][height][width]);
        }
        
        int[][] point_neighbors = new int[h][w];
        //myColor[][] all_colors = new 
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                double dist = 500;
                int idx = 0;
                
                for(int count = 0; count < k; count++){
                    if(distance(colors[0][i][j], colors[1][i][j], colors[2][i][j], palette[count]) < dist){
                        dist = distance(colors[0][i][j], colors[1][i][j], colors[2][i][j], palette[count]);
                        idx = count;
                    }
                }
                
                Point cur = new Point(i, j);
                palette[idx].add_neighbor(cur);
                point_neighbors[i][j] = idx;
            }
        }

        myColor[] old_palette = new myColor[k];
        System.arraycopy(palette, 0, old_palette, 0, k);
        
        System.out.println("FIRST FIND CENTER");
        find_gravity_center(k, palette);
        
        
        while(find_neighbors(k, palette, point_neighbors, old_palette)){
            System.out.println("NEXT FIND CENTER");
            System.arraycopy(palette, 0, old_palette, 0, k);
            find_gravity_center(k, palette);
        }
        
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                colors[0][i][j] = palette[point_neighbors[i][j]].R;
                colors[1][i][j] = palette[point_neighbors[i][j]].G;
                colors[2][i][j] = palette[point_neighbors[i][j]].B;
            }
        }
        System.out.println("Finish");
    }
    
    private boolean find_neighbors(int k, myColor[] palette, int[][] point_neighbors, myColor[] old_palette){
        boolean result = false;
        
        for(int i = 0; i < k; i++){
            palette[i].clean_neighbors();
        }
        //Foreach point
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                double dist = 500;
                int idx = 0;
                
                //Find smallest diatance 
                for(int count = 0; count < k; count++){
                    if(distance(colors[0][i][j], colors[1][i][j], colors[2][i][j], palette[count]) < dist){
                        dist = distance(colors[0][i][j], colors[1][i][j], colors[2][i][j], palette[count]);
                        idx = count;
                    }
                }
                
                myColor old = old_palette[point_neighbors[i][j]];
                point_neighbors[i][j] = idx;
                
                
                Point cur = new Point(i, j);
                palette[idx].add_neighbor(cur);

                if(old.R != palette[point_neighbors[i][j]].R || old.G != palette[point_neighbors[i][j]].G || old.B != palette[point_neighbors[i][j]].B)
                    result = true;
                }
            }
        return result;
    }
    
    public void find_gravity_center(int k, myColor[] palette){
        //Calculate average for each group of colours 
        for (int i = 0; i < k; i++)
            {

                if (palette[i].neighbors == null) continue;
                int sr = 0;
                int sg = 0;
                int sb = 0;
                for(int n = 0; n < palette[i].neighbors.length; n++)
                {
                    sr += colors[0][palette[i].neighbors[n].x][palette[i].neighbors[n].y];
                    sg += colors[1][palette[i].neighbors[n].x][palette[i].neighbors[n].y];
                    sb += colors[2][palette[i].neighbors[n].x][palette[i].neighbors[n].y];
                    
                }

                palette[i].R = (int)(sr / palette[i].neighbors.length);                
                palette[i].G = (int)(sg / palette[i].neighbors.length);
                palette[i].B = (int)(sb / palette[i].neighbors.length);


            }
    }
    
    public double distance(int R, int G, int B, myColor color){
        int dr, dg, db;
        if(R > color.R)
            dr = R - color.R;
        else
            dr = color.R - R;
        if(G > color.G)
            dg = G - color.G;
        else
            dg = color.G - G;
        if(B > color.B)
            db = B - color.B;
        else
            db = color.B - B;
        
        return Math.sqrt(Math.pow(dr, 2) + Math.pow(dg, 2) + Math.pow(db, 2));
    }
    
    
    public void uniform_quantization(int k_R, int k_G, int k_B){
        int step_R = 255 / k_R;
        int step_G = 255 / k_G;
        int step_B = 255 / k_B;
        
        //Assigning borders and centers for each colour level
        int[] levels_R = new int[k_R];
        int[] centers_R = new int[k_R - 1];
        for(int i = 0; i < k_R; i++){
            levels_R[i] = (int)i * (255 / (k_R - 1));
            
            if(i > 0){
                centers_R[i - 1] = levels_R[i - 1] + Math.round((levels_R[i] - levels_R[i - 1]) / 2);
            }
        } 
        int[] levels_G = new int[k_G];
        int[] centers_G = new int[k_G - 1];
        for(int i = 0; i < k_G; i++){
            levels_G[i] = (int)i * (255 / (k_G - 1));
            if(i > 0){
                centers_G[i - 1] = levels_G[i - 1] + Math.round((levels_G[i] - levels_G[i - 1]) / 2);
            }
        } 
        int[] levels_B = new int[k_B];
        int[] centers_B = new int[k_B - 1];
        for(int i = 0; i < k_B; i++){
            levels_B[i] = (int)i * (255 / (k_B - 1));
            if(i > 0){
                centers_B[i - 1] = levels_B[i - 1] + Math.round((levels_B[i] - levels_B[i - 1]) / 2);
            }
        } 
        
        //Assigning each colour to it's center value
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                for(int count = 0; count < k_R - 1; count++){
                    if(colors[0][i][j] >= levels_R[count] && colors[0][i][j] <= levels_R[count + 1]){
                        colors[0][i][j] = centers_R[count];
                    }
                }
                for(int count = 0; count < k_G - 1; count++){
                    if(colors[1][i][j] >= levels_G[count] && colors[1][i][j] <= levels_G[count + 1]){
                        colors[1][i][j] = centers_G[count];
                    }
                }
                for(int count = 0; count < k_B - 1; count++){
                    if(colors[2][i][j] >= levels_B[count] && colors[2][i][j] <= levels_B[count + 1]){
                        colors[2][i][j] = centers_B[count];
                    }
                }
            }
        }
    }
    
    public void average_filter(){
        int[][][] copy = new int [3][h][w];
        for(int col = 0; col <= 2; col++){
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    copy[col][i][j] = colors[col][i][j]; 
                }
            }
        }
        for(int i = 0; i < h; i++){//for each pixel
            for(int j = 0; j < w; j++){
                int sum[] = new int[3]; //sum[0] - red colour, sum[1] - green, sum[2] - blue
                for(int s = 0; s < 3; s++){//initiate 
                        sum[s] = 0;
                        for (int k = i - 1; k <= i + 1; k++){//from minus one to plus
                            for (int t = j - 1; t <= j + 1; t++){
                                    int k1, t1;
                                    if(k < 0){
                                            k1 = 0;
                                    } else if(k >= h){
                                            k1 = h-1;
                                    } else{
                                            k1 = k;
                                    }
                                    if(t < 0){
                                            t1 = 0;
                                    } else if( t>= w){
                                            t1 = w-1;
                                    } else{
                                            t1 = t;
                                    }
                                    //System.out.println(" " +sum[0] + "green " + sum[1] + "blue " + sum[2]);
                                    sum[s] += copy[s][k1][t1];
                            }
                        }
                        colors[s][i][j] = Truncate((Double.valueOf(sum[s])/av_divisor) + av_offset);
                }
            }
        } 
    }
    
    public void gaussian_filter(){
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                int sum[] = new int[3];
                for(int s = 0; s < 3; s++){//initiate 
                        sum[s] = 0;
                        for (int k = -1; k < 2; k++){
                            for (int t = -1; t < 2; t++){
                                    int k1, t1;
                                    if(i + k < 0){
                                            k1 = 0;
                                    } else if(i + k >= h){
                                            k1 = h-1;
                                    } else{
                                            k1 = i + k;
                                    }
                                    if(j + t < 0){
                                            t1 = 0;
                                    } else if(j + t >= w){
                                            t1 = w-1;
                                    } else{
                                            t1 = j + t;
                                    }
                                    sum[s] += gauss_mask[k + 1][t + 1] * colors[s][k1][t1];
                            }
                        }
                        colors[s][i][j] = Truncate((Double.valueOf(sum[s])/gauss_divisor) + gauss_offset);
                }
            }
        } 
    }
    
    public void sharpenning_filter(){   
        //Creating copy array to use original pixel values
        int[][][] copy = new int [3][h][w];
        for(int col = 0; col <= 2; col++){
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    copy[col][i][j] = colors[col][i][j]; 
                }
            }
        }
        //for each pixel
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                //for each color
                int sum[] = new int[3];
                for(int s = 0; s < 3; s++){//initiate 
                        sum[s] = 0;
                        //From previous to next pixel in width and in height
                        for (int k = -1; k < 2; k++){
                            for (int t = -1; t < 2; t++){
                                    int k1, t1;
                                    //Check for index in bounds
                                    if(i + k < 0){
                                            k1 = 0;
                                    } else if(i + k >= h){
                                            k1 = h-1;
                                    } else{
                                            k1 = i + k;
                                    }
                                    if(j + t < 0){
                                            t1 = 0;
                                    } else if(j + t >= w){
                                            t1 = w-1;
                                    } else{
                                            t1 = j + t;
                                    }
                                    //Summing up pixel values multiplied by kernel cells
                                    sum[s] += sharp_mask[k + 1][t + 1] * copy[s][k1][t1];
                            }
                        }
                        colors[s][i][j] = Truncate((Double.valueOf(sum[s])/sharp_divisor) + sharp_offset);
                }
            }
        } 
    }
    
    public void emboss_filter(){
        
        int[][][] copy = new int [3][h][w];
        for(int col = 0; col <= 2; col++){
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    copy[col][i][j] = colors[col][i][j]; 
                }
            }
        }
        
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                int sum[] = new int[3];
                for(int s = 0; s < 3; s++){//initiate 
                        sum[s] = 0;
                        for (int k = -1; k < 2; k++){
                            for (int t = -1; t < 2; t++){
                                    int k1, t1;
                                    if(i + k < 0){
                                            k1 = 0;
                                    } else if(i + k >= h){
                                            k1 = h-1;
                                    } else{
                                            k1 = i + k;
                                    }
                                    if(j + t < 0){
                                            t1 = 0;
                                    } else if(j + t >= w){
                                            t1 = w-1;
                                    } else{
                                            t1 = j + t;
                                    }
                                    sum[s] += emboss_mask[k + 1][t + 1] * copy[s][k1][t1];
                            }
                        }
                        colors[s][i][j] = Truncate((Double.valueOf(sum[s])/emboss_divisor) + emboss_offset);
                }
            }
        } 
    }
    
    public void edge_detection_filter(){        
        int[][][] copy = new int [3][h][w];
        for(int col = 0; col <= 2; col++){
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    copy[col][i][j] = colors[col][i][j]; 
                }
            }
        }
        
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                int sum[] = new int[3];
                for(int s = 0; s < 3; s++){//initiate 
                        sum[s] = 0;
                        for (int k = -1; k < 2; k++){
                            for (int t = -1; t < 2; t++){
                                    int k1, t1;
                                    if(i + k < 0){
                                            k1 = 0;
                                    } else if(i + k >= h){
                                            k1 = h-1;
                                    } else{
                                            k1 = i + k;
                                    }
                                    if(j + t < 0){
                                            t1 = 0;
                                    } else if(j + t >= w){
                                            t1 = w-1;
                                    } else{
                                            t1 = j + t;
                                    }
                                    sum[s] += edge_mask[k + 1][t + 1] * copy[s][k1][t1];
                            }
                        }
                        colors[s][i][j] = Truncate(((double)(sum[s])/edge_divisor) + edge_offset);
                }
            }
        } 
    }
    
    public void custom_filter(int[][] mask, double divisor, double offset, int Y, int X){
        int w_mask = mask[0].length;//initializing data
        int h_mask = mask.length;
        
        custom_mask = new int[h_mask][w_mask];
        
        for(int i = 0; i < h_mask; i++){
            for(int j = 0; j < w_mask; j++){
                custom_mask[i][j] = mask[i][j];
            }
        }
        
        custom_divisor = divisor;
        custom_offset = offset;
        
        c_row_num = Y - 1;//casting from beginning on 1 to array beginning on index 0
        c_col_num = X - 1;
        
        //До элемента будет кол-во строк с его индексом и кол-во столбцов с его индексом
        //applying filter
        
        //tmp colors array to hold initial values
        int[][][] copy = new int [3][h][w];
        for(int col = 0; col <= 2; col++){
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    copy[col][i][j] = colors[col][i][j]; 
                }
            }
        }
        
        //for each pixel
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                int sum[] = new int[3];
                //for each color
                for(int s = 0; s < 3; s++){//initiate 
                        sum[s] = 0;
                        //from min row to max in a kernel
                        for (int k = -c_row_num; k < h_mask - c_row_num; k++){
                            //from min column to max in a kernel
                            for (int t = -c_col_num; t < w_mask - c_col_num; t++){
                                    int k1, t1;
                                    if(i + k < 0){
                                            k1 = 0;
                                    } else if(i + k >= h){
                                            k1 = h-1;
                                    } else{
                                            k1 = i + k;
                                    }
                                    if(j + t < 0){
                                            t1 = 0;
                                    } else if(j + t >= w){
                                            t1 = w-1;
                                    } else{
                                            t1 = j + t;
                                    }
                                    sum[s] += custom_mask[c_row_num + k][c_col_num + t] * copy[s][k1][t1];
                                    //System.out.println("Mask " + custom_mask[c_row_num + k][c_col_num + t]);
                            }
                        }
                        //System.out.println("sum " + sum[s]);
                       // System.out.println("value " + Double.valueOf(sum[s])/custom_divisor);
                        colors[s][i][j] = Truncate((Double.valueOf(sum[s])/custom_divisor) + custom_offset);
                }
            }
        } 
        
    }
    
    
    public void draw_line_DDA(Point start, Point end, int R, int G, int B){
        float step;
        float dy = Math.abs(end.y - start.y);
        float dx = Math.abs(end.x - start.x);
        
        
        //System.out.println("dy: " + dy + " dx:" + dx);
        if(dx >= dy){
            step = dx;
        } else {
            step = dy;
        }
        
        dx = dx / step;
        dy = dy / step;
        
        float x = start.x;
        float y = start.y;
        int i = 1;
        
        while(i <= step){
            putPixel(Math.round(y), Math.round(x), R, G, B);
            
            if(end.y - start.y >= 0) y += dy;
            else y -= dy;
              
            if(end.x - start.x >= 0) x += dx;
            else x -= dx;

            i += 1;
        }
    } 
    
    public void draw_midpoint_circle(Point center, int radius){
       // int dx = (int)Math.abs(radius.x - center.x);
       // int dy = (int)Math.abs(radius.y - center.y);
       // int R = (int) Math.sqrt(dx*dx + dy*dy);
        
        int d = 1 - radius;
        int x = 0; // 0;
        int y = radius;
        
        putPixel(center.y + y, center.x + x, 0, 0, 0);
        putPixel(center.y - y, center.x + x, 0, 0, 0);
        putPixel(center.y - x, center.x + y, 0, 0, 0);
        putPixel(center.y + x, center.x - y, 0, 0, 0);
        
        while (y > x)
            {
                if (d < 0) //move to E
                    d += 2 * x + 3;
                else //move to SE
                {
                    d += 2 * x - 2 * y + 5;
                    --y;
                }
                ++x;
                putPixel(center.y + y, center.x + x, 0, 0, 0);
                putPixel(center.y + x, center.x + y, 0, 0, 0);
                putPixel(center.y + x, center.x - y, 0, 0, 0);
                putPixel(center.y + y, center.x - x, 0, 0, 0);
                putPixel(center.y - y, center.x - x, 0, 0, 0);
                putPixel(center.y - x, center.x - y, 0, 0, 0);
                putPixel(center.y - x, center.x + y, 0, 0, 0);
                putPixel(center.y - y, center.x + x, 0, 0, 0);
            }
    }
    
    
    double cov (double dist, double r){
        if(dist > r) {
            return 0;
        } else {
            double cov;
            cov = (Math.acos( dist / r )) / Math.PI;
            cov -= ( (dist * Math.sqrt( Math.pow(r, 2) - Math.pow(dist, 2) ) ) / ( Math.PI * Math.pow(r, 2) ) );
            return cov;
        }
    }
    
    double coverage(double thickness, double distance){
        double coverage = 0;
        
        if(thickness >= 1){
            if(thickness <= distance){
                coverage = cov(distance - thickness, 1);
            } else if (distance < thickness && 0 <= distance){
                coverage = 1 - cov(thickness - distance, 1);
            }
        } else if (thickness < 1){
            if(distance < thickness && 0 <= distance){
                coverage = 1 - cov(thickness - distance, 1) - cov(thickness + distance, 1);
            } else if (thickness <= distance && distance <= 1 - thickness){
                coverage = cov(distance - thickness, 1) - cov(distance + thickness, 1);
            } else if (1 - thickness <= distance && distance <= 1 + thickness) {
                coverage = cov(distance - thickness, 1);
            }
        }
        return coverage;
    }
    

    int lerp(int bg_color, int line_color, double cov){
        return (int) (255 - cov * 255);
    }
    
    void  IntensifyPixel(int x, int y, float thickness, double distance){
        double D;
        if(distance >= 0) D = distance;
        else D = -distance;
        
        double cov = coverage(thickness, D);
        
        if ( cov > 0 ){
            int color = lerp(255, 0, cov);
            putPixel(y, x, color, color, color);
            //System.out.println("X: " + x + "Y: " + y + "Intensity: " + color);
        }
    }
        
    void AA_Gupta_Sproull(Point start, Point end, float thickness){
        if(Math.abs(end.y - start.y) < Math.abs(end.x - start.x)){
            if(start.x > end.x)
                GS_low_line(end, start, thickness); //4, 5
            else
                GS_low_line(start, end, thickness); //1, 8
        } else {
            if(start.y > end.y)
                GS_high_line(end, start, thickness); //6, 7
            else
                GS_high_line(start, end, thickness); //2, 3
        }
      
    }
    
    void GS_low_line(Point start, Point end, float thickness){
        System.out.println("LOW LINE, Y++");
        int dx = end.x - start.x;
        int dy = end.y - start.y;
        
        int yi = 1;
        
        if (dy < 0) //VIII, V
            {
                dy = -dy;
                yi = -1;
            }
        
        
        int dE = 2*dy;
        int dNE = 2*(dy - dx);
        
        int d = 2*dy - dx;
        
        int two_v_dx = 0; //numerator, v=0 for the first pixel, incremental form: d +- dx
        double invDenom = 1/(2 * Math.sqrt(dx*dx + dy*dy)); //inverted denominator
        double two_dx_invDenom = 2*dx*invDenom; //precomputed constant
        

        int x = start.x;
        int y = start.y;
        int i;
        
        //System.out.println("X: " + x + "Y: " + y + " invDenom: " + invDenom + " dx " + dx + " Distance: " + two_dx_invDenom);
        IntensifyPixel(x, y, thickness, 0);
        
        //intensify pixels above and below
        for (i=1; i <= thickness; ++i){           
            IntensifyPixel(x, y + i, thickness, i*two_dx_invDenom);
            IntensifyPixel(x, y - i, thickness, i*two_dx_invDenom);
        } 
        
        while ( x < end.x ){
            //move to E
            if ( d < 0 ) {
                two_v_dx = d + dx;
                d += dE;
                x++;
            //move to NE
            } else {
                two_v_dx = d-dx;
                d += dNE;
                
                y += yi;
                x++;
            }
            
            //System.out.println("X: " + x + "Y: " + y + "Distance: " + two_v_dx*invDenom);
            // Now set the chosen pixel and its neighbors
            IntensifyPixel(x, y, thickness, two_v_dx*invDenom);
            //System.out.println("X: " + x + "Y: " + y + "invDenom: " + invDenom + "dx" + dx + "Distance: " + two_v_dx*invDenom);
            for (i=1; i <= thickness; ++i){//if yi<0 y-i
                if(yi < 0){
                    IntensifyPixel(x, y - i, thickness, i*two_dx_invDenom - two_v_dx*invDenom);
                    IntensifyPixel(x, y + i, thickness, i*two_dx_invDenom + two_v_dx*invDenom); 
                } else {
                    IntensifyPixel(x, y + i, thickness, i*two_dx_invDenom - two_v_dx*invDenom);
                    IntensifyPixel(x, y - i, thickness, i*two_dx_invDenom + two_v_dx*invDenom);
                }
            }
        }
    }
    
    void GS_high_line(Point start, Point end, float thickness){//first part
        System.out.println("HIGH LINE, X++");
        int dx = end.x - start.x;
        int dy = end.y - start.y;
        int xi = 1;
        
        if (dx < 0)
            {
                dx = -dx;
                xi = -1;
            }
        
        
        int dE = 2*dx;
        int dNE = 2*(dx - dy);
        
        int d = 2*dx - dy;
        
        int two_v_dx = 0; //numerator, v=0 for the first pixel
        double invDenom = 1/(2 * Math.sqrt(dx*dx + dy*dy)); //inverted denominator
        double two_dx_invDenom = 2*dy*invDenom; //precomputed constant
        

        int x = start.x;
        int y = start.y;
        int i;
        
        //System.out.println("X: " + x + "Y: " + y + " invDenom: " + invDenom + " dx " + dx + " Distance: " + two_dx_invDenom);
        IntensifyPixel(x, y, thickness, 0);
        
        //intensify pixels above and below
        for (i=1; i <= thickness; ++i){
            IntensifyPixel(x + i, y, thickness, i*two_dx_invDenom);
            IntensifyPixel(x - i, y, thickness, i*two_dx_invDenom);
        } 
        
        while ( y < end.y ){
            //move to E
            if ( d < 0 ) {
                two_v_dx = d + dy;
                d += dE;
                y++;
            //move to NE
            } else {
                two_v_dx = d-dy;
                d += dNE;
                
                y = y + 1;
                x = x + xi;
            }
            
            // Now set the chosen pixel and its neighbors
            IntensifyPixel(x, y, thickness, two_v_dx*invDenom);
            for (i=1; i <= thickness; ++i){
                if(xi < 0){
                    IntensifyPixel(x - i, y, thickness, i*two_dx_invDenom - two_v_dx*invDenom);
                    IntensifyPixel(x + i, y, thickness, i*two_dx_invDenom + two_v_dx*invDenom);
                } else {
                    IntensifyPixel(x + i, y, thickness, i*two_dx_invDenom - two_v_dx*invDenom);
                    IntensifyPixel(x - i, y, thickness, i*two_dx_invDenom + two_v_dx*invDenom);
                }               
            }
        }
    }
    
    void thick_lines_pen(Point start, Point end, int thickness){
        float step;
        float dy = Math.abs(end.y - start.y);
        float dx = Math.abs(end.x - start.x);
        
        
        if(dx >= dy){
            step = dx;
        } else {
            step = dy;
        }
        
        dx = dx / step;
        dy = dy / step;
        
        float x = start.x;
        float y = start.y;
        int i = 1;
        
        while(i <= step){
            this.putPixel(Math.round(y), Math.round(x), 0, 0, 0);
            Point cur = new Point(Math.round(x), Math.round(y));
            //System.out.println("cur: " + cur.toString());
            this.draw_midpoint_circle(cur, Math.round(thickness/2));
            
            if(end.y - start.y >= 0) y += dy;
            else y -= dy;
              
            if(end.x - start.x >= 0) x += dx;
            else x -= dx;

            i += 1;
        }
        
        for(i = 1; i < Math.round(thickness / 2); i++){
            this.draw_midpoint_circle(start, i);
            this.draw_midpoint_circle(end, i);
        }
    }

    void scaling_lines(Point start, Point end, int thickness, image virt_image){
        /*if (thickness == 3)
            thickness = 5;

        if (thickness == 1)
            thickness = 3;*/
        image th = this;
        int im_height = this.h;
        int im_weight = this.w;
 
        //System.out.println(this.h + " im we: " + this.w);
        for(int i = 0; i < im_height - 4; i++){
            for(int j = 0; j < im_weight - 4; j++){
                int[] pixel_1 = new int[3];
                int[] pixel_2 = new int[3];
                int[] pixel_3 = new int[3];
                int[] pixel_4 = new int[3];
                
                //System.out.println(virt_image.w + " he: " + virt_image.h);
                for(int k = 0; k < 3; k++){
                   
                    pixel_1[k] = virt_image.colors[k][2 * i][2 * j];                   
                    pixel_2[k] = virt_image.colors[k][2 * i][2 * j + 1];
                    pixel_3[k] = virt_image.colors[k][2 * i + 1][2 * j];
                    pixel_4[k] = virt_image.colors[k][2 * i + 1][2 * j + 1];
                }
                
                int red = (pixel_1[0] + pixel_2[0] + pixel_3[0] + pixel_4[0])/4;
                int green = (pixel_1[1] + pixel_2[1] + pixel_3[1] + pixel_4[1])/4;
                int blue = (pixel_1[2] + pixel_2[2] + pixel_3[2] + pixel_4[2])/4;
                
                th.colors[0][i][j] = red;
                th.colors[1][i][j] = green;
                th.colors[2][i][j] = blue;
                
            }
        }
    }
    
    ArrayList<Point> result;
    Point[] clipper;
    
    
    Point[] define_borders(Point[] edges){
        //Define borders
        Point[] out = new Point[4];
       // int y_min = 0, y_max = 0, x_max = 0, x_min = 0;
        for(int c = 0; c < 4; c++){
            
            int j;
            if(c != 3) j = c + 1;
            else j = 0;
            
            int next;
            if(j != 3) next = j + 1;
            else next = 0;
            
            if(edges[c].x == edges[j].x){
                if(edges[j].x > edges[next].x){
                    out[1] = edges[j];
                } else {
                    out[3] = edges[j];
                }
            } else if (edges[c].y == edges[j].y){
                if(edges[j].y > edges[next].y){
                    out[2] = edges[j];
                } else {
                    out[0] = edges[j];
                }
            }
        }
        
        return out;
    }
    
    private boolean isInside(Point start_edge, Point end_edge, Point c) {
        return (start_edge.x - c.x) * (end_edge.y - c.y) > (start_edge.y - c.y) * (end_edge.x - c.x);
    }
 
    private Point intersection(Point start_edge, Point end_edge, Point p, Point q) {
        double A1 = end_edge.y - start_edge.y;
        double B1 = start_edge.x - end_edge.x;
        double C1 = A1 * start_edge.x + B1 * start_edge.y;
 
        double A2 = q.y - p.y;
        double B2 = p.x - q.x;
        double C2 = A2 * p.x + B2 * p.y;
 
        double det = A1 * B2 - A2 * B1;
        int x = (int) Math.round((B2 * C1 - B1 * C2) / det);
        int y = (int) Math.round((A1 * C2 - A2 * C1) / det);
 
        return new Point(x, y);
    }
    
    ArrayList<Point> clipping_sh(ArrayList<Point> in_poly, Point[] edges){
        result = in_poly;
        clipper = define_borders(edges);
        int len = clipper.length;
        for(int i = 0; i < 4; i++){
            int len2 = result.size();
            ArrayList<Point> input = result;
            result = new ArrayList();
            
            Point A = clipper[(i + len - 1) % len];
            Point B = clipper[i];
 
            for (int j = 0; j < len2; j++) {
 
                Point P = input.get((j + len2 - 1) % len2);
                Point Q = input.get(j);
 
                if (isInside(A, B, Q)) {
                    if (!isInside(A, B, P))
                        result.add(intersection(A, B, P, Q));
                    result.add(Q);
                } else if (isInside(A, B, P))
                    result.add(intersection(A, B, P, Q));
            }
        }
        return result;
    }
    
    
    void filling_et(ArrayList<Point> fill_poly, int R, int G, int B){
        
        ArrayList<Edge> ET = new ArrayList();
        ArrayList<Edge> AET = new ArrayList();
        
        //Make Edge Table
        int len = fill_poly.size();
        for(int i = 0; i < fill_poly.size(); i++){
            Point start = fill_poly.get((i + len - 1) % len);
            Point end = fill_poly.get(i);
            Edge e = new Edge(start, end);
            ET.add(e);
        }
        
        //Sorted by y min
        Collections.sort(ET, (Edge a1, Edge a2) -> Integer.compare(a1.y_min, a2.y_min));
        for(int i = 0; i < ET.size(); i++){
            System.out.println(ET.get(i).toString());
        }
        
        
        //Starting point for scanline
        int scanline = ET.get(0).y_min;
        
        while(!AET.isEmpty() || !ET.isEmpty()){
            //Add edges for which y_min is scanline
            for(int i = ET.size() - 1; i >= 0; i--){
                if(ET.get(i).y_min == scanline){
                    AET.add(ET.get(i));
                    ET.remove(i);
                }
            }
            
            //System.out.println("Scanline: " + scanline);
            //System.out.println("Size Aet: " + AET.size());
            //System.out.println("Size et: " + ET.size());
            
            //Calculate intersections
            for(int i = 0; i < AET.size(); i++){
                Point inter = intersection(new Point(0, scanline), new Point(w-1, scanline), AET.get(i).start, AET.get(i).end);
                AET.get(i).cur_x = inter.x;
                //System.out.println("i: " + inter.x);
            }
            
            //Sort intersections by increasing x coordinate
            Collections.sort(AET, (Edge a1, Edge a2) -> Integer.compare(a1.cur_x, a2.cur_x));
            for(int i = 0; i < AET.size(); i++){
                System.out.println(AET.get(i).toString());
            }
             
            //Fill pixels between pairs of intersections that lie interior to the polygon
            int x_entry = AET.get(0).cur_x + 1;
            int x_last = AET.get(AET.size() - 1).cur_x;
            int cur = 0;
            
            while(x_entry <= x_last){
                if(cur != AET.size() - 1){
                    if(x_entry == AET.get(cur + 1).cur_x) cur++;
                }               
                if((cur % 2) == 0){
                    putPixel(scanline, x_entry, R, G, B);
                } 
                x_entry++;
            }
            
            //Remove edges for which y_max is scanline - 1
            for(int i = AET.size() - 1; i >= 0; i--){
                if(AET.get(i).y_max == scanline){
                    AET.remove(i);
                }
            }
            
            scanline++;
        }
    }
    
   
}
