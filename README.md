# Stack Machine

A series of implementatinos of stack machines to optimize evaluation of arithmetic expressions.


## Benchmarks

`benchmarks / Jmh / run`

Example results

Ryzen 5 3600, JDK 21

```
[info] Benchmark                                                Mode  Cnt     Score     Error  Units
[info] FibonnaciBenchmark.algebraicSimplificationFibBenchmark  thrpt   25  1630.928 ±  85.052  ops/s
[info] FibonnaciBenchmark.allFibBenchmark                      thrpt   25  7612.448 ± 420.554  ops/s
[info] FibonnaciBenchmark.baseFibBenchmark                     thrpt   25  2754.433 ±  18.351  ops/s
[info] FibonnaciBenchmark.basicStackFibBenchmark               thrpt   25   676.426 ±  18.061  ops/s
[info] FibonnaciBenchmark.byteCodeFibBenchmark                 thrpt   25  4057.114 ±  69.842  ops/s
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
