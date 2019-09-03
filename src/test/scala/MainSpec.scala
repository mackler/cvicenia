package org.mackler.cvičenie

import org.scalatest._

class MainSpec extends FlatSpec with Matchers {
  "The Main object" should "pass a test" in {
    (1 + 1) shouldEqual 2
  }
}
