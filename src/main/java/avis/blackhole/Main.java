package avis.blackhole;

import avis.juikit.Juikit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 1200;
    
    private static final int BLACKHOLE_RADIUS = 50;
    
    public static class Single extends Vector {
        
        Vector vector;
        boolean marked = false;
        
        double beforeX;
        double beforeY;
    
        public Single(double x, double y) {
            super(x, y);
        }
    }
    
    public static class Vector {
        
        double x;
        double y;
    
        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    
        public double length() {
            return Math.sqrt(x * x + y * y);
        }
        
        public Vector normalize() {
            return new Vector(x / length(), y / length());
        }
        
        public Vector subtract(Vector vector) {
            double x = this.x - vector.x;
            double y = this.y - vector.y;
            
            return new Vector(x, y);
        }
        
        public Vector add(Vector vector) {
            return new Vector(x + vector.x, y + vector.y);
        }
        
        public Vector multiply(Vector vector) {
            return new Vector(x * vector.x, y * vector.y);
        }
        
        public Vector multiply(double multiply) {
            return new Vector(x * multiply, y * multiply);
        }
        
        public double distance(Vector vector) {
            double diffX = this.x - vector.x;
            double diffY = this.y - vector.y;
            
            return Math.sqrt(diffX * diffX + diffY * diffY);
        }
        
    }
    
    private static final Random RANDOM = new Random();
    private static final List<Single> SINGLES = new ArrayList<>();
    private static final Vector SINGULARITY = new Vector(WIDTH / 2d, HEIGHT / 2d);
    
    public static void main(String args[]) {
        for(int i = 0; i < 2000; i++) {
            Vector vector = new Vector(4, 0);
            Single single = new Single(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
            single.beforeX = single.x;
            single.beforeY = single.y;
            single.vector = vector;
            SINGLES.add(single);
        }
        Juikit.createFrame()
                .size(WIDTH, HEIGHT)
                .centerAlign()
                .antialiasing(true)
                .visibility(true)
                .closeOperation(WindowConstants.EXIT_ON_CLOSE)
                .repaintInterval(10L)
                .background(Color.BLACK)
                .painter((juikit, graphics) -> {
                    graphics.setColor(Color.DARK_GRAY);
                    graphics.fillOval(WIDTH / 2 - (BLACKHOLE_RADIUS / 2), HEIGHT / 2 - (BLACKHOLE_RADIUS / 2), BLACKHOLE_RADIUS, BLACKHOLE_RADIUS);
                    
                    for(Single single : SINGLES) {
                        double distance = SINGULARITY.distance(single);
                        if(distance < BLACKHOLE_RADIUS) {
                            single.marked = true;
                        }
                        
                        Vector trajectory = SINGULARITY.subtract(single).normalize();
                        single.vector = single.vector.add(trajectory);
                        
                        single.beforeX = single.x;
                        single.beforeY = single.y;
                        
                        single.x += single.vector.x;
                        single.y += single.vector.y;
                        
                        double length = single.vector.length();
                        
                        Color color;
                        if(length > 30) {
                            color = Color.RED;
                        } else if(length > 20) {
                            color = Color.YELLOW;
                        } else if(length > 10) {
                            color = Color.GREEN;
                        } else {
                            color = Color.BLUE;
                        }
                        graphics.setColor(color);
                        graphics.fillOval((int) single.x, (int) single.y, 2, 2);
                        graphics.drawLine((int) single.beforeX, (int) single.beforeY, (int) single.x, (int) single.y);
                    }
                    
                    SINGLES.removeIf(single -> single.marked);
                });
    }

}
