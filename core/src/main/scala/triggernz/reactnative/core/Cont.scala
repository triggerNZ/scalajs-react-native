package triggernz.reactnative.core

import cats.effect.IO

import scalajs.js
import japgolly.scalajs.react.CallbackTo
import scalaz.syntax.monad._
import japgolly.scalajs.react.ScalazReact._
import japgolly.scalajs.react._

import scala.scalajs.js.{Thenable, |}

class ContT[R, M[_], +A](val run: (A => M[Unit]) => M[R])(implicit M: scalaz.Monad[M]) {
  def apply(k: A => M[Unit]): M[R] = run(k)

  def flatMap[B](k: A => ContT[R, M, B]): ContT[R, M, B] = {
    ContT[R, M, B] { g: (B => M[Unit]) =>
      run(a => k(a).run(g) >> M.point(()))
    }
  }

  def map[B](k: A => B): ContT[R, M, B] =
    ContT[R, M, B](z => apply(z compose k))

  def voidR: ContT[Unit, M, A] = new ContT[Unit, M, A] (run.andThen(M.void))
}

object ContT {
  type Async[A] = ContT[Unit, CallbackTo, A]
  type AsyncE[E, V] = Async[Either[E, V]]
  type AsyncR[A, R] = ContT[R, CallbackTo, A]
  type AsyncRE[E,V,R] = AsyncR[Either[E,V], R]

  object AsyncRE {
    def apply[E, V, R](run: (Either[E, V] => CallbackTo[Unit]) => CallbackTo[R]): AsyncRE[E, V, R] =
      new ContT[R, CallbackTo, Either[E, V]](run)
  }

  object AsyncE {
    def apply[E, V](run: (Either[E, V] => CallbackTo[Unit]) => CallbackTo[Unit]): AsyncE[E, V] =
      new ContT[Unit, CallbackTo, Either[E, V]](run)

    def point[E, V](ev: Either[E, V]): AsyncE[E, V] = apply(cb => cb(ev))

    def fromPromise[E, V](p: => js.Promise[V]): AsyncE[Any, V] = apply[Any, V] { cb =>
      Callback {
        val k = CallbackKleisli(cb)
        val errHandler: js.Function1[Any, Unit | Thenable[Unit]] = { (e: Any) =>
          k.contramap[Any](Left.apply).toJsFn(e)
        }
        p.`then`[Unit]({ v => k.contramap[V](Right.apply).toJsFn(v) }, errHandler)
      }
    }

    def fromIO[T](iot: IO[T]): AsyncE[Throwable, T] = AsyncE { cb =>
      Callback {
        iot.unsafeRunAsync(eort => cb(eort).runNow())
      }
    }
  }

  object Async {
    def apply[V](run: (V => CallbackTo[Unit]) => CallbackTo[Unit]): Async[V] =
      new ContT[Unit, CallbackTo, V](run)

    def point[V](v: => V): Async[V] = apply(cb => cb(v))

    def fromPromise[V](p: => js.Promise[V]): Async[V] = apply[V] { cb =>
      Callback {
        val k = CallbackKleisli(cb)
        p.`then`[Unit]({ v => k.toJsFn(v) }, js.undefined)
      }
    }

    object instances {
      implicit val catsInstances: cats.Monad[Async]  = new cats.Monad[Async] with cats.StackSafeMonad [Async] {
        override def flatMap[A, B](fa: Async[A])(f: A => Async[B]): Async[B] = fa.flatMap(f)

        override def pure[A](x: A): Async[A] = Async.point(x)
        }
      }
  }

  def apply[R, M[_]: scalaz.Monad, T](run: (T => M[Unit]) => M[R]) = new ContT(run)

}