package rungenPlot;

//
// file: dglplot.java
// 

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class dglplot extends Applet {
	int count = 0, stop;
	int size, X, Y, clip = 10;
	int NGRID = 20;
	boolean solve = false;
	double h = 0.005, W = 1, H = 1;
	double boundmin = -H, boundmax = +H;
	int N = 500;
	double[] x = new double[N + 1], y = new double[N + 1];
	rungexy slv = new rungexy();
	rungexy slv1 = new rungexy();

	public void init() // wird automatisch aufgerufen
	{
		setName(slv.Title);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setBackground(Color.white);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		double r, pi2 = 2. * Math.PI;
		
		int width = getSize().width;
		int height = getSize().height;
		g.setColor(Color.black);
		g.drawRect(clip, clip, width - 2 * clip, height - 2 * clip);
		g.setClip(clip, clip, width - 2 * clip, height - 2 * clip);
		Window w = new Window(g, -W, -H, W, H);
		double h1 = 2 * W / NGRID;
		double k1 = 2 * H / NGRID;
		for (int i = 1; i < NGRID; i++) {
			double x, y, fac = H * NGRID / width / 2., l, hx, hy;
			x = -W + i * h1;
			for (int k = 1; k < NGRID; k++) {
				y = -H + k * k1;
				hy = slv.f(x, y);
				hx = 1.;
				l = Math.sqrt(hx * hx + hy * hy);
				w.line(x - hx / l * fac, y - hy / l * fac, x + hx / l * fac, y
						+ hy / l * fac);
			}
		}
		
		
		g.setColor(Color.red);

		if (solve) {
			int i;
			double x0 = w.sx(X), y0 = w.sy(Y);
			w.point(x0, y0);
			slv.rkn(x0, y0, h, N, boundmin, boundmax);
			w.polyline(slv.x, slv.y, slv.steps);
			slv1.rkn(x0, y0, -h, N, boundmin, boundmax);
			w.polyline(slv1.x, slv1.y, slv1.steps);
		}
	}

	public void processMouseEvent(MouseEvent evt) {
		if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
			X = evt.getX();
			Y = evt.getY();
			solve = true;
		}
		repaint();
		super.processMouseEvent(evt);
	}

}

class rungexy extends runge {
	String Title = "Lösung der DGL y'= x^2 + y^2";

	double f(double x, double y) {
		return x * x + y * y;
	}
}