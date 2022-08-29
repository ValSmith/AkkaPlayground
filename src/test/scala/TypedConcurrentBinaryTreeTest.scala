import AkkaTypedBinaryTree._
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorRef
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Await

class TypedConcurrentBinaryTreeTest extends AnyWordSpec with BeforeAndAfterAll with Matchers {

  private val testKit = ActorTestKit()

  override def afterAll(): Unit = testKit.shutdownTestKit()

  "Concurrent Binary Tree" should {
    "return empty" in {
      val tree: ActorRef[BinaryTreeOperations[String]] = testKit.spawn( AkkaTypedBinaryTree[String]() )
      val probe = testKit.createTestProbe[GetResult[String]]()
      tree ! Get( -10, 10, probe.ref )
      probe.expectMessage( GetResult( Seq[String]() ) )
    }

    "return selection" in {
      val tree: ActorRef[BinaryTreeOperations[String]] = testKit.spawn( AkkaTypedBinaryTree[String]() )
      val probe = testKit.createTestProbe[PutResult]()
      (0 until 20).foreach( i => tree ! Put( i / 20.0, i.toString, probe.ref ) )
      probe.receiveMessages( 20 )

      val getProbe = testKit.createTestProbe[GetResult[String]]()
      tree ! Get( 0.1, 0.3, getProbe.ref )
      getProbe.expectMessage( GetResult( Seq( "2", "3", "4", "5" ) ) )
    }

    "maintain synchronization" in {
      val tree: ActorRef[BinaryTreeOperations[String]] = testKit.spawn( AkkaTypedBinaryTree[String]() )
      (0 until 20).foreach( i => tree ! Put( i / 20.0, i.toString, testKit.createTestProbe[PutResult]().ref ) )

      val getProbe = testKit.createTestProbe[GetResult[String]]()
      val putProbe = testKit.createTestProbe[PutResult]()
      tree ! Get( 0.1, 0.3, getProbe.ref )
      tree ! Put( 0.15, "NEW", putProbe.ref )
      tree ! Get( 0.1, 0.3, getProbe.ref )

      putProbe.expectMessage( PutResult( true ) )
      getProbe.expectMessage( GetResult( Seq( "2", "3", "4", "5" ) ) )
      getProbe.expectMessage( GetResult( Seq( "2", "NEW", "4", "5" ) ) )
    }
  }

}
