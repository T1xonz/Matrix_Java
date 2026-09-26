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

    public static Complex parse(String text) {
        String s = text.trim().replace(',', '.');
        if (s.isEmpty()) throw new NumberFormatException("Пустое число");

        if (!s.endsWith("i")) {
            return new Complex(Double.parseDouble(s), 0);
        }

        String body = s.substring(0, s.length() - 1);   //without 'i'

        int split = -1;
        for (int k = body.length() - 1; k > 0; k--) {
            char c = body.charAt(k);
            if ((c == '+' || c == '-')) {
                split = k;
                break;
            }
        }

        String reStr = split == -1 ? "" : body.substring(0, split);
        String imStr = split == -1 ? body : body.substring(split);

        double im;
        if (imStr.isEmpty() || imStr.equals("+")) im = 1;
        else if (imStr.equals("-"))               im = -1;
        else                                       im = Double.parseDouble(imStr);

        double re = reStr.isEmpty() ? 0 : Double.parseDouble(reStr);
        return new Complex(re, im);
    }

}
