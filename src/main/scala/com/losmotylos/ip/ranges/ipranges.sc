import com.risksense.ipaddr.{IpAddress, IpNetwork, IpRange, IpSet}

import scala.collection.immutable

val r1 = IpRange("197.203.0.0", "197.206.9.255")
val r2 = IpRange("197.204.0.0", "197.204.0.24")
val s1 = IpSet(r1)
val s2 = IpSet(r2)


val tst = IpSet(IpRange("197.204.0.0", "197.204.0.26"))

val c1 = IpSet(IpRange("197.204.0.25", "197.204.0.25"))
val c2 = IpSet(IpRange("197.204.0.26", "197.204.0.26"))
val c3 = IpSet(IpRange("197.204.0.27", "197.204.0.27"))

val cr1 = IpRange("197.204.0.1", "197.204.0.25")
val cr2 = IpRange("197.204.0.26", "197.204.0.29")
val cr3 = IpRange("197.204.0.30", "197.204.0.50")

val cc = c1 | c2 | c3
val gg = c1 | c2
gg.isContiguous
gg.size

def join(c1: IpRange, c2: IpRange): Seq[IpRange] = {
  val gg = IpSet(c1) | IpSet(c2)
  if(gg.isContiguous) {
    Seq(gg.ipRange)
  } else {
    Seq(c1, c2)
  }
}


def joinS(c1: Seq[IpRange], c2: Seq[IpRange]): Seq[IpRange] = {
  val gg = IpSet(c1.last) | IpSet(c2.last)
  if(gg.isContiguous) {
    val allButLast: Seq[IpRange] = c1.slice(0, c1.size - 1)
    allButLast :+ gg.ipRange
  } else {
    c1 ++ c2
  }
}


val qq1 = join(cr1, cr2)
val qq2 = join(cr1, cr3)

val nn: IndexedSeq[Seq[IpRange]] = cc.networkSeq.sorted.map(n => Seq(IpRange(IpAddress(n.first).toString, IpAddress(n.last).toString)))
val nnRed = nn.reduceLeft((a, b) => joinS(a, b))


cc.isContiguous
cc.networkSeq.sorted.map{n =>
  println("------")
  println("F: " + IpAddress(n.first))
  println("L: " + IpAddress(n.last))
  println("------")
//  IpRange(n.first, n.last)
}

val q1 = IpSet(IpRange("197.203.0.0", "197.206.9.255"))
val q2 = IpSet(IpRange("197.204.0.0", "197.204.0.24"))
val q3 = IpSet(IpRange("201.233.7.160", "201.233.7.168"))
val q4 = IpSet(IpRange("201.233.7.164", "201.233.7.168"))
val q5 = IpSet(IpRange("201.233.7.167", "201.233.7.167"))
val q6 = IpSet(IpRange("203.133.0.0", "203.133.255.255"))


//val sss: IpSet = q1 ^ q2 ^ q3 ^ q4 ^ q5 ^ q6 ^ (q1 & q2 & q3) ^ (q2 & q3 & q4) ^ (q3 & q4 & q5) ^ (q4 & q5 & q6)
//val sss =  q3 ^ q4 ^ q5 ^ (q3 & q4 & q5)
//val sss =  q1 & q2 & q3 & q4 & q5 & q6

val input: Seq[IpSet] = Seq(q1, q2, q3, q4, q5, q6)

val tt = Seq("a", "b", "c", "d", "e", "f")
//tt.slice(0, 3)
//tt.slice(1, 4)
//tt.slice(2, 5)
//tt.slice(3, 6)
//tt.slice(4, 7)
val ttC = (3 to tt.size - 1).map{ i =>

  (0 to tt.size - i).map{j =>
    val wi = tt.slice(j, j + i)
    println(wi)
    wi
  }
}


//val ssss1: IpSet = input.reduceLeft((a, b) => a ^ b)
val symmetricDiffOfInput = input.reduceLeft((a, b) => a ^ b)

val symmetricDiffOfIntersections = (3 to input.size - 1).map{ i =>
  val slidingIntersections = (0 to input.size - i).map{j =>
    input
      .slice(j, j + i)
      .reduceLeft((a, b) => a & b)
  }

  slidingIntersections.reduceLeft((a, b) => a ^ b)
}.reduceLeft((a, b) => a ^ b)

val sss = symmetricDiffOfInput ^ symmetricDiffOfIntersections

val ssss: IndexedSeq[Seq[IpRange]] =
  sss.networkSeq.sorted
    .map(n => IpRange(IpAddress(n.first).toString, IpAddress(n.last).toString))
    .map(Seq(_))
val toGo = ssss.reduceLeft((a, b) => joinS(a, b))




//val ss: IpSet = s1 - r2
//val common = s1.intersect(s2)
//val all = s1 | s2
//val res = all.diff(common)
//IpSet(res.toSeq)


//val ss: IpSet = s1.symmetricDiff(s2).map(_.network)
//ss.networkSeq.foreach{n =>
//  println("------")
//  println("F: " + IpAddress(n.first))
//  println("L: " + IpAddress(n.last))
//  println("------")
//}
//IpAddress(ss.first)
//IpAddress(ss.last)


//val n5 = IpNetwork("192.0.2.1/24")
//n5.size
//IpAddress(n5.first)
//IpAddress(n5.last)