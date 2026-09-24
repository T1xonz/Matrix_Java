import java.util.Locale;

public final class Complex {

    private final double re; // real part
    private final double im; //imaginary part

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE  = new Complex(1, 0);

    public Complex plus(Complex o)  { return new Complex(re + o.re, im + o.im); }
    public Complex minus(Complex o) { return new Complex(re - o.re, im - o.im); }


    //use form: (a+bi)(c+bi) == (ac-bd)+(ad+bc)i
    public Complex multiply(Complex o) {
        return new Complex(re * o.re - im * o.im,
                re * o.im + im * o.re);
    }
    // use form: (a+bi)(a-bi) == a^2-b^2i^2 == a^2+b^2
    public Complex divide(Complex o) {
        double d = o.re * o.re + o.im * o.im;
        if (d == 0) throw new ArithmeticException("Деление на ноль");
        return new Complex((re * o.re + im * o.im) / d,
                (im * o.re - re * o.im) / d);
    }

}
