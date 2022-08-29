import akka.actor.typed.{ActorRef, Behavior}

object AkkaTypedBinaryTree {
  sealed trait BinaryTreeOperations[T]

  /**
    * Adds to the binary tree. Response will indicate if it is a new value or replaces an existing value.
    */
  case class Put[T]( weight: Double, value: T, replyTo: ActorRef[PutResult] ) extends BinaryTreeOperations[T]
  case class PutResult( newValue: Boolean )

  /**
    * Retrieves items over a range in the tree.
    */
  case class Get[T]( minWeight: Double, maxWeight: Double, replyTo: ActorRef[GetResult[T]] ) extends BinaryTreeOperations[T]
  case class GetResult[T]( items: Seq[T] )

  /**
    * A node for a binary tree. Tree should operate concurrently. Each operation should block necessary portions of the tree so that
    * it stays synchronized. The blocking should however be minimal so that multiple requests can be handled concurrently but the result is
    * completely deterministic based on the order of incoming requests.
    */
  def apply[T](): Behavior[BinaryTreeOperations[T]] = ???
}
