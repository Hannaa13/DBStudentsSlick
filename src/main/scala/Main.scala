
import slick.dbio.Effect
import slick.jdbc.PostgresProfile.api._
import slick.sql.FixedSqlAction

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  val connectionUrl = "jdbc:postgresql://localhost/students?user=postgres&password=postgres"
  val db = Database.forURL(connectionUrl, driver = "org.postgresql.Driver")

  val studentModel = new StudentModel
    with CuratorModel
    with GroupModel {}
  val studentDAO = new StudentDAOImpl(studentModel)

  val curatorModel = new CuratorModel {}
  val curatorDAO = new CuratorDAOImpl(curatorModel)

  val groupModel = new GroupModel
    with CuratorModel {}
  val groupDAO = new GroupDAOImpl(groupModel)



  val curator1 = Curator(
    id = 1,
    name = "Ivanov D. V.",
    degree = AcademicDegree.Assistant
  )
//  Await.result(db.run(curatorDAO.insert(curator1)), Duration.Inf)


  val student1 = Student(
    id = 1,
    name = "Mark",
    groupId = 2,
    averageMark = "B",
    curatorId = 1
  )
//  Await.result(db.run(studentDAO.insert(student1)), Duration.Inf)


  val group1 = Group(
    id = None,
    name = "Quantum Physics",
    department = TypeOfDepartment.Quantum,
    curatorId = 1
  )
//  Await.result(db.run(groupDAO.insert(group1)), Duration.Inf)

   val res = Await.result(db.run(studentDAO.selectStudent(Seq( "B"))), Duration.Inf)
  println(res)
}
