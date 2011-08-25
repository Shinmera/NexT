/**********************\
  file: Line.java
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

public class Line {
    private Vector a,b;

    public Line(){a=new Vector();b=new Vector();}
    public Line(double ax,double ay,double az,double bx,double by,double bz){a = new Vector(ax,ay,az);b = new Vector(bx,by,bz);}
    public Line(Vector a,Vector b){this.a=a;this.b=b;}

    public Vector getA(){return a;}
    public Vector getB(){return b;}

    public void setA(Vector a){this.a=a;}
    public void setB(Vector b){this.b=b;}

    public Vector getPoint(double t){
        return new Ray(a,b).getPoint(t);
    }

    public double getIntersection2D(Ray r){
        //SHITEY CALCULATIONS.
        double u =  (r.getDY()*(a.getX()-r.getX())-r.getDX()*(a.getY()-r.getY()))/
                    (r.getDY()*(a.getX()-b.getX())-r.getDX()*(a.getY()-b.getY()));  //POSITION ON THE LINE
        double t = 0;
        if(Toolkit.p(r.getDX())>Toolkit.p(r.getDY())){
            t =  (a.getX()-r.getX()+(b.getX()-a.getX())*u)/(r.getDX());             //POSITION ON THE RAY
        } else{
            t =  (a.getY()-r.getY()+(b.getY()-a.getY())*u)/(r.getDY());
        }
        if (t < 0 || u < 0 || u > 1)return 0;                                       //NOPE.
        return t;
    }
}
