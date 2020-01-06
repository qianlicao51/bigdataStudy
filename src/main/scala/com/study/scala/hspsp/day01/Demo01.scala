package com.study.scala.hspsp.day01

object Demo01 {
  def main(args: Array[String]): Unit = {
    println("scala ")
    val s = "12.5"
    //    val int = s.toInt //转int会出错
    //    println(int)

    //方法转为函数
    val dog = new Dog
    val f1 = dog.sum _
    println(f1)

    try {
      test()
    } catch {
      case ex:Exception => println("异常：",ex.getMessage)
    }
  }

  /**
   * 异常
   */
  def test(): Unit = {
    throw new Exception("异常出现~")
  }

  /**
   * 函数
   *
   */

  def getRes(n1: Int, n2: Int, oper: Char) = {
    if (oper == "+") {
      n1 + n2
    } else if (oper == '-') {
      n1 - n2
    } else {
      null
    }
  }
}

class Dog {
  // 方法
  def sum(n0: Int, n2: Int): Int = {
    n0 + n2
  }

}
