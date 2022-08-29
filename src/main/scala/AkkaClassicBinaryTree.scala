import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import ClassicBinaryTreeOps._

/**
  * Simple wrapper to lock in a type for the tree.
  */
class AkkaClassicBinaryTree[T]( actorSystem: ActorSystem ) {
  implicit private val timeout: Timeout = Timeout( 1.second )
  private val root = actorSystem.actorOf( Props[BinaryTreeNode] )

  def put( weight: Double, value: T ): Future[PutResult] = (root ? Put( weight, value )).mapTo[PutResult]
  def get( minWeight: Double, maxWeight: Double ): Future[GetResult[T]] = (root ? Get( minWeight, maxWeight )).mapTo[GetResult[T]]
}

object ClassicBinaryTreeOps {

  /**
    * Adds to the binary tree. Response will indicate if it is a new value or replaces an existing value.
    */
  case class Put[T]( weight: Double, value: T )
  case class PutResult( newValue: Boolean )

  /**
    * Retrieves items over a range in the tree.
    */
  case class Get( minWeight: Double, maxWeight: Double )
  case class GetResult[T]( items: Seq[T] )
}

/**
  * A node for a binary tree. Tree should operate concurrently. Each operation should block necessary portions of the tree so that
  * it stays synchronized. The blocking should however be minimal so that multiple requests can be handled concurrently but the result is
  * completely deterministic based on the order of incoming requests.
  */
class BinaryTreeNode extends Actor {
  override def receive: Receive = ???
}
