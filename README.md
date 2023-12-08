# Stack Machine

A series of implementatinos of stack machines to optimize evaluation of arithmetic expressions.


## Benchmarks

`benchmarks / Jmh / run`

Example results

```
[info] FibonnaciBenchmark.baseFibBenchmark              thrpt   25  2748.000 ±  17.741  ops/s
[info] FibonnaciBenchmark.basicStackFibBenchmark        thrpt   25   644.858 ±  32.125  ops/s
[info] FibonnaciBenchmark.byteCodeFibBenchmark          thrpt   25  1675.357 ±   5.391  ops/s
[info] FibonnaciBenchmark.optimizedStack2FibBenchmark   thrpt   25  3563.952 ±  71.645  ops/s
[info] FibonnaciBenchmark.optimizedStack3FibBenchmark   thrpt   25  3540.126 ±  51.814  ops/s
[info] FibonnaciBenchmark.optimizedStackFibBenchmark    thrpt   25  3630.538 ±  12.488  ops/s
[info] FibonnaciBenchmark.stackCachingFibBenchmark      thrpt   25  3508.967 ± 234.853  ops/s
[info] FibonnaciBenchmark.superInstructionFibBenchmark  thrpt   25  3839.907 ± 452.660  ops/s
```
