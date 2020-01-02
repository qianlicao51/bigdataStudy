package com.study.scala

import scala.swing._
object Hello_Gui extends SimpleSwingApplication{

  def top = new MainFrame{
  val  time = "hello, scala gui"
    contents = new Button{
      text = "scala => spark!!!"
    }
  }
}
