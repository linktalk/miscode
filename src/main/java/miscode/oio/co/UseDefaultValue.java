package miscode.oio.co;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Use default or explicit initialization value?
 *
 * Question: If the initialization value is the same as the default one, does
 *   the explicit assignment impact the performance?
 *
 *       boolean v = false;
 *       vs
 *       boolean v;    // use default value: false
 *
 * A sample of the Benchmark results (JDK 12):
 * Benchmark                 Mode   Cnt           Score           Error  Units
 * withDefaultValue          thrpt   25  3713078690.377 ±  23089789.596  ops/s
 * noDefaultValue            thrpt   25  3701103280.943 ±  71561673.246  ops/s
 *
 * withVolatileDefaultValue  thrpt   25  3587656113.778 ± 150725597.145  ops/s
 * noVolatileDefaultValue    thrpt   25  3715301582.315 ±  23250778.591  ops/s
 */
public class UseDefaultValue {
    /**
     * Run
     *  1) via command line:
     *    $mvn clean install
     *    $java -jar target/benchmarks.jar UseDefaultValue -t 4 -f 1
     *     (use 4 threads, 1 fork by default)
     *  2) via built-in JMH
     *    $mvn clean install
     *    $java -jar UseDefaultValue -t 4 -f 1
     *
     * Help:
     *    $mvn clean install
     *    $java -jar UseDefaultValue -h
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(UseDefaultValue.class.getSimpleName())
                .threads(Threads.MAX)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    /**
     * A sample benchmark result (JDK 12):
     *   3587656113.778 ±(99.9%) 150725597.145 ops/s [Average]
     *   (min, avg, max) = (3064840103.958, 3587656113.778, 3738863833.856),
     *       stdev = 201214359.893
     *   CI (99.9%): [3436930516.633, 3738381710.924]
     *       (assumes normal distribution)
     */
    @Benchmark
    @Threads(Threads.MAX)
    public void withVolatileDefaultValue() {
        new A();
    }

    /**
     * A sample benchmark result (JDK 12):
     *   3715301582.315 ±(99.9%) 23250778.591 ops/s [Average]
     *   (min, avg, max) = (3613825116.698, 3715301582.315, 3743200186.669),
     *       stdev = 31039124.208
     *   CI (99.9%): [3692050803.724, 3738552360.905]
     *       (assumes normal distribution)
     */
    @Benchmark
    @Threads(Threads.MAX)
    public void noVolatileDefaultValue() {
        new B();
    }

    /**
     * A sample benchmark result (JDK 12):
     *   3713078690.377 ±(99.9%) 23089789.596 ops/s [Average]
     *   (min, avg, max) = (3625079740.417, 3713078690.377, 3739875971.640),
     *       stdev = 30824208.506
     *   CI (99.9%): [3689988900.782, 3736168479.973]
     *       (assumes normal distribution)
     */
    @Benchmark
    @Threads(Threads.MAX)
    public void withDefaultValue() {
        new C();
    }

    /**
     * A sample benchmark result (JDK 12):
     *   3701103280.943 ±(99.9%) 71561673.246 ops/s [Average]
     *   (min, avg, max) = (3251156581.539, 3701103280.943, 3750684432.555),
     *       stdev = 95532786.387
     *   CI (99.9%): [3629541607.697, 3772664954.189]
     *       (assumes normal distribution)
     */
    @Benchmark
    @Threads(Threads.MAX)
    public void noDefaultValue() {
        new D();
    }

    /**
     * A class with assignment for a volatile field.
     */
    private class A {
        private volatile boolean a = false;

        private void set() {
            a = true;
        }
    }

    /**
     * A class without assignment for a volatile field.
     */
    private class B {
        private volatile boolean b;

        private void set() {
            b = true;
        }
    }

    /**
     * A class with assignment for a field.
     */
    private class C {
        private boolean c = false;

        private void set() {
            c = true;
        }
    }

    /**
     * A class without assignment for a field.
     */
    private class D {
        private boolean d;

        private void set() {
            d = true;
        }
    }
}
