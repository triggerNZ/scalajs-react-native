package triggernz.reactnative.core

import japgolly.scalajs.react.CallbackTo
import scalaz.Monad
import scalaz.syntax.monad._
import japgolly.scalajs.react.ScalazReact._

class ContT[R, M[_], +A](val run: (A => M[Unit]) => M[R])(implicit M: Monad[M]) {
  def apply(k: A => M[Unit]): M[R] = run(k)

  def flatMap[B](k: A => ContT[R, M, B]): ContT[R, M, B] = {
    ContT[R, M, B] { g: (B => M[Unit]) =>
      run(a => k(a).run(g) >> M.point(()))
    }
  }

  def map[B](k: A => B): ContT[R, M, B] =
    ContT[R, M, B](z => apply(z compose k))
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
  }

  def apply[R, M[_]: Monad, T](run: (T => M[Unit]) => M[R]) = new ContT(run)
}