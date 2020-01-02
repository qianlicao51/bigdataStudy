package com.study.scala.booka.char04

object FallWinterSpringSummer extends App {
  for (season <- List("fall", "winter", "spring")) {
    println(season)
  }
  private val rational = new Rational(1, 2)
  private val rational1 = new Rational(2, 3)
  val result: Rational = rational add rational1
  println(result)
  println("结果：", new Rational(66, 42))
  private val rational2 = new Rational(66, 42)

//push
}


class Rational(n: Int, d: Int) {
  require(d != 0)
  private val g = gcd(n.abs, d.abs)

  val number: Int = n / g
  val denom = d / g

  override def toString: String = s"${number}/${denom}"

  // 辅助构造函数
  def this(n: Int) = this(n, 1)

  /**
   * 最大公约数
   *
   * @param a
   * @param b
   * @return
   */
  private def gcd(a: Int, b: Int): Int = {
    if (b == 0) a else gcd(b, a % b)
  }

  def add(that: Rational): Rational = {
    new Rational(
      number * that.denom + that.number * denom,
      denom * that.denom
    )
  }
}
