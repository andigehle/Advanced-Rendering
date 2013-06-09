

public abstract class runge {
	double h;

	public abstract double f(double x, double y);

	int steps;
	double[] x, y;

	/*
	 * Führt n Schritte des RK-Verfahrens aus, falls Schranke boundmax
	 * überschritten oder boundmin unterschritten wird erfolgt Abbruch
	 */
	void rkn(double x0, double y0, double h, int N, double boundmin,
			double boundmax) {
		int i;
		x = new double[N + 1];
		y = new double[N + 1];
		x[0] = x0;
		y[0] = y0;
		for (i = 1; i <= N; i++) {
			x[i] = x0 + i * h;
			y[i] = rk(x[i - 1], y[i - 1], h);
			if (y[i] > boundmax || y[i] < boundmin)
				break;
		}
		steps = i;
	}

	/* Führt einen Schritt des RK-Verfahrens aus */
	double rk(double x, double y, double h) {
		double k1, k2, k3, k4;
		k1 = h * f(x, y);
		k2 = h * f(x + h / 2, y + k1 / 2);
		k3 = h * f(x + h / 2, y + k2 / 2);
		k4 = h * f(x, y + k3);
		return y + (k1 + 2 * k2 + 2 * k3 + k4) / 6.;
	}
	
	public String toString(){
		String out = "RungeKutta calculated:";
		
		for(int i=0; i < x.length; i++){
			out += " ["+x[i]+","+y[i]+"]";			
		}
		
		return out;
		
	}
}
