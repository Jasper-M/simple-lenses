/*
 * Copyright 2017 Jasper Moeys
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simplelenses

object implicits {
  import scala.language.experimental.macros
  import scala.annotation.compileTimeOnly

  implicit class AssignSyntax[T](expr: T) {
    @compileTimeOnly("`a <~ b` expressions should only be used inside a `but`.")
    def <~(value: T): T = ???
  }

  implicit class ButSyntax[T](param: T) {
    def but[S](assign: T => S): T = macro internal.LensMacro.but
  }
}

package internal {
  object LensMacro {
    import scala.reflect.macros.blackbox.Context

    def but(c: Context)(assign: c.Tree): c.Tree = {
      import c.universe._

      val ImplicitsPackage = typeOf[simplelenses.implicits.type].termSymbol

      val q"($param) => $path.AssignSyntax[..$_]($expr).<~($value)" = assign
      val ValDef(_,paramName,_,_) = param
      val Param = paramName

      if(path.symbol != ImplicitsPackage)
        c.abort(c.enclosingPosition, "Something is not right...")

      val argTree = c.prefix.tree
      val q"$path2.ButSyntax[..$_]($arg)" = argTree

      if(path2.symbol != ImplicitsPackage)
        c.abort(c.enclosingPosition, "Something is not right...")

      def rec(peel: c.Tree, build: c.Tree): c.Tree = peel match {
        case Ident(Param) => build
        case Select(rest,current: TermName) => rec(rest, q"$rest.copy($current = $build)")
      }

      q"($param => ${rec(expr, value)})($arg)"
    }
  }
}
