/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

public class Vector {
    private double x=0,y=0,z=0;

    public Vector(){}
    public Vector(double x,double y){this.x=x;this.y=y;}
    public Vector(double x,double y,double z){this.x=x;this.y=y;this.z=z;}

    public double getX(){return x;}
    public double getY(){return y;}
    public double getZ(){return z;}
    public void setX(double x){this.x=x;}
    public void setY(double y){this.y=y;}
    public void setZ(double z){this.z=z;}

    public Vector subtract(Vector v){
        Vector ret = new Vector(x-v.x,y-v.y,z-v.z);
        return ret;
    }

    public Vector add(Vector v){
        Vector ret = new Vector(x+v.x,y+v.y,z+v.z);
        return ret;
    }

    public Vector sub(Vector v){
        Vector ret = new Vector(x-v.x,y-v.y,z-v.z);
        return ret;
    }

    public Vector multiply(double d){
        Vector ret = new Vector(x*d,y*d,z*d);
        return ret;
    }

    public Vector divide(double d){
        Vector ret = new Vector(x/d,y/d,z/d);
        return ret;
    }

    public Vector cross(Vector v){
        Vector ret = new Vector(y*v.z-z*v.y,
                                z*v.x-x*v.z,
                                x*v.y-y*v.x);
        return ret;
    }

    public double scalar(Vector v){
        return x*v.x+y*v.y+z*v.z;
    }

    public double length(){
        return java.lang.Math.sqrt(java.lang.Math.pow(x,2)+java.lang.Math.pow(y,2)+java.lang.Math.pow(z,2));
    }

    public void normalize(){
        double d = Toolkit.p(length());
        x/=d;y/=d;z/=d;
    }

    public void stretch(double d){
        normalize();
        x*=d;y*=d;z*=d;
    }
}
