
import slick.dbio.{DBIO, Effect}
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction

import scala.concurrent.ExecutionContext


trait GroupDAO {

//  def init: DBIO[Unit]

//  def initIdGenerator: SqlAction[Int, NoStream, Effect]

  def insert(group: Group): DBIO[Group]

  def delete: DBIOAction[Unit, NoStream, Effect.Write]

  def zipCurator: DBIO[Seq[(String, String)]]
}

class GroupDAOImpl(groupModel: GroupModel with CuratorModel)
                    (implicit executionContext: ExecutionContext) extends GroupDAO {

  import groupModel.{groups, groupSeq, curators}

//  def init = groups.schema.create
//  def initIdGenerator: SqlAction[Int, NoStream, Effect] = sqlu"""create sequence  "group_id_seq" start with 1 increment by 1;"""

  def insert(group: Group): DBIO[Group] = {
    for {
      newId <- groupSeq.next.result
      gr = group.copy(id = Some(newId))
      _ <- groups += gr
    } yield gr
  }

  def delete: DBIOAction[Unit, NoStream, Effect.Write] = {
    for {
      _ <- DBIO.seq(groups.delete)
    } yield ()
  }



  def zipCurator: DBIO[Seq[(String, String)]] = {
    for {
      (g, c) <- groups zip curators
    } yield (g.name, c.name)
  }.result

}
