package com.study.scala.booka.char04

abstract class Expr
case class Car(name:String) extends Expr
case class Number(num:Double) extends Expr
case class UnOp(operator :String,arg:Expr) extends Expr
case class BinOp(operator:String,left: Expr,right: Expr) extends Expr
