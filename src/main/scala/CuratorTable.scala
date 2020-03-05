import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

sealed abstract class AcademicDegree(val value: String)

object AcademicDegree {

  case object Assistant extends AcademicDegree("Assistant")

  case object Doctor extends AcademicDegree("Doctor")

  case object Professor extends AcademicDegree("Professor")

  def withName(`type`: String): AcademicDegree = allValues.find(v => v.value == `type`).getOrElse(throw new Exception("unexpected Degree"))

  private val allValues: Seq[AcademicDegree] = Seq(Assistant, Doctor, Professor)

}


final case class Curator(
                          id: Int,
                          name: String,
                          degree: AcademicDegree
                        )


trait CuratorModel {

  implicit val typeOfDegreeMapped: JdbcType[AcademicDegree] with BaseTypedType[AcademicDegree] = MappedColumnType.base[AcademicDegree, String](
    in => in.value,
    out => AcademicDegree.withName(out)
  )

  class CuratorTable(tag: Tag) extends Table[Curator](tag, "curator") {

    def id = column[Int]("curator_id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def degree = column[AcademicDegree]("degree")

    override def * = (id, name, degree).mapTo[Curator]

  }


  lazy val curators = TableQuery[CuratorTable]
  lazy val curatorsSeq: Sequence[Int] = Sequence[Int]("curator_seq")
}
