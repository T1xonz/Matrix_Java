public final class ComplexMatrix {
    private final int rows;
    private final int cols;
    private final Complex[][] data;

    public ComplexMatrix(Complex[][] source) {
        if (source == null || source.length == 0 || source[0].length == 0)
            throw new IllegalArgumentException("Матрица не может быть пустой");
        this.rows = source.length;
        this.cols = source[0].length;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (source[i].length != cols)
                throw new IllegalArgumentException("Строки матрицы разной длины");
            for (int j = 0; j < cols; j++)
                data[i][j] = source[i][j];
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public boolean isSquare() { return rows == cols; }

    public static ComplexMatrix identity(int n) {
        Complex[][] e = new Complex[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                e[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
        return new ComplexMatrix(e);
    }

    public ComplexMatrix plus(ComplexMatrix o) {
        if (rows != o.rows || cols != o.cols)
            throw new IllegalArgumentException(
                    "Сложение возможно только для матриц одного размера ("
                            + rows + "x" + cols + " и " + o.rows + "x" + o.cols + ")");
        Complex[][] r = new Complex[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r[i][j] = data[i][j].plus(o.data[i][j]);
        return new ComplexMatrix(r);
    }

    public ComplexMatrix multiply(ComplexMatrix o) {
        if (cols != o.rows)
            throw new IllegalArgumentException(
                    "Умножение невозможно: число столбцов первой (" + cols
                            + ") не равно числу строк второй (" + o.rows + ")");
        Complex[][] r = new Complex[rows][o.cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < o.cols; j++) {
                Complex sum = Complex.ZERO;
                for (int k = 0; k < cols; k++)
                    sum = sum.plus(data[i][k].multiply(o.data[k][j]));
                r[i][j] = sum;
            }
        }
        return new ComplexMatrix(r);
    }

    public ComplexMatrix transpose() {
        Complex[][] r = new Complex[cols][rows];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r[j][i] = data[i][j];
        return new ComplexMatrix(r);
    }


    public Complex determinant() {
        if (!isSquare())
            throw new IllegalArgumentException("Определитель существует только у квадратных матриц");
        int n = rows;
        Complex[][] a = copyData();
        Complex det = Complex.ONE;

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int r = col + 1; r < n; r++)
                if (a[r][col].abs() > a[pivot][col].abs()) pivot = r;
            if (a[pivot][col].isZero()) return Complex.ZERO;
            if (pivot != col) {
                Complex[] tmp = a[pivot]; a[pivot] = a[col]; a[col] = tmp;
                det = det.anti();
            }
            det = det.multiply(a[col][col]);
            for (int r = col + 1; r < n; r++) {
                Complex f = a[r][col].divide(a[col][col]);
                for (int c = col; c < n; c++)
                    a[r][c] = a[r][c].minus(f.multiply(a[col][c]));
            }
        }
        return det;
    }

    private Complex[][] copyData() {
        Complex[][] c = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) c[i] = data[i].clone();
        return c;
    }

    public ComplexMatrix inverse() {
        if (!isSquare())
            throw new IllegalArgumentException("Обратная матрица существует только для квадратных матриц");
        int n = rows;

        Complex[][] a = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j]     = data[i][j];
                a[i][n + j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int r = col + 1; r < n; r++)
                if (a[r][col].abs() > a[pivot][col].abs()) pivot = r;
            if (a[pivot][col].isZero())
                throw new ArithmeticException("Матрица вырождена (определитель равен 0), обратной нет");

            Complex[] tmp = a[pivot]; a[pivot] = a[col]; a[col] = tmp;

            Complex p = a[col][col];
            for (int c = 0; c < 2 * n; c++) a[col][c] = a[col][c].divide(p);
            for (int r = 0; r < n; r++) {
                if (r == col) continue;
                Complex f = a[r][col];
                if (f.isZero()) continue;
                for (int c = 0; c < 2 * n; c++)
                    a[r][c] = a[r][c].minus(f.multiply(a[col][c]));
            }
        }

        Complex[][] inv = new Complex[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                inv[i][j] = a[i][n + j];
        return new ComplexMatrix(inv);
    }

    public ComplexMatrix divide(ComplexMatrix o) {
        if (!o.isSquare())
            throw new IllegalArgumentException("Делитель должен быть квадратной матрицей");
        if (cols != o.rows)
            throw new IllegalArgumentException(
                    "Деление невозможно: число столбцов делимого (" + cols
                            + ") не равно размеру делителя (" + o.rows + ")");
        return this.multiply(o.inverse());
    }

}

