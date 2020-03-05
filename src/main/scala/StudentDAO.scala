import slick.dbio.{DBIO, Effect}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction, SqlAction}

import scala.concurrent.ExecutionContext


trait StudentDao {

//  def init: DBIO[Unit]
//  def initIdGenerator: SqlAction[Int, NoStream, Effect]

  def insert(student: Student): DBIO[Student]

  def selectMarkA: DBIO[Seq[String]]

  def delete: DBIOAction[Unit, NoStream, Effect.Write]

  def leftJoinFilterId: DBIO[Seq[StudentsAndCurators]]

  def joinAllTable: DBIO[Seq[(TypeOfDepartment, String, String)]]

  def selectStudent(m: Seq[String]): DBIO[Seq[String]]

}

class StudentDAOImpl(studentModel: StudentModel with CuratorModel with GroupModel)
                    (implicit executionContext: ExecutionContext) extends StudentDao {

  import studentModel.{students, studentSeq, curators, groups}

//  def init = students.schema.create

//  def initIdGenerator: SqlAction[Int, NoStream, Effect] = sqlu"""create sequence  "stud_id_seq" start with 1 increment by 1;"""

  def insert(student: Student): DBIO[Student] = {
    for {
      newId <- studentSeq.next.result
      st = student.copy(id = newId)
      _ <- students += st

    } yield st
  }

  def selectMarkA: DBIO[Seq[String]] = {
    students
      .filterOpt(Option("A")) {
        _.column[String]("averageMark") === _
      }
      .map(_.name)
      .result

  }

  def leftJoinFilterId: DBIO[Seq[StudentsAndCurators]] = {
    val r = for {
      (s, c) <- students joinLeft curators on (_.curatorIdS === _.id)
    } yield (s, c)
    r.result.map(_.map {
      case (st, cr) => StudentsAndCurators(st.name, cr.map(_.name))
    })
  }

  def delete: DBIOAction[Unit, NoStream, Effect.Write] = {
    for {
      _ <- DBIO.seq(students.delete)

    } yield ()
  }


  def joinAllTable: DBIO[Seq[(TypeOfDepartment, String, String)]] = {
    val res = for {
      g <- groups
      s <- students if g.groupId === s.groupIdS
      c <- curators if s.curatorIdS === c.id
    } yield (g, s, c)

    res.result.map(_.map {
      case (g, s, c) => (g.department, s.name, c.name)
    })
  }


  def selectStudent(m: Seq[String]): DBIO[Seq[String]] = {
    students
      .filter(_.averageMark inSet m)
      .map(_.name)
  }.result


}

