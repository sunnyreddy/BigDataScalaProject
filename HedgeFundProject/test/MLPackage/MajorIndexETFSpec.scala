package MLPackage
import org.scalatest.{FlatSpec, Matchers}
import MLPackage.MajorIndexETF.{highest}

class MajorIndexETFSpec extends FlatSpec with Matchers {

    behavior of "Helpers"

    it should "get higest 3" in {
      highest(List(1,2,3),1) shouldBe 3
    }
  // other function return value keeps changing with time, so won't have a specific range

}
