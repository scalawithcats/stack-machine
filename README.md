# Stack Machine

A series of implementations of stack machines to investigate performance of interpreters on the JVM.


## Benchmarks

To run all benchmarks

`benchmarks / Jmh / run`

Example results are below.


To inspect performance:

1. Uncomment `javaOptions` in `build.sbt`.

2. It is not necessary to run the benchmarks for so many iterations. You can
   instead use

   ```
   benchmarks / Jmh / run -i 1 -wi 0 -f1 -t1 directThreadedFibBenchmark -prof jfr
   ```
   
   replacing `directThreadedFibBenchmark` with the name of the benchmark you're
   interested in. (This means: 1 iteration, 0 warmup iterations, 1 fork, 1
   thread, record profiling information in JFR format)


### Analysis

Get JITWatch from https://github.com/AdoptOpenJDK/jitwatch/releases

`java -jar <jitwatch.jar>`

Open `hotspot_pidXXXXX.log` in the `benchmarks` directory.


### Benchmark Results

Ryzen 5 3600, JDK 21

```
[info] Benchmark                                                Mode  Cnt     Score     Error  Units
[info] FibonnaciBenchmark.algebraicSimplificationFibBenchmark  thrpt   25  1630.928 ±  85.052  ops/s
[info] FibonnaciBenchmark.allFibBenchmark                      thrpt   25  7612.448 ± 420.554  ops/s
[info] FibonnaciBenchmark.baseFibBenchmark                     thrpt   25  2754.433 ±  18.351  ops/s
[info] FibonnaciBenchmark.basicStackFibBenchmark               thrpt   25   676.426 ±  18.061  ops/s
[info] FibonnaciBenchmark.byteCodeFibBenchmark                 thrpt   25  4057.114 ±  69.842  ops/s
[info] FibonnaciBenchmark.directThreadedFibBenchmark           thrpt   25  451783334.122 ± 18008803.137  ops/s
[info] FibonnaciBenchmark.directThreaded3FibBenchmark          thrpt   25  528655333.613 ± 13105914.900  ops/s
[info] FibonnaciBenchmark.optimizedStack2FibBenchmark          thrpt   25  3575.290 ±  45.085  ops/s
[info] FibonnaciBenchmark.optimizedStack3FibBenchmark          thrpt   25  3567.815 ±  50.533  ops/s
[info] FibonnaciBenchmark.optimizedStackFibBenchmark           thrpt   25  3631.189 ±   8.750  ops/s
[info] FibonnaciBenchmark.stackCachingFibBenchmark             thrpt   25  3698.103 ± 108.849  ops/s
[info] FibonnaciBenchmark.superInstructionFibBenchmark         thrpt   25  3706.695 ±  79.696  ops/s
```


M1, JDK 21

```
[info] Benchmark                                                Mode  Cnt     Score    Error  Units
[info] FibonnaciBenchmark.algebraicSimplificationFibBenchmark  thrpt   25  4818.448 ± 21.517  ops/s
[info] FibonnaciBenchmark.allFibBenchmark                      thrpt   25  7098.065 ± 40.146  ops/s
[info] FibonnaciBenchmark.baseFibBenchmark                     thrpt   25  3934.926 ± 20.942  ops/s
[info] FibonnaciBenchmark.basicStackFibBenchmark               thrpt   25  1004.157 ± 14.752  ops/s
[info] FibonnaciBenchmark.byteCodeFibBenchmark                 thrpt   25  3355.754 ± 15.455  ops/s
[info] FibonnaciBenchmark.directThreadedFibBenchmark           thrpt   25  611167913.856 ± 3757656.851  ops/s
[info] FibonnaciBenchmark.optimizedStack2FibBenchmark          thrpt   25  2953.961 ± 10.760  ops/s
[info] FibonnaciBenchmark.optimizedStack3FibBenchmark          thrpt   25  2962.975 ± 24.769  ops/s
[info] FibonnaciBenchmark.optimizedStackFibBenchmark           thrpt   25  2953.212 ± 21.948  ops/s
[info] FibonnaciBenchmark.stackCachingFibBenchmark             thrpt   25  3237.171 ± 28.888  ops/s
[info] FibonnaciBenchmark.superInstructionFibBenchmark         thrpt   25  4689.022 ± 31.661  ops/s
```

`

Super instruction benchmarks are very volatile.
