/*
 * Copyright 2018 Jasper Moeys
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

  implicit class Syntax[T <: Product](self: T) {
    def update[S](proj: T => S, newVal: S): T = macro internal.LensMacro.lens
  }
}

package internal {
  object LensMacro {
    import scala.reflect.macros.blackbox.Context

    def lens(c: Context)(proj: c.Tree, newVal: c.Tree): c.Tree = {
      import c.universe._

      val ImplicitsPackage = typeOf[simplelenses.implicits.type].termSymbol

      val q"($param) => $expr" = proj
      val ValDef(_, paramName, _, _) = param
      val Param = paramName

      val syntax = c.prefix.tree
      val q"$path.Syntax[..$_]($self)" = syntax

      if(path.symbol != ImplicitsPackage)
        c.abort(c.enclosingPosition, "Something is not right...")

      def rec(peel: c.Tree, build: c.Tree): c.Tree = peel match {
        case Ident(Param) => build
        case Select(rest, current: TermName) => rec(rest, q"$rest.copy($current = $build)")
      }

      q"($param => ${rec(expr, newVal)})($self)"
    }
  }
}
