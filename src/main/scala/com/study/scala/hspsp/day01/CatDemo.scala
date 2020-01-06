package com.study.scala.hspsp.day01

object CatDemo {
  def main(args: Array[String]): Unit = {
    val cat = new Cat
    cat.age = 10
    cat.name = "小白"
    cat.color = "白色"
    println(cat)
  }
}

class Cat {
  var name: String = _ //需要给定初始值
  // 1 声明使用var，在底层对于的是private age
  // 2 底层生成了get set
  var age: Int = _ //int默认值是0
  var color: String = _
}
