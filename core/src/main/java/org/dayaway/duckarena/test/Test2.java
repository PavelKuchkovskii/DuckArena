package org.dayaway.duckarena.test;

import com.badlogic.gdx.math.Vector2;

public class Test2 {

    public static void main(String[] args) {

        Vector2 vector1 = new Vector2(-2,-1);
        Vector2 vector2 = new Vector2(-4,-3);

        System.out.println(getVector(vector1, vector2));
    }

    static float getVector(Vector2 vector1, Vector2 vector2) {
        float leg1;
        float leg2;

        if(vector1.x > vector2.x) {
            leg1 = Math.abs(vector1.x - vector2.x);
        }
        else {
            leg1 = Math.abs(vector2.x - vector1.x);
        }

        if(vector1.y > vector2.y) {
            leg2 = Math.abs(vector1.y - vector2.y);
        }
        else {
            leg2 = Math.abs(vector2.y - vector1.y);
        }

        System.out.println(leg1);
        System.out.println(leg2);

        return (float) Math.sqrt((leg1 * leg1) + (leg2 * leg2));
    }
}
