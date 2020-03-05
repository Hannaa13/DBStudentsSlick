
import slick.dbio.{DBIO, Effect}
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction

import scala.concurrent.ExecutionContext


trait CuratorDao {

  //  def init: DBIO[Unit]

  //  def initIdGenerator: SqlAction[Int, NoStream, Effect]

  def insert(curator: Curator): DBIO[Curator]

  def delete: DBIOAction[Unit, NoStream, Effect.Write]
}

class CuratorDAOImpl(curatorModel: CuratorModel)
                    (implicit executionContext: ExecutionContext) extends CuratorDao {

  import curatorModel.{curators, curatorsSeq}

  //  def init = curators.schema.create

  //  def initIdGenerator: SqlAction[Int, NoStream, Effect] = sqlu"""create sequence  "curator_seq" start with 1 increment by 1;"""

  def insert(curator: Curator): DBIO[Curator] = {
    for {
      newId <- curatorsSeq.next.result
      cr = curator.copy(id = newId)
      _ <- curators += cr
    } yield cr
  }

  def delete: DBIOAction[Unit, NoStream, Effect.Write] = {
    for {
      _ <- DBIO.seq(curators.delete)
    } yield ()
  }


}
