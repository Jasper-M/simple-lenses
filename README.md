# simple-lenses
```scala
scala> import simplelenses.implicits._
import simplelenses.implicits._

scala> case class A(b: B); case class B(c: C); case class C(i: Int)
defined class A
defined class B
defined class C

scala> val a = A(B(C(4)))
a: A = A(B(C(4)))

scala> val a2 = a but ( _.b.c.i <~ 8 )
a2: A = A(B(C(8)))
```
