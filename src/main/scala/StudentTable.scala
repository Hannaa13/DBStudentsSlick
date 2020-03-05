import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

final case class Student(
                          id: Int,
                          name: String,
                          groupId: Int,
                          averageMark: String,
                          curatorId: Int
                        )

trait StudentModel {

  this: CuratorModel with GroupModel =>

  class StudentTable(tag: Tag) extends Table[Student](tag, "student") {

    def studentId = column[Int]("student_id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def groupIdS = column[Int]("group_ids")

    def averageMark = column[String]("averageMark")

    def curatorIdS = column[Int]("curator_ids")

    def curatorFK = foreignKey("curator_fks", curatorIdS, curators)(_.id)

    def groupFK = foreignKey("group_fk", groupIdS.?, groups)(_.groupId)

    def * = (studentId, name, groupIdS, averageMark, curatorIdS).mapTo[Student]
  }

  lazy val students = TableQuery[StudentTable]
  lazy val studentSeq: Sequence[Int] = Sequence[Int]("student_seq")


}

