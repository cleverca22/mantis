package io.iohk.ethereum.utils

import io.iohk.ethereum.utils.RLP._
import io.iohk.ethereum.utils.RLPImplicits._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.FunSuite
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.spongycastle.util.encoders.Hex

class RLPSuite extends FunSuite
  with PropertyChecks
  with GeneratorDrivenPropertyChecks {

  test("Byte Encoding") {
    val expected = Array[Byte](0x80.toByte)
    val data = RLP.encode(0: Byte)
    assert(data.isSuccess)
    assert(expected sameElements data.get)
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: Byte = dataObtained.get
    assert((0: Byte) == obtained)

    val expected2 = Array[Byte](0x78.toByte)
    val data2 = RLP.encode(120: Byte)
    assert(data2.isSuccess)
    assert(expected2 sameElements data2.get)
    val dataObtained2 = RLP.decode(data2.get)
    assert(dataObtained2.isSuccess)
    val obtained2: Byte = dataObtained2.get
    assert((120: Byte) == obtained2)

    val expected3 = Array[Byte](0x7F.toByte)
    val data3 = RLP.encode(127: Byte)
    assert(data3.isSuccess)
    assert(expected3 sameElements data3.get)
    val dataObtained3 = RLP.decode(data3.get)
    assert(dataObtained2.isSuccess)
    val obtained3: Byte = dataObtained3.get
    assert((127: Byte) == obtained3)

    forAll(Gen.choose[Byte](Byte.MinValue, Byte.MaxValue)) {
      (aByte: Byte) => {
        val data = RLP.encode(aByte)
        assert(data.isSuccess)
        val dataObtained = RLP.decode(data.get)
        assert(dataObtained.isSuccess)
        val obtained: Byte = dataObtained.get
        assert(aByte == obtained)
      }
    }
  }

  test("Short Encoding") {
    val expected4 = Array[Byte](0x82.toByte, 0x76.toByte, 0x5F.toByte)
    val data4 = RLP.encode(30303.toShort)
    assert(data4.isSuccess)
    assert(expected4 sameElements data4.get)
    val dataObtained4 = RLP.decode(data4.get)
    assert(dataObtained4.isSuccess)
    val obtained4: Short = dataObtained4.get
    assert((30303: Short) == obtained4)

    val expected5 = Array[Byte](0x82.toByte, 0x4E.toByte, 0xEA.toByte)
    val data5 = RLP.encode(20202.toShort)
    assert(data5.isSuccess)
    assert(expected5 sameElements data5.get)
    val dataObtained5 = RLP.decode(data5.get)
    assert(dataObtained5.isSuccess)
    val obtained5: Short = dataObtained5.get
    assert((20202: Short) == obtained5)

    val expected6 = Array[Byte](0x82.toByte, 0x9D.toByte, 0x0A.toByte)
    val data6 = RLP.encode(40202.toShort)
    assert(data6.isSuccess)
    assert(expected6 sameElements data6.get)
    val dataObtained6 = RLP.decode(data6.get)
    assert(dataObtained6.isSuccess)
    val obtained6: Short = dataObtained6.get
    assert(40202.toShort == obtained6)

    forAll(Gen.choose[Short](Short.MinValue, Short.MaxValue)) {
      (aShort: Short) => {
        val data = RLP.encode(aShort)
        assert(data.isSuccess)
        val dataObtained = RLP.decode(data.get)
        assert(dataObtained.isSuccess)
        val obtained: Short = dataObtained.get
        assert(aShort == obtained)
      }
    }
  }

  test("String encoding") {
    val expected = Array[Byte](0x80.toByte)
    val data = RLP.encode("")
    assert(data.isSuccess)
    assert(expected sameElements data.get)
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: String = dataObtained.get
    assert("" == obtained)

    val expected2 = Array[Byte](0x90.toByte, 0x45.toByte, 0x74.toByte, 0x68.toByte, 0x65.toByte, 0x72.toByte, 0x65.toByte,
      0x75.toByte, 0x6D.toByte, 0x4A.toByte, 0x20.toByte, 0x43.toByte, 0x6C.toByte, 0x69.toByte, 0x65.toByte, 0x6E.toByte, 0x74.toByte)
    val data2 = RLP.encode("EthereumJ Client")
    assert(data2.isSuccess)
    assert(expected2 sameElements data2.get)
    val dataObtained2 = RLP.decode(data2.get)
    assert(dataObtained2.isSuccess)
    val obtained2: String = dataObtained2.get
    assert("EthereumJ Client" == obtained2)

    val expected3 = Array[Byte](
      0xAD.toByte, 0x45.toByte, 0x74.toByte, 0x68.toByte, 0x65.toByte, 0x72.toByte, 0x65.toByte,
      0x75.toByte, 0x6D.toByte, 0x28.toByte, 0x2B.toByte, 0x2B.toByte, 0x29.toByte, 0x2F.toByte,
      0x5A.toByte, 0x65.toByte, 0x72.toByte, 0x6F.toByte, 0x47.toByte, 0x6F.toByte, 0x78.toByte,
      0x2F.toByte, 0x76.toByte, 0x30.toByte, 0x2E.toByte, 0x35.toByte, 0x2E.toByte, 0x30.toByte,
      0x2F.toByte, 0x6E.toByte, 0x63.toByte, 0x75.toByte, 0x72.toByte, 0x73.toByte, 0x65.toByte,
      0x73.toByte, 0x2F.toByte, 0x4C.toByte, 0x69.toByte, 0x6E.toByte, 0x75.toByte, 0x78.toByte,
      0x2F.toByte, 0x67.toByte, 0x2B.toByte, 0x2B.toByte
    )
    val data3 = RLP.encode("Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++")
    assert(data3.isSuccess)
    assert(expected3 sameElements data3.get)
    val dataObtained3 = RLP.decode(data3.get)
    assert(dataObtained3.isSuccess)
    val obtained3: String = dataObtained3.get
    assert("Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++" == obtained3)

    val expected4 = Array[Byte](
      0xB8.toByte, 0x5A.toByte,
      0x45.toByte, 0x74.toByte, 0x68.toByte, 0x65.toByte, 0x72.toByte, 0x65.toByte,
      0x75.toByte, 0x6D.toByte, 0x28.toByte, 0x2B.toByte, 0x2B.toByte, 0x29.toByte, 0x2F.toByte,
      0x5A.toByte, 0x65.toByte, 0x72.toByte, 0x6F.toByte, 0x47.toByte, 0x6F.toByte, 0x78.toByte,
      0x2F.toByte, 0x76.toByte, 0x30.toByte, 0x2E.toByte, 0x35.toByte, 0x2E.toByte, 0x30.toByte,
      0x2F.toByte, 0x6E.toByte, 0x63.toByte, 0x75.toByte, 0x72.toByte, 0x73.toByte, 0x65.toByte,
      0x73.toByte, 0x2F.toByte, 0x4C.toByte, 0x69.toByte, 0x6E.toByte, 0x75.toByte, 0x78.toByte,
      0x2F.toByte, 0x67.toByte, 0x2B.toByte, 0x2B.toByte,

      0x45.toByte, 0x74.toByte, 0x68.toByte, 0x65.toByte, 0x72.toByte, 0x65.toByte,
      0x75.toByte, 0x6D.toByte, 0x28.toByte, 0x2B.toByte, 0x2B.toByte, 0x29.toByte, 0x2F.toByte,
      0x5A.toByte, 0x65.toByte, 0x72.toByte, 0x6F.toByte, 0x47.toByte, 0x6F.toByte, 0x78.toByte,
      0x2F.toByte, 0x76.toByte, 0x30.toByte, 0x2E.toByte, 0x35.toByte, 0x2E.toByte, 0x30.toByte,
      0x2F.toByte, 0x6E.toByte, 0x63.toByte, 0x75.toByte, 0x72.toByte, 0x73.toByte, 0x65.toByte,
      0x73.toByte, 0x2F.toByte, 0x4C.toByte, 0x69.toByte, 0x6E.toByte, 0x75.toByte, 0x78.toByte,
      0x2F.toByte, 0x67.toByte, 0x2B.toByte, 0x2B.toByte
    )
    val data4 = RLP.encode("Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++")
    assert(data4.isSuccess)
    assert(expected4 sameElements data4.get)
    val dataObtained4 = RLP.decode(data4.get)
    assert(dataObtained4.isSuccess)
    val obtained4: String = dataObtained4.get
    assert("Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++Ethereum(++)/ZeroGox/v0.5.0/ncurses/Linux/g++" == obtained4)

    val strGen = (n: Int) => Gen.choose(0, n).flatMap(long => Gen.listOfN(long, Gen.alphaChar).map(_.mkString))

    forAll(strGen(10000)) {
      (aString: String) => {
        val data = RLP.encode(aString)
        assert(data.isSuccess)
        val dataObtained = RLP.decode(data.get)
        assert(dataObtained.isSuccess)
        val obtained: String = dataObtained.get
        assert(aString == obtained)
      }
    }
  }

  test("Int Encoding") {
    val expected = Array[Byte](0x80.toByte)
    val data = RLP.encode(0)
    assert(data.isSuccess)
    assert(expected sameElements data.get)
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: Int = dataObtained.get
    assert(0 == obtained)

    val expected2 = Array(0x78.toByte)
    val data2 = RLP.encode(120)
    assert(data.isSuccess)
    assert(expected2 sameElements data2.get)
    val dataObtained2 = RLP.decode(data2.get)
    assert(dataObtained2.isSuccess)
    val obtained2: Int = dataObtained2.get
    assert(120 == obtained2)

    val expected3 = Array(0x7F.toByte)
    val data3 = RLP.encode(127)
    assert(data3.isSuccess)
    assert(expected3 sameElements data3.get)
    val dataObtained3 = RLP.decode(data3.get)
    assert(dataObtained3.isSuccess)
    val obtained3: Int = dataObtained3.get
    assert(127 == obtained3)

    val expected4 = Array(0x82.toByte, 0x76.toByte, 0x5F.toByte)
    val data4 = RLP.encode(30303)
    assert(data4.isSuccess)
    assert(expected4 sameElements data4.get)
    val dataObtained4 = RLP.decode(data4.get)
    assert(dataObtained4.isSuccess)
    val obtained4: Int = dataObtained4.get
    assert(30303 == obtained4)

    val expected5 = Array(0x82.toByte, 0x4E.toByte, 0xEA.toByte)
    val data5 = RLP.encode(20202)
    assert(data5.isSuccess)
    assert(expected5 sameElements data5.get)
    val dataObtained5 = RLP.decode(data5.get)
    assert(dataObtained5.isSuccess)
    val obtained5: Int = dataObtained5.get
    assert(20202 == obtained5)

    val expected6 = Array(0x83.toByte, 1.toByte, 0.toByte, 0.toByte)
    val data6 = RLP.encode(65536)
    assert(data6.isSuccess)
    assert(expected6 sameElements data6.get)
    val dataObtained6 = RLP.decode(data6.get)
    assert(dataObtained6.isSuccess)
    val obtained6: Int = dataObtained6.get
    assert(65536 == obtained6)

    val expected7 = Array(0x84.toByte, 0x80.toByte, 0x00.toByte, 0x00.toByte, 0x00.toByte)
    val data7 = RLP.encode(Integer.MIN_VALUE)
    assert(data7.isSuccess)
    assert(expected7 sameElements data7.get)
    val dataObtained7 = RLP.decode(data7.get)
    assert(dataObtained7.isSuccess)
    val obtained7: Int = dataObtained7.get
    assert(Integer.MIN_VALUE == obtained7)

    val expected8 = Array(0x84.toByte, 0x7F.toByte, 0xFF.toByte, 0xFF.toByte, 0xFF.toByte)
    val data8 = RLP.encode(Integer.MAX_VALUE)
    assert(data8.isSuccess)
    assert(expected8 sameElements data8.get)
    val dataObtained8 = RLP.decode(data8.get)
    assert(dataObtained8.isSuccess)
    val obtained8: Int = dataObtained8.get
    assert(Integer.MAX_VALUE == obtained8)

    val expected9 = Array(0x84.toByte, 0xFF.toByte, 0xFF.toByte, 0xFF.toByte, 0xFF.toByte)
    val data9 = RLP.encode(0xFFFFFFFF)
    assert(data9.isSuccess)
    assert(expected9 sameElements data9.get)
    val dataObtained9 = RLP.decode(data9.get)
    assert(dataObtained9.isSuccess)
    val obtained9: Int = dataObtained9.get
    assert(0xFFFFFFFF == obtained9)

    forAll(Gen.choose[Int](Int.MinValue, Int.MaxValue)) {
      (anInt: Int) => {
        val data = RLP.encode(anInt)
        assert(data.isSuccess)
        val dataObtained = RLP.decode(data.get)
        assert(dataObtained.isSuccess)
        val obtained: Int = dataObtained.get
        assert(anInt == obtained)
      }
    }
  }

  test("BigInt Encoding") {
    val expected = Array[Byte](0x80.toByte)
    val data = RLP.encode(BigInt(0))
    assert(data.isSuccess)
    assert(expected sameElements data.get)
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: BigInt = dataObtained.get
    assert(BigInt(0) == obtained)


    val bigInt = BigInt("100102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", 16);
    val expected2 = "a0100102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"
    val data2 = RLP.encode(bigInt)
    assert(data2.isSuccess)
    assert(expected2 equals Hex.toHexString(data2.get))
    val dataObtained2 = RLP.decode(data2.get)
    assert(dataObtained2.isSuccess)
    val obtained2: BigInt = dataObtained2.get
    assert(bigInt == obtained2)


    forAll(Arbitrary.arbitrary[BigInt]) {
      (aBigIntSigned: BigInt) => {
        val aBigInt = aBigIntSigned.abs
        val data = RLP.encode(aBigInt)
        assert(data.isSuccess)
        val dataObtained = RLP.decode(data.get)
        assert(dataObtained.isSuccess)
        val obtained: BigInt = dataObtained.get
        assert(aBigInt == obtained)
      }
    }
  }

  test("Byte Array Encoding") {
    val byteArr = "ce73660a06626c1b3fda7b18ef7ba3ce17b6bf604f9541d3c6c654b7ae88b239407f659c78f419025d785727ed017b6add21952d7e12007373e321dbc31824ba"
    val byteArray = Hex.decode(byteArr)
    val expected = "b840" + byteArr

    val data = RLP.encode(byteArray)
    assert(data.isSuccess)
    assert(expected equals Hex.toHexString(data.get))
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: Array[Byte] = dataObtained.get
    assert(byteArray sameElements obtained)

    forAll(Gen.nonEmptyListOf(Arbitrary.arbitrary[Byte])) {
      (aByteList: List[Byte]) => {
        val data = RLP.encode(aByteList.toArray)
        assert(data.isSuccess)
        val dataObtained = RLP.decode(data.get)
        assert(dataObtained.isSuccess)
        val obtained: Array[Byte] = dataObtained.get
        assert(aByteList.toArray sameElements obtained)
      }
    }
  }

  test("Encode Empty List") {
    val expected = "c0"
    val data = RLP.encode(RLPList())

    assert(data.isSuccess)
    assert(expected == Hex.toHexString(data.get))

    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: RLPList = dataObtained.get
    assert(RLPList() sameElements obtained)
  }

  test("Encode Short  List") {
    val expected = "c88363617483646f67"
    val data = RLP.encode(RLPList("cat", "dog"))
    assert(data.isSuccess)
    assert(expected == Hex.toHexString(data.get))
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: RLPList = dataObtained.get
    assert(RLPList("cat", "dog") sameElements obtained)

    val expected2 = "cc83646f6783676f6483636174"
    val data2 = RLP.encode(RLPList("dog", "god", "cat"))
    assert(data2.isSuccess)
    assert(expected2 == Hex.toHexString(data2.get))
    val dataObtained2 = RLP.decode(data2.get)
    assert(dataObtained2.isSuccess)
    val obtained2: RLPList = dataObtained2.get
    assert(RLPList("dog", "god", "cat") sameElements obtained2)
  }

  test("Encode Long  List") {
    val list = RLPList("cat", "Lorem ipsum dolor sit amet, consectetur adipisicing elit")
    val expected = "f83e83636174b8384c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e7365637465747572206164697069736963696e6720656c6974"
    val data = RLP.encode(list)
    assert(data.isSuccess)
    assert(expected == Hex.toHexString(data.get))
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: RLPList = dataObtained.get
    assert(list sameElements obtained)
  }

  test("Encode multilist") {
    val expected = "cc01c48363617483646f67c102"
    val multilist = RLPList(1, RLPList("cat"), "dog", RLPList(2))
    val data = RLP.encode(multilist)
    assert(data.isSuccess)
    assert(expected == Hex.toHexString(data.get))
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: RLPList = dataObtained.get
    assert(multilist sameElements obtained)

    val multilist2 = RLPList(RLPList("cat", "dog"), RLPList(1, 2), RLPList())
    val expected2 = "cdc88363617483646f67c20102c0"
    val data2 = RLP.encode(multilist2)
    assert(data2.isSuccess)
    assert(expected2 == Hex.toHexString(data2.get))
    val dataObtained2 = RLP.decode(data2.get)
    assert(dataObtained2.isSuccess)
    val obtained2: RLPList = dataObtained2.get
    assert(multilist2 sameElements obtained2)
  }

  test("Encode Empty List Of List") {
    val emptyListOfList = RLPList(RLPList(RLPList(), RLPList()), RLPList())
    val expected = "c4c2c0c0c0"
    val data = RLP.encode(emptyListOfList)
    assert(data.isSuccess)
    assert(expected == Hex.toHexString(data.get))
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: RLPList = dataObtained.get
    assert(emptyListOfList sameElements obtained)
  }

  test("Encode Rep Of Two List Of List") {
    val twoListOfList = RLPList(RLPList(), RLPList(RLPList()), RLPList(RLPList(), RLPList(RLPList())))
    val expected = "c7c0c1c0c3c0c1c0"
    val data = RLP.encode(twoListOfList)
    assert(data.isSuccess)
    assert(expected == Hex.toHexString(data.get))
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: RLPList = dataObtained.get
    assert(twoListOfList sameElements obtained)
  }

  test("https://github.com/ethereum/tests/blob/master/rlptest.txt") {
    for (
      input: (RLPEncodeable, String) <- rlpTestData
    ) {
      val data = RLP.encode(input._1)
      assert(data.isSuccess)
      assert(input._2 == Hex.toHexString(data.get))
      val dataObtained = RLP.decode(data.get)
      assert(dataObtained.isSuccess)
      val obtained: RLPEncodeable = dataObtained.get
      assert(input._1 == obtained)
    }
  }

  val rlpTestData: Seq[(RLPEncodeable, String)] = Seq(
    (0: RLPValue) -> "80",
    ("": RLPValue) -> "80",
    ("d": RLPValue) -> "64",
    ("cat": RLPValue) -> "83636174",
    ("dog": RLPValue) -> "83646f67",
    RLPList("cat", "dog") -> "c88363617483646f67",
    RLPList("dog", "god", "cat") -> "cc83646f6783676f6483636174",
    (1: RLPValue) -> "01",
    (10: RLPValue) -> "0a",
    (100: RLPValue) -> "64",
    (1000: RLPValue) -> "8203e8",
    (BigInt("115792089237316195423570985008687907853269984665640564039457584007913129639935"): RLPValue) -> "a0ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
    (BigInt("115792089237316195423570985008687907853269984665640564039457584007913129639936"): RLPValue) -> "a1010000000000000000000000000000000000000000000000000000000000000000",
    RLPList(1, 2, RLPList()) -> "c30102c0",
    RLPList(RLPList(RLPList(), RLPList()), RLPList()) -> "c4c2c0c0c0",
    RLPList("zw", RLPList(4), "wz") -> "c8827a77c10482777a"
  )

  test("SimpleBlock encoding") {
    val tx0 = SimpleTransaction(1, "cat")
    val tx1 = SimpleTransaction(2, "dog")
    val block = SimpleBlock(127, -127: Short, "horse", 1000, Seq[SimpleTransaction](tx0, tx1), Seq[Int](1, 2))
    val data = RLP.encode(block)
    assert(data.isSuccess)
    val dataObtained = RLP.decode(data.get)
    assert(dataObtained.isSuccess)
    val obtained: SimpleBlock = dataObtained.get
    assert(block == obtained)
  }

  ignore("Performance decode") {
    val performanceEnabled: Boolean = true
    if (performanceEnabled) {
      val blockRaw: String = "f8cbf8c7a00000000000000000000000000000000000000000000000000000000000000000a01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347940000000000000000000000000000000000000000a02f4399b08efe68945c1cf90ffe85bbe3ce978959da753f9e649f034015b8817da00000000000000000000000000000000000000000000000000000000000000000834000008080830f4240808080a004994f67dc55b09e814ab7ffc8df3686b4afb2bb53e60eae97ef043fe03fb829c0c0"
      val payload: Array[Byte] = Hex.decode(blockRaw)
      val ITERATIONS: Int = 10000000
      println("Starting " + ITERATIONS + " decoding iterations...")
      val start1: Long = System.currentTimeMillis
      (1 to ITERATIONS).foreach { _ => decode(payload); Unit }
      val end1: Long = System.currentTimeMillis
      println("Result RLP.decode()\t: " + (end1 - start1) + "ms")
    }
    else println("Performance test for RLP.decode() disabled")
  }

  test("Ping message encoded using EthereumJ implementation") {
    val decoded = RLP.decode(Hex.decode("ee04d38c38352e36352e31392e32333182765f82765fd38c38352e36352e31392e32333182765f82765f845855366e"))
    val parsed: PingMessage = decoded.get

    assert(parsed.version == 4)
    assert(new String(parsed.from.address) == "85.65.19.231")
    assert(parsed.from.tcpPort == 30303)
    assert(parsed.from.udpPort == 30303)

    assert(new String(parsed.to.address) == "85.65.19.231")
    assert(parsed.to.tcpPort == 30303)
    assert(parsed.to.udpPort == 30303)

    assert(parsed.expiration == 1481979502)
  }

}

// FIXME this a port from EthereumJ Ping message in order to check parsing, once we define the this entities in
// our codebase, this should be removed
case class Endpoint(address: Array[Byte], tcpPort: Long, udpPort: Long) extends RLPList {
  override def items: Seq[RLPEncodeable] = Seq(address, tcpPort, udpPort)
}

object Endpoint {
  implicit def fromRLPEncodeable(enc: RLPEncodeable): Endpoint = {
    enc match {
      case l: RLPList => Endpoint(l.items.head, l.items(1), l.items(2))
      case _ => throw new RuntimeException("Not an Edndpoint")
    }
  }
}

object SimpleTransaction {
  implicit def fromRLPEncodeable(encTx: RLPEncodeable): SimpleTransaction = {
    encTx match {
      case encTx: RLPList if encTx.items.size == 2 =>
        SimpleTransaction(encTx.items(0), encTx.items(1))
      case _ => throw new Exception("Can't transform RLPEncodeable to tx")
    }
  }
}

case class SimpleTransaction(id: Int, name: String) extends RLPList {
  def items: Seq[RLPEncodeable] = Seq(id, name)
}

object SimpleBlock {
  implicit def fromRLPEncodeable(encBlock: RLPEncodeable): SimpleBlock = {
    encBlock match {
      case encBlock: RLPList if encBlock.items.size == 6 =>
        val txs: Seq[SimpleTransaction] = encBlock.items(4) match {
          case txs: RLPList => txs.items.map(implicitly[RLPEncodeable => SimpleTransaction])
          case _ => throw new Exception("Can't transaform txs to Seq[SimpleTransaction]")
        }
        val unclesIds: Seq[Int] = encBlock.items(5) match {
          case unclesIds: RLPList => unclesIds.items.map(implicitly[RLPEncodeable => Int])
          case _ => throw new Exception("Can't transaform unclesIds to Seq[Int]")
        }
        SimpleBlock(encBlock.items(0), encBlock.items(1), encBlock.items(2), encBlock.items(3), txs, unclesIds)
      case _ => throw new Exception("Can't transform RLPEncodeable to block")
    }
  }
}

case class SimpleBlock(id: Byte, parentId: Short, owner: String, nonce: Int, txs: Seq[SimpleTransaction], unclesIds: Seq[Int]) extends RLPList {
  def items: Seq[RLPEncodeable] = Seq(id, parentId, owner, nonce, RLPList(txs), RLPList(unclesIds))
}

case class PingMessage(version: Int, from: Endpoint, to: Endpoint, expiration: Long) extends RLPList {
  override def items: Seq[RLPEncodeable] = Seq(version, from, to, expiration)
}

object PingMessage {
  implicit def fromRLPEncodeable(enc: RLPEncodeable): PingMessage = {
    enc match {
      case l: RLPList => PingMessage(l.items.head, l.items(1), l.items(2), l.items(3))
      case _ => throw new RuntimeException("Not a Ping Message")
    }
  }
}
