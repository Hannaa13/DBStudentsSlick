import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

sealed abstract class TypeOfDepartment(val department: String)

object TypeOfDepartment {

  case object BioPhysics extends TypeOfDepartment("BioPhysics")

  case object Quantum extends TypeOfDepartment("Quantum Information")

  case object Solid extends TypeOfDepartment("Solid State Physics")

  case object Nano extends TypeOfDepartment("Functional Nanostructured Materials")

  def withName(`type`: String): TypeOfDepartment = allValues.find(_.department == `type`).getOrElse(throw new Exception("unexpected Department"))

  private val allValues: Seq[TypeOfDepartment] = Seq(BioPhysics, Quantum, Solid, Nano)

}

final case class Group(id: Option[Int],
                       name: String,
                       department: TypeOfDepartment,
                       curatorId: Int
                      )

trait GroupModel {

  this: CuratorModel =>


  implicit val typeOfDepartmentMapped: JdbcType[TypeOfDepartment] with BaseTypedType[TypeOfDepartment] = MappedColumnType.base[TypeOfDepartment, String](
    in => in.department,
    out => TypeOfDepartment.withName(out)
  )


  class GroupTable(tag: Tag) extends Table[Group](tag, "group") {

    def groupId = column[Option[Int]]("group_id", O.PrimaryKey)

    def name = column[String]("name")

    def department = column[TypeOfDepartment]("department")

    def curatorIdG = column[Int]("curator_idg")

    def curatorFK = foreignKey("curator_fk", curatorIdG, curators)(_.id)

    def * = (groupId, name, department, curatorIdG).mapTo[Group]
  }

  lazy val groups = TableQuery[GroupTable]
  lazy val groupSeq: Sequence[Int] = Sequence[Int]("group_seq")
}