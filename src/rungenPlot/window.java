package rungenPlot;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.lang.*;

  class Window
  {
    private double xfac, yfac, x_null, y_null;
    Graphics g;
    int width, height, X0, Y0;
    Window(Graphics g, double xmin, double ymin, double xmax, double ymax)
    {
      X0     = g.getClipBounds().x + 1;
      Y0     = g.getClipBounds().y + 1;
      width  = g.getClipBounds().width  - 2;
      height = g.getClipBounds().height - 2;
      this.xfac =     width/ (xmax - xmin);
      this.yfac =   - height/(ymax - ymin);
      this.x_null =   X0     - xfac * xmin;
      this.y_null =   Y0 -     yfac * ymax;
      this.g =  g;

    }

    void line(double x0,  double y0, double x1, double y1)
    {
      int[] X  = new int[2];
      int[] Y  = new int[2];
      X[0] = scalex(x0);
      X[1] = scalex(x1);
      Y[0] = scaley(y0);
      Y[1] = scaley(y1);
      this.g.drawLine(X[0], Y[0], X[1], Y[1]);
    }


    void circle(double x,  double y, double r)
    {
      int X, Y, RX, RY;
      X  = scalex(x);
      Y =  scaley(y);
      RX = (int) (xfac * r);
      RY = -(int) (yfac * r);
      this.g.drawArc(X - RX, Y - RY, 2* RX, 2* RY, 0, 360);
    }

    void polyline(double x[], double y[], int n)
    {
      int[] X = new int[n];
      int[] Y = new int[n];
      for(int i = 0; i < n; i++)
      {
         X[i] = scalex(x[i]);
         Y[i] = scaley(y[i]);
      }
      this.g.drawPolyline(X, Y, n);
    }

    private int scalex(double x)
    {
      return(int) (x * xfac +  0.5 + x_null);
    }
    double sx(int X)
    {
      return (X - x_null)/xfac;
    }
    double sy(int Y)
    {
      return (Y - y_null)/yfac;
    }

    private int scaley(double y)
    {
      return(int) (y * yfac +  0.5 + y_null);
    }
    Polygon karo(int x, int y)
    {
      Polygon p = new Polygon();
      p.addPoint(x,y+3);
      p.addPoint(x+3,y);
      p.addPoint(x,y-3);
      p.addPoint(x-3,y);
      return p;
    }

    void point(double x0, double y0)
    {
      g.fillPolygon(karo(scalex(x0), scaley(y0)));
    }
    void polypoint(double x[], double y[], int n)
    {
      for(int i = 0; i < n; i++)
      {
        point(x[i], y[i]);
      }
    }
  }
