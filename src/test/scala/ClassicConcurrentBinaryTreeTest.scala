import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import ClassicBinaryTreeOps._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ClassicConcurrentBinaryTreeTest()
    extends TestKit( ActorSystem( "MySpec" ) )
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll(): Unit =
    TestKit.shutdownActorSystem( system )

  "Concurrent Binary Tree" should {
    "return empty" in {
      val tree = new AkkaClassicBinaryTree[String]( system )
      tree.get( -10, 10 ) shouldEventuallyBe GetResult( Seq() )
    }

    "return selection" in {
      val tree = new AkkaClassicBinaryTree[String]( system )
      (0 until 20).foreach( i => Await.ready( tree.put( i / 20.0, i.toString ), 1.second ) )
      tree.get( 0.1, 0.3 ) shouldEventuallyBe GetResult( Seq( "2", "3", "4", "5" ) )
    }

    "maintain synchronization" in {
      val tree = new AkkaClassicBinaryTree[String]( system )
      (0 until 20).map( i => tree.put( i / 20.0, i.toString ) )

      val result = tree.get( 0.1, 0.3 )
      tree.put( 0.15, "NEW" ) shouldEventuallyBe PutResult( true )
      result shouldEventuallyBe GetResult( Seq( "2", "3", "4", "5" ) )
      tree.get( 0.1, 0.3 ) shouldEventuallyBe GetResult( Seq( "2", "NEW", "4", "5" ) )
    }
  }

  implicit private class FutureAssert[T]( actual: Future[T] ) {
    def shouldEventuallyBe( expected: T ): Unit = Await.result( actual, 2.seconds ) shouldBe expected
  }
}
