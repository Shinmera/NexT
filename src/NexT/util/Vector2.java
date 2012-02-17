/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

public class Vector2 {
    public double x=0,y=0;

    public Vector2(){}
    public Vector2(double x,double y){this.x=x;this.y=y;}

    public double getX(){return x;}
    public double getY(){return y;}
    public void setX(double x){this.x=x;}
    public void setY(double y){this.y=y;}

    public Vector2 subtract(Vector2 v){
        Vector2 ret = new Vector2(x-v.x,y-v.y);
        return ret;
    }
    
    public Vector2 subtract(Vector v){
        Vector2 ret = new Vector2(x-v.x,y-v.y);
        return ret;
    }

    public Vector2 add(Vector2 v){
        Vector2 ret = new Vector2(x+v.x,y+v.y);
        return ret;
    }
    
    public Vector2 add(Vector v){
        Vector2 ret = new Vector2(x+v.x,y+v.y);
        return ret;
    }

    public Vector2 sub(Vector2 v){return subtract(v);}
    public Vector2 sub(Vector v){return subtract(v);}

    public Vector2 multiply(double d){
        Vector2 ret = new Vector2(x*d,y*d);
        return ret;
    }

    public Vector2 divide(double d){
        Vector2 ret = new Vector2(x/d,y/d);
        return ret;
    }

    public double scalar(Vector2 v){
        return x*v.x+y*v.y;
    }
    
    public double scalar(Vector v){
        return x*v.x+y*v.y;
    }

    public double length(){
        return java.lang.Math.sqrt(java.lang.Math.pow(x,2)+java.lang.Math.pow(y,2));
    }

    public void normalize(){
        double d = Toolkit.p(length());
        x/=d;y/=d;
    }

    public void stretch(double d){
        normalize();
        x*=d;y*=d;
    }
}
