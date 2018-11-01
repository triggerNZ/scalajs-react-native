package triggernz.reactnative.core
import utest._

import PlatformIO._

object PlatformTests {
  val tests = Tests {
    'cannotComposeAndroidAndIos - {
      val android2 = AndroidIO(2)


      android2.flatMap(x => AndroidIO(x + 1))
    }
  }
}
