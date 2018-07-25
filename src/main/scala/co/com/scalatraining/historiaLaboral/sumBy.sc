case class test(a: Int, b: Int, c: Int)

val a = List(
  test(1, 2, 3),
  test(3, 2, 1),
  test(1, 3, 2)
)

a.maxBy(t => (t.b, t.a, t.c))