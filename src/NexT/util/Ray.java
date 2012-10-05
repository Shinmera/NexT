/**********************\
  file: Ray.java
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

public class Ray {
    private Vector pos,dir;
    public Ray(){pos = new Vector();dir = new Vector();}
    public Ray(double x,double y,double z){pos = new Vector(x,y,z);dir = new Vector();}
    public Ray(Vector pos){this.pos=pos;dir = new Vector();}
    public Ray(double x,double y,double z,double dx,double dy,double dz){pos = new Vector(x,y,z);dir = new Vector(dx,dy,dz);dir.normalize();}
    public Ray(Vector pos,Vector dir){this.pos=pos.multiply(1);this.dir=dir.multiply(1);this.dir.normalize();}
    
    public Vector getPoint(double t){
        return pos.add(dir.multiply(t));
    }
    public double getX(){return pos.getX();}
    public double getY(){return pos.getY();}
    public double getZ(){return pos.getZ();}
    public double getDX(){return dir.getX();}
    public double getDY(){return dir.getY();}
    public double getDZ(){return dir.getZ();}
    public Vector getPosition(){return pos;}
    public Vector getDirection(){return dir;}

    public void setX(double x){pos.setX(x);}
    public void setY(double y){pos.setY(y);}
    public void setZ(double z){pos.setZ(z);}
    public void setPosition(Vector pos){this.pos=pos;}
    public void setDirection(Vector dir){this.dir=dir;dir.normalize();}
}
