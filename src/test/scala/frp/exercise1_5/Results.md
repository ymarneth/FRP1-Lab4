| Number of Elements | Duration (nanoseconds) | Parallel Duration (nanoseconds) |
|--------------------|------------------------|---------------------------------|
| 20                 | 609246                 | 79354                           |
| 50                 | 200946                 | 208257                          |
| 100                | 326717                 | 263958                          |
| 1000               | 2292693                | 1686681                         |

The parallel version is faster than the sequential version for the 100 and 1000 elements case.
With 100 elements, the benefit is not that big, but with 1000 elements, the parallel version is almost 1.5 times faster.
