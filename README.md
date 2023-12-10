# Stack Machine

A series of implementatinos of stack machines to optimize evaluation of arithmetic expressions.


## Benchmarks

`benchmarks / Jmh / run`

Example results

```
[info] Benchmark                                                Mode  Cnt     Score     Error  Units
[info] FibonnaciBenchmark.baseFibBenchmark                     thrpt   25  2754.433 ±  18.351  ops/s
[info] FibonnaciBenchmark.basicStackFibBenchmark               thrpt   25   676.426 ±  18.061  ops/s
[info] FibonnaciBenchmark.byteCodeFibBenchmark                 thrpt   25  4057.114 ±  69.842  ops/s
[info] FibonnaciBenchmark.optimizedStack2FibBenchmark          thrpt   25  3575.290 ±  45.085  ops/s
[info] FibonnaciBenchmark.optimizedStack3FibBenchmark          thrpt   25  3567.815 ±  50.533  ops/s
[info] FibonnaciBenchmark.optimizedStackFibBenchmark           thrpt   25  3631.189 ±   8.750  ops/s
[info] FibonnaciBenchmark.stackCachingFibBenchmark             thrpt   25  3698.103 ± 108.849  ops/s
[info] FibonnaciBenchmark.superInstructionFibBenchmark         thrpt   25  1239.706 ± 364.866  ops/s
[info] FibonnaciBenchmark.algebraicSimplificationFibBenchmark  thrpt   25  1630.928 ± 85.052  ops/s
[info] FibonnaciBenchmark.allFibBenchmark                      thrpt   25  7612.448 ± 420.554  ops/s
```

Super instruction benchmarks are very volatile.
